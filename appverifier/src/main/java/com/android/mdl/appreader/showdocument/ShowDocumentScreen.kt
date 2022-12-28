package com.android.mdl.appreader.showdocument

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.widget.TextView
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.android.mdl.appreader.common.CreateRequestDropDown
import com.android.mdl.appreader.home.DocumentElementsRequest
import com.android.mdl.appreader.home.RequestingDocumentState
import com.android.mdl.appreader.theme.ReaderAppTheme

@Composable
fun ShowDocumentScreen(
    modifier: Modifier = Modifier,
    state: ShowDocumentState,
    selectionState: RequestingDocumentState,
    onCloseWithMessage: () -> Unit,
    onCloseSpecific: () -> Unit,
    onCloseNone: () -> Unit,
    onDone: () -> Unit,
    onSelectionUpdated: (elements: DocumentElementsRequest) -> Unit,
    onRequestConfirm: (request: RequestingDocumentState) -> Unit,
) {
    Box(modifier = modifier) {
        val scrollState = rememberScrollState()
        var dropDownOpened by remember { mutableStateOf(false) }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .verticalScroll(scrollState),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Spacer(modifier = Modifier.height(24.dp))
                state.portrait?.let { portrait ->
                    Image(
                        modifier = Modifier.clip(RoundedCornerShape(12.dp)),
                        bitmap = portrait.asImageBitmap(),
                        contentDescription = null
                    )
                }
                AndroidView(
                    factory = { context ->
                        TextView(context)
                    },
                    update = { view ->
                        view.text = state.resultText
                    }
                )
                state.signature?.let { signature ->
                    Image(bitmap = signature.asImageBitmap(), contentDescription = null)
                }
                Spacer(modifier = Modifier.height(24.dp))
            }
            BottomButtonControls(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                isTerminated = state.isTerminated,
                onCloseWithMessage = onCloseWithMessage,
                onCloseSpecific = onCloseSpecific,
                onCloseNone = onCloseNone,
                onNewRequest = { dropDownOpened = true },
                onDone = onDone
            )
        }
        CreateRequestDropDown(
            modifier = Modifier
                .padding(top = 24.dp, start = 16.dp, end = 16.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp)),
            selectionState = selectionState,
            dropDownOpened = dropDownOpened,
            onSelectionUpdated = onSelectionUpdated,
            onConfirm = {
                onRequestConfirm(it)
                dropDownOpened = false
            }
        )
    }
}

@Composable
private fun BottomButtonControls(
    modifier: Modifier = Modifier,
    isTerminated: Boolean,
    onCloseWithMessage: () -> Unit,
    onCloseSpecific: () -> Unit,
    onCloseNone: () -> Unit,
    onNewRequest: () -> Unit,
    onDone: () -> Unit
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        if (isTerminated) {
            Button(onClick = onDone) {
                Text(
                    modifier = Modifier.widthIn(50.dp),
                    text = "OK",
                    textAlign = TextAlign.Center
                )
            }
        } else {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    modifier = Modifier.weight(1f),
                    onClick = onCloseWithMessage
                ) {
                    Text(
                        text = "Close\n(Message)",
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.labelSmall
                    )
                }
                OutlinedButton(
                    modifier = Modifier.weight(1f),
                    onClick = onCloseSpecific
                ) {
                    Text(
                        text = "Close\n(Specific)",
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.labelSmall
                    )
                }
                OutlinedButton(
                    modifier = Modifier.weight(1f),
                    onClick = onCloseNone
                ) {
                    Text(
                        text = "Close\n(None)",
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }
            Button(onClick = onNewRequest) {
                Text(text = "New Request")
            }
        }
    }
}

@Composable
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_NO)
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
private fun ShowDocumentScreenPreview() {
    ReaderAppTheme {
        ShowDocumentScreen(
            modifier = Modifier.fillMaxSize(),
            state = ShowDocumentState(),
            selectionState = RequestingDocumentState(),
            onCloseWithMessage = {},
            onCloseSpecific = {},
            onCloseNone = {},
            onDone = {},
            onRequestConfirm = {},
            onSelectionUpdated = {}
        )
    }
}