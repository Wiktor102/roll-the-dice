package com.example.rollthedice.utilities

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.example.rollthedice.MainNavGraph
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

val LocalSnackbarController =
    compositionLocalOf<SnackbarController> { error("No SnackbarController found!") }

class SnackbarController(
    private val scope: CoroutineScope,
    private val snackbarHostState: SnackbarHostState
) {
    fun showSnackBar(message: String) {
        scope.launch {
            snackbarHostState.showSnackbar(message)
        }
    }

    companion object {
        @Composable
        fun Provider(content: @Composable () -> Unit) {
            val scope = rememberCoroutineScope()
            val snackbarHostState = remember { SnackbarHostState() }
            val snackbarController = remember { SnackbarController(scope, snackbarHostState) }

            CompositionLocalProvider(LocalSnackbarController provides snackbarController) {
                Scaffold(snackbarHost = {
                    SnackbarHost(hostState = snackbarHostState)
                }) {
                    Box(Modifier.padding(it)) {
                        MainNavGraph()
                    }
                }
            }
        }
    }
}
