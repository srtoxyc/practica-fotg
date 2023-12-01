package es.tiernoparla.dam.moviles.model.database

import android.content.Context

/**
 * Singleton que controla las instancias de objetos de acceso a la base de datos local.
 * @author Iván Vicente Morales (<a href="https://github.com/srtoxyc">@srtoxyc</a>)
 */
object DBFactory {
    public val MODE_TEST = 0;
    public val MODE_SQLITE = 1;

    /**
     * Devuelve una instancia de un objeto de acceso a datos dependiendo del modo que se le proporcione.
     * @param mode Modo de la factoría.
     * @param context Contexto de la aplicación de Android.
     * @return Instancia de un objeto de acceso a datos.
     * @see Context
     * @author Iván Vicente Morales (<a href="https://github.com/srtoxyc">@srtoxyc</a>)
     */
    public fun getDAO(mode: Int, context: Context): DBDAO? {
        return when(mode) {
            MODE_TEST -> null
            MODE_SQLITE -> SQLiteDAO(context)
            else -> null
        }
    }
}