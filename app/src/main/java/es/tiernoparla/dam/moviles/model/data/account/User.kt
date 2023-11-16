package es.tiernoparla.dam.moviles.data

import es.tiernoparla.dam.moviles.model.data.Email
import es.tiernoparla.dam.moviles.model.data.Exportable

data class User (
    private var username: String,
    private var email: Email,
) : Exportable {
    companion object {
        public val DEFAULT_ROLE: String = "user"
    }
    private var pass: ByteArray? = null
    private var salt: ByteArray? = null
    private var role: String     = DEFAULT_ROLE

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