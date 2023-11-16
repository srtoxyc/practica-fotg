package es.tiernoparla.dam.moviles.model.server

import android.content.Context

object ServerFactory {
    public val MODE_TEST = 0;
    public val MODE_SERVER = 1;

    public fun getDAO(mode: Int): ServerDAO? {
        return when(mode) {
            MODE_TEST -> null
            MODE_SERVER -> ServerAccountsDAO()
            else -> null
        }
    }
}