package com.example.rollthedice.settings

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.get
import com.example.rollthedice.MainActivity
import com.google.gson.Gson
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SettingsViewModel:ViewModel() {
    private val _theme = MutableStateFlow("auto")
    val theme: StateFlow<String> = _theme

    private val _vibrations = MutableStateFlow(true)
    val vibrations: StateFlow<Boolean> = _vibrations

    init {
        read()
    }

    fun changeTheme(newTheme: String) {
        if (theme.value == newTheme || !listOf("auto", "light", "dark").contains(theme.value)) return
        _theme.value = newTheme
        save()
    }

    fun toggleVibrations (toggleOn: Boolean) {
        _vibrations.value = toggleOn
        save()
    }

    private fun save() {
        val sharedPref = MainActivity.appContext.getSharedPreferences("roll-the-dice", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()

        val settingsAsMap = mapOf(
            "theme" to theme.value,
            "vibrations" to vibrations.value
        )

        editor.putString("settings", Gson().toJson(settingsAsMap))
        editor.apply()
    }

    private fun read() {
        val sharedPref = MainActivity.appContext.getSharedPreferences("roll-the-dice", Context.MODE_PRIVATE)
        val jsonString = sharedPref.getString("settings", null)

        if (jsonString != null && jsonString != "") {
            val settingsAsMap = Gson().fromJson(jsonString, Map::class.java)
            _theme.value = settingsAsMap["theme"] as String? ?: "auto"
            _vibrations.value = settingsAsMap["vibrations"] as Boolean? ?: true
        }
    }

    companion object {
        fun get(context: Context): SettingsViewModel {
            val viewModelStoreOwner = context as ViewModelStoreOwner;
            val settingsViewModel = ViewModelProvider(viewModelStoreOwner).get<SettingsViewModel>();
            return settingsViewModel;
        }
    }
}