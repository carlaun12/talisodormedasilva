package com.talisodormedasilva.desktop

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.talisodormedasilva.APPLICATION_NAME
import com.talisodormedasilva.App

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = APPLICATION_NAME,
    ) {
        App()
    }
}
