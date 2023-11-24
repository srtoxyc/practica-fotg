package es.tiernoparla.dam.moviles.model.server


import android.util.Log
import es.tiernoparla.dam.moviles.data.User
import es.tiernoparla.dam.moviles.model.data.Email
import es.tiernoparla.dam.moviles.model.data.account.ServerState
import es.tiernoparla.dam.moviles.model.data.game.GameCharacter
import es.tiernoparla.dam.moviles.model.database.DBDAO
import io.ktor.client.HttpClient
import io.ktor.client.request.get

class ServerAccountsDAO() : ServerDAO {
    private val URL = "https://login-server-dev-cspg.2.sg-1.fl0.io"

    private suspend fun fetch(url: String): String {
        val client = HttpClient()

        try {
            return client.get<String>(url)
        } finally {
            client.close()
        }
    }

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
        var teamStringified: String = ""

        for(character in team) {
            if(character != null) {
                teamStringified += String.format("%d;", character.getID())
            } else {
                teamStringified += String.format("%d;", 0)
            }
        }

        Log.e("sreyfgsa", teamStringified)

        return ServerState.convertIntToServerState(this.fetch(URL + "/team/${user}/${teamStringified}").toInt())!!
    }
}