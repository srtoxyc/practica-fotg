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
import es.tiernoparla.dam.moviles.model.data.account.SignUpState
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
                        SignUpState.STATE_ERROR_USERNAME -> {
                            Log.e("SIGNUP ERROR", "Username is not correct.")
                            appController.alertConfirm(this@LoginActivity, "Error", "El nombre de usuario no es correcto.").show()
                        }
                        SignUpState.STATE_ERROR_EMAIL -> {
                            Log.e("SIGNUP ERROR", "Email is not correct.")
                            appController.alertConfirm(this@LoginActivity, "Error", "El email no es correcto.").show()
                        }
                        SignUpState.STATE_ERROR_DATABASE -> {
                            Log.e("SIGNUP ERROR", "The database connection has failed.")
                            appController.alertConfirm(this@LoginActivity, "Error", "Ha ocurrido un error inesperado con la base de datos.").show()
                        }
                        SignUpState.STATE_ERROR_PASSWORD -> {
                            Log.e("SIGNUP ERROR", "Password is not correct.")
                            appController.alertConfirm(this@LoginActivity, "Error", "La contraseña no es correcta.").show()
                        }
                        SignUpState.STATE_SUCCESS -> {
                            Log.d("SIGNUP SUCCESS", "Your user has been registered successfully!")
                            appController.alertConfirm(this@LoginActivity, "Bienvenido", "El usuario se ha registrado.").show()
                        }
                    }
                } else {
                    if(appController.checkLogin(inputUser.text.toString(), inputPass.text.toString())) {
                        appController.openView(MainActivity::class.java)
                    } else {
                        Log.e("ERROR", "Login failed.")
                        appController.alertConfirm(this@LoginActivity, "Error", "Nombre de usuario o contraseña incorrectos.").show()
                    }
                }

            }
        }

        lblChangeState.setOnClickListener {
            if(viewState) {
                inputLytEmail.isEnabled = false
                inputLytEmail.visibility = TextInputLayout.GONE

                lblChangeState.text = TXT_SIGNUP
            } else {
                inputLytEmail.isEnabled = true
                inputLytEmail.visibility = TextInputLayout.VISIBLE

                lblChangeState.text = TXT_LOGIN
            }

            viewState = !viewState
        }
    }
}