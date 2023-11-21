package es.tiernoparla.dam.moviles.model.server

import es.tiernoparla.dam.moviles.data.User
import es.tiernoparla.dam.moviles.model.data.Email
import es.tiernoparla.dam.moviles.model.data.account.ServerState

interface ServerDAO {
    suspend fun checkLogin(user: String, password: String): Boolean
    suspend fun signUp(user: String, email: Email, password: String): ServerState
    suspend fun modifyUser(user: String, newUsername: String, password: String): ServerState
    suspend fun modifyEmail(user: String, newEmail: Email, password: String): ServerState
    suspend fun modifyPassword(user: String, newPassword: String, password: String): ServerState
    suspend fun getSession(user: String, password: String): User?
}