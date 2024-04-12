package com.example.rollthedice

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.get
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

enum class CharacterRace(private val typeName: String) {
    HUMAN("Człowiek"),
    DWARF("Krasnolód"),
    ELF("Elf"),
    HALFLING("Niziołek"),
    GNOME("Gnom"),
    HALFORC("Półork"),
    DRAGONBORN("Drakonid"),
    GOBLIN("Goblin");

    override fun toString(): String {
        return typeName;
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
        return typeName;
    }
}

enum class CharacterAlignment(private val typeName: String) {
    LAWFUL_GOOD("Uczciwy dobry"),
    NEUTRAL_GOOD("Neutralny dobry"),
    CHAOTIC_GOOD("Chaotyczny dobry"),
    TRUE_NEUTRAL("Prawdziwie neutralny"),
    CHAOTIC_NEUTRAL("Chaotyczny neutralny"),
    LAWFUL_EVIL("Uczciwy wrogi"),
    NEUTRAL_EVIL("Neutralny wrogi"),
    CHAOTIC_EVIL("Chaotyczny wrogi");

    override fun toString(): String {
        return typeName;
    }
}

data class Character(
    val name: String,
    val race: CharacterRace,
    val characterClass: CharacterClass,
    val alignment: CharacterAlignment,
    val health: Int
);

class CharactersViewModel : ViewModel() {
    private val _characters = MutableStateFlow<List<Character>>(emptyList());
    val characters: StateFlow<List<Character>> = _characters;

    fun addCharacter(newCharacter: Character) {
        val updatedList = _characters.value.toMutableList()
        updatedList.add(newCharacter)
        _characters.value = updatedList
    }

    fun updateCharacterHealth(characterName: String, newHealth: Int) {
        val updatedList = _characters.value.toMutableList()
        val productIndex = updatedList.indexOfFirst { it.name == characterName }
        if (productIndex != -1) {
            updatedList[productIndex] = updatedList[productIndex].copy(health = newHealth)
            _characters.value = updatedList
        }
    }

    companion object {
        fun get(context: Context): CharactersViewModel {
            val viewModelStoreOwner = context as ViewModelStoreOwner;
            val quotesModel = ViewModelProvider(viewModelStoreOwner).get<CharactersViewModel>();
            return quotesModel;
        }
    }
}