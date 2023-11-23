package es.tiernoparla.dam.moviles.view

import android.content.Context
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TableLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.textfield.TextInputEditText
import es.tiernoparla.dam.moviles.R
import es.tiernoparla.dam.moviles.controller.AppController
import es.tiernoparla.dam.moviles.controller.Controller
import es.tiernoparla.dam.moviles.data.User
import es.tiernoparla.dam.moviles.databinding.ActivityMainBinding
import es.tiernoparla.dam.moviles.model.data.Email
import es.tiernoparla.dam.moviles.model.data.account.ServerState
import es.tiernoparla.dam.moviles.view.utils.CharacterListGenerator
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var appController: Controller? = null

    private fun buildCharactersTeam() {
        val teamListGenerator: CharacterListGenerator = CharacterListGenerator(this, this.appController!!, 135, 16)

        val tableTeam                                   = findViewById<TableLayout>(R.id.tableSelectCharacters)
        val team: MutableList<ImageView>                = ArrayList<ImageView>()
        val teamProfile: MutableList<ImageView>         = ArrayList<ImageView>()

        for(i: Int in 1..User.TEAM_MAX_SIZE) {
            var imageTeam           = findViewById<ImageView>(resources.getIdentifier("imgTeam${i}", "id", packageName))
            var imageTeamProfile    = findViewById<ImageView>(resources.getIdentifier("imgTeamProfile${i}", "id", packageName))

            if(AppController.session!!.getFromTeam(i-1) != null) {
                Glide.with(this).load(AppController.session!!.getFromTeam(i-1)!!.getIMG()).transform(RoundedCorners(16)).into(imageTeam)
                Glide.with(this).load(AppController.session!!.getFromTeam(i-1)!!.getIMG()).transform(RoundedCorners(16)).into(imageTeamProfile)
            } else {
                imageTeam.setImageResource(R.drawable.none)
                imageTeamProfile.setImageResource(R.drawable.none)
            }

            team.add(imageTeam)
            teamProfile.add(imageTeamProfile)
        }

        for(character in this.appController!!.listCharacters()) {
            teamListGenerator.mountListTeam(tableTeam, team, teamProfile, character)
        }
    }

    private fun buildCharactersCollection() {
        val collectionListGenerator: CharacterListGenerator = CharacterListGenerator(this, this.appController!!, 150, 20)

        val tableCollection                                 = findViewById<TableLayout>(R.id.tableCollectionCharacters)

        for(character in this.appController!!.listCharacters()) {
            collectionListGenerator.mountListCollection(tableCollection, character)
        }
    }

    private suspend fun modifyUserEvent(inputField1: EditText, inputField2: EditText, alertDialog: AlertDialog) {
        when(appController!!.modifyUser(AppController.session!!.getUsername(), inputField1.getText().toString(), inputField2.getText().toString())) {
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
                this.appController!!.refreshSession(inputField1.getText().toString(), inputField2.getText().toString())
                alertDialog.dismiss()
                appController!!.alertConfirm(this@MainActivity, "Cambio correcto", "Nombre de usuario cambiado correctamente.").show()
            }
        }
    }

    private suspend fun modifyEmailEvent(inputField1: EditText, inputField2: EditText, alertDialog: AlertDialog) {
        when(appController!!.modifyEmail(AppController.session!!.getUsername(), Email(inputField1.getText().toString()), inputField2.getText().toString())) {
            ServerState.STATE_ERROR_USERNAME -> {
                alertDialog.dismiss()
                appController!!.alertConfirm(this@MainActivity, "Error", "El nombre de usuario de la sesión no es correcto.").show()
            }
            ServerState.STATE_ERROR_EMAIL -> {
                alertDialog.dismiss()
                appController!!.alertConfirm(this@MainActivity, "Error", "El email no es correcto.").show()
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
                this.appController!!.refreshSession(AppController.session!!.getUsername(), inputField2.getText().toString())
                alertDialog.dismiss()
                appController!!.alertConfirm(this@MainActivity, "Cambio correcto", "Email cambiado correctamente.").show()
            }
        }
    }

    private suspend fun modifyPassEvent(inputField1: EditText, inputField2: EditText, alertDialog: AlertDialog) {
        when(appController!!.modifyPassword(AppController.session!!.getUsername(), inputField1.getText().toString(), inputField2.getText().toString())) {
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
                this.appController!!.refreshSession(AppController.session!!.getUsername(), inputField2.getText().toString())
                alertDialog.dismiss()
                appController!!.alertConfirm(this@MainActivity, "Cambio correcto", "Contraseña cambiada correctamente.").show()
            }
        }
    }

    fun alertForm(context: Context, selector: String) {
        val SELECTOR_USER       = "USER"
        val SELECTOR_EMAIL      = "EMAIL"
        val SELECTOR_PASS       = "PASS"

        val SELECTOR_ERR_TITLE  = "Error"
        val SELECTOR_ERR_MSG    = "Wrong selector used to display the form alert."

        val inflater            = LayoutInflater.from(context)
        val dialogView: View    = inflater.inflate(R.layout.dialog_view, null)

        val inputField1         = dialogView.findViewById<EditText>(R.id.inputField1)
        val inputField2         = dialogView.findViewById<EditText>(R.id.inputField2)
        val btnCancel           = dialogView.findViewById<Button>(R.id.btnCancel)
        val btnConfirm          = dialogView.findViewById<Button>(R.id.btnConfirm)

        val alertDialog = AlertDialog.Builder(context)
            .setView(dialogView)
            .setCancelable(false)
            .create()

        when(selector) {
            SELECTOR_USER -> {
                inputField1.hint = SpannableStringBuilder("New Username");
                inputField2.hint = SpannableStringBuilder("Password");

                btnConfirm.setOnClickListener {
                    lifecycleScope.launch {
                        modifyUserEvent(inputField1, inputField2, alertDialog)
                    }

                    alertDialog.dismiss()

                }
            }
            SELECTOR_EMAIL -> {
                inputField1.hint = SpannableStringBuilder("New Email");
                inputField2.hint = SpannableStringBuilder("Password");

                btnConfirm.setOnClickListener {
                    lifecycleScope.launch {
                        modifyEmailEvent(inputField1, inputField2, alertDialog)
                    }

                    alertDialog.dismiss()
                }
            }
            SELECTOR_PASS -> {
                inputField1.hint = SpannableStringBuilder("Old Password");
                inputField2.hint = SpannableStringBuilder("New Password");

                btnConfirm.setOnClickListener {
                    lifecycleScope.launch {
                        modifyPassEvent(inputField1, inputField2, alertDialog)
                    }

                    alertDialog.dismiss()
                }
            }
            else -> appController!!.alertConfirm(this, SELECTOR_ERR_TITLE, SELECTOR_ERR_MSG).show()
        }

        btnCancel.setOnClickListener {
            alertDialog.dismiss()
        }

        alertDialog.show()
    }



    /* ============# MAIN VIEW #============ */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        this.appController = AppController(this)



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

        btnModifyUser.setOnClickListener {
            alertForm(this, "USER")

            lblUser.text                = AppController.session!!.getUsername()
            lblEmail.text               = AppController.session!!.getEmail().toString()
            lblRole.text                = String.format("Role: %s", AppController.session!!.getRole())
        }

        btnModifyEmail.setOnClickListener {
            alertForm(this, "EMAIL")

            lblUser.text                = AppController.session!!.getUsername()
            lblEmail.text               = AppController.session!!.getEmail().toString()
            lblRole.text                = String.format("Role: %s", AppController.session!!.getRole())
        }

        btnModifyPass.setOnClickListener {
            alertForm(this, "PASS")

            lblUser.text                = AppController.session!!.getUsername()
            lblEmail.text               = AppController.session!!.getEmail().toString()
            lblRole.text                = String.format("Role: %s", AppController.session!!.getRole())
        }

        btnLogOut.setOnClickListener {
            AppController.session = null
            this.appController!!.closeView(this)
        }



        /* ==========# TEAM LAYOUT #========== */

        buildCharactersTeam()



        /* ==========# CHARACTERS LAYOUT #========== */

        buildCharactersCollection()
    }
}