package es.tiernoparla.dam.moviles.controller

import android.content.Context
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import es.tiernoparla.dam.moviles.model.data.Email
import es.tiernoparla.dam.moviles.model.data.account.ServerState
import es.tiernoparla.dam.moviles.model.data.game.GameAbility
import es.tiernoparla.dam.moviles.model.data.game.GameCharacter

/**
 * Interfaz de los controladores de aplicación.
 * @author Iván Vicente Morales
 */
interface Controller {
    /**
     * Evalúa si el usuario existe en la base de datos y si los parámetros de login son correctos, si es así, permite el acceso a la cuenta.
     * @param user Nombre del usuario.
     * @param password
     * @return Si el usuario puede o no puede acceder a la cuenta. No dará acceso si los parámetros de login son incorrectos o si ocurre un error en la comunicación con la base de datos.
     * @author Iván Vicente Morales
     */
    suspend fun checkLogin(user: String, password: String): Boolean

    /**
     * Registra un usuario.
     * @param user Nombre del usuario.
     * @param email Email del usuario.
     * @param password
     * @return Estado del registro del usuario.
     * @author Iván Vicente Morales
     */
    suspend fun signUp(user: String, email: Email, password: String): ServerState

    /**
     * Modifica el nombre del usuario.
     * @param username Anterior nombre del usuario.
     * @param newUsername Nuevo nombre del usuario.
     * @param password
     * @return Estado de la modificación del nombre del usuario.
     * @author Iván Vicente Morales
     */
    suspend fun modifyUser(username: String, newUsername: String, password: String): ServerState

    /**
     * Modifica el email del usuario.
     * @param user Nombre del usuario
     * @param password
     * @return Estado de la modificación del email.
     * @author Iván Vicente Morales
     */
    suspend fun modifyEmail(user: String, newEmail: Email, password: String): ServerState

    /**
     * Modifica la contraseña del usuario.
     * @param user Nombre del usuario
     * @param oldPassword Anterior contraseña del usuario.
     * @param newPassword Nueva contraseña del usuario.
     * @return Estado de la modificación de la contraseña del usuario.
     * @author Iván Vicente Morales
     */
    suspend fun modifyPassword(user: String, oldPassword: String, newPassword: String): ServerState

    /**
     * Establece el equipo de un usuario (los IDs).
     * @param user Nombre del usuario.
     * @param team Lista de identificadores de los personajes que conforman el equipo del usuario.
     * @return Estado de la modificación del equipo del usuario.
     * @author Iván Vicente Morales
     */
    suspend fun setTeam(user: String, team: MutableList<GameCharacter?>): ServerState

    suspend fun refreshSession(username: String, password: String)

    fun getCharacter(id: Int): GameCharacter

    fun listCharacters(): MutableList<GameCharacter>
}