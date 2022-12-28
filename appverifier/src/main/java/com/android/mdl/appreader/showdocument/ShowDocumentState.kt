package com.android.mdl.appreader.showdocument

import android.graphics.Bitmap
import android.text.Spanned
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.core.text.buildSpannedString

@Stable
@Immutable
data class ShowDocumentState(
    val resultText: Spanned? = null,
    val portrait: Bitmap? = null,
    val signature: Bitmap? = null,
    val isTerminated: Boolean = false
)