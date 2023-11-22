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

// La idea es que aquí no se toque la vista, sino que retorne los elementos.
// Por ende, esta clase no tendrá acceso a elementos de la vista, sino que se le pasarán por parámetros.

class CharacterListGenerator(
    private var context: Context,
    private var appController: Controller?,
    private val imageSize: Int,
    private val textSize: Int
) {
    private var countItemsRow: Int     = 0
    private var tableRow: TableRow?    = null

    private fun applyTeamListener(imgCharacter: ImageView, team: MutableList<ImageView>, teamProfile: MutableList<ImageView>, character: GameCharacter) {
        val ERR_TITLE: String       = "Full team"
        val ERR_MSG: String         = "Your team is already full."

        imgCharacter.setOnClickListener {
            if(User.getTeamElementsCount() < User.TEAM_MAX_SIZE) {
                Glide.with(this.context).load(character.getIMG()).transform(RoundedCorners(16)).into(team[User.getTeamElementsCount()])
                Glide.with(this.context).load(character.getIMG()).transform(RoundedCorners(16)).into(teamProfile[User.getTeamElementsCount()])

                imgCharacter.colorFilter                            = ColorMatrixColorFilter(ColorMatrix().apply { setSaturation(0f) })
                team[User.getTeamElementsCount()].tag               = imgCharacter.tag
                teamProfile[User.getTeamElementsCount()].tag        = imgCharacter.tag

                imgCharacter.setEnabled(false)

                AppController.session!!.addToTeam(character)
            } else {
                appController!!.alertConfirm(this.context, ERR_TITLE, ERR_MSG).show()
            }
        }
    }

    private fun applyCollectionListener(imgCharacter: ImageView, character: GameCharacter) {
        imgCharacter.setOnClickListener {
            AppController.characterSelected = character
            appController!!.openView(CharacterActivity::class.java)
        }
    }

    private fun generateTableRow(): TableRow {
        val tableRow = TableRow(this.context)

        tableRow.layoutParams = TableLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        tableRow.gravity = Gravity.CENTER

        return tableRow
    }

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

    private fun generateFrame(): FrameLayout {
        val frameCharacter = FrameLayout(this.context)

        frameCharacter.layoutParams = FrameLayout.LayoutParams(
            this.imageSize,
            this.imageSize
        )

        return frameCharacter
    }

    private fun generateView(): View {
        val viewCharacter = View(this.context)

        viewCharacter.layoutParams = ViewGroup.LayoutParams(
            this.imageSize,
            this.imageSize
        )
        viewCharacter.setBackgroundResource(R.drawable.image_border)

        return viewCharacter
    }

    private fun generateImage(character: GameCharacter): ImageView {
        val imgCharacter        = ImageView(this.context)

        imgCharacter.layoutParams = LinearLayout.LayoutParams(
            this.imageSize,
            this.imageSize
        )
        imgCharacter.setImageResource(R.drawable.logo)
        imgCharacter.setBackgroundResource(R.drawable.image_border)
        imgCharacter.scaleType  = ImageView.ScaleType.CENTER_CROP
        imgCharacter.tag        = String.format("imgCharacter%d", character.getID())

        Glide.with(this.context).load(character.getIMG()).transform(RoundedCorners(16)).into(imgCharacter)

        return imgCharacter
    }

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