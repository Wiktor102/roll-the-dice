package com.example.rollthedice.characters

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.get
import com.example.rollthedice.utilities.mapState
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

data class Character(
    val name: String,
    val race: CharacterRace,
    val characterClass: CharacterClass,
    val health: Int
);

class CharacterViewModel : ViewModel() {
    private val _characters = MutableStateFlow<List<Character>>(emptyList());
    val characters: StateFlow<List<Character>> = _characters;

    fun getCharacter(name: String): StateFlow<Character?> {
        return _characters.mapState {items ->
            items.find {it.name == name}
        };
    }

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
        fun get(context: Context): CharacterViewModel {
            val viewModelStoreOwner = context as ViewModelStoreOwner;
            val quotesModel = ViewModelProvider(viewModelStoreOwner).get<CharacterViewModel>();
            return quotesModel;
        }
    }
}