package es.tiernoparla.dam.moviles.model.server


import es.tiernoparla.dam.moviles.data.User
import es.tiernoparla.dam.moviles.model.data.Email
import es.tiernoparla.dam.moviles.model.data.account.ServerState
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

    override suspend fun modifyUser(user: String, newUsername: String, password: String): ServerState {
        return this.fetch(URL + "/modify/user/${user}/${newUsername}/${password}").toInt() as ServerState
    }

    override suspend fun modifyEmail(user: String, newEmail: Email, password: String): ServerState {
        return this.fetch(URL + "/modify/user/${user}/${newEmail.toString()}/${password}").toInt() as ServerState
    }

    override suspend fun modifyPassword(user: String, newPassword: String, password: String): ServerState {
        return this.fetch(URL + "/modify/password/${user}/${newPassword}/${password}").toInt() as ServerState
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
}