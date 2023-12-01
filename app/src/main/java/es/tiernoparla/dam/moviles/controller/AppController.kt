package es.tiernoparla.dam.moviles.controller

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import es.tiernoparla.dam.moviles.data.User
import es.tiernoparla.dam.moviles.model.data.Email
import es.tiernoparla.dam.moviles.model.data.account.ServerState
import es.tiernoparla.dam.moviles.model.data.game.GameAbility
import es.tiernoparla.dam.moviles.model.data.game.GameCharacter
import es.tiernoparla.dam.moviles.model.database.DBDAO
import es.tiernoparla.dam.moviles.model.database.DBFactory
import es.tiernoparla.dam.moviles.model.server.ServerDAO
import es.tiernoparla.dam.moviles.model.server.ServerFactory

/**
 * Controlador de la aplicación.
 * @param context Contexto de la aplicación.
 * @see Context
 * @author Iván Vicente Morales (<a href="https://github.com/srtoxyc">@srtoxyc</a>)
 */
class AppController(private var context: Context) : Controller {
    var dbDAO: DBDAO?           = null
    var serverDAO: ServerDAO?   = null

    companion object {
        var session: User?                      = null
        var characterSelected: GameCharacter?   = null
    }

    init {
        dbDAO       = DBFactory.getDAO(DBFactory.MODE_SQLITE, context)
        serverDAO   = ServerFactory.getDAO(ServerFactory.MODE_SERVER)
    }

    override suspend fun checkLogin(user: String, password: String): Boolean {
        try {
            return if(serverDAO!!.checkLogin(user, password)) {
                session = serverDAO!!.getSession(user, password)
                session!!.setTeam(serverDAO!!.getTeam(user, dbDAO!!))
                true
            } else {
                false
            }
        } catch(e: Exception) {
            throw e
        }
    }

    override suspend fun signUp(user: String, email: Email, password: String): ServerState {
        try {
            return serverDAO!!.signUp(user, email, password)
        } catch(e: Exception) {
            throw e
        }
    }

    override suspend fun modifyUser(username: String, newUsername: String, password: String): ServerState {
        try {
            return serverDAO!!.modifyUser(username, newUsername, password)
        } catch(e: Exception) {
            throw e
        }
    }

    override suspend fun modifyEmail(user: String, newEmail: Email, password: String): ServerState {
        try {
            return serverDAO!!.modifyEmail(user, newEmail, password)
        } catch(e: Exception) {
            throw e
        }
    }

    override suspend fun modifyPassword(user: String, oldPassword: String, newPassword: String): ServerState {
        try {
            return serverDAO!!.modifyPassword(user, oldPassword, newPassword)
        } catch(e: Exception) {
            throw e
        }
    }

    override suspend fun modifyPasswordForgotten(user: String, email: String, newPassword: String): ServerState {
        try {
            return serverDAO!!.modifyPasswordForgotten(user, email, newPassword)
        } catch(e: Exception) {
            throw e
        }
    }

    override suspend fun refreshSession(username: String, password: String) {
        try {
            session = serverDAO!!.getSession(username, password)
        } catch(e: Exception) {
            throw e
        }
    }

    override suspend fun setTeam(user: String, team: MutableList<GameCharacter?> ): ServerState {
        try {
            return serverDAO!!.setTeam(user, team)
        } catch(e: Exception) {
            throw e
        }
    }

    override fun getCharacter(id: Int): GameCharacter {
        try {
            return dbDAO!!.getCharacter(id)!!
        } catch(e: Exception) {
            throw e;
        }
    }

    override fun listCharacters(): MutableList<GameCharacter> {
        try {
            return dbDAO!!.listCharacters()
        } catch(e: Exception) {
            throw e;
        }
    }
}