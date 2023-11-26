package es.tiernoparla.dam.moviles.controller

import android.content.Context
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import es.tiernoparla.dam.moviles.model.data.Email
import es.tiernoparla.dam.moviles.model.data.account.ServerState
import es.tiernoparla.dam.moviles.model.data.game.GameAbility
import es.tiernoparla.dam.moviles.model.data.game.GameCharacter

interface Controller {
    /**
     * Evalúa si el login del usuario es correcto (existe en la base de datos y la contraseña es correcta).
     * De ser así, devuelve true, de no ser así o fallar la base de datos, devuelve false.
     * @param user Nombre o Email (indistintamente) del usuario.
     * @param password Contraseña del usuario.
     * @return True si el login es satisfactorio, false si no lo es o falla el servidor.
     * @author Iván Vicente Morales
     */
    suspend fun checkLogin(user: String, password: String): Boolean

    /**
     * Efectúa un registro en la base de datos de usuarios.
     * @param user Nombre del usuario.
     * @param Email Email del usuario.
     * @param password Contraseña del usuario.
     * @return Un código de estado del servidor indicando qué ha ocurrido con la transacción.
     * @author Iván Vicente Morales
     * @see Email
     * @see ServerState
     */
    suspend fun signUp(user: String, email: Email, password: String): ServerState

    suspend fun modifyUser(username: String, newUsername: String, password: String): ServerState
    suspend fun modifyEmail(user: String, newEmail: Email, password: String): ServerState
    suspend fun modifyPassword(user: String, oldPassword: String, newPassword: String): ServerState

    suspend fun setTeam(user: String, team: MutableList<GameCharacter?>): ServerState

    suspend fun refreshSession(username: String, password: String)

    fun getCharacter(id: Int): GameCharacter
    fun listCharacters(): MutableList<GameCharacter>
}