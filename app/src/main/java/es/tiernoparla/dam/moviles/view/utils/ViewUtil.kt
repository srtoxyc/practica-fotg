package es.tiernoparla.dam.moviles.view.utils

import android.view.View
import android.view.ViewGroup

object ViewUtil {
    fun findViewByTag(viewGroup: ViewGroup, tag: String): View? {
        for (i in 0 until viewGroup.childCount) {
            val child = viewGroup.getChildAt(i)
            if (child is ViewGroup) {
                val result = findViewByTag(child, tag)
                if (result != null) {
                    return result
                }
            } else if (child.tag == tag) {
                return child
            }
        }
        return null
    }
}