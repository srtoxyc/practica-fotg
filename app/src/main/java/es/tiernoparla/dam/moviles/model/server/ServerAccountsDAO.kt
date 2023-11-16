package es.tiernoparla.dam.moviles.model.server


import es.tiernoparla.dam.moviles.model.data.Email
import io.ktor.client.HttpClient
import io.ktor.client.request.get

class ServerAccountsDAO() : ServerDAO {
    private val URL = "https://ivm-accounts.onrender.com/"

    private suspend fun fetch(url: String): String {
        val client = HttpClient()

        try {
            return client.get<String>(url)
        } finally {
            client.close()
        }
    }

    override suspend fun checkLogin(username: String, pass: String): Boolean {
        return this.fetch(URL + "login/${username}/${pass}").toBoolean()
    }

    override suspend fun checkLogin(email: Email, pass: String): Boolean {
        return this.fetch(URL + "login/${email.toString()}/${pass}").toBoolean()
    }
}