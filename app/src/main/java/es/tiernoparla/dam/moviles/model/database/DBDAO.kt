package es.tiernoparla.dam.moviles.model.database

import es.tiernoparla.dam.moviles.model.data.game.GameAbility
import es.tiernoparla.dam.moviles.model.data.game.GameCharacter

interface DBDAO {
    fun getCharacter(id: Int): GameCharacter?

    fun listCharacters(): MutableList<GameCharacter>
}