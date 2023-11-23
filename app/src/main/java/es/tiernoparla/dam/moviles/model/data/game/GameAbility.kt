package es.tiernoparla.dam.moviles.model.data.game

import es.tiernoparla.dam.moviles.model.data.Exportable

data class GameAbility(
    private var id: Int,
    private var name: String,
    private var desc: String,
) : Exportable {
    fun getID(): Int {
        return this.id
    }
    fun setID(id: Int) {
        this.id = id
    }

    fun getName(): String {
        return this.name
    }
    fun setName(name: String) {
        this.name = name
    }

    fun getDesc(): String {
        return this.desc
    }
    fun setDesc(desc: String) {
        this.desc = desc
    }

    override fun toString(): String {
        return this.toJSON()
    }

    override fun toTXT(): String {
        val TXT_FORMAT: String = "" +
                "[ID: %d]\n" +
                ".....\n" +
                "[name: %s]\n" +
                "[description: %s]\n"

        return String.format(TXT_FORMAT, this.id, this.name, this.desc)
    }

    override fun toXML(): String {
        val XML_FORMAT: String = "" +
                "<gameAbility id=\"%d\">\n\t" +
                "<name>%s</name>\n\t" +
                "<desc>%s</desc>\n" +
                "</gameAbility>"

        return String.format(XML_FORMAT, this.id, this.name, this.desc)
    }

    override fun toJSON(): String {
        val JSON_FORMAT: String = "" +
                "{\n\t" +
                "id: \"%d\", \n\t" +
                "name: \"%s\",\n\t" +
                "desc: \"%s\",\n" +
                "}"

        return String.format(JSON_FORMAT, this.id, this.name, this.desc)
    }
}