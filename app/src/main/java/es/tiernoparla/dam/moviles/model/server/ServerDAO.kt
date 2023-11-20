package es.tiernoparla.dam.moviles.model.server

import es.tiernoparla.dam.moviles.data.User
import es.tiernoparla.dam.moviles.model.data.Email
import es.tiernoparla.dam.moviles.model.data.account.SignUpState

interface ServerDAO {
    suspend fun checkLogin(username: String, pass: String): Boolean
    suspend fun checkLogin(email: Email, pass: String): Boolean
    suspend fun signUp(username: String, email: Email, pass: String): SignUpState
    suspend fun getSession(username: String, pass: String): User?
    suspend fun getSession(email: Email, pass: String): User?
}