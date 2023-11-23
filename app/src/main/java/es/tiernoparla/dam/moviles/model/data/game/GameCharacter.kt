package es.tiernoparla.dam.moviles.model.data.game

import es.tiernoparla.dam.moviles.model.data.Exportable

data class GameCharacter (
    private var id: Int,
    private var name: String,
    private var desc: String,
    private var attack: Int?,
    private var defense: Int?,
    private var accuracy: Int?,
    private var life: Int?,
    private var ether: Int?,
    private var movement: Int?,
    private var movementType: Int?,
    private var img: String?,
    private var splash: String?,
    private var ability: GameAbility?,
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

    fun getAccuracy(): Int {
        return this.accuracy!!
    }
    fun setAccuracy(accuracy: Int) {
        this.accuracy = accuracy
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

    fun getMovement(): Int {
        return this.movement!!
    }
    fun setMovement(movement: Int) {
        this.movement = movement
    }

    fun getMovementType(): Int {
        return this.movementType!!
    }
    fun setMovementType(movementType: Int) {
        this.movementType = movementType
    }

    fun getIMG(): String {
        return this.img!!
    }
    fun setIMG(img: String) {
        this.img = img
    }

    fun getSplash(): String {
        return this.splash!!
    }
    fun setSplash(splash: String) {
        this.splash = splash
    }

    fun getAbility(): GameAbility {
        return this.ability!!
    }
    fun setAbility(ability: GameAbility) {
        this.ability = ability
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
                "[attack: %d]\n" +
                "[defense: %d]\n" +
                "[accuracy: %d]\n" +
                "[life: %d]\n" +
                "[ether: %d]\n" +
                "[movement: %d]\n" +
                "[movementType: %d]\n\n" +
                "[img: %s]\n" +
                "[splash: %s]\n"

        return String.format(TXT_FORMAT, this.id, this.name, this.desc, this.attack, this.defense, this.accuracy, this.life, this.ether, this.movement, this.movementType, this.img, this.splash)
    }

    override fun toXML(): String {
        val XML_FORMAT: String = "" +
                "<gameItem id=\"%d\">\n\t" +
                "<name>%s</name>\n\t" +
                "<desc>%s</desc>\n\t" +
                "<attack>%d</attack>\n\t" +
                "<defense>%d</defense>\n\t" +
                "<accuracy>%d</accuracy>\n\t" +
                "<life>%d</life>\n\t" +
                "<ether>%d</ether>\n\t" +
                "<movement>%d</movement>\n\t" +
                "<movementType>%d</movementType>\n\t" +
                "<img>%s</img>\n\t" +
                "<splash>%s</splash>\n" +
                "</gameItem>"

        return String.format(XML_FORMAT, this.id, this.name, this.desc, this.attack, this.defense, this.accuracy, this.life, this.ether, this.movement, this.movementType, this.img, this.splash)
    }

    override fun toJSON(): String {
        val JSON_FORMAT: String = "" +
                "{\n\t" +
                "id: \"%d\", \n\t" +
                "name: \"%s\",\n\t" +
                "desc: \"%s\",\n\n\t" +
                "attack: \"%d\",\n\t" +
                "defense: \"%d\",\n\t" +
                "accuracy: \"%d\",\n\t" +
                "life: \"%d\",\n\t" +
                "ether: \"%d\",\n\t" +
                "movement: \"%d\",\n\t" +
                "movementType: \"%d\",\n\n\t" +
                "img: \"%s\",\n\t" +
                "splash: \"%s\"\n" +
                "}"

        return String.format(JSON_FORMAT, this.id, this.name, this.desc, this.attack, this.defense, this.accuracy, this.life, this.ether, this.movement, this.movementType, this.img, this.splash)
    }
}