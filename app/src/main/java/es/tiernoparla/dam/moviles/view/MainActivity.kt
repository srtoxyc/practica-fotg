package es.tiernoparla.dam.moviles.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.textfield.TextInputEditText
import es.tiernoparla.dam.moviles.R
import es.tiernoparla.dam.moviles.controller.AppController
import es.tiernoparla.dam.moviles.controller.Controller
import es.tiernoparla.dam.moviles.databinding.ActivityMainBinding
import es.tiernoparla.dam.moviles.model.data.Email
import es.tiernoparla.dam.moviles.model.data.account.ServerState
import es.tiernoparla.dam.moviles.model.data.game.GameCharacter
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var appController: Controller? = null

    private fun fillCharactersLayout(layout: LinearLayout, character: GameCharacter) {
        val imgCharacter        = ImageView(this)
        val nameCharacter       = TextView(this)

        val imageSize           = 135
        val textSize            = 16

        // CHARACTER'S IMAGE ATTRIBUTES
        imgCharacter.layoutParams = LinearLayout.LayoutParams(
            imageSize,
            imageSize
        )
        imgCharacter.setImageResource(R.drawable.logo)
        imgCharacter.setBackgroundResource(R.drawable.image_border)
        imgCharacter.scaleType = ImageView.ScaleType.CENTER_CROP

        imgCharacter.setImageResource(resources.getIdentifier(character.getIMG(), "drawable", packageName))

        // CHARACTER'S NAME ATTRIBUTES
        var characterNameParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        characterNameParams.setMargins(0, 2, 0, 0)
        nameCharacter.layoutParams = characterNameParams
        nameCharacter.setTextColor(ContextCompat.getColor(this, R.color.golden_light))
        nameCharacter.textSize = textSize.toFloat()
        nameCharacter.typeface = ResourcesCompat.getFont(this, R.font.cinzel)
        nameCharacter.text = character.getName()
        nameCharacter.gravity = Gravity.CENTER

        layout.addView(imgCharacter)
        layout.addView(nameCharacter)
    }

    private fun generateChararactesGrid() {
        val tableTeam: TableLayout = findViewById(R.id.tableSelectCharacters)

        val ITEMS_PER_ROW: Int = 4
        var countItemsRow: Int = 0

        var tableRow: TableRow? = null

        for (character in appController!!.listCharacters()) {

            // NEW ROW EACH TIME ONE GETS FILLED.
            if (countItemsRow == 0 || countItemsRow == ITEMS_PER_ROW) {
                tableRow = TableRow(this)
                tableRow.layoutParams = TableLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                tableRow.gravity = Gravity.CENTER

                tableTeam.addView(tableRow)
                countItemsRow = 0
            }

            // LAYOUT OF EACH CHARACTER
            val charactersLayout = LinearLayout(this)
            var charactersLayoutParams = TableRow.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            charactersLayoutParams.setMargins(5, 5, 5, 5)
            charactersLayout.layoutParams = charactersLayoutParams
            charactersLayout.orientation = LinearLayout.VERTICAL  // Cambiado a vertical
            charactersLayout.gravity = Gravity.CENTER

            charactersLayout.setOnClickListener {
                AppController.session!!.addToTeam(character)

                findViewById<ImageView>(R.id.imgTeam1).setImageResource(resources.getIdentifier(character.getIMG(), "drawable", packageName))
                findViewById<ImageView>(R.id.imgTeamProfile1).setImageResource(resources.getIdentifier(character.getIMG(), "drawable", packageName))
            }

            fillCharactersLayout(charactersLayout, character)

            tableRow?.addView(charactersLayout)
            countItemsRow++
        }
    }

    fun alertFormPassword(context: Context) {
        val inflater            = LayoutInflater.from(context)
        val dialogView: View    = inflater.inflate(R.layout.dialog_password_view, null)

        val inputOldPass        = dialogView.findViewById<TextInputEditText>(R.id.inputOldPass)
        val inputNewPass        = dialogView.findViewById<TextInputEditText>(R.id.inputNewPass)
        val btnCancel           = dialogView.findViewById<Button>(R.id.btnCancel)
        val btnConfirm          = dialogView.findViewById<Button>(R.id.btnConfirm)

        val alertDialog = AlertDialog.Builder(context)
            .setView(dialogView)
            .setCancelable(false)
            .create()

        btnCancel.setOnClickListener {
            alertDialog.dismiss()
        }

        btnConfirm.setOnClickListener {
            val oldPassword = inputOldPass.getText().toString()
            val newPassword = inputNewPass.getText().toString()

            lifecycleScope.launch {
                    when(appController!!.modifyPassword(AppController.session!!.getUsername(), oldPassword, newPassword)) {
                        ServerState.STATE_ERROR_USERNAME -> {
                            alertDialog.dismiss()
                            appController!!.alertConfirm(this@MainActivity, "Error", "El nombre de usuario de la sesión no es correcto.").show()
                        }
                        ServerState.STATE_ERROR_EMAIL -> {
                            alertDialog.dismiss()
                            appController!!.alertConfirm(this@MainActivity, "Error", "El email de la sesión no es correcto.").show()
                        }
                        ServerState.STATE_ERROR_DATABASE -> {
                            alertDialog.dismiss()
                            appController!!.alertConfirm(this@MainActivity, "Error", "Ha ocurrido un error inesperado con la base de datos.").show()
                        }
                        ServerState.STATE_ERROR_PASSWORD -> {
                            alertDialog.dismiss()
                            appController!!.alertConfirm(this@MainActivity, "Error", "La anterior contraseña no es correcta.").show()
                        }
                        ServerState.STATE_SUCCESS -> {
                            alertDialog.dismiss()
                            appController!!.alertConfirm(this@MainActivity, "Cambio correcto", "Contraseña cambiada correctamente.").show()
                        }
                    }
            }

            alertDialog.dismiss()
        }

        alertDialog.show()
    }

    /* fun alertFormEmail(context: Context) {
        val inflater            = LayoutInflater.from(context)
        val dialogView: View    = inflater.inflate(R.layout.dialog_email_view, null)

        val inputPass           = dialogView.findViewById<TextInputEditText>(R.id.inputPass)
        val inputNewEmail       = dialogView.findViewById<TextInputEditText>(R.id.inputNewEmail)
        val btnCancel           = dialogView.findViewById<Button>(R.id.btnCancel)
        val btnConfirm          = dialogView.findViewById<Button>(R.id.btnConfirm)

        val alertDialog = AlertDialog.Builder(context)
            .setView(dialogView)
            .setCancelable(false)
            .create()

        btnCancel.setOnClickListener {
            alertDialog.dismiss()
        }

        btnConfirm.setOnClickListener {
            val password = inputPass.getText().toString()
            val newEmail = inputNewEmail.getText().toString()

            lifecycleScope.launch {
                when(appController!!.modifyEmail(AppController.session!!.getUsername(), Email(newEmail), password)) {
                    ServerState.STATE_ERROR_USERNAME -> {
                        alertDialog.dismiss()
                        appController!!.alertConfirm(this@MainActivity, "Error", "El nombre de usuario de la sesión no es correcto.").show()
                    }
                    ServerState.STATE_ERROR_EMAIL -> {
                        alertDialog.dismiss()
                        appController!!.alertConfirm(this@MainActivity, "Error", "El email de la sesión no es correcto.").show()
                    }
                    ServerState.STATE_ERROR_DATABASE -> {
                        alertDialog.dismiss()
                        appController!!.alertConfirm(this@MainActivity, "Error", "Ha ocurrido un error inesperado con la base de datos.").show()
                    }
                    ServerState.STATE_ERROR_PASSWORD -> {
                        alertDialog.dismiss()
                        appController!!.alertConfirm(this@MainActivity, "Error", "La contraseña no es correcta.").show()
                    }
                    ServerState.STATE_SUCCESS -> {
                        alertDialog.dismiss()
                        appController!!.alertConfirm(this@MainActivity, "Cambio correcto", "Email cambiado correctamente.").show()
                    }
                }
            }

            alertDialog.dismiss()
        }

        alertDialog.show()
    }*/

    /*fun alertFormUser(context: Context) {
        val inflater            = LayoutInflater.from(context)
        val dialogView: View    = inflater.inflate(R.layout.dialog_user_view, null)

        val inputPass           = dialogView.findViewById<TextInputEditText>(R.id.inputPass)
        val inputNewUser        = dialogView.findViewById<TextInputEditText>(R.id.inputNewUser)
        val btnCancel           = dialogView.findViewById<Button>(R.id.btnCancel)
        val btnConfirm          = dialogView.findViewById<Button>(R.id.btnConfirm)

        val alertDialog = AlertDialog.Builder(context)
            .setView(dialogView)
            .setCancelable(false)
            .create()

        btnCancel.setOnClickListener {
            alertDialog.dismiss()
        }

        btnConfirm.setOnClickListener {
            val password = inputPass.getText().toString()
            val newUser = inputNewUser.getText().toString()

            lifecycleScope.launch {
                when(appController!!.modifyUser(AppController.session!!.getUsername(), newUser, password)) {
                    ServerState.STATE_ERROR_USERNAME -> {
                        alertDialog.dismiss()
                        appController!!.alertConfirm(this@MainActivity, "Error", "El nombre de usuario de la sesión no es correcto.").show()
                    }
                    ServerState.STATE_ERROR_EMAIL -> {
                        alertDialog.dismiss()
                        appController!!.alertConfirm(this@MainActivity, "Error", "El email de la sesión no es correcto.").show()
                    }
                    ServerState.STATE_ERROR_DATABASE -> {
                        alertDialog.dismiss()
                        appController!!.alertConfirm(this@MainActivity, "Error", "Ha ocurrido un error inesperado con la base de datos.").show()
                    }
                    ServerState.STATE_ERROR_PASSWORD -> {
                        alertDialog.dismiss()
                        appController!!.alertConfirm(this@MainActivity, "Error", "La contraseña no es correcta.").show()
                    }
                    ServerState.STATE_SUCCESS -> {
                        alertDialog.dismiss()
                        appController!!.alertConfirm(this@MainActivity, "Cambio correcto", "Nombre de usuario cambiado correctamente.").show()
                    }
                }
            }

            alertDialog.dismiss()
        }

        alertDialog.show()
    }*/

    /* ============# MAIN VIEW #============ */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        appController = AppController(this)



        /* ==========# BOTTOM NAVIGATION #========== */

        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottom_navigation)

        bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.navigation_profile -> {
                    findViewById<LinearLayout>(R.id.lytProfile).visibility      = LinearLayout.VISIBLE
                    findViewById<LinearLayout>(R.id.lytTeam).visibility         = LinearLayout.GONE
                    findViewById<LinearLayout>(R.id.lytCharacters).visibility   = LinearLayout.GONE
                    true
                }
                R.id.navigation_team -> {
                    findViewById<LinearLayout>(R.id.lytProfile).visibility      = LinearLayout.GONE
                    findViewById<LinearLayout>(R.id.lytTeam).visibility         = LinearLayout.VISIBLE
                    findViewById<LinearLayout>(R.id.lytCharacters).visibility   = LinearLayout.GONE
                    true
                }
                R.id.navigation_characters -> {
                    findViewById<LinearLayout>(R.id.lytProfile).visibility      = LinearLayout.GONE
                    findViewById<LinearLayout>(R.id.lytTeam).visibility         = LinearLayout.GONE
                    findViewById<LinearLayout>(R.id.lytCharacters).visibility   = LinearLayout.VISIBLE
                    true
                }
                else -> false
            }
        }



        /* ==========# PROFILE LAYOUT #========== */

        val lblUser: TextView       = findViewById(R.id.lblUser)
        val lblEmail: TextView      = findViewById(R.id.lblEmail)
        val lblRole: TextView       = findViewById(R.id.lblRole)
        val lblUserTeam: TextView   = findViewById(R.id.lblUserTeamTitleProfile)

        val btnModifyPass: Button   = findViewById(R.id.btnModifyPass)
        val btnModifyEmail: Button  = findViewById(R.id.btnModifyEmail)
        val btnModifyUser: Button   = findViewById(R.id.btnModifyUsername)
        val btnLogOut: Button       = findViewById(R.id.btnLogOut)

        lblUser.text                = AppController.session!!.getUsername()
        lblEmail.text               = AppController.session!!.getEmail().toString()
        lblRole.text                = String.format("Role: %s", AppController.session!!.getRole())
        lblUserTeam.text            = String.format("%s's Team", AppController.session!!.getUsername())

        btnModifyPass.setOnClickListener {
            alertFormPassword(this)
        }

        btnModifyEmail.setOnClickListener {
            // alertFormEmail(this)
        }

        btnModifyUser.setOnClickListener {
            // alertFormUser(this)
        }

        btnLogOut.setOnClickListener {
            AppController.session = null
            appController!!.closeView(this)
        }



        /* ==========# TEAM LAYOUT #========== */

        generateChararactesGrid()



        /* ==========# CHARACTERS LAYOUT #========== */
    }
}