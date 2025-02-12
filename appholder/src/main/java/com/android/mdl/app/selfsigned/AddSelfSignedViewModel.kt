package com.android.mdl.app.selfsigned

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.android.identity.android.securearea.KeystoreUtil
import com.android.mdl.app.document.DocumentColor
import com.android.mdl.app.document.DocumentType
import com.android.mdl.app.document.SecureAreaImplementationState
import com.android.mdl.app.selfsigned.AddSelfSignedScreenState.AndroidAuthKeyCurveOption
import com.android.mdl.app.selfsigned.AddSelfSignedScreenState.AndroidAuthKeyCurveState
import com.android.mdl.app.selfsigned.AddSelfSignedScreenState.AuthTypeState
import com.android.mdl.app.selfsigned.AddSelfSignedScreenState.MdocAuthOptionState
import com.android.mdl.app.selfsigned.AddSelfSignedScreenState.MdocAuthStateOption
import com.android.mdl.app.util.getState
import com.android.mdl.app.util.updateState
import kotlinx.coroutines.flow.StateFlow
import java.lang.Integer.max

class AddSelfSignedViewModel(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private var capabilities = KeystoreUtil.DeviceCapabilities()

    val screenState: StateFlow<AddSelfSignedScreenState> = savedStateHandle.getState(
        AddSelfSignedScreenState()
    )

    fun loadConfiguration(context: Context) {
        capabilities = KeystoreUtil(context).getDeviceCapabilities()
        savedStateHandle.updateState<AddSelfSignedScreenState> {
            it.copy(
                allowLSKFUnlocking = AuthTypeState(true, capabilities.configureUserAuthenticationType),
                allowBiometricUnlocking = AuthTypeState(true, capabilities.configureUserAuthenticationType),
                useStrongBox = AuthTypeState(false, capabilities.strongBox),
                androidMdocAuthState = MdocAuthOptionState(
                    isEnabled = if (it.useStrongBox.isEnabled) capabilities.strongBoxEcdh else capabilities.ecdh
                ),
                androidAuthKeyCurveState = AndroidAuthKeyCurveState(
                    isEnabled = if (it.useStrongBox.isEnabled) capabilities.strongBox25519 else capabilities.curve25519
                )
            )
        }
    }

    fun updateDocumentType(newValue: DocumentType) {
        savedStateHandle.updateState<AddSelfSignedScreenState> {
            it.copy(documentType = newValue, documentName = documentNameFor(newValue))
        }
    }

    fun updateCardArt(newValue: DocumentColor) {
        savedStateHandle.updateState<AddSelfSignedScreenState> {
            it.copy(cardArt = newValue)
        }
    }

    fun updateDocumentName(newValue: String) {
        savedStateHandle.updateState<AddSelfSignedScreenState> {
            it.copy(documentName = newValue)
        }
    }

    fun updateKeystoreImplementation(newValue: SecureAreaImplementationState) {
        savedStateHandle.updateState<AddSelfSignedScreenState> {
            it.copy(secureAreaImplementationState = newValue)
        }
    }

    fun updateUserAuthentication(newValue: Boolean) {
        savedStateHandle.updateState<AddSelfSignedScreenState> {
            it.copy(userAuthentication = newValue)
        }
    }

    fun updateUserAuthenticationTimeoutSeconds(seconds: Int) {
        if (seconds < 0) return
        savedStateHandle.updateState<AddSelfSignedScreenState> {
            it.copy(userAuthenticationTimeoutSeconds = seconds)
        }
    }

    fun updateLskfUnlocking(newValue: Boolean) {
        savedStateHandle.updateState<AddSelfSignedScreenState> {
            val allowLskfUnlock = if (it.allowBiometricUnlocking.isEnabled) newValue else true
            it.copy(allowLSKFUnlocking = it.allowLSKFUnlocking.copy(isEnabled = allowLskfUnlock))
        }
    }

    fun updateBiometricUnlocking(newValue: Boolean) {
        savedStateHandle.updateState<AddSelfSignedScreenState> {
            val allowBiometricUnlock = if (it.allowLSKFUnlocking.isEnabled) newValue else true
            it.copy(allowBiometricUnlocking = it.allowBiometricUnlocking.copy(isEnabled = allowBiometricUnlock))
        }
    }

    fun updateStrongBox(newValue: Boolean) {
        savedStateHandle.updateState<AddSelfSignedScreenState> {
            it.copy(
                useStrongBox = it.useStrongBox.copy(isEnabled = newValue),
                androidMdocAuthState = MdocAuthOptionState(
                    isEnabled = if (newValue) capabilities.strongBoxEcdh else capabilities.ecdh
                ),
                androidAuthKeyCurveState = AndroidAuthKeyCurveState(
                    isEnabled = if (newValue) capabilities.strongBox25519 else capabilities.curve25519
                )
            )
        }
    }

    fun updateMdocAuthOption(newValue: MdocAuthStateOption) {
        savedStateHandle.updateState<AddSelfSignedScreenState> {
            it.copy(
                androidMdocAuthState = it.androidMdocAuthState.copy(mDocAuthentication = newValue),
                androidAuthKeyCurveState = it.androidAuthKeyCurveState.copy(authCurve = AndroidAuthKeyCurveOption.P_256)
            )
        }
    }

    fun updateAndroidAuthKeyCurve(newValue: AndroidAuthKeyCurveOption) {
        savedStateHandle.updateState<AddSelfSignedScreenState> {
            it.copy(androidAuthKeyCurveState = it.androidAuthKeyCurveState.copy(authCurve = newValue))
        }
    }

    fun updateValidityInDays(newValue: Int) {
        val state = savedStateHandle.getState(AddSelfSignedScreenState())
        if (newValue < state.value.minValidityInDays) return
        savedStateHandle.updateState<AddSelfSignedScreenState> {
            it.copy(validityInDays = newValue)
        }
    }

    fun updateMinValidityInDays(newValue: Int) {
        if (newValue <= 0) return
        savedStateHandle.updateState<AddSelfSignedScreenState> {
            val validityDays = max(newValue, it.validityInDays)
            it.copy(minValidityInDays = newValue, validityInDays = validityDays)
        }
    }

    fun updatePassphrase(newValue: String) {
        savedStateHandle.updateState<AddSelfSignedScreenState> {
            it.copy(passphrase = newValue)
        }
    }

    fun updateNumberOfMso(newValue: Int) {
        if (newValue <= 0) return
        savedStateHandle.updateState<AddSelfSignedScreenState> {
            it.copy(numberOfMso = newValue)
        }
    }

    fun updateMaxUseOfMso(newValue: Int) {
        if (newValue <= 0) return
        savedStateHandle.updateState<AddSelfSignedScreenState> {
            it.copy(maxUseOfMso = newValue)
        }
    }

    private fun documentNameFor(documentType: DocumentType): String {
        return when (documentType) {
            is DocumentType.MDL -> "Driving License"
            is DocumentType.MVR -> "Vehicle Registration"
            is DocumentType.MICOV -> "Vaccination Document"
            is DocumentType.EUPID -> "EU Personal ID"
        }
    }
}
