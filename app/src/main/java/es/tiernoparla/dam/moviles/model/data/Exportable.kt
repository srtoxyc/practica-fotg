package es.tiernoparla.dam.moviles.model.data

interface Exportable {
    public fun toTXT(): String
    public fun toXML(): String
    public fun toJSON(): String
}