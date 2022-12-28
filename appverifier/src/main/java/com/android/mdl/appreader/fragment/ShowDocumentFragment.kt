package com.android.mdl.appreader.fragment

import android.content.res.Resources
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.annotation.AttrRes
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.android.mdl.appreader.R
import com.android.mdl.appreader.home.CreateRequestViewModel
import com.android.mdl.appreader.showdocument.ShowDocumentScreen
import com.android.mdl.appreader.showdocument.ShowDocumentViewModel
import com.android.mdl.appreader.theme.ReaderAppTheme
import com.android.mdl.appreader.transfer.TransferManager
import com.android.mdl.appreader.util.KeysAndCertificates
import com.android.mdl.appreader.util.TransferStatus
import com.android.mdl.appreader.util.logDebug

class ShowDocumentFragment : Fragment() {

    private val showDocumentViewModel: ShowDocumentViewModel by viewModels()
    private val createRequestViewModel: CreateRequestViewModel by viewModels()

    private lateinit var transferManager: TransferManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        transferManager = TransferManager.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
        return ComposeView(requireContext()).apply {
            setContent {
                ReaderAppTheme {
                    val state = showDocumentViewModel.state.collectAsState().value
                    val selectionState = createRequestViewModel.state.collectAsState().value
                    ShowDocumentScreen(
                        modifier = Modifier.fillMaxSize(),
                        state = state,
                        selectionState = selectionState,
                        onCloseWithMessage = { terminateWithMessage() },
                        onCloseSpecific = { terminateTransportSpecific() },
                        onCloseNone = { terminateCloseConnection() },
                        onDone = { navigateToHome() },
                        onSelectionUpdated = { createRequestViewModel.onRequestUpdate(it) },
                        onRequestConfirm = { onNewRequest() }
                    )
                }
            }
        }
    }

    private fun onNewRequest() {
        val documents = createRequestViewModel.calculateRequestDocumentList(false)
        val destination = ShowDocumentFragmentDirections.newTransferRequest(documents, true)
        findNavController().navigate(destination)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        transferManager.getTransferStatus().observe(viewLifecycleOwner) {
            when (it) {
                TransferStatus.ENGAGED -> logDebug("Device engagement received.")
                TransferStatus.CONNECTED -> logDebug("Device connected received.")
                TransferStatus.RESPONSE -> logDebug("Device response received.")
                TransferStatus.DISCONNECTED -> {
                    logDebug("Device disconnected received.")
                    hideButtons()
                }

                TransferStatus.ERROR -> {
                    showTransferErrorToast()
                    transferManager.disconnect()
                    hideButtons()
                }

                else -> {}
            }
        }
        showDocumentViewModel.processDocuments(
            transferManager.getDeviceResponse().documents,
            KeysAndCertificates.getTrustedIssuerCertificates(requireContext()),
            transferManager.mdocConnectionMethod,
            requireContext().theme.attr(R.attr.colorPrimary).data
        )
    }

    private fun showTransferErrorToast() {
        Toast.makeText(requireContext(), "Error with the connection.", Toast.LENGTH_SHORT)
            .show()
    }

    private fun terminateWithMessage() {
        transferManager.stopVerification(
            sendSessionTerminationMessage = true,
            useTransportSpecificSessionTermination = false
        )
        showDocumentViewModel.terminate()
    }

    private fun terminateTransportSpecific() {
        transferManager.stopVerification(
            sendSessionTerminationMessage = true,
            useTransportSpecificSessionTermination = true
        )
        showDocumentViewModel.terminate()
    }

    private fun terminateCloseConnection() {
        transferManager.stopVerification(
            sendSessionTerminationMessage = false,
            useTransportSpecificSessionTermination = false
        )
        showDocumentViewModel.terminate()
    }

    private fun navigateToHome() {
        val destination = ShowDocumentFragmentDirections.toRequestOptions(false)
        findNavController().navigate(destination)
    }

    private fun hideButtons() {
        showDocumentViewModel.terminate()
    }

    private fun Resources.Theme.attr(@AttrRes attribute: Int): TypedValue {
        val typedValue = TypedValue()
        if (!resolveAttribute(attribute, typedValue, true)) {
            throw IllegalArgumentException("Failed to resolve attribute: $attribute")
        }
        return typedValue
    }

    private var callback = object : OnBackPressedCallback(true /* enabled by default */) {
        override fun handleOnBackPressed() {
            transferManager.disconnect()
            val destination = ShowDocumentFragmentDirections.toRequestOptions(false)
            findNavController().navigate(destination)
        }
    }
}