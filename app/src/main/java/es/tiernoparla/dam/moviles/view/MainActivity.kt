package es.tiernoparla.dam.moviles.view

import android.content.Context
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import es.tiernoparla.dam.moviles.model.data.game.GameCharacter
import es.tiernoparla.dam.moviles.view.utils.CharacterListGenerator
import es.tiernoparla.dam.moviles.view.utils.ViewUtil
import kotlinx.coroutines.launch
import kotlin.properties.Delegates

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var appController: Controller? = null


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
        val DIALOG_TEAM_SAVED       = "Equipo guardado"
        val MSG_ERROR_USERNAME      = "El nombre de usuario de la sesión no es correcto."
        val MSG_ERROR_EMAIL         = "El email de la sesión no es correcto."
        val MSG_ERROR_DATABASE      = "Ha ocurrido un error inesperado con la base de datos."
        val MSG_ERROR_PASSWORD      = "La contraseña no es correcta."
        val MSG_ERROR_SUCCESS       = "El equipo ha sido guardado correctamente."

        val lblUser: TextView       = findViewById(R.id.lblUser)
        val lblEmail: TextView      = findViewById(R.id.lblEmail)
        val lblRole: TextView       = findViewById(R.id.lblRole)
        val lblUserTeam: TextView   = findViewById(R.id.lblUserTeamTitleProfile)

        val btnSaveTeam: Button     = findViewById(R.id.btnSaveTeam)
        val btnModifyPass: Button   = findViewById(R.id.btnModifyPass)
        val btnModifyEmail: Button  = findViewById(R.id.btnModifyEmail)
        val btnModifyUser: Button   = findViewById(R.id.btnModifyUsername)
        val btnLogOut: Button       = findViewById(R.id.btnLogOut)

        lblUser.text                = AppController.session!!.getUsername()
        lblEmail.text               = AppController.session!!.getEmail().toString()
        lblRole.text                = String.format("Rol: %s", AppController.session!!.getRole())
        lblUserTeam.text            = String.format("Equipo de %s", AppController.session!!.getUsername())

        btnSaveTeam.setOnClickListener {
            lifecycleScope.launch {
                when(appController!!.setTeam(AppController.session!!.getUsername(), AppController.session!!.getTeam())) {
                    ServerState.STATE_ERROR_USERNAME -> {
                        ViewUtil.alertConfirm(this@MainActivity, R.drawable.logo, ViewUtil.DIALOG_TITLE_ERROR, MSG_ERROR_USERNAME).show()
                    }
                    ServerState.STATE_ERROR_EMAIL -> {
                        ViewUtil.alertConfirm(this@MainActivity, R.drawable.logo, ViewUtil.DIALOG_TITLE_ERROR, MSG_ERROR_EMAIL).show()
                    }
                    ServerState.STATE_ERROR_DATABASE -> {
                        ViewUtil.alertConfirm(this@MainActivity, R.drawable.logo, ViewUtil.DIALOG_TITLE_ERROR, MSG_ERROR_DATABASE).show()
                    }
                    ServerState.STATE_ERROR_PASSWORD -> {
                        ViewUtil.alertConfirm(this@MainActivity, R.drawable.logo, ViewUtil.DIALOG_TITLE_ERROR, MSG_ERROR_PASSWORD).show()
                    }
                    ServerState.STATE_SUCCESS -> {
                        ViewUtil.alertConfirm(this@MainActivity, R.drawable.logo, DIALOG_TEAM_SAVED, MSG_ERROR_SUCCESS).show()
                    }
                }
            }
        }

        btnModifyUser.setOnClickListener {
            ViewUtil.alertForm(this@MainActivity.appController, this@MainActivity, this@MainActivity, ViewUtil.DIALOG_SELECTOR_USER)
        }

        btnModifyEmail.setOnClickListener {
            ViewUtil.alertForm(this.appController, this, this@MainActivity, ViewUtil.DIALOG_SELECTOR_EMAIL)
        }

        btnModifyPass.setOnClickListener {
            ViewUtil.alertForm(this.appController, this, this@MainActivity, ViewUtil.DIALOG_SELECTOR_PASS)
        }

        btnLogOut.setOnClickListener {
            AppController.session = null
            ViewUtil.closeView(this)
        }



        /* ==========# TEAM LAYOUT #========== */
        val TEAM_IMAGE_SIZE           = 135
        val TEAM_TEXT_SIZE            = 16

        buildCharactersTeam(TEAM_IMAGE_SIZE, TEAM_TEXT_SIZE)



        /* ==========# CHARACTERS LAYOUT #========== */

        val CHARACTERS_IMAGE_SIZE     = 150
        val CHARACTERS_TEXT_SIZE      = 20

        buildCharactersCollection(CHARACTERS_IMAGE_SIZE, CHARACTERS_TEXT_SIZE)
    }

    override fun onDestroy() {
        lifecycleScope.launch {
            appController!!.setTeam(AppController.session!!.getUsername(), AppController.session!!.getTeam())
        }
        super.onDestroy()
    }



    /* ============# PRIVATE FUNCTIONS #============ */

    /**
     * Construye la lista de personajes del equipo del usuario en la pantalla de selección del equipo y de perfil.
     * @param imageSize Tamaño de la imagen, siguiendo una relación de aspecto 1:1.
     * @param textSize Tamaño del texto del nombre del personaje.
     * @author Iván Vicente Morales (<a href="https://github.com/srtoxyc">@srtoxyc</a>)
     */
    private fun buildCharactersTeam(imageSize: Int, textSize: Int) {
        val DEFAULT_CHARACTER_TAG                       = "0"

        val teamListGenerator: CharacterListGenerator   = CharacterListGenerator(this, this.appController!!, imageSize, textSize)

        val tableTeam                                   = findViewById<TableLayout>(R.id.tableSelectCharacters)
        val team: MutableList<ImageView>                = ArrayList<ImageView>()
        val teamProfile: MutableList<ImageView>         = ArrayList<ImageView>()

        for(i: Int in 1..User.TEAM_MAX_SIZE) {
            var imageTeam           = findViewById<ImageView>(resources.getIdentifier("imgTeam${i}", "id", packageName))
            var imageTeamProfile    = findViewById<ImageView>(resources.getIdentifier("imgTeamProfile${i}", "id", packageName))

            // Loads each character of the team from the user session at the beginning of the application.
            if(AppController.session!!.getFromTeam(i-1) != null) {
                Glide.with(this).load(AppController.session!!.getFromTeam(i-1)!!.getIMG()).transform(RoundedCorners(16)).into(imageTeam)
                Glide.with(this).load(AppController.session!!.getFromTeam(i-1)!!.getIMG()).transform(RoundedCorners(16)).into(imageTeamProfile)

                imageTeam.tag         = AppController.session!!.getFromTeam(i-1)!!.getID().toString()
                imageTeamProfile.tag  = AppController.session!!.getFromTeam(i-1)!!.getID().toString()
            } else {
                imageTeam.setImageResource(R.drawable.none)
                imageTeamProfile.setImageResource(R.drawable.none)

                imageTeam.tag         = DEFAULT_CHARACTER_TAG
                imageTeamProfile.tag  = DEFAULT_CHARACTER_TAG
            }

            // Adds all of the images from the lists (profile and team selection sections) to a list.
            team.add(imageTeam)
            teamProfile.add(imageTeamProfile)

            // Sets an event listener to each image from the team table, representing the removal of the character from the team.
            imageTeam.setOnClickListener {
                teamImageEvent(i-1, team, teamProfile, tableTeam)
            }
        }

        // Mounts the list of characters on the team selection section.
        for(character in this.appController!!.listCharacters()) {
            teamListGenerator.mountListTeam(tableTeam, team, teamProfile, character)
        }
    }

    /**
     * Construye la lista de personajes del equipo del usuario en la pantalla de selección de la colección.
     * @param imageSize Tamaño de la imagen, siguiendo una relación de aspecto 1:1.
     * @param textSize Tamaño del texto del nombre del personaje.
     * @author Iván Vicente Morales (<a href="https://github.com/srtoxyc">@srtoxyc</a>)
     */
    private fun buildCharactersCollection(imageSize: Int, textSize: Int) {
        val collectionListGenerator: CharacterListGenerator = CharacterListGenerator(this, this.appController!!, imageSize, textSize)

        val tableCollection                                 = findViewById<TableLayout>(R.id.tableCollectionCharacters)

        for(character in this.appController!!.listCharacters()) {
            collectionListGenerator.mountListCollection(tableCollection, character)
        }
    }

    /**
     * Realiza la acción pertinente cuando se da click a la imagen de un personaje del equipo en la lista de la pantalla de selección del equipo.
     * @param imageID Identificador numérico de la imagen en la lista.
     * @param team Lista mutable de ImageView del equipo del usuario en la pantalla de selección de equipo.
     * @param teamProfile Lista mutable de ImageView del equipo del usuario en la pantalla de perfil.
     * @param table Tabla que contendrá los elementos.
     * @see ImageView
     * @see TableLayout
     * @author Iván Vicente Morales (<a href="https://github.com/srtoxyc">@srtoxyc</a>)
     */
    private fun teamImageEvent(imageID: Int, team: MutableList<ImageView>, teamProfile: MutableList<ImageView>, tableTeam: TableLayout) {
        val DEFAULT_CHARACTER_TAG       = "0"

        var imgCharacter: ImageView?    = ViewUtil.findViewByTag<ImageView>(tableTeam, team[imageID].tag.toString())

        if(imgCharacter != null) {
            // Removal of the character from the list (team) of the user.
            AppController.session!!.removeFromTeam(imageID)

            // Team tables refreshes after the removal.
            for(i in 0..<User.TEAM_MAX_SIZE) {
                if(AppController.session!!.getFromTeam(i) != null) {
                    Glide.with(this).load(AppController.session!!.getFromTeam(i)!!.getIMG()).transform(RoundedCorners(16)).into(team[i])
                    Glide.with(this).load(AppController.session!!.getFromTeam(i)!!.getIMG()).transform(RoundedCorners(16)).into(teamProfile[i])

                    team[i].tag         = AppController.session!!.getFromTeam(i)!!.getID().toString()
                    teamProfile[i].tag  = AppController.session!!.getFromTeam(i)!!.getID().toString()
                } else {
                    team[i].setImageResource(R.drawable.none)
                    teamProfile[i].setImageResource(R.drawable.none)

                    team[i].tag         = DEFAULT_CHARACTER_TAG
                    teamProfile[i].tag  = DEFAULT_CHARACTER_TAG
                }
            }

            // Enables the character selection.
            imgCharacter.isEnabled      = true
            imgCharacter.colorFilter    = null
        }
    }
}