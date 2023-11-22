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

        this.appController = AppController(this)
        this.characterSelected = AppController.characterSelected

        val imgCharacter        = findViewById<ImageView>(R.id.imgCharacter)
        val lblName             = findViewById<TextView>(R.id.lblName)
        val lblDesc             = findViewById<TextView>(R.id.lblDesc)

        Glide.with(this).load(characterSelected!!.getIMG()).transform(RoundedCorners(16)).into(imgCharacter)
        lblName.text            = characterSelected!!.getName()
        lblDesc.text            = characterSelected!!.getDesc()
    }
}