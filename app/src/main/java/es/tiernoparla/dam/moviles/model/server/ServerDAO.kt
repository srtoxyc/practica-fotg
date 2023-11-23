package es.tiernoparla.dam.moviles.model.server

import es.tiernoparla.dam.moviles.data.User
import es.tiernoparla.dam.moviles.model.data.Email
import es.tiernoparla.dam.moviles.model.data.account.ServerState
import es.tiernoparla.dam.moviles.model.data.game.GameCharacter
import es.tiernoparla.dam.moviles.model.database.DBDAO

interface ServerDAO {
    suspend fun checkLogin(user: String, password: String): Boolean
    suspend fun signUp(user: String, email: Email, password: String): ServerState

    suspend fun modifyUser(username: String, newUsername: String, password: String): ServerState
    suspend fun modifyEmail(user: String, newEmail: Email, password: String): ServerState
    suspend fun modifyPassword(user: String, oldPassword: String, newPassword: String): ServerState

    suspend fun getSession(user: String, password: String): User?

    suspend fun getTeam(user: String, password: String, dbDAO: DBDAO): MutableList<GameCharacter?>
    suspend fun setTeam(user: String, password: String, team: MutableList<GameCharacter>): ServerState
}