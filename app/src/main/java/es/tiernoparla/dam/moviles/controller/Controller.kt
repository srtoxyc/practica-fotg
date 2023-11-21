package es.tiernoparla.dam.moviles.controller

import android.content.Context
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import es.tiernoparla.dam.moviles.model.data.Email
import es.tiernoparla.dam.moviles.model.data.account.ServerState
import es.tiernoparla.dam.moviles.model.data.game.GameAbility
import es.tiernoparla.dam.moviles.model.data.game.GameCharacter

interface Controller {
    suspend fun checkLogin(user: String, password: String): Boolean
    suspend fun signUp(user: String, email: Email, password: String): ServerState

    suspend fun modifyUser(user: String, newUsername: String, password: String): ServerState
    suspend fun modifyEmail(user: String, newEmail: Email, password: String): ServerState
    suspend fun modifyPassword(user: String, newPassword: String, password: String): ServerState

    fun getCharacter(id: Int): GameCharacter
    fun getAbility(id: Int): GameAbility
    fun listCharacters(): MutableList<GameCharacter>
    fun listAbilities(): MutableList<GameAbility>

    fun openView(activityClass: Class<out AppCompatActivity>)
    fun closeView(activity: AppCompatActivity)

    fun alertConfirm(context: Context, title: String, msg: String): AlertDialog
}