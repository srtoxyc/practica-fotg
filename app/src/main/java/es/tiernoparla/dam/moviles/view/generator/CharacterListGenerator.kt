package es.tiernoparla.dam.moviles.view.generator

import android.content.Context
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
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

// La idea es que aquí no se toque la vista, sino que retorne los elementos.
// Por ende, esta clase no tendrá acceso a elementos de la vista, sino que se le pasarán por parámetros.

class CharacterListGenerator(
    private var context: Context,
    private var appController: Controller?
) {
    public fun fillCharactersLayout(layout: LinearLayout, character: GameCharacter, imageTeam: ImageView, imageTeamProfile: ImageView) {
        val frameCharacter      = FrameLayout(this.context)
        val viewCharacter       = View(this.context)
        val imgCharacter        = ImageView(this.context)
        val nameCharacter       = TextView(this.context)

        val imageSize           = 135
        val textSize            = 16

        // FRAME ATTRIBUTES
        frameCharacter.layoutParams = FrameLayout.LayoutParams(
            imageSize,
            imageSize
        )

        // VIEW ATTRIBUTES
        viewCharacter.layoutParams = ViewGroup.LayoutParams(
            imageSize,
            imageSize
        )
        viewCharacter.setBackgroundResource(R.drawable.image_border)

        // CHARACTER'S IMAGE ATTRIBUTES
        imgCharacter.layoutParams = LinearLayout.LayoutParams(
            imageSize,
            imageSize
        )
        imgCharacter.setImageResource(R.drawable.logo)
        imgCharacter.setBackgroundResource(R.drawable.image_border)
        imgCharacter.scaleType  = ImageView.ScaleType.CENTER_CROP
        imgCharacter.tag        = String.format("imgCharacter%d", character.getID())

        Glide.with(this.context).load(character.getIMG()).transform(RoundedCorners(16)).into(imgCharacter)

        imgCharacter.setOnClickListener {
            // var imageTeam           = findViewById<ImageView>(resources.getIdentifier("imgTeam${User.getTeamElementsCount()}", "id", packageName))
            // var imageTeamProfile    = findViewById<ImageView>(resources.getIdentifier("imgTeamProfile${User.getTeamElementsCount()}", "id", packageName))

            AppController.session!!.addToTeam(character)

            if(User.getTeamElementsCount() <= User.TEAM_MAX_SIZE) {
                Glide.with(this.context).load(character.getIMG()).transform(RoundedCorners(16)).into(imageTeam)
                Glide.with(this.context).load(character.getIMG()).transform(RoundedCorners(16)).into(imageTeamProfile)

                imgCharacter.colorFilter    = ColorMatrixColorFilter(ColorMatrix().apply { setSaturation(0f) })
                imageTeam.tag               = imgCharacter.tag
                imageTeamProfile.tag        = imgCharacter.tag
                imgCharacter.setEnabled(false)
            } else {
                appController!!.alertConfirm(this.context, "Full team", "Your team is already full.")
            }
        }

        // CHARACTER'S NAME ATTRIBUTES
        var characterNameParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        characterNameParams.setMargins(0, 2, 0, 0)
        nameCharacter.setTextColor(ContextCompat.getColor(this.context, R.color.golden_light))
        nameCharacter.layoutParams  = characterNameParams
        nameCharacter.textSize      = textSize.toFloat()
        nameCharacter.typeface      = ResourcesCompat.getFont(this.context, R.font.cinzel)
        nameCharacter.text          = character.getName()
        nameCharacter.gravity       = Gravity.CENTER

        frameCharacter.addView(imgCharacter)
        frameCharacter.addView(viewCharacter)
        layout.addView(frameCharacter)
        layout.addView(nameCharacter)
    }

    public fun generateChararactesGrid(table: TableLayout) {
        // val tableTeam: TableLayout = findViewById(R.id.tableSelectCharacters)

        val ITEMS_PER_ROW: Int = 4
        var countItemsRow: Int = 0

        var tableRow: TableRow? = null

        for (character in appController!!.listCharacters()) {

            // NEW ROW EACH TIME ONE GETS FILLED.
            if (countItemsRow == 0 || countItemsRow == ITEMS_PER_ROW) {
                tableRow = TableRow(this.context)
                tableRow.layoutParams = TableLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                tableRow.gravity = Gravity.CENTER

                table.addView(tableRow)
                countItemsRow = 0
            }

            // LAYOUT OF EACH CHARACTER
            val charactersLayout        = LinearLayout(this.context)
            var charactersLayoutParams  = TableRow.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            charactersLayoutParams.setMargins(5, 5, 5, 5)
            charactersLayout.layoutParams   = charactersLayoutParams
            charactersLayout.orientation    = LinearLayout.VERTICAL
            charactersLayout.gravity        = Gravity.CENTER

            //fillCharactersLayout(charactersLayout, character)

            tableRow?.addView(charactersLayout)
            countItemsRow++
        }
    }

    private fun generateImage(character: GameCharacter, imageTeam: ImageView, imageTeamProfile: ImageView) {
        val imgCharacter        = ImageView(this.context)
        val imageSize           = 135


        // ==========# ATTRIBUTES #========== \\
        imgCharacter.layoutParams = LinearLayout.LayoutParams(
            imageSize,
            imageSize
        )
        imgCharacter.setImageResource(R.drawable.logo)
        imgCharacter.setBackgroundResource(R.drawable.image_border)
        imgCharacter.scaleType  = ImageView.ScaleType.CENTER_CROP
        imgCharacter.tag        = String.format("imgCharacter%d", character.getID())

        Glide.with(this.context).load(character.getIMG()).transform(RoundedCorners(16)).into(imgCharacter)

        // Se puede sacar, la idea es que retorne la imagen, pero el event listener se lo metería un método mayor.
        // ==========# EVENT LISTENERS #========== \\
        imgCharacter.setOnClickListener {
            AppController.session!!.addToTeam(character)

            if(User.getTeamElementsCount() <= User.TEAM_MAX_SIZE) {
                Glide.with(this.context).load(character.getIMG()).transform(RoundedCorners(16)).into(imageTeam)
                Glide.with(this.context).load(character.getIMG()).transform(RoundedCorners(16)).into(imageTeamProfile)

                imgCharacter.colorFilter    = ColorMatrixColorFilter(ColorMatrix().apply { setSaturation(0f) })
                imageTeam.tag               = imgCharacter.tag
                imageTeamProfile.tag        = imgCharacter.tag
                imgCharacter.setEnabled(false)
            } else {
                appController!!.alertConfirm(this.context, "Full team", "Your team is already full.")
            }
        }
    }
}