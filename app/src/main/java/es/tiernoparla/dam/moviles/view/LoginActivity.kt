package es.tiernoparla.dam.moviles.view

import android.os.Bundle
import android.util.Log
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
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_view)

        val appController: Controller       = AppController(this)

        val inputLytUser: TextInputLayout   = findViewById(R.id.inputLytUser)
        val inputLytEmail: TextInputLayout  = findViewById(R.id.inputLytEmail)
        val inputLytPass: TextInputLayout   = findViewById(R.id.inputLytPass)

        val inputUser: TextInputEditText    = findViewById(R.id.inputUser)
        val inputEmail: TextInputEditText   = findViewById(R.id.inputEmail)
        val inputPass: TextInputEditText    = findViewById(R.id.inputPass)

        val btnLogin: Button                = findViewById(R.id.btnLogin)
        val lblChangeState: TextView        = findViewById(R.id.lblChangeState)

        val TXT_SIGNUP: String              = "Sign up"
        val TXT_LOGIN: String               = "Log in"

        var viewState: Boolean              = false

        btnLogin.setOnClickListener {
            lifecycleScope.launch {
                if(viewState) {
                    when(appController.signUp(inputUser.text.toString(), Email(inputEmail.text.toString()), inputPass.text.toString())) {
                        ServerState.STATE_ERROR_USERNAME -> {
                            appController.alertConfirm(this@LoginActivity, "Error", "El nombre de usuario no es correcto.").show()
                        }
                        ServerState.STATE_ERROR_EMAIL -> {
                            appController.alertConfirm(this@LoginActivity, "Error", "El email no es correcto.").show()
                        }
                        ServerState.STATE_ERROR_DATABASE -> {
                            appController.alertConfirm(this@LoginActivity, "Error", "Ha ocurrido un error inesperado con la base de datos.").show()
                        }
                        ServerState.STATE_ERROR_PASSWORD -> {
                            appController.alertConfirm(this@LoginActivity, "Error", "La contraseña no es correcta.").show()
                        }
                        ServerState.STATE_SUCCESS -> {
                            appController.alertConfirm(this@LoginActivity, "Bienvenido", "El usuario se ha registrado.").show()
                        }
                    }
                } else {
                    if(appController.checkLogin(inputUser.text.toString(), inputPass.text.toString())) {
                        appController.openView(LoadingActivity::class.java)
                        overridePendingTransition(R.anim.scale_in, R.anim.scale_out)
                    } else {
                        Log.e("ERROR", "Login failed.")
                        appController.alertConfirm(this@LoginActivity, "Error", "Nombre de usuario o contraseña incorrectos.").show()
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
            } else {
                inputLytEmail.isEnabled     = true
                inputLytEmail.visibility    = TextInputLayout.VISIBLE

                lblChangeState.text         = TXT_LOGIN
                btnLogin.text               = TXT_SIGNUP
            }

            viewState = !viewState
        }
    }
}