package es.tiernoparla.dam.moviles.view.utils

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup

object ViewUtil {
    fun <T : View> findViewByTag(viewGroup: ViewGroup, tag: String): T? {
        for (i in 0 until viewGroup.childCount) {
            val child = viewGroup.getChildAt(i)
            if (child is ViewGroup) {
                val result = findViewByTag<T>(child, tag)
                if (result != null) {
                    return result
                }
            } else if (child.tag == tag) {
                return child as T
            }
        }
        return null
    }
}