package es.tiernoparla.dam.moviles.model.server


import android.util.Log
import es.tiernoparla.dam.moviles.data.User
import es.tiernoparla.dam.moviles.model.data.Email
import es.tiernoparla.dam.moviles.model.data.account.ServerState
import es.tiernoparla.dam.moviles.model.data.game.GameCharacter
import es.tiernoparla.dam.moviles.model.database.DBDAO
import io.ktor.client.HttpClient
import io.ktor.client.request.get

/**
 * Objeto de acceso a un servidor.
 * @see <a href="https://gy-coding.github.io/login-server/">Server Docs</a>
 * @author Iván Vicente Morales (<a href="https://github.com/srtoxyc">@srtoxyc</a>)
 */
class ServerAccountsDAO() : ServerDAO {
    private val URL = "https://login-server-szcx.2.ie-1.fl0.io"

    /**
     * Realiza una petición HTTP a una URL especificada.
     * @param url
     * @return Respuesta
     * @see HttpClient
     * @author Iván Vicente Morales (<a href="https://github.com/srtoxyc">@srtoxyc</a>)
     */
    private suspend fun fetch(url: String): String {
        val client = HttpClient()

        try {
            return client.get<String>(url)
        } finally {
            client.close()
        }
    }

    /**
     * Separa una cadena de caracteres en base al delimitador ";".
     * @param string
     * @return Lista inmutable de las cadenas de caracteres resultantes de la separación.
     * @author Iván Vicente Morales (<a href="https://github.com/srtoxyc">@srtoxyc</a>)
     */
    private fun tokenize(string: String): List<String> {
        val DELIM: String = ";"
        return string.split(DELIM)
    }

    override suspend fun checkLogin(user: String, password: String): Boolean {
        return this.fetch(URL + "/login/${user}/${password}").toBoolean()
    }

    override suspend fun signUp(user: String, email: Email, password: String): ServerState {
        return ServerState.convertIntToServerState(this.fetch(URL + "/signup/${user}/${email.toString()}/${password}").toInt())!!
    }

    override suspend fun modifyUser(username: String, newUsername: String, password: String): ServerState {
        return ServerState.convertIntToServerState(this.fetch(URL + "/update/user/${username}/${newUsername}/${password}").toInt())!!
    }

    override suspend fun modifyEmail(user: String, newEmail: Email, password: String): ServerState {
        return ServerState.convertIntToServerState(this.fetch(URL + "/update/email/${user}/${newEmail.toString()}/${password}").toInt())!!
    }

    override suspend fun modifyPassword(user: String, oldPassword: String, newPassword: String): ServerState {
        return ServerState.convertIntToServerState(this.fetch(URL + "/update/password/${user}/${oldPassword}/${newPassword}").toInt())!!
    }

    override suspend fun modifyPasswordForgotten(user: String, email: String, newPassword: String): ServerState {
        return ServerState.convertIntToServerState(this.fetch(URL + "/update/password-forgotten/${user}/${email}/${newPassword}").toInt())!!
    }

    override suspend fun getSession(user: String, password: String): User? {
        val USER_NOT_FOUND: String = "null"
        val result: String = this.fetch(URL + "/session/${user}/${password}")

        return if(result == USER_NOT_FOUND) {
            null
        } else {
            val resultTokenized = this.tokenize(result)
            val user = User(resultTokenized.get(0), Email(resultTokenized.get(1)))
            user.setRole(resultTokenized.get(2))
            user
        }
    }

    override suspend fun getTeam(user: String, dbDAO: DBDAO): MutableList<GameCharacter?> {
        val DELIM: String                       = ";"
        var listIDs: MutableList<Int>           = this.fetch(URL + "/team/${user}").split(DELIM).map { it.toInt() }.toMutableList()
        var team: MutableList<GameCharacter?>    = ArrayList<GameCharacter?>()

        for(characterID in listIDs) {
            if(characterID == 0) {
                team.add(null)
            } else {
                team.add(dbDAO.getCharacter(characterID)!!)
            }
        }

        return team
    }

    override suspend fun setTeam(user: String, team: MutableList<GameCharacter?>): ServerState {
        val DELIM: String                   = ";"
        var teamIDs: MutableList<Int>       = ArrayList<Int>()

        for(element in team) {
            if(element != null) {
                teamIDs.add(element.getID())
            } else {
                teamIDs.add(0)
            }
        }

        return ServerState.convertIntToServerState(this.fetch(URL + "/team/${user}/${teamIDs.joinToString(DELIM)}").toInt())!!
    }
}