package es.tiernoparla.dam.moviles.model.data.account

enum class ServerState(val value: Int) {
    STATE_ERROR_USERNAME(-3),
    STATE_ERROR_EMAIL(-2),
    STATE_ERROR_PASSWORD(-1),
    STATE_ERROR_DATABASE(0),
    STATE_SUCCESS(1)
}