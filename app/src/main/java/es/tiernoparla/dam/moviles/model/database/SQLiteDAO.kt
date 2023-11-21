package es.tiernoparla.dam.moviles.model.database

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.AssetManager
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import es.tiernoparla.dam.moviles.model.data.game.GameAbility
import es.tiernoparla.dam.moviles.model.data.game.GameCharacter
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class SQLiteDAO(private var context: Context) : DBDAO, SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private const val DATABASE_VERSION  = 1
        private const val DATABASE_NAME     = "fallofthegods_database.db"
    }

    private val assets: AssetManager
    private val databaseDir: String

    init {
        assets = context.assets
        databaseDir = context.applicationInfo.dataDir + "/databases/"
        val file = File(databaseDir)
        if (!file.exists()) file.mkdir()
    }

    /**
     * Executes when the DAO is initialized.
     */
    override fun onCreate(db: SQLiteDatabase) {
        // Nothing, as we do not want to create or insert any data at the initialization.
    }

    /**
     * Executes when the DAO is updated.
     */
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        copyDatabase()
    }

    /**
     * Returns a writable instance of the database.
     */
    override fun getWritableDatabase(): SQLiteDatabase {
        if (!doesDatabaseExist()) copyDatabase()
        return super.getWritableDatabase()
    }

    /**
     * Returns a readable instance of the database.
     */
    override fun getReadableDatabase(): SQLiteDatabase {
        if (!doesDatabaseExist()) copyDatabase()
        return super.getReadableDatabase()
    }

    /**
     * States whether the database exists or not.
     */
    private fun doesDatabaseExist(): Boolean {
        return File(databaseDir + DATABASE_NAME).exists()
    }

    /**
     * Reads and opens the database.
     */
    private fun copyDatabase() {
        try {
            val inputStream     = assets.open("databases/${DATABASE_NAME}")
            val outputStream    = FileOutputStream(databaseDir + DATABASE_NAME)
            val buffer          = ByteArray(8 * 1024)

            var readed: Int

            while (inputStream.read(buffer).also { readed = it } != -1) {
                outputStream.write(buffer, 0, readed)
            }

            outputStream.flush()
            outputStream.close()
            inputStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }



    /* =============# DBDAO implementation #============= */

    @SuppressLint("Range")
    override fun getCharacter(id: Int): GameCharacter? {
        val db                           = readableDatabase
        var character: GameCharacter?    = null

        val query           = "SELECT * FROM CharactersGame WHERE id = ?"
        val selectionArgs   = arrayOf(id.toString())
        val cursor          = db.rawQuery(query, selectionArgs)

        try {
            character = if (cursor.moveToFirst()) {
                val characterId             = cursor.getInt(cursor.getColumnIndex("id"))
                val characterName           = cursor.getString(cursor.getColumnIndex("name"))
                val characterDesc           = cursor.getString(cursor.getColumnIndex("description"))
                val characterStatAttack     = cursor.getInt(cursor.getColumnIndex("attack"))
                val characterStatDefense    = cursor.getInt(cursor.getColumnIndex("defense"))
                val characterStatAccuracy   = cursor.getInt(cursor.getColumnIndex("accuracy"))
                val characterStatLife       = cursor.getInt(cursor.getColumnIndex("life"))
                val characterStatEther      = cursor.getInt(cursor.getColumnIndex("ether"))
                val characterStatMovement   = cursor.getInt(cursor.getColumnIndex("movement"))
                val characterMovementType   = cursor.getInt(cursor.getColumnIndex("movementType"))
                val characterImage          = cursor.getString(cursor.getColumnIndex("image"))
                val characterSplash         = cursor.getString(cursor.getColumnIndex("splash"))

                GameCharacter(characterId, characterName, characterDesc, characterStatAttack, characterStatDefense, characterStatAccuracy, characterStatLife, characterStatEther, characterStatMovement, characterMovementType, characterImage, characterSplash)
            } else {
                null
            }
        } finally {
            cursor.close()
        }

        db.close()
        return character
    }

    @SuppressLint("Range")
    override fun getAbility(id: Int): GameAbility? {
        val db                           = readableDatabase
        var ability: GameAbility?        = null

        val query           = "SELECT * FROM AbilitiesGame WHERE id = ?"
        val selectionArgs   = arrayOf(id.toString())
        val cursor          = db.rawQuery(query, selectionArgs)

        try {
            ability = if (cursor.moveToFirst()) {
                val characterId             = cursor.getInt(cursor.getColumnIndex("idCharacter"))
                val abilityName             = cursor.getString(cursor.getColumnIndex("abilityName"))
                val abilityDesc             = cursor.getString(cursor.getColumnIndex("abilityDesc"))
                val abilityDamage           = cursor.getInt(cursor.getColumnIndex("abilityAttack"))
                val abilityAttack           = cursor.getInt(cursor.getColumnIndex("abilityAttack"))
                val abilityDefense          = cursor.getInt(cursor.getColumnIndex("abilityDefense"))
                val abilityLife             = cursor.getInt(cursor.getColumnIndex("abilityLife"))
                val abilityEther            = cursor.getInt(cursor.getColumnIndex("abilityEther"))

                GameAbility(characterId, abilityName, abilityDesc, abilityDamage, abilityAttack, abilityDefense, abilityLife, abilityEther)
            } else {
                null
            }
        } finally {
            cursor.close()
        }

        db.close()
        return ability
    }

    @SuppressLint("Range")
    override fun listCharacters(): MutableList<GameCharacter> {
        val db                                        = readableDatabase
        val characters: MutableList<GameCharacter>    = ArrayList<GameCharacter>()

        val query           = "SELECT * FROM CharactersGame"
        val selectionArgs   = emptyArray<String>()
        val cursor          = db.rawQuery(query, selectionArgs)

        try {
            while (cursor.moveToNext()) {
                val characterId             = cursor.getInt(cursor.getColumnIndex("id"))
                val characterName           = cursor.getString(cursor.getColumnIndex("name"))
                val characterDesc           = cursor.getString(cursor.getColumnIndex("description"))
                val characterStatAttack     = cursor.getInt(cursor.getColumnIndex("attack"))
                val characterStatDefense    = cursor.getInt(cursor.getColumnIndex("defense"))
                val characterStatAccuracy   = cursor.getInt(cursor.getColumnIndex("accuracy"))
                val characterStatLife       = cursor.getInt(cursor.getColumnIndex("life"))
                val characterStatEther      = cursor.getInt(cursor.getColumnIndex("ether"))
                val characterStatMovement   = cursor.getInt(cursor.getColumnIndex("movement"))
                val characterMovementType   = cursor.getInt(cursor.getColumnIndex("movementType"))
                val characterImage          = cursor.getString(cursor.getColumnIndex("image"))
                val characterSplash         = cursor.getString(cursor.getColumnIndex("splash"))

                Log.i("eeeeeee", "${characterImage}")

                characters.add(GameCharacter(characterId, characterName, characterDesc, characterStatAttack, characterStatDefense, characterStatAccuracy, characterStatLife, characterStatEther, characterStatMovement, characterMovementType, characterImage, characterSplash))
            }
        } finally {
            cursor.close()
        }

        db.close()

        return characters
    }

    @SuppressLint("Range")
    override fun listAbilities(): MutableList<GameAbility> {
        val db                                      = readableDatabase
        var abilities: MutableList<GameAbility>     = ArrayList<GameAbility>()

        val query           = "SELECT * FROM AbilitiesGame"
        val selectionArgs   = emptyArray<String>()
        val cursor          = db.rawQuery(query, selectionArgs)

        try {
            while (cursor.moveToNext()) {
                val characterId             = cursor.getInt(cursor.getColumnIndex("idCharacter"))
                val abilityName             = cursor.getString(cursor.getColumnIndex("abilityName"))
                val abilityDesc             = cursor.getString(cursor.getColumnIndex("abilityDesc"))
                val abilityDamage           = cursor.getInt(cursor.getColumnIndex("abilityAttack"))
                val abilityAttack           = cursor.getInt(cursor.getColumnIndex("abilityAttack"))
                val abilityDefense          = cursor.getInt(cursor.getColumnIndex("abilityDefense"))
                val abilityLife             = cursor.getInt(cursor.getColumnIndex("abilityLife"))
                val abilityEther            = cursor.getInt(cursor.getColumnIndex("abilityEther"))

                abilities.add(GameAbility(characterId, abilityName, abilityDesc, abilityDamage, abilityAttack, abilityDefense, abilityLife, abilityEther))
            }
        } finally {
            cursor.close()
        }

        db.close()
        return abilities
    }
}