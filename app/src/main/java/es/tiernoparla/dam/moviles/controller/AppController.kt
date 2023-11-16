package es.tiernoparla.dam.moviles.controller

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import es.tiernoparla.dam.moviles.data.User
import es.tiernoparla.dam.moviles.model.data.Email
import es.tiernoparla.dam.moviles.model.data.game.GameAbility
import es.tiernoparla.dam.moviles.model.data.game.GameCharacter
import es.tiernoparla.dam.moviles.model.database.DBDAO
import es.tiernoparla.dam.moviles.model.database.DBFactory
import es.tiernoparla.dam.moviles.model.server.ServerDAO
import es.tiernoparla.dam.moviles.model.server.ServerFactory

class AppController(private var context: Context) : Controller {
    var dbDAO: DBDAO?           = null
    var serverDAO: ServerDAO?   = null

    init {
        dbDAO       = DBFactory.getDAO(DBFactory.MODE_SQLITE, context)
        serverDAO   = ServerFactory.getDAO(ServerFactory.MODE_SERVER)
    }

    override suspend fun checkLogin(username: String, pass: String): Boolean {
        try {
            return serverDAO!!.checkLogin(username, pass)
        } catch(e: Exception) {
            throw e
        }
    }

    override suspend fun checkLogin(email: Email, pass: String): Boolean {
        try {
            return serverDAO!!.checkLogin(email, pass)
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
}