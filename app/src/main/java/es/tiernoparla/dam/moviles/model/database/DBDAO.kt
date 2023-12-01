package es.tiernoparla.dam.moviles.model.database

import es.tiernoparla.dam.moviles.model.data.game.GameAbility
import es.tiernoparla.dam.moviles.model.data.game.GameCharacter

/**
 * Interfaz de objeto de acceso a la base de datos local.
 * @author Iván Vicente Morales (<a href="https://github.com/srtoxyc">@srtoxyc</a>)
 */
interface DBDAO {
    /**
     * Devuelve un objeto del personaje del juego.
     * @param id Número identificador del personaje en la base de datos.
     * @return Objeto del personaje del juego.
     * @author Iván Vicente Morales (<a href="https://github.com/srtoxyc">@srtoxyc</a>)
     */
    fun getCharacter(id: Int): GameCharacter?

    /**
     * Devuelve todos los personajes del juego.
     * @return Lista mutable de todos los personajes del juego.
     * @author Iván Vicente Morales (<a href="https://github.com/srtoxyc">@srtoxyc</a>)
     */
    fun listCharacters(): MutableList<GameCharacter>
}