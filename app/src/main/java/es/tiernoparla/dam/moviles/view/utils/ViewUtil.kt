package es.tiernoparla.dam.moviles.view.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import es.tiernoparla.dam.moviles.R
import es.tiernoparla.dam.moviles.controller.AppController
import es.tiernoparla.dam.moviles.controller.Controller
import es.tiernoparla.dam.moviles.data.User
import es.tiernoparla.dam.moviles.model.data.Email
import es.tiernoparla.dam.moviles.model.data.account.ServerState
import kotlinx.coroutines.launch

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
     * Evento de modificación del nombre del usuario utilizando un AlertDialog.
     * @param appController Controlador de la aplicación asociado a la vista sobre la que se muestra el diálogo de alerta.
     * @param context Contexto de la vista sobre la que se muestra el diálogo de alerta.
     * @param inputField1 Campo 1 del formulario de modificación.
     * @param inputField2 Campo 2 del formulario de modificación.
     * @param alertDialog Diálogo de alerta a mostrar por pantalla.
     * @see EditText
     * @see AlertDialog
     * @author Iván Vicente Morales (<a href="https://github.com/srtoxyc">@srtoxyc</a>)
     */
    private suspend fun modifyUserEvent(appController: Controller?, context: Context, inputField1: EditText, inputField2: EditText, alertDialog: AlertDialog) {
        val MSG_ERROR_USERNAME      = "El nombre de usuario de la sesión no es correcto."
        val MSG_ERROR_EMAIL         = "El email de la sesión no es correcto."
        val MSG_ERROR_DATABASE      = "Ha ocurrido un error inesperado con la base de datos."
        val MSG_ERROR_PASSWORD      = "La contraseña no es correcta."
        val MSG_ERROR_SUCCESS       = "Nombre de usuario cambiado correctamente."

        when(appController!!.modifyUser(AppController.session!!.getUsername(), inputField1.getText().toString(), inputField2.getText().toString())) {
            ServerState.STATE_ERROR_USERNAME -> {
                alertDialog.dismiss()
                ViewUtil.alertConfirm(context, R.drawable.logo,ViewUtil.DIALOG_TITLE_ERROR, MSG_ERROR_USERNAME).show()
            }
            ServerState.STATE_ERROR_EMAIL -> {
                alertDialog.dismiss()
                ViewUtil.alertConfirm(context, R.drawable.logo, ViewUtil.DIALOG_TITLE_ERROR, MSG_ERROR_EMAIL).show()
            }
            ServerState.STATE_ERROR_DATABASE -> {
                alertDialog.dismiss()
                ViewUtil.alertConfirm(context, R.drawable.logo, ViewUtil.DIALOG_TITLE_ERROR, MSG_ERROR_DATABASE).show()
            }
            ServerState.STATE_ERROR_PASSWORD -> {
                alertDialog.dismiss()
                ViewUtil.alertConfirm(context, R.drawable.logo, ViewUtil.DIALOG_TITLE_ERROR, MSG_ERROR_PASSWORD).show()
            }
            ServerState.STATE_SUCCESS -> {
                appController!!.refreshSession(inputField1.getText().toString(), inputField2.getText().toString())
                alertDialog.dismiss()
                ViewUtil.alertConfirm(context, R.drawable.logo, ViewUtil.DIALOG_TITLE_CHANGE, MSG_ERROR_SUCCESS).show()
            }
        }
    }

    /**
     * Evento de modificación del correo electrónico del usuario utilizando un AlertDialog.
     * @param appController Controlador de la aplicación asociado a la vista sobre la que se muestra el diálogo de alerta.
     * @param context Contexto de la vista sobre la que se muestra el diálogo de alerta.
     * @param inputField1 Campo 1 del formulario de modificación.
     * @param inputField2 Campo 2 del formulario de modificación.
     * @param alertDialog Diálogo de alerta a mostrar por pantalla.
     * @see EditText
     * @see AlertDialog
     * @author Iván Vicente Morales (<a href="https://github.com/srtoxyc">@srtoxyc</a>)
     */
    private suspend fun modifyEmailEvent(appController: Controller?, context: Context, inputField1: EditText, inputField2: EditText, alertDialog: AlertDialog) {
        val MSG_ERROR_USERNAME      = "El nombre de usuario de la sesión no es correcto."
        val MSG_ERROR_EMAIL         = "El email no es correcto."
        val MSG_ERROR_DATABASE      = "Ha ocurrido un error inesperado con la base de datos."
        val MSG_ERROR_PASSWORD      = "La contraseña no es correcta."
        val MSG_ERROR_SUCCESS       = "Email cambiado correctamente."

        when(appController!!.modifyEmail(AppController.session!!.getUsername(), Email(inputField1.getText().toString()), inputField2.getText().toString())) {
            ServerState.STATE_ERROR_USERNAME -> {
                alertDialog.dismiss()
                ViewUtil.alertConfirm(context, R.drawable.logo, ViewUtil.DIALOG_TITLE_ERROR, MSG_ERROR_USERNAME).show()
            }
            ServerState.STATE_ERROR_EMAIL -> {
                alertDialog.dismiss()
                ViewUtil.alertConfirm(context, R.drawable.logo, ViewUtil.DIALOG_TITLE_ERROR, MSG_ERROR_EMAIL).show()
            }
            ServerState.STATE_ERROR_DATABASE -> {
                alertDialog.dismiss()
                ViewUtil.alertConfirm(context, R.drawable.logo, ViewUtil.DIALOG_TITLE_ERROR, MSG_ERROR_DATABASE).show()
            }
            ServerState.STATE_ERROR_PASSWORD -> {
                alertDialog.dismiss()
                ViewUtil.alertConfirm(context, R.drawable.logo, ViewUtil.DIALOG_TITLE_ERROR, MSG_ERROR_PASSWORD).show()
            }
            ServerState.STATE_SUCCESS -> {
                appController!!.refreshSession(AppController.session!!.getUsername(), inputField2.getText().toString())
                alertDialog.dismiss()
                ViewUtil.alertConfirm(context, R.drawable.logo, ViewUtil.DIALOG_TITLE_CHANGE, MSG_ERROR_SUCCESS).show()
            }
        }
    }

    /**
     * Evento de modificación de la contraseña del usuario utilizando un AlertDialog.
     * @param appController Controlador de la aplicación asociado a la vista sobre la que se muestra el diálogo de alerta.
     * @param context Contexto de la vista sobre la que se muestra el diálogo de alerta.
     * @param inputField1 Campo 1 del formulario de modificación.
     * @param inputField2 Campo 2 del formulario de modificación.
     * @param alertDialog Diálogo de alerta a mostrar por pantalla.
     * @see EditText
     * @see AlertDialog
     * @author Iván Vicente Morales (<a href="https://github.com/srtoxyc">@srtoxyc</a>)
     */
    private suspend fun modifyPassEvent(appController: Controller?, context: Context, inputField1: EditText, inputField2: EditText, alertDialog: AlertDialog) {
        val MSG_ERROR_USERNAME      = "El nombre de usuario de la sesión no es correcto."
        val MSG_ERROR_EMAIL         = "El email de la sesión no es correcto."
        val MSG_ERROR_DATABASE      = "Ha ocurrido un error inesperado con la base de datos."
        val MSG_ERROR_PASSWORD      = "La anterior contraseña no es correcta."
        val MSG_ERROR_SUCCESS       = "Contraseña cambiada correctamente."

        when(appController!!.modifyPassword(AppController.session!!.getUsername(), inputField1.getText().toString(), inputField2.getText().toString())) {
            ServerState.STATE_ERROR_USERNAME -> {
                alertDialog.dismiss()
                ViewUtil.alertConfirm(context, R.drawable.logo, ViewUtil.DIALOG_TITLE_ERROR, MSG_ERROR_USERNAME).show()
            }
            ServerState.STATE_ERROR_EMAIL -> {
                alertDialog.dismiss()
                ViewUtil.alertConfirm(context, R.drawable.logo, ViewUtil.DIALOG_TITLE_ERROR, MSG_ERROR_EMAIL).show()
            }
            ServerState.STATE_ERROR_DATABASE -> {
                alertDialog.dismiss()
                ViewUtil.alertConfirm(context, R.drawable.logo, ViewUtil.DIALOG_TITLE_ERROR, MSG_ERROR_DATABASE).show()
            }
            ServerState.STATE_ERROR_PASSWORD -> {
                alertDialog.dismiss()
                ViewUtil.alertConfirm(context, R.drawable.logo, ViewUtil.DIALOG_TITLE_ERROR, MSG_ERROR_PASSWORD).show()
            }
            ServerState.STATE_SUCCESS -> {
                appController!!.refreshSession(AppController.session!!.getUsername(), inputField2.getText().toString())
                alertDialog.dismiss()
                ViewUtil.alertConfirm(context, R.drawable.logo, ViewUtil.DIALOG_TITLE_CHANGE, MSG_ERROR_SUCCESS).show()
            }
        }
    }

    /**
     * Evento de modificación de la contraseña del usuario, en caso de que esta haya sido olvidada, utilizando un AlertDialog.
     * @param appController Controlador de la aplicación asociado a la vista sobre la que se muestra el diálogo de alerta.
     * @param context Contexto de la vista sobre la que se muestra el diálogo de alerta.
     * @param inputField1 Campo 1 del formulario de modificación.
     * @param inputField2 Campo 2 del formulario de modificación.
     * @param inputField3 Campo 3 del formulario de modificación.
     * @param alertDialog Diálogo de alerta a mostrar por pantalla.
     * @see EditText
     * @see AlertDialog
     * @author Iván Vicente Morales (<a href="https://github.com/srtoxyc">@srtoxyc</a>)
     */
    private suspend fun modifyPassForgottenEvent(appController: Controller?, context: Context, inputField1: EditText, inputField2: EditText, inputField3: EditText, alertDialog: AlertDialog) {
        val MSG_ERROR_USERNAME      = "El nombre de usuario no es correcto."
        val MSG_ERROR_EMAIL         = "El email no es correcto."
        val MSG_ERROR_DATABASE      = "Ha ocurrido un error inesperado con la base de datos."
        val MSG_ERROR_PASSWORD      = "La contraseña no es correcta."
        val MSG_ERROR_SUCCESS       = "Contraseña cambiada correctamente."

        when(appController!!.modifyPasswordForgotten(inputField1.getText().toString(), inputField2.getText().toString(), inputField3.getText().toString())) {
            ServerState.STATE_ERROR_USERNAME -> {
                alertDialog.dismiss()
                ViewUtil.alertConfirm(context, R.drawable.logo, ViewUtil.DIALOG_TITLE_ERROR, MSG_ERROR_USERNAME).show()
            }
            ServerState.STATE_ERROR_EMAIL -> {
                alertDialog.dismiss()
                ViewUtil.alertConfirm(context, R.drawable.logo, ViewUtil.DIALOG_TITLE_ERROR, MSG_ERROR_EMAIL).show()
            }
            ServerState.STATE_ERROR_DATABASE -> {
                alertDialog.dismiss()
                ViewUtil.alertConfirm(context, R.drawable.logo, ViewUtil.DIALOG_TITLE_ERROR, MSG_ERROR_DATABASE).show()
            }
            ServerState.STATE_ERROR_PASSWORD -> {
                alertDialog.dismiss()
                ViewUtil.alertConfirm(context, R.drawable.logo, ViewUtil.DIALOG_TITLE_ERROR, MSG_ERROR_PASSWORD).show()
            }
            ServerState.STATE_SUCCESS -> {
                alertDialog.dismiss()
                ViewUtil.alertConfirm(context, R.drawable.logo, ViewUtil.DIALOG_TITLE_CHANGE, MSG_ERROR_SUCCESS).show()
            }
        }
    }

    /**
     * Crea un diálogo de alerta.
     * @param appController Controlador de la aplicación asociado a la vista sobre la que se muestra el diálogo de alerta.
     * @param view Vista sobre la que se mostrará el diálogo de alerta.
     * @param context Contexto de la vista sobre el que se mostrará el diálogo de alerta.
     * @param selector Selector del diálogo de alerta que se pide mostrar.
     * @see Context
     * @author Iván Vicente Morales (<a href="https://github.com/srtoxyc">@srtoxyc</a>)
     */
    public fun alertForm(appController: Controller?, view: AppCompatActivity, context: Context, selector: String) {
        val SELECTOR_ERR_MSG    = "Wrong selector used to display the form alert."
        val TITLE_USERNAME      = "Change Username"
        val TITLE_EMAIL         = "Change Email"
        val TITLE_PASSWORD      = "Change Password"
        val HINT_USERNAME       = "Username"
        val HINT_EMAIL          = "Email"
        val HINT_NEW_USERNAME   = "New Username"
        val HINT_NEW_EMAIL      = "New Email"
        val HINT_OLD_PASSWORD   = "Old Password"
        val HINT_NEW_PASSWORD   = "New Password"
        val HINT_PASSWORD       = "Password"

        val inflater            = LayoutInflater.from(context)
        val dialogView: View    = inflater.inflate(R.layout.dialog_view, null)

        val lblTitle            = dialogView.findViewById<TextView>(R.id.lblTitle)

        val inputField1         = dialogView.findViewById<EditText>(R.id.inputField1)
        val inputField2         = dialogView.findViewById<EditText>(R.id.inputField2)
        val inputField3         = dialogView.findViewById<EditText>(R.id.inputField3)

        val btnCancel           = dialogView.findViewById<Button>(R.id.btnCancel)
        val btnConfirm          = dialogView.findViewById<Button>(R.id.btnConfirm)

        val alertDialog = AlertDialog.Builder(context)
            .setView(dialogView)
            .setCancelable(false)
            .create()

        alertDialog.setCanceledOnTouchOutside(true)

        when (selector) {
            DIALOG_SELECTOR_USER -> {
                lblTitle.text    = TITLE_USERNAME
                inputField1.hint = SpannableStringBuilder(HINT_NEW_USERNAME)
                inputField2.hint = SpannableStringBuilder(HINT_PASSWORD)

                btnConfirm.setOnClickListener {
                    view.lifecycleScope.launch {
                        modifyUserEvent(appController, context, inputField1, inputField2, alertDialog)
                    }

                    alertDialog.dismiss()

                }
            }

            DIALOG_SELECTOR_EMAIL -> {
                lblTitle.text    = TITLE_EMAIL
                inputField1.hint = SpannableStringBuilder(HINT_NEW_EMAIL);
                inputField2.hint = SpannableStringBuilder(HINT_PASSWORD);

                btnConfirm.setOnClickListener {
                    view.lifecycleScope.launch {
                        modifyEmailEvent(appController, context, inputField1, inputField2, alertDialog)
                    }

                    alertDialog.dismiss()
                }
            }

            DIALOG_SELECTOR_PASS -> {
                lblTitle.text    = TITLE_PASSWORD
                inputField1.hint = SpannableStringBuilder(HINT_OLD_PASSWORD)
                inputField2.hint = SpannableStringBuilder(HINT_NEW_PASSWORD)

                btnConfirm.setOnClickListener {
                    view.lifecycleScope.launch {
                        modifyPassEvent(appController, context, inputField1, inputField2, alertDialog)
                    }

                    alertDialog.dismiss()
                }
            }

            DIALOG_SELECTOR_PASS_FORGOTTEN -> {
                lblTitle.text    = TITLE_PASSWORD
                inputField1.hint = SpannableStringBuilder(HINT_USERNAME)
                inputField2.hint = SpannableStringBuilder(HINT_EMAIL)
                inputField3.hint = SpannableStringBuilder(HINT_NEW_PASSWORD)

                inputField3.visibility = EditText.VISIBLE

                btnConfirm.setOnClickListener {
                    view.lifecycleScope.launch {
                        modifyPassForgottenEvent(appController, context, inputField1, inputField2, inputField3, alertDialog)
                    }

                    inputField3.visibility = EditText.GONE

                    alertDialog.dismiss()
                }
            }

            else -> alertConfirm(
                context,
                R.drawable.logo,
                ViewUtil.DIALOG_TITLE_ERROR,
                SELECTOR_ERR_MSG
            ).show()
        }

        btnCancel.setOnClickListener {
            alertDialog.dismiss()
        }

        alertDialog.show()
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