package es.tiernoparla.dam.moviles.controller

import android.content.Context
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import es.tiernoparla.dam.moviles.model.data.Email
import es.tiernoparla.dam.moviles.model.data.account.SignUpState
import es.tiernoparla.dam.moviles.model.data.game.GameAbility
import es.tiernoparla.dam.moviles.model.data.game.GameCharacter

interface Controller {
    suspend fun checkLogin(username: String, pass: String): Boolean
    suspend fun checkLogin(email: Email, pass: String): Boolean
    suspend fun signUp(username: String, email: Email, pass: String): SignUpState

    fun getCharacter(id: Int): GameCharacter
    fun getAbility(id: Int): GameAbility
    fun listCharacters(): MutableList<GameCharacter>
    fun listAbilities(): MutableList<GameAbility>

    fun openView(activityClass: Class<out AppCompatActivity>)
    fun closeView(activity: AppCompatActivity)

    fun alertConfirm(context: Context, title: String, msg: String): AlertDialog
}