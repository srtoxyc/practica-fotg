package es.tiernoparla.dam.moviles.model.server


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

    override suspend fun checkLogin(username: String, pass: String): Boolean {
        return this.fetch(URL + "/login/${username}/${pass}").toBoolean()
    }

    override suspend fun checkLogin(email: Email, pass: String): Boolean {
        return this.fetch(URL + "/login/${email.toString()}/${pass}").toBoolean()
    }

    override suspend fun signUp(username: String, email: Email, pass: String): SignUpState {
        return this.fetch(URL + "/signup/${username}/${email.toString()}/${pass}").toInt() as SignUpState
    }
}