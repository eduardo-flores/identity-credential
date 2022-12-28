package com.android.mdl.appreader.showdocument

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class ShowDocumentTest {

    @Test
    fun defaultState() {
        val viewModel = ShowDocumentViewModel()
        assertThat(viewModel.state.value).isEqualTo(ShowDocumentState())
    }

    @Test
    fun termination() {
        val viewModel = ShowDocumentViewModel()

        viewModel.terminate()

        assertThat(viewModel.state.value).isEqualTo(ShowDocumentState(isTerminated = true))
    }
}