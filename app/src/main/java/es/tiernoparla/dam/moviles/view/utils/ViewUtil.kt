package es.tiernoparla.dam.moviles.view.utils

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup

@SuppressLint("StaticFieldLeak")
object ViewUtil {
    var viewGroup: ViewGroup? = null

    fun <T : View> findViewByTag(tag: String): T? {
        for (i in 0 until viewGroup!!.childCount) {
            val child = viewGroup!!.getChildAt(i)
            if (child is ViewGroup) {
                val result = findViewByTag<T>(tag)
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