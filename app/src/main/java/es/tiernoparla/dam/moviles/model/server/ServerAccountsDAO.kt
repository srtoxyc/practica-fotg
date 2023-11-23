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
        return this.fetch(URL + "/signup/${user}/${email.toString()}/${password}").toInt() as ServerState
    }

    override suspend fun modifyUser(newUsername: String, password: String): ServerState {
        return this.fetch(URL + "/modify/user/${newUsername}/${password}").toInt() as ServerState
    }

    override suspend fun modifyEmail(user: String, newEmail: Email, password: String): ServerState {
        return this.fetch(URL + "/modify/email/${user}/${newEmail.toString()}/${password}").toInt() as ServerState
    }

    override suspend fun modifyPassword(user: String, oldPassword: String, newPassword: String): ServerState {
        return this.fetch(URL + "/modify/password/${user}/${oldPassword}/${newPassword}").toInt() as ServerState
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

    override suspend fun getTeam(user: String, password: String, dbDAO: DBDAO): MutableList<GameCharacter> {
        val DELIM: String                       = ";"
        var listIDs: MutableList<Int>           = this.fetch(URL + "/team/${user}/${password}").split(DELIM).map { it.toInt() }.toMutableList()
        var team: MutableList<GameCharacter>    = ArrayList<GameCharacter>()

        for(characterID in listIDs) {
            team.add(dbDAO.getCharacter(characterID)!!)
        }

        return team
    }

    override suspend fun setTeam(user: String, password: String, team: MutableList<GameCharacter>): ServerState {
        var teamStringified: String = String.format("%d;%d;%d;%d;%d;%d;%d;%d", team[0].getID(), team[1].getID(), team[2].getID(), team[3].getID(), team[4].getID(), team[5].getID(), team[6].getID(), team[7].getID())

        return this.fetch(URL + "/team/${user}/${password}/${teamStringified}").toInt() as ServerState
    }
}