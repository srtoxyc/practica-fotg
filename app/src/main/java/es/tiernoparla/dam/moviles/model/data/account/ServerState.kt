package es.tiernoparla.dam.moviles.model.data.account

/**
 * Enumeración de los estados posibles que puede devolver el servidor.
 * @param value Valor como número entero.
 * @author Iván Vicente Morales (<a href="https://github.com/srtoxyc">@srtoxyc</a>)
 */
enum class ServerState(val value: Int) {
    STATE_ERROR_USERNAME(-3),
    STATE_ERROR_EMAIL(-2),
    STATE_ERROR_PASSWORD(-1),
    STATE_ERROR_DATABASE(0),
    STATE_SUCCESS(1);

    companion object {
        public fun convertIntToServerState(responseCode: Int): ServerState? {
            return when (responseCode) {
                1 -> ServerState.STATE_SUCCESS
                0 -> ServerState.STATE_ERROR_DATABASE
                -1 -> ServerState.STATE_ERROR_PASSWORD
                -2 -> ServerState.STATE_ERROR_EMAIL
                -3 -> ServerState.STATE_ERROR_USERNAME
                else -> null
            }
        }
    }
}