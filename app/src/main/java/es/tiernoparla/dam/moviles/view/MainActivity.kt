package es.tiernoparla.dam.moviles.view

import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TableLayout
import android.widget.TextView
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import es.tiernoparla.dam.moviles.R
import es.tiernoparla.dam.moviles.controller.AppController
import es.tiernoparla.dam.moviles.controller.Controller
import es.tiernoparla.dam.moviles.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val appController: Controller = AppController(this)


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

        lblUser.text                = AppController.session!!.getUsername()
        lblEmail.text               = AppController.session!!.getEmail().toString()
        lblRole.text                = String.format("Role: %s", AppController.session!!.getRole())
        lblUserTeam.text            = String.format("%s's Team", AppController.session!!.getUsername())


        /* ==========# TEAM LAYOUT #========== */
        val tableTeam: TableLayout = findViewById(R.id.tableTeam)

        for(character in appController.listCharacters()) {

        }

        /* ==========# CHARACTERS LAYOUT #========== */
    }
}