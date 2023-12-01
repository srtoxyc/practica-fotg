package es.tiernoparla.dam.moviles.view.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import es.tiernoparla.dam.moviles.R
import es.tiernoparla.dam.moviles.controller.AppController
import es.tiernoparla.dam.moviles.controller.Controller
import es.tiernoparla.dam.moviles.data.User

/**
 * Singleton que provee de múltiples funcionalidades básicas de controladores de vista.
 * @author Iván Vicente Morales (<a href="https://github.com/srtoxyc">@srtoxyc</a>)
 */
object ViewUtil {
    val DIALOG_TITLE_ERROR              = "Error"
    val DIALOG_TITLE_CHANGE             = "Cambio realizado"

    val DIALOG_SELECTOR_USER            = "USER"
    val DIALOG_SELECTOR_EMAIL           = "EMAIL"
    val DIALOG_SELECTOR_PASS            = "PASS"
    val DIALOG_SELECTOR_PASS_FORGOTTEN  = "PASS_FORGOTTEN"

    /**
     * Abre una vista nueva en otra 'pestaña'.
     * @param context Contexto desde el cual se efectuará la apertura de la nueva vista.
     * @param activityClass Controlador de la vista que será abierta.
     * @author Iván Vicente Morales (<a href="https://github.com/srtoxyc">@srtoxyc</a>)
     * @see Context
     * @see AppCompatActivity
     */
    fun openView(context: Context, activityClass: Class<out AppCompatActivity>) {
        context.startActivity(Intent(context, activityClass))
    }

    /**
     * Cierra la vista actual, volviendo a la anterior.
     * @param activity Actividad actual que va a ser cerrada.
     * @author Iván Vicente Morales (<a href="https://github.com/srtoxyc">@srtoxyc</a>)
     * @see AppCompatActivity
     */
    fun closeView(activity: AppCompatActivity) {
        activity.finish()
    }

    /**
     * Devuelve un diálogo de alerta informativo con un botón de confirmación.
     * Es personalizable con logo, título y mensaje a mostrar.
     * @param context Contexto sobre el cual se mostrará el diálogo.
     * @param logo Identificador (referencia) de la imagen a usar como logo.
     * @param title Título del diálogo.
     * @param msg Mensaje del diálogo.
     * @return Diálogo de alerta listo para ser mostrado.
     * @author Iván Vicente Morales (<a href="https://github.com/srtoxyc">@srtoxyc</a>)
     * @see Context
     */
    fun alertConfirm(context: Context, logo: Int, title: String, msg: String): AlertDialog {
        val MSG_CONFIRM: String = "Agree"

        return AlertDialog.Builder(context)
            .setTitle(title)
            .setMessage(msg)
            .setIcon(logo)
            .setPositiveButton(MSG_CONFIRM, null)
            .create()
    }

    /**
     * Busca recursivamente dentro de un contenedor dado, un elemento con la etiqueta especificada.
     * Devolverá ese elemento si lo encuentra, si no, devolverá null.
     * @param viewGroup Contenedor de vista sobre el que buscará.
     * @param tag Etiqueta del elemento a buscar.
     * @return Elemento encontrado, o null si no existe.
     * @author Iván Vicente Morales (<a href="https://github.com/srtoxyc">@srtoxyc</a>)
     */
    fun <T : View> findViewByTag(viewGroup: ViewGroup, tag: String): T? {
        for (i in 0 until viewGroup.childCount) {
            val child = viewGroup.getChildAt(i)
            if (child is ViewGroup) {
                val result = findViewByTag<T>(child, tag)
                if (result != null) {
                    return result
                }
            } else if (child!!.tag == tag) {
                return child as T
            }
        }
        return null
    }
}