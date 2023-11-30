package es.tiernoparla.dam.moviles.data

import android.util.Log
import es.tiernoparla.dam.moviles.controller.AppController
import es.tiernoparla.dam.moviles.model.data.Email
import es.tiernoparla.dam.moviles.model.data.Exportable
import es.tiernoparla.dam.moviles.model.data.game.GameCharacter

/**
 * Representación de un usuario.
 * @param username Nombre del usuario.
 * @param email Email del usuario como objeto.
 * @author Iván Vicente Morales
 */
data class User (
    private var username: String,
    private var email: Email,
) : Exportable {
    companion object {
        public val DEFAULT_ROLE: String     = "USER"
        public val TEAM_MAX_SIZE: Int     = 8

        private var teamCounter: Int        = 0

        public fun getTeamElementsCount(): Int {
            return teamCounter
        }
    }

    private var pass: ByteArray? = null
    private var salt: ByteArray? = null
    private var role: String     = DEFAULT_ROLE

    private var team: MutableList<GameCharacter?> = ArrayList<GameCharacter?>(TEAM_MAX_SIZE)

    fun getUsername(): String {
        return this.username
    }
    fun setUsername(username: String) {
        this.username = username
    }

    fun getEmail(): Email {
        return this.email
    }
    fun setEmail(email: Email) {
        this.email = email
    }

    fun getPass(): ByteArray {
        return this.pass!!
    }
    fun setPass(pass: ByteArray) {
        this.pass = pass
    }

    fun getSalt(): ByteArray {
        return this.salt!!
    }
    fun setSalt(salt: ByteArray) {
        this.salt = salt
    }

    fun getRole(): String {
        return this.role
    }
    fun setRole(role: String) {
        this.role = role
    }

    fun getTeam(): MutableList<GameCharacter?> {
        return this.team
    }
    fun addToTeam(character: GameCharacter) {
        this.team.set(teamCounter, character)
        teamCounter++
    }
    fun removeFromTeam(index: Int) {
        this.team.set(index, null)
        teamCounter--
        this.team = this.team.sortedWith(compareBy { it == null }).toMutableList()
    }
    fun setTeam(team: MutableList<GameCharacter?>) {
        var nullCounter: Int    = 0
        this.team               = team

        for (character in this.team) {
            if(character == null) {
                nullCounter++
            }
        }
        teamCounter = TEAM_MAX_SIZE - nullCounter
    }
    fun getFromTeam(index: Int): GameCharacter? {
        return this.team.get(index)
    }

    override fun toString(): String {
        return "[${this.getRole()}]\nUsername: ${this.getUsername()}\nEmail: ${this.getEmail()}\n"
    }

    override fun toTXT(): String {
        TODO("Not yet implemented")
    }

    override fun toXML(): String {
        TODO("Not yet implemented")
    }

    override fun toJSON(): String {
        return "{\"username\": \"${this.getUsername()}\",\"email\": \"${this.getEmail()}\",\"password\": \"${this.getPass()}\",\"salt\": \"${this.getSalt()}\",\"role\": \"${this.getRole()}\"}"
    }
}