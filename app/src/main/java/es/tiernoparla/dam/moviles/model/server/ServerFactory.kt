package es.tiernoparla.dam.moviles.model.server

import android.content.Context

/**
 * Singleton que controla las instancias de objetos de acceso a servidores.
 * @author Iván Vicente Morales (<a href="https://github.com/srtoxyc">@srtoxyc</a>)
 */
object ServerFactory {
    public val MODE_TEST = 0;
    public val MODE_SERVER = 1;

    /**
     * Devuelve una instancia de un objeto de acceso a datos dependiendo del modo que se le proporcione.
     * @param mode Modo de la factoría.
     * @return Instancia de un objeto de acceso a datos.
     * @author Iván Vicente Morales (<a href="https://github.com/srtoxyc">@srtoxyc</a>)
     */
    public fun getDAO(mode: Int): ServerDAO? {
        return when(mode) {
            MODE_TEST -> null
            MODE_SERVER -> ServerAccountsDAO()
            else -> null
        }
    }
}