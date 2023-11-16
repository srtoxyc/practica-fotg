package es.tiernoparla.dam.moviles.model.database

import android.content.Context

object DBFactory {
    public val MODE_TEST = 0;
    public val MODE_SQLITE = 1;

    public fun getDAO(mode: Int, context: Context): DBDAO? {
        return when(mode) {
            MODE_TEST -> null
            MODE_SQLITE -> SQLiteDAO(context)
            else -> null
        }
    }
}