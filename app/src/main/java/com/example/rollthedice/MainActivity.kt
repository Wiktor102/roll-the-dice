package com.example.rollthedice

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.rollthedice.characters.CharacterViewModel
import com.example.rollthedice.dice.RollHistoryViewModel
import com.example.rollthedice.settings.SettingsViewModel
import com.example.rollthedice.ui.theme.AppTheme
import com.example.rollthedice.utilities.SnackbarController

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appContext = this
        setContent {
            val characterViewModel: CharacterViewModel = viewModel()
            val rollHistoryViewModel: RollHistoryViewModel = viewModel()
            val settingsViewModel: SettingsViewModel = viewModel()

            AppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    SnackbarController.Provider {
                        MainNavGraph()
                    }
                }
            }
        }
    }

    companion object {
        lateinit var appContext: Context
    }
}