package com.android.mdl.appreader.showdocument

import android.graphics.BitmapFactory
import android.icu.text.SimpleDateFormat
import android.icu.util.GregorianCalendar
import android.icu.util.TimeZone
import android.text.Html
import androidx.lifecycle.ViewModel
import com.android.identity.ConnectionMethod
import com.android.identity.DeviceResponseParser
import com.android.mdl.appreader.issuerauth.SimpleIssuerTrustStore
import com.android.mdl.appreader.util.FormatUtil
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import java.security.MessageDigest
import java.security.cert.X509Certificate

class ShowDocumentViewModel : ViewModel() {

    private val mutableState = MutableStateFlow(ShowDocumentState())
    val state: StateFlow<ShowDocumentState> = mutableState

    fun terminate() {
        mutableState.update { it.copy(isTerminated = true) }
    }

    fun processDocuments(
        documents: Collection<DeviceResponseParser.Document>,
        certificateList: List<X509Certificate>,
        connectionMethod: ConnectionMethod?,
        themeColor: Int
    ) {
        // Create the trustManager to validate the DS Certificate against the list of known
        // certificates in the app
        val simpleIssuerTrustStore = SimpleIssuerTrustStore(certificateList)
        var portraitBytes: ByteArray? = null
        var signatureBytes: ByteArray? = null
        val stringBuffer = StringBuffer()
        stringBuffer.append("Number of documents returned: <b>${documents.size}</b><br>")
        stringBuffer.append("Address: <b>$connectionMethod</b><br>")
        stringBuffer.append("<br>")
        for (doc in documents) {
            // Get primary color from theme to use in the HTML formatted document.
            val color = String.format("#%06X", 0xFFFFFF and themeColor)
            stringBuffer.append("<h3>Doctype: <font color=\"$color\">${doc.docType}</font></h3>")
            val certPath =
                simpleIssuerTrustStore.createCertificationTrustPath(doc.issuerCertificateChain.toList())
            val isDSTrusted = simpleIssuerTrustStore.validateCertificationTrustPath(certPath)
            var commonName = ""
            // Use the issuer certificate chain if we could not build the certificate trust path
            val certChain = if (certPath?.isNotEmpty() == true) {
                certPath
            } else {
                doc.issuerCertificateChain.toList()
            }

            certChain.last().issuerX500Principal.name.split(",").forEach { line ->
                val (key, value) = line.split("=", limit = 2)
                if (key == "CN") {
                    commonName = "($value)"
                }
            }
            stringBuffer.append("${getFormattedCheck(isDSTrusted)}Issuer’s DS Key Recognized: $commonName<br>")
            stringBuffer.append("${getFormattedCheck(doc.issuerSignedAuthenticated)}Issuer Signed Authenticated<br>")
            var macOrSignatureString = "MAC"
            if (doc.deviceSignedAuthenticatedViaSignature)
                macOrSignatureString = "ECDSA"
            stringBuffer.append("${getFormattedCheck(doc.deviceSignedAuthenticated)}Device Signed Authenticated (${macOrSignatureString})<br>")

            stringBuffer.append("<h6>MSO</h6>")

            val df = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX")
            val calSigned = GregorianCalendar(TimeZone.getTimeZone("UTC"))
            val calValidFrom = GregorianCalendar(TimeZone.getTimeZone("UTC"))
            val calValidUntil = GregorianCalendar(TimeZone.getTimeZone("UTC"))
            calSigned.timeInMillis = doc.validityInfoSigned.toEpochMilli()
            calValidFrom.timeInMillis = doc.validityInfoValidFrom.toEpochMilli()
            calValidUntil.timeInMillis = doc.validityInfoValidUntil.toEpochMilli()
            stringBuffer.append("${getFormattedCheck(true)}Signed: ${df.format(calSigned)}<br>")
            stringBuffer.append("${getFormattedCheck(true)}Valid From: ${df.format(calValidFrom)}<br>")
            stringBuffer.append("${getFormattedCheck(true)}Valid Until: ${df.format(calValidUntil)}<br>")
            if (doc.validityInfoExpectedUpdate != null) {
                val calExpectedUpdate = GregorianCalendar(TimeZone.getTimeZone("UTC"))
                calExpectedUpdate.timeInMillis = doc.validityInfoExpectedUpdate!!.toEpochMilli()
                stringBuffer.append(
                    "${getFormattedCheck(true)}Expected Update: ${
                        df.format(
                            calExpectedUpdate
                        )
                    }<br>"
                )
            }
            // TODO: show warning if MSO is valid for more than 30 days

            // Just show the SHA-1 of DeviceKey since all we're interested in here is whether
            // we saw the same key earlier.
            stringBuffer.append("<h6>DeviceKey</h6>")
            val deviceKeySha1 = FormatUtil.encodeToString(
                MessageDigest.getInstance("SHA-1").digest(doc.deviceKey.encoded)
            )
            stringBuffer.append("${getFormattedCheck(true)}SHA-1: ${deviceKeySha1}<br>")
            // TODO: log DeviceKey's that we've seen and show warning if a DeviceKey is seen
            //  a second time. Also would want button in Settings page to clear the log.

            for (ns in doc.issuerNamespaces) {
                stringBuffer.append("<br>")
                stringBuffer.append("<h5>Namespace: $ns</h5>")
                stringBuffer.append("<p>")
                for (elem in doc.getIssuerEntryNames(ns)) {
                    val value: ByteArray = doc.getIssuerEntryData(ns, elem)
                    var valueStr: String
                    if (isPortraitElement(doc.docType, ns, elem)) {
                        valueStr = String.format("(%d bytes, shown above)", value.size)
                        portraitBytes = doc.getIssuerEntryByteString(ns, elem)
                    } else if (doc.docType == MICOV_DOCTYPE && ns == MICOV_ATT_NAMESPACE && elem == "fac") {
                        valueStr = String.format("(%d bytes, shown above)", value.size)
                        portraitBytes = doc.getIssuerEntryByteString(ns, elem)
                    } else if (doc.docType == MDL_DOCTYPE && ns == MDL_NAMESPACE && elem == "extra") {
                        valueStr = String.format("%d bytes extra data", value.size)
                    } else if (doc.docType == MDL_DOCTYPE && ns == MDL_NAMESPACE && elem == "signature_usual_mark") {
                        valueStr = String.format("(%d bytes, shown below)", value.size)
                        signatureBytes = doc.getIssuerEntryByteString(ns, elem)
                    } else if (doc.docType == EU_PID_DOCTYPE && ns == EU_PID_NAMESPACE && elem == "biometric_template_finger") {
                        valueStr = String.format("%d bytes", value.size)
                    } else {
                        valueStr = FormatUtil.cborPrettyPrint(value)
                    }
                    stringBuffer.append(
                        "${
                            getFormattedCheck(
                                doc.getIssuerEntryDigestMatch(
                                    ns,
                                    elem
                                )
                            )
                        }<b>$elem</b> -> $valueStr<br>"
                    )
                }
                stringBuffer.append("</p><br>")
            }
        }
        val resultText = Html.fromHtml(stringBuffer.toString(), Html.FROM_HTML_MODE_COMPACT)
        val portrait = portraitBytes?.let { bytes ->
            BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
        }
        val signature = signatureBytes?.let { bytes ->
            BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
        }
        mutableState.update {
            it.copy(resultText = resultText, portrait = portrait, signature = signature)
        }
    }

    private fun isPortraitElement(
        docType: String,
        namespace: String?,
        entryName: String?
    ): Boolean {
        val hasPortrait = docType == MDL_DOCTYPE || docType == EU_PID_DOCTYPE
        val namespaceContainsPortrait = namespace == MDL_NAMESPACE || namespace == EU_PID_NAMESPACE
        return hasPortrait && namespaceContainsPortrait && entryName == "portrait"
    }

    private fun getFormattedCheck(authenticated: Boolean) = if (authenticated) {
        "<font color=green>&#x2714;</font> "
    } else {
        "<font color=red>&#x274C;</font> "
    }

    private companion object {
        private const val MDL_DOCTYPE = "org.iso.18013.5.1.mDL"
        private const val MICOV_DOCTYPE = "org.micov.1"
        private const val MDL_NAMESPACE = "org.iso.18013.5.1"
        private const val MICOV_ATT_NAMESPACE = "org.micov.attestation.1"
        private const val EU_PID_DOCTYPE = "eu.europa.ec.eudiw.pid.1"
        private const val EU_PID_NAMESPACE = "eu.europa.ec.eudiw.pid.1"
    }
}
