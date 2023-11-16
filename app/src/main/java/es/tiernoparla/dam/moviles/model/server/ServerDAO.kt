package es.tiernoparla.dam.moviles.model.server

import es.tiernoparla.dam.moviles.data.User
import es.tiernoparla.dam.moviles.model.data.Email

interface ServerDAO {
    suspend fun checkLogin(username: String, pass: String): Boolean
    suspend fun checkLogin(email: Email, pass: String): Boolean
}