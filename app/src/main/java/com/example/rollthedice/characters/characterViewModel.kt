package com.example.rollthedice.characters

import android.content.Context
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.get
import com.example.rollthedice.MainActivity
import com.example.rollthedice.utilities.LocalSnackbarController
import com.example.rollthedice.utilities.SnackbarController
import com.example.rollthedice.utilities.mapState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlin.math.floor
import kotlin.random.Random
import com.google.gson.*

enum class CharacterRace(private val typeName: String, val speed: Int) {
    HUMAN("Człowiek", 30),
    DWARF("Krasnolód", 25),
    ELF("Elf", 30),
    HALF_ELF("Półelf", 30),
    HALFLING("Niziołek", 25),
    GNOME("Gnom", 25),
    DRAGONBORN("Drakonid", 30),
    ORC("Ork", 30),
    HALF_ORC("Półork", 30),
    TIEFLING("Diablę", 35),
    GOBLIN("Goblin", 30);

    override fun toString(): String {
        return typeName
    }
}

enum class CharacterClass(private val typeName: String) {
    FIGHTER("Wojownik"),
    WIZARD("Mag"),
    ROGUE("Łotrzyk"),
    CLERIC("Kleryk"),
    BARBARIAN("Barbarzyńca"),
    BARD("Bard"),
    PALADIN("Paladyn"),
    SORCERER("Czarodziej"),
    MONK("Mnich"),
    DRUID("Druid");

    override fun toString(): String {
        return typeName
    }
}

data class Character(
    val name: String,
    val race: CharacterRace,
    val characterClass: CharacterClass,
    val stats: CharacterStats,
    val health: Int
)

data class CharacterStats(
    val strength: Int,
    val dexterity: Int,
    val constitution: Int,
    val intelligence: Int,
    val wisdom: Int,
    val charisma: Int,
) {
    val initiative: Int = getModifier(dexterity) + Random.nextInt(1, 21)
    val armorClass: Int = getModifier(dexterity) + 10

    companion object {
        fun generate(): CharacterStats {
            return CharacterStats(
                strength = Random.nextInt(1, 21),
                dexterity = Random.nextInt(1, 21),
                constitution = Random.nextInt(1, 21),
                intelligence = Random.nextInt(1, 21),
                wisdom = Random.nextInt(1, 21),
                charisma = Random.nextInt(1, 21),
            )
        }

        fun getModifier(sourceValue: Int): Int {
            return floor(((sourceValue - 10 )/ 2).toDouble()).toInt()
        }

        fun getModifierAsString(sourceValue: Int?): String {
            if (sourceValue == null) return "?"
            val modifier = getModifier(sourceValue)
            if (modifier >= 0) return "+${modifier}"
            return modifier.toString()
        }
    }
}

class CharacterViewModel() : ViewModel() {
    private val _characters = MutableStateFlow<List<Character>>(emptyList());
    val characters: StateFlow<List<Character>> = _characters;

    init {
        read()
    }

    fun getCharacter(name: String): StateFlow<Character?> {
        return _characters.mapState {items ->
            items.find {it.name == name}
        }
    }

    fun addCharacter(newCharacter: Character) {
        val updatedList = _characters.value.toMutableList()
        updatedList.add(newCharacter)
        _characters.value = updatedList
        save()
    }

    fun deleteCharacter(characterName: String) {
        _characters.value = characters.value.filter { it.name != characterName }
        save()
    }

    fun updateCharacterHealth(characterName: String, newHealth: Int) {
        val updatedList = _characters.value.toMutableList()
        val productIndex = updatedList.indexOfFirst { it.name == characterName }
        if (productIndex != -1) {
            updatedList[productIndex] = updatedList[productIndex].copy(health = newHealth)
            _characters.value = updatedList
        }
    }

    private fun save() {
        val sharedPref = MainActivity.appContext.getSharedPreferences("roll-the-dice", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()

        val charactersAsJson = characters.value.joinToString(";") { Gson().toJson(it) }
        editor.putString("characters", charactersAsJson)
        editor.apply()
    }

    private fun read() {
        val sharedPref = MainActivity.appContext.getSharedPreferences("roll-the-dice", Context.MODE_PRIVATE)
        val jsonString = sharedPref.getString("characters", null)

        if (jsonString != null) {
            val gson = Gson()
            val parsed: List<Character> = jsonString.split(";").map { gson.fromJson(it, Character::class.java) }
            _characters.value = parsed
        }
    }

    companion object {
        fun get(context: Context): CharacterViewModel {
            val viewModelStoreOwner = context as ViewModelStoreOwner;
            val quotesModel = ViewModelProvider(viewModelStoreOwner).get<CharacterViewModel>();
            return quotesModel;
        }
    }
}