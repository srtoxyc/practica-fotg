package es.tiernoparla.dam.moviles.view

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import es.tiernoparla.dam.moviles.R
import es.tiernoparla.dam.moviles.controller.AppController
import es.tiernoparla.dam.moviles.controller.Controller
import es.tiernoparla.dam.moviles.data.User
import es.tiernoparla.dam.moviles.databinding.ActivityMainBinding
import es.tiernoparla.dam.moviles.model.data.game.GameCharacter

class CharacterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var appController: Controller?          = null
    private var characterSelected: GameCharacter?   = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.character_view)

        this.appController      = AppController(this)
        this.characterSelected  = AppController.characterSelected

        val imgCharacter        = findViewById<ImageView>(R.id.imgCharacter)
        val lblName             = findViewById<TextView>(R.id.lblName)
        val lblDesc             = findViewById<TextView>(R.id.lblDesc)
        val lblAttack           = findViewById<TextView>(R.id.lblAttack)
        val lblDefense          = findViewById<TextView>(R.id.lblDefense)
        val lblAccuracy         = findViewById<TextView>(R.id.lblAccuracy)
        val lblLife             = findViewById<TextView>(R.id.lblLife)
        val lblEther            = findViewById<TextView>(R.id.lblEther)
        val lblMovement         = findViewById<TextView>(R.id.lblMovement)
        val lblAbilityTitle     = findViewById<TextView>(R.id.lblAbilityTitle)
        val lblAbility          = findViewById<TextView>(R.id.lblAbility)
        val lblAbilityDesc      = findViewById<TextView>(R.id.lblAbilityDesc)

        Glide.with(this).load(characterSelected!!.getIMG()).transform(RoundedCorners(16)).into(imgCharacter)
        lblName.text            = characterSelected!!.getName()
        lblDesc.text            = characterSelected!!.getDesc()
        lblAttack.text          = characterSelected!!.getAttack().toString()
        lblDefense.text         = characterSelected!!.getDefense().toString()
        lblAccuracy.text        = characterSelected!!.getAccuracy().toString()
        lblLife.text            = characterSelected!!.getLife().toString()
        lblEther.text           = characterSelected!!.getEther().toString()
        lblMovement.text        = characterSelected!!.getMovement().toString()
        lblAbilityTitle.text    = String.format("Habilidad de %s", characterSelected!!.getName())
        lblAbility.text         = characterSelected!!.getAbility().getName()
        lblAbilityDesc.text     = characterSelected!!.getAbility().getDesc()
    }
}