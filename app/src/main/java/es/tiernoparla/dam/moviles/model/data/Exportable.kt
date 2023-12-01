package es.tiernoparla.dam.moviles.model.data

/**
 * Interfaz de objetos exportables. Admite TXT, XML y JSON.
 * @author Iván Vicente Morales (<a href="https://github.com/srtoxyc">@srtoxyc</a>)
 */
interface Exportable {
    /**
     * Transforma un objeto en una representación TXT.
     * @author Iván Vicente Morales (<a href="https://github.com/srtoxyc">@srtoxyc</a>)
     */
    public fun toTXT(): String

    /**
     * Transforma un objeto en una representación XML.
     * @author Iván Vicente Morales (<a href="https://github.com/srtoxyc">@srtoxyc</a>)
     */
    public fun toXML(): String

    /**
     * Transforma un objeto en una representación JSON.
     * @author Iván Vicente Morales (<a href="https://github.com/srtoxyc">@srtoxyc</a>)
     */
    public fun toJSON(): String
}