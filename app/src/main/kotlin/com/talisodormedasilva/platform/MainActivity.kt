package com.talisodormedasilva.platform

import android.os.Bundle
import android.graphics.Color as AndroidColor
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.talisodormedasilva.presentation.CalendarScreen
import com.talisodormedasilva.presentation.CalendarTheme
import com.talisodormedasilva.presentation.CalendarViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(AndroidColor.TRANSPARENT, AndroidColor.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.light(AndroidColor.TRANSPARENT, AndroidColor.TRANSPARENT),
        )
        val repository = (application as CalendarApplication).repository
        setContent {
            val model: CalendarViewModel = viewModel(factory = viewModelFactory {
                initializer { CalendarViewModel(repository, createSavedStateHandle()) }
            })
            CalendarTheme {
                Scaffold(containerColor = MaterialTheme.colorScheme.background) { padding ->
                    CalendarScreen(
                        state = model.state.collectAsStateWithLifecycle().value,
                        onPreviousMonth = model::previousMonth,
                        onNextMonth = model::nextMonth,
                        onSelectDate = model::selectDate,
                        modifier = Modifier.padding(padding),
                    )
                }
            }
        }
    }
}
