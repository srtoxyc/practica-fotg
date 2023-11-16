package es.tiernoparla.dam.moviles

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import es.tiernoparla.dam.moviles.R
import es.tiernoparla.dam.moviles.controller.AppController
import es.tiernoparla.dam.moviles.controller.Controller
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
            appController.openView(MainActivity::class.java)
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