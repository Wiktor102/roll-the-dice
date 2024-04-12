package com.example.rollthedice

enum class CharacterRace(private val typeName: String) {
    HUMAN("Człowiek"),
    DWARF("Krasnolód"),
    ELF("Elf"),
    HALFLING("Niziołek "),
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

//data class Character (val name: String, val type: CharacterType, var health: Int);
//
//class CharactersViewModel: ViewModel() {
//    private val characters =
//}