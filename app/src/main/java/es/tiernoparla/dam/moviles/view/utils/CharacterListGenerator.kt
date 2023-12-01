package es.tiernoparla.dam.moviles.view.utils

import android.content.Context
import android.graphics.Color
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import es.tiernoparla.dam.moviles.R
import es.tiernoparla.dam.moviles.controller.AppController
import es.tiernoparla.dam.moviles.controller.Controller
import es.tiernoparla.dam.moviles.data.User
import es.tiernoparla.dam.moviles.model.data.game.GameCharacter
import es.tiernoparla.dam.moviles.view.CharacterActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

/**
 * Su objetivo es proveer de la funcionalidad necesaria para generar listas de personajes sobre tablas.
 * @param context Contexto sobre el que se generará la lista.
 * @param appController Objeto controlador de la app (MVC).
 * @param imageSize Tamaño de la imagen (relación de aspecto 1:1) de cada personaje de la lista.
 * @param textSize Tamaño del texto del nombre de cada personaje.
 * @author Iván Vicente Morales (<a href="https://github.com/srtoxyc">@srtoxyc</a>)
 * @see Context
 * @see TableLayout
 * @see TableRow
 */
class CharacterListGenerator(
    private var context: Context,
    private var appController: Controller?,
    private val imageSize: Int,
    private val textSize: Int
) {
    private var countItemsRow: Int     = 0
    private var tableRow: TableRow?    = null

    /**
     * Aplica un event listener a cada imagen de la colección de personajes del equipo en la pantalla de selección de equipo.
     * @param imgCharacter ImagenView del personaje.
     * @param team Lista mutable de ImageView del equipo del usuario en la pantalla de selección de equipo.
     * @param teamProfile Lista mutable de ImageView del equipo del usuario en la pantalla de perfil.
     * @param character Personaje del juego.
     * @see ImageView
     * @author Iván Vicente Morales (<a href="https://github.com/srtoxyc">@srtoxyc</a>)
     */
    private fun applyTeamListener(imgCharacter: ImageView, team: MutableList<ImageView>, teamProfile: MutableList<ImageView>, character: GameCharacter) {
        val ERR_TITLE: String       = "Full team"
        val ERR_MSG: String         = "Your team is already full."

        imgCharacter.setOnClickListener {
            if(User.getTeamElementsCount() < User.TEAM_MAX_SIZE) {
                // Adds the image of the character to the correct position on the table.
                Glide.with(this.context).load(character.getIMG()).transform(RoundedCorners(16)).into(team[User.getTeamElementsCount()])
                Glide.with(this.context).load(character.getIMG()).transform(RoundedCorners(16)).into(teamProfile[User.getTeamElementsCount()])

                // Sets a tag on the ImageView with the character's ID that has been added to the tables position.
                team[User.getTeamElementsCount()].tag               = character.getID().toString()
                teamProfile[User.getTeamElementsCount()].tag        = character.getID().toString()

                // Sets a color filter to the character image and disables it.
                imgCharacter.colorFilter = ColorMatrixColorFilter(ColorMatrix().apply { setSaturation(0f) })
                imgCharacter.setEnabled(false)

                AppController.session!!.addToTeam(character)
            } else {
                ViewUtil.alertConfirm(this.context, R.drawable.logo, ERR_TITLE, ERR_MSG).show()
            }
        }
    }

    /**
     * Aplica un event listener a cada imagen de la colección de personajes del equipo en la pantalla de colección de personajes.
     * @param imgCharacter ImagenView del personaje.
     * @param character Personaje del juego.
     * @see ImageView
     * @author Iván Vicente Morales (<a href="https://github.com/srtoxyc">@srtoxyc</a>)
     */
    private fun applyCollectionListener(imgCharacter: ImageView, character: GameCharacter) {
        imgCharacter.setOnClickListener {
            AppController.characterSelected = character
            ViewUtil.openView(this.context, CharacterActivity::class.java)
        }
    }

    /**
     * Genera una fila de una tabla.
     * @see TableRow
     * @author Iván Vicente Morales (<a href="https://github.com/srtoxyc">@srtoxyc</a>)
     */
    private fun generateTableRow(): TableRow {
        val tableRow = TableRow(this.context)

        tableRow.layoutParams = TableLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        tableRow.gravity = Gravity.CENTER

        return tableRow
    }

    /**
     * Genera un layout lineal.
     * @see LinearLayout
     * @author Iván Vicente Morales (<a href="https://github.com/srtoxyc">@srtoxyc</a>)
     */
    private fun generateLayout(): LinearLayout {
        val charactersLayout = LinearLayout(this.context)

        var charactersLayoutParams = TableRow.LayoutParams(
            imageSize + textSize,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        charactersLayoutParams.setMargins(0, 5, 5, 5)
        charactersLayout.layoutParams   = charactersLayoutParams
        charactersLayout.orientation    = LinearLayout.VERTICAL
        charactersLayout.gravity        = Gravity.CENTER_HORIZONTAL

        return charactersLayout
    }

    /**
     * Genera un layout de frames.
     * @see FrameLayout
     * @author Iván Vicente Morales (<a href="https://github.com/srtoxyc">@srtoxyc</a>)
     */
    private fun generateFrame(): FrameLayout {
        val frameCharacter = FrameLayout(this.context)

        frameCharacter.layoutParams = FrameLayout.LayoutParams(
            this.imageSize,
            this.imageSize
        )

        return frameCharacter
    }

    /**
     * Genera una vista.
     * @see View
     * @author Iván Vicente Morales (<a href="https://github.com/srtoxyc">@srtoxyc</a>)
     */
    private fun generateView(): View {
        val viewCharacter = View(this.context)

        viewCharacter.layoutParams = ViewGroup.LayoutParams(
            this.imageSize,
            this.imageSize
        )
        viewCharacter.setBackgroundResource(R.drawable.image_border)

        return viewCharacter
    }

    /**
     * Genera una imagen.
     * @see ImageView
     * @author Iván Vicente Morales (<a href="https://github.com/srtoxyc">@srtoxyc</a>)
     */
    private fun generateImage(character: GameCharacter): ImageView {
        val imgCharacter        = ImageView(this.context)

        imgCharacter.layoutParams = LinearLayout.LayoutParams(
            this.imageSize,
            this.imageSize
        )
        imgCharacter.setImageResource(R.drawable.logo)
        imgCharacter.setBackgroundResource(R.drawable.image_border)
        imgCharacter.scaleType  = ImageView.ScaleType.CENTER_CROP
        imgCharacter.tag        = character.getID().toString()

        Glide.with(this.context).load(character.getIMG()).transform(RoundedCorners(16)).into(imgCharacter)

        return imgCharacter
    }

    /**
     * Genera una vista de texto.
     * @see TextView
     * @author Iván Vicente Morales (<a href="https://github.com/srtoxyc">@srtoxyc</a>)
     */
    private fun generateText(character: GameCharacter): TextView {
        val nameCharacter = TextView(this.context)

        var characterNameParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        characterNameParams.setMargins(0, 2, 0, 0)

        nameCharacter.setTextColor(ContextCompat.getColor(this.context, R.color.golden_light))
        nameCharacter.layoutParams  = characterNameParams
        if(character.getName().length > 8) {
            nameCharacter.textSize = textSize.toFloat() - (textSize.toFloat() * 0.3f)
        } else {
            nameCharacter.textSize = textSize.toFloat()
        }
        nameCharacter.typeface      = ResourcesCompat.getFont(this.context, R.font.cinzel)
        nameCharacter.text          = character.getName()
        nameCharacter.gravity       = Gravity.CENTER

        return nameCharacter
    }

    /**
     * Monta la lista del equipo del usuario en la pantalla del perfil.
     * @param table Tabla que contendrá los elementos.
     * @param team Lista mutable de ImageView del equipo del usuario en la pantalla de selección de equipo.
     * @param teamProfile Lista mutable de ImageView del equipo del usuario en la pantalla de perfil.
     * @param character Personaje del juego.
     * @see ImageView
     * @see TableLayout
     * @author Iván Vicente Morales (<a href="https://github.com/srtoxyc">@srtoxyc</a>)
     */
    public fun mountListTeam(table: TableLayout, team: MutableList<ImageView>, teamProfile: MutableList<ImageView>, character: GameCharacter) {
        val layout: LinearLayout    = this.generateLayout()
        val frame: FrameLayout      = this.generateFrame()
        val view: View              = this.generateView()
        val image: ImageView        = this.generateImage(character)
        val text: TextView          = this.generateText(character)

        val ITEMS_PER_ROW: Int      = 4

        if (this.countItemsRow == 0 || this.countItemsRow == ITEMS_PER_ROW) {
            this.tableRow = this.generateTableRow()

            table.addView(this.tableRow)
            this.countItemsRow = 0
        }

        applyTeamListener(image, team, teamProfile, character)

        frame.addView(image)
        frame.addView(view)
        layout.addView(frame)
        layout.addView(text)
        this.tableRow?.addView(layout)

        this.countItemsRow++
    }

    /**
     * Monta la lista del equipo del usuario en la pantalla de la colección de personajes.
     * @param table Tabla que contendrá los elementos.
     * @param character Personaje del juego.
     * @see TableLayout
     * @author Iván Vicente Morales (<a href="https://github.com/srtoxyc">@srtoxyc</a>)
     */
    public fun mountListCollection(table: TableLayout, character: GameCharacter) {
        val layout: LinearLayout    = this.generateLayout()
        val frame: FrameLayout      = this.generateFrame()
        val view: View              = this.generateView()
        val image: ImageView        = this.generateImage(character)
        val text: TextView          = this.generateText(character)

        val ITEMS_PER_ROW: Int      = 4

        if (this.countItemsRow == 0 || this.countItemsRow == ITEMS_PER_ROW) {
            this.tableRow = this.generateTableRow()

            table.addView(this.tableRow)
            this.countItemsRow = 0
        }

        applyCollectionListener(image, character)

        frame.addView(image)
        frame.addView(view)
        layout.addView(frame)
        layout.addView(text)
        this.tableRow?.addView(layout)

        this.countItemsRow++
    }
}