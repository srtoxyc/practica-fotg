package es.tiernoparla.dam.moviles.model.data.game

import es.tiernoparla.dam.moviles.model.data.Exportable

data class GameAbility(
    private var id: Int,
    private var name: String,
    private var desc: String,
    private var damage: Int?,
    private var attack: Int?,
    private var defense: Int?,
    private var life: Int?,
    private var ether: Int?,
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

    fun getDamage(): Int {
        return this.damage!!
    }
    fun setDamage(damage: Int) {
        this.damage = damage
    }

    fun getAttack(): Int {
        return this.attack!!
    }
    fun setAttack(attack: Int) {
        this.attack = attack
    }

    fun getDefense(): Int {
        return this.defense!!
    }
    fun setDefense(defense: Int) {
        this.defense = defense
    }

    fun getLife(): Int {
        return this.life!!
    }
    fun setLife(life: Int) {
        this.life = life
    }

    fun getEther(): Int {
        return this.ether!!
    }
    fun setEther(ether: Int) {
        this.ether = ether
    }

    override fun toString(): String {
        return this.toJSON()
    }

    override fun toTXT(): String {
        val TXT_FORMAT: String = "" +
                "[ID: %d]\n" +
                ".....\n" +
                "[name: %s]\n" +
                "[description: %s]\n\n" +
                "[damage: %d]\n" +
                "[attack: %d]\n" +
                "[defense: %d]\n" +
                "[life: %d]\n" +
                "[ether: %d]\n"

        return String.format(TXT_FORMAT, this.id, this.name, this.desc, this.damage, this.attack, this.defense, this.life, this.ether)
    }

    override fun toXML(): String {
        val XML_FORMAT: String = "" +
                "<gameAbility id=\"%d\">\n\t" +
                "<name>%s</name>\n\t" +
                "<desc>%s</desc>\n\t" +
                "<damage>%d</damage>\n\t" +
                "<attack>%d</attack>\n\t" +
                "<defense>%d</defense>\n\t" +
                "<life>%d</life>\n\t" +
                "<ether>%d</ether>\n" +
                "</gameAbility>"

        return String.format(XML_FORMAT, this.id, this.name, this.desc, this.damage, this.attack, this.defense, this.life, this.ether)
    }

    override fun toJSON(): String {
        val JSON_FORMAT: String = "" +
                "{\n\t" +
                "id: \"%d\", \n\t" +
                "name: \"%s\",\n\t" +
                "desc: \"%s\",\n\n\t" +
                "damage: \"%d\",\n\t" +
                "attack: \"%d\",\n\t" +
                "defense: \"%d\",\n\t" +
                "life: \"%d\",\n\t" +
                "ether: \"%d\",\n" +
                "}"

        return String.format(JSON_FORMAT, this.id, this.name, this.desc, this.damage, this.attack, this.defense, this.life, this.ether)
    }
}