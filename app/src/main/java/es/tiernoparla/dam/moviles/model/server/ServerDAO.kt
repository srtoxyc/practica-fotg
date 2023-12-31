package es.tiernoparla.dam.moviles.model.server

import es.tiernoparla.dam.moviles.data.User
import es.tiernoparla.dam.moviles.model.data.Email
import es.tiernoparla.dam.moviles.model.data.account.ServerState
import es.tiernoparla.dam.moviles.model.data.game.GameCharacter
import es.tiernoparla.dam.moviles.model.database.DBDAO

/**
 * Interfaz de objeto de acceso a un servidor.
 * @see <a href="https://gy-coding.github.io/login-server/">Server Docs</a>
 * @author Iván Vicente Morales (<a href="https://github.com/srtoxyc">@srtoxyc</a>)
 */
interface ServerDAO {
    /**
     * Evalúa si el usuario existe en la base de datos y si los parámetros de login son correctos, si es así, permite el acceso a la cuenta.
     * @param user Nombre del usuario.
     * @param password
     * @return Si el usuario puede o no puede acceder a la cuenta. No dará acceso si los parámetros de login son incorrectos o si ocurre un error en la comunicación con la base de datos.
     * @author Iván Vicente Morales (<a href="https://github.com/srtoxyc">@srtoxyc</a>)
     */
    suspend fun checkLogin(user: String, password: String): Boolean

    /**
     * Registra un usuario.
     * @param user Nombre del usuario.
     * @param email Email del usuario.
     * @param password
     * @return Estado del registro del usuario.
     * @author Iván Vicente Morales (<a href="https://github.com/srtoxyc">@srtoxyc</a>)
     */
    suspend fun signUp(user: String, email: Email, password: String): ServerState

    /**
     * Modifica el nombre del usuario.
     * @param username Anterior nombre del usuario.
     * @param newUsername Nuevo nombre del usuario.
     * @param password
     * @return Estado de la modificación del nombre del usuario.
     * @author Iván Vicente Morales (<a href="https://github.com/srtoxyc">@srtoxyc</a>)
     */
    suspend fun modifyUser(username: String, newUsername: String, password: String): ServerState

    /**
     * Modifica el email del usuario.
     * @param user Nombre del usuario.
     * @param password
     * @return Estado de la modificación del email.
     * @author Iván Vicente Morales (<a href="https://github.com/srtoxyc">@srtoxyc</a>)
     */
    suspend fun modifyEmail(user: String, newEmail: Email, password: String): ServerState

    /**
     * Modifica la contraseña del usuario.
     * @param user Nombre del usuario.
     * @param oldPassword Anterior contraseña del usuario.
     * @param newPassword Nueva contraseña del usuario.
     * @return Estado de la modificación de la contraseña del usuario.
     * @author Iván Vicente Morales (<a href="https://github.com/srtoxyc">@srtoxyc</a>)
     */
    suspend fun modifyPassword(user: String, oldPassword: String, newPassword: String): ServerState

    /**
     * Modifica la contraseña del usuario si esta ha sido olvidada.
     * @param user Nombre del usuario.
     * @param email Email del usuario
     * @param newPassword Nueva contraseña del usuario.
     * @return Estado de la modificación de la contraseña del usuario.
     * @author Iván Vicente Morales (<a href="https://github.com/srtoxyc">@srtoxyc</a>)
     */
    suspend fun modifyPasswordForgotten(user: String, email: String, newPassword: String): ServerState

    /**
     * Devuelve la sesión de la cuenta que haga login.
     * @param user Nombre del usuario.
     * @param password
     * @return Usuario que está iniciado.
     * @author Iván Vicente Morales (<a href="https://github.com/srtoxyc">@srtoxyc</a>)
     */
    suspend fun getSession(user: String, password: String): User?

    /**
     * Devuelve los IDs del equipo completo del usuario.
     * @param user Nombre del usuario.
     * @return IDs del equipo completo del usuario compactos en una cadena de caracteres.
     * @author Iván Vicente Morales (<a href="https://github.com/srtoxyc">@srtoxyc</a>)
     */
    suspend fun getTeam(user: String, dbDAO: DBDAO): MutableList<GameCharacter?>

    /**
     * Establece el equipo de un usuario (los IDs).
     * @param user Nombre del usuario.
     * @param team Lista de identificadores de los personajes que conforman el equipo del usuario.
     * @return Estado de la modificación del equipo del usuario.
     * @author Iván Vicente Morales (<a href="https://github.com/srtoxyc">@srtoxyc</a>)
     */
    suspend fun setTeam(user: String, team: MutableList<GameCharacter?>): ServerState
}