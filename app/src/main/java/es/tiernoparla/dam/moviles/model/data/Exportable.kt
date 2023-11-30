package es.tiernoparla.dam.moviles.model.data

/**
 * Interfaz de objetos exportables. Admite TXT, XML y JSON.
 */
interface Exportable {
    /**
     * Transforma un objeto en una representación TXT.
     */
    public fun toTXT(): String

    /**
     * Transforma un objeto en una representación XML.
     */
    public fun toXML(): String

    /**
     * Transforma un objeto en una representación JSON.
     */
    public fun toJSON(): String
}