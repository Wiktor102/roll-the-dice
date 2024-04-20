package com.example.rollthedice.characters

import android.content.Context
import android.os.Bundle
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.SaverScope
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.get
import com.example.rollthedice.utilities.mapState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlin.math.floor
import kotlin.random.Random
import kotlin.reflect.KProperty

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
//    operator fun getValue(nothing: Nothing?, property: KProperty<*>): Any {
//        return this
//    }
//
//    operator fun setValue(nothing: Nothing?, property: KProperty<*>, any: Any) {
//
//    }

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

val CharacterStatsSaver = Saver<CharacterStats, Bundle>(
    save = {value ->
        val bundle = Bundle()
        bundle.putInt("strength", value.strength)
        bundle.putInt("dexterity", value.dexterity)
        bundle.putInt("constitution", value.constitution)
        bundle.putInt("intelligence", value.intelligence)
        bundle.putInt("wisdom", value.wisdom)
        bundle.putInt("charisma", value.charisma)
        return@Saver bundle
    },
    restore = {value ->
        CharacterStats(
            value.getInt("strength"),
            value.getInt("dexterity"),
            value.getInt("constitution"),
            value.getInt("intelligence"),
            value.getInt("wisdom"),
            value.getInt("charisma")
        )
    }
)

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