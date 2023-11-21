package es.tiernoparla.dam.moviles.view

import android.content.Context
import android.graphics.Color
import android.os.Bundle
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
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.textfield.TextInputEditText
import es.tiernoparla.dam.moviles.R
import es.tiernoparla.dam.moviles.controller.AppController
import es.tiernoparla.dam.moviles.controller.Controller
import es.tiernoparla.dam.moviles.databinding.ActivityMainBinding
import es.tiernoparla.dam.moviles.model.data.game.GameCharacter

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var appController: Controller? = null

    private fun createCharactersRow(): TableRow {
        var tableRow = TableRow(this)

        // TableRow attributes.
        tableRow.setLayoutParams(
            TableLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        )

        return tableRow
    }

    private fun createCharactersLayout(character: GameCharacter): LinearLayout {
        var layout = LinearLayout(this)

        // LinearLayout attributes.
        layout.setLayoutParams(
            TableRow.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        )
        layout.setOrientation(LinearLayout.HORIZONTAL)
        layout.setGravity(Gravity.CENTER)
        layout.setBackgroundColor(Color.parseColor("#EDEDED"))

        fillCharactersLayout(layout, character)

        return layout
    }

    private fun fillCharactersLayout(layout: LinearLayout, character: GameCharacter) {
        var imgCharacter: ImageView = ImageView(this)
        var nameCharacter: TextView = TextView(this)

        imgCharacter.setLayoutParams(
            LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        )
        imgCharacter.setImageResource(R.drawable.ic_launcher_background)    // CAMBIAR.

        nameCharacter.setLayoutParams(
            LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        )
        nameCharacter.setText(character.getName())
        nameCharacter.setGravity(Gravity.CENTER)
    }

    fun alertFormPassword(context: Context) {
        val inflater            = LayoutInflater.from(context)
        val dialogView: View    = inflater.inflate(R.layout.dialog_password_view, null)

        val lblTitle            = dialogView.findViewById<TextView>(R.id.lblTitle)
        val inputOldPass        = dialogView.findViewById<TextInputEditText>(R.id.inputOldPass)
        val inputNewPass        = dialogView.findViewById<TextInputEditText>(R.id.inputNewPass)
        val btnCancel           = dialogView.findViewById<Button>(R.id.btnCancel)
        val btnConfirm          = dialogView.findViewById<Button>(R.id.btnConfirm)

        val builder = AlertDialog.Builder(context)
        builder.setView(dialogView)
        builder.setCancelable(false)

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

            // appController!!.modifyPass()

            alertDialog.dismiss()
        }

        alertDialog.show()
    }



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
            //alertFormEmail(this)
        }

        btnModifyUser.setOnClickListener {
            //alertFormUser(this)
        }

        btnLogOut.setOnClickListener {
            AppController.session = null
            appController!!.closeView(this)
        }



        /* ==========# TEAM LAYOUT #========== */

        val tableTeam: TableLayout  = findViewById(R.id.tableTeam)
        val ITEMS_PER_ROW: Int      = 2

        var countItemsRow: Int      = 0

        for(character in appController!!.listCharacters()) {
            var tableRow: TableRow?             = findViewById(R.id.charactersRowTeams1)
            var lytCharacter: LinearLayout      = this.createCharactersLayout(character)

            if(countItemsRow == ITEMS_PER_ROW) {
                tableRow = this.createCharactersRow()
                countItemsRow = 0
            }

            tableRow?.addView(lytCharacter)
            countItemsRow++
        }



        /* ==========# CHARACTERS LAYOUT #========== */
    }
}