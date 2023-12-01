package es.tiernoparla.dam.moviles.view

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import es.tiernoparla.dam.moviles.R
import es.tiernoparla.dam.moviles.controller.AppController
import es.tiernoparla.dam.moviles.controller.Controller
import es.tiernoparla.dam.moviles.model.data.Email
import es.tiernoparla.dam.moviles.model.data.account.ServerState
import es.tiernoparla.dam.moviles.view.utils.ViewUtil
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_view)

        val rootView: View = findViewById<View>(android.R.id.content)

        val appController: Controller       = AppController(this)

        val inputLytEmail: TextInputLayout  = findViewById(R.id.inputLytEmail)

        val inputUser: TextInputEditText    = findViewById(R.id.inputUser)
        val inputEmail: TextInputEditText   = findViewById(R.id.inputEmail)
        val inputPass: TextInputEditText    = findViewById(R.id.inputPass)

        val btnLogin: Button                = findViewById(R.id.btnLogin)
        val lblChangeState: TextView        = findViewById(R.id.lblChangeState)
        val lblPasswordForgotten: TextView  = findViewById(R.id.lblPasswordForgotten)

        val TXT_SIGNUP: String              = "Sign up"
        val TXT_LOGIN: String               = "Log in"
        val MSG_ERROR_LOGIN: String         = "Nombre de usuario o contraseña incorrectos."
        val MSG_ERROR_USERNAME: String      = "El nombre de usuario no es correcto."
        val MSG_ERROR_EMAIL: String         = "El email no es correcto."
        val MSG_ERROR_DATABASE: String      = "Ha ocurrido un error inesperado con la base de datos."
        val MSG_ERROR_PASSWORD: String      = "La contraseña no es correcta."
        val MSG_SIGNUP: String              = "El usuario se ha registrado."
        val DIALOG_TITLE_WELCOME            = "Bienvenido"

        var viewState: Boolean              = false     // 'false' = LogIn Activity, 'true' = SignUp Activity.

        btnLogin.setOnClickListener {
            lifecycleScope.launch {
                if(viewState) {
                    when(appController.signUp(inputUser.text.toString(), Email(inputEmail.text.toString()), inputPass.text.toString())) {
                        ServerState.STATE_ERROR_USERNAME -> {
                            ViewUtil.alertConfirm(this@LoginActivity, R.drawable.logo, ViewUtil.DIALOG_TITLE_ERROR, MSG_ERROR_USERNAME).show()
                        }
                        ServerState.STATE_ERROR_EMAIL -> {
                            ViewUtil.alertConfirm(this@LoginActivity, R.drawable.logo, ViewUtil.DIALOG_TITLE_ERROR, MSG_ERROR_EMAIL).show()
                        }
                        ServerState.STATE_ERROR_DATABASE -> {
                            ViewUtil.alertConfirm(this@LoginActivity, R.drawable.logo, ViewUtil.DIALOG_TITLE_ERROR, MSG_ERROR_DATABASE).show()
                        }
                        ServerState.STATE_ERROR_PASSWORD -> {
                            ViewUtil.alertConfirm(this@LoginActivity, R.drawable.logo, ViewUtil.DIALOG_TITLE_ERROR, MSG_ERROR_PASSWORD).show()
                        }
                        ServerState.STATE_SUCCESS -> {
                            ViewUtil.alertConfirm(this@LoginActivity, R.drawable.logo, DIALOG_TITLE_WELCOME, MSG_SIGNUP).show()
                        }
                    }
                } else {
                    if(appController.checkLogin(inputUser.text.toString(), inputPass.text.toString())) {
                        ViewUtil.openView(this@LoginActivity, LoadingActivity::class.java)
                        overridePendingTransition(R.anim.scale_in, R.anim.scale_out)
                    } else {
                        ViewUtil.alertConfirm(this@LoginActivity, R.drawable.logo,ViewUtil.DIALOG_TITLE_ERROR, MSG_ERROR_LOGIN).show()
                    }
                }
            }
        }

        lblChangeState.setOnClickListener {
            if(viewState) {
                inputLytEmail.isEnabled     = false
                inputLytEmail.visibility    = TextInputLayout.GONE

                lblChangeState.text         = TXT_SIGNUP
                btnLogin.text               = TXT_LOGIN

                lblPasswordForgotten.visibility = TextView.VISIBLE
            } else {
                inputLytEmail.isEnabled     = true
                inputLytEmail.visibility    = TextInputLayout.VISIBLE

                lblChangeState.text         = TXT_LOGIN
                btnLogin.text               = TXT_SIGNUP

                lblPasswordForgotten.visibility = TextView.GONE
            }

            viewState = !viewState
        }

        lblPasswordForgotten.setOnClickListener {
            ViewUtil.alertForm(appController, this, this@LoginActivity, ViewUtil.DIALOG_SELECTOR_PASS_FORGOTTEN)
        }

        rootView.setOnTouchListener { _: Any, event: MotionEvent ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
                return@setOnTouchListener true
            }
            return@setOnTouchListener false
        }
    }
}