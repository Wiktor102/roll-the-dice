package com.example.rollthedice.dice

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.get
import com.example.rollthedice.MainActivity
import com.google.gson.Gson
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.UUID

data class Roll(val dice: String, val result: Int, val date: String = LocalDateTime.now().toString(), val id: UUID = UUID.randomUUID()) {
    fun getDate() : LocalDateTime {
        return LocalDateTime.parse(date, DateTimeFormatter.ofPattern("uuuu-MM-dd'T'HH:mm:ss.SSSSSS"))
    }
}

class RollHistoryViewModel : ViewModel() {
    private val _rolls = MutableStateFlow<List<Roll>>(emptyList())
    val rolls: StateFlow<List<Roll>> = _rolls

    init {
        read()
    }

    fun addRoll (newRoll: Roll) {
        _rolls.value = listOf(newRoll) + _rolls.value
        save()
    }

    fun deleteRoll(uuid: UUID) {
        val ml = _rolls.value.toMutableList()
        _rolls.value = ml.filter { it.id != uuid }
        save()
    }

    fun delete() {
        _rolls.value = listOf()
        save()
    }

    private fun save() {
        val sharedPref = MainActivity.appContext.getSharedPreferences("roll-the-dice", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()

        val charactersAsJson = rolls.value.joinToString(";") { Gson().toJson(it) }
        editor.putString("roll-history", charactersAsJson)
        editor.apply()
    }

    private fun read() {
        val sharedPref = MainActivity.appContext.getSharedPreferences("roll-the-dice", Context.MODE_PRIVATE)
        val jsonString = sharedPref.getString("roll-history", null)

        if (jsonString != null) {
            val gson = Gson()
            val parsed: List<Roll> = jsonString.split(";").map { gson.fromJson(it, Roll::class.java) }
            _rolls.value = parsed
        }
    }

    companion object {
        fun get(context: Context): RollHistoryViewModel {
            val viewModelStoreOwner = context as ViewModelStoreOwner;
            val model = ViewModelProvider(viewModelStoreOwner).get<RollHistoryViewModel>();
            return model;
        }
    }
}