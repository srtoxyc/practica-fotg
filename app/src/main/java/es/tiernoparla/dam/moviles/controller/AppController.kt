package es.tiernoparla.dam.moviles.controller

import android.content.Context
import android.content.Intent
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

class AppController(private var context: Context) : Controller {
    var dbDAO: DBDAO?           = null
    var serverDAO: ServerDAO?   = null

    companion object {
        var session: User? = null
    }

    init {
        dbDAO       = DBFactory.getDAO(DBFactory.MODE_SQLITE, context)
        serverDAO   = ServerFactory.getDAO(ServerFactory.MODE_SERVER)
    }

    override suspend fun checkLogin(user: String, password: String): Boolean {
        try {
            return if(serverDAO!!.checkLogin(user, password)) {
                session = serverDAO!!.getSession(user, password)
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

    override suspend fun modifyUser(user: String, newUsername: String, password: String): ServerState {
        try {
            return serverDAO!!.modifyUser(user, newUsername, password)
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

    override suspend fun modifyPassword(user: String, newPassword: String, password: String): ServerState {
        try {
            return serverDAO!!.modifyPassword(user, newPassword, password)
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

    override fun getAbility(id: Int): GameAbility {
        try {
            return dbDAO!!.getAbility(id)!!
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

    override fun listAbilities(): MutableList<GameAbility> {
        try {
            return dbDAO!!.listAbilities()
        } catch(e: Exception) {
            throw e;
        }
    }

    override fun openView(activityClass: Class<out AppCompatActivity>) {
        this.context.startActivity(Intent(this.context, activityClass))
    }

    override fun closeView(activity: AppCompatActivity) {
        activity.finish()
    }

    override fun alertConfirm(context: Context, title: String, msg: String): AlertDialog {
        val MSG_CONFIRM: String = "Agree"

        return AlertDialog.Builder(context)
            .setTitle(title)
            .setMessage(msg)
            .setPositiveButton(MSG_CONFIRM, null)
            .create()
    }
}