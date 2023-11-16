package es.tiernoparla.dam.moviles.model.database

import es.tiernoparla.dam.moviles.model.data.game.GameAbility
import es.tiernoparla.dam.moviles.model.data.game.GameCharacter

interface DBDAO {
    fun getCharacter(id: Int): GameCharacter?
    fun getAbility(id: Int): GameAbility?

    fun listCharacters(): MutableList<GameCharacter>
    fun listAbilities():  MutableList<GameAbility>
}