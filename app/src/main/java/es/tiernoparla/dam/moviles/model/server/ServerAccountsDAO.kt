package es.tiernoparla.dam.moviles.model.server


import es.tiernoparla.dam.moviles.data.User
import es.tiernoparla.dam.moviles.model.data.Email
import es.tiernoparla.dam.moviles.model.data.account.SignUpState
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

    override suspend fun checkLogin(username: String, pass: String): Boolean {
        return this.fetch(URL + "/login/${username}/${pass}").toBoolean()
    }

    override suspend fun checkLogin(email: Email, pass: String): Boolean {
        return this.fetch(URL + "/login/${email.toString()}/${pass}").toBoolean()
    }

    override suspend fun signUp(username: String, email: Email, pass: String): SignUpState {
        return this.fetch(URL + "/signup/${username}/${email.toString()}/${pass}").toInt() as SignUpState
    }

    override suspend fun getSession(username: String, pass: String): User? {
        val USER_NOT_FOUND: String = "notfound"
        val result: String = this.fetch(URL + "/session/${username}/${pass}")

        return if(result == USER_NOT_FOUND) {
            null
        } else {
            val resultTokenized = this.tokenize(result)
            val user = User(resultTokenized.get(0), Email(resultTokenized.get(1)))
            user.setRole(resultTokenized.get(2))
            user
        }
    }

    override suspend fun getSession(email: Email, pass: String): User? {
        val USER_NOT_FOUND: String = "null"
        val result: String = this.fetch(URL + "/session/${email.toString()}/${pass}")

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