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

/**
 * Objeto de acceso a datos de la base de datos local.
 * @param context Contexto de la aplicación Android.
 * @see Context
 * @see SQLiteOpenHelper
 * @see AssetManager
 * @author Iván Vicente Morales
 */
class SQLiteDAO(private var context: Context) : DBDAO, SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private const val DATABASE_VERSION  = 1
        private const val DATABASE_NAME     = "fallofthegods.db"
    }

    private val assets: AssetManager
    private val databaseDir: String

    init {
        assets = context.assets
        databaseDir = context.applicationInfo.dataDir + "/databases/"
        val file = File(databaseDir)
        if (!file.exists()) file.mkdir()
    }
    override fun onCreate(db: SQLiteDatabase) {
        // Nothing, as we do not want to create or insert any data at the initialization.
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        copyDatabase()
    }

    override fun getWritableDatabase(): SQLiteDatabase {
        if (!doesDatabaseExist()) copyDatabase()
        return super.getWritableDatabase()
    }

    override fun getReadableDatabase(): SQLiteDatabase {
        if (!doesDatabaseExist()) copyDatabase()
        return super.getReadableDatabase()
    }

    /**
     * Evalúa si la base de datos existe en el dispositivo Android.
     * @author Iván Vicente Morales
     */
    private fun doesDatabaseExist(): Boolean {
        return File(databaseDir + DATABASE_NAME).exists()
    }

    /**
     * Copia la base de datos al paquete del sistema del dispositivo Android.
     * @author Iván Vicente Morales
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

        val query           = "SELECT * FROM CharactersGame WHERE id = ${id}"
        val selectionArgs   = emptyArray<String>()
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
                val characterImage          = cursor.getString(cursor.getColumnIndex("image"))
                val abilityName             = cursor.getString(cursor.getColumnIndex("abilityName"))
                val abilityDesc             = cursor.getString(cursor.getColumnIndex("abilityDesc"))

                GameCharacter(characterId, characterName, characterDesc, characterStatAttack, characterStatDefense, characterStatAccuracy, characterStatLife, characterStatEther, characterStatMovement, characterImage, GameAbility(characterId, abilityName, abilityDesc))
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
                val characterImage          = cursor.getString(cursor.getColumnIndex("image"))
                val abilityName             = cursor.getString(cursor.getColumnIndex("abilityName"))
                val abilityDesc             = cursor.getString(cursor.getColumnIndex("abilityDesc"))

                characters.add(GameCharacter(characterId, characterName, characterDesc, characterStatAttack, characterStatDefense, characterStatAccuracy, characterStatLife, characterStatEther, characterStatMovement, characterImage, GameAbility(characterId, abilityName, abilityDesc)))
            }
        } finally {
            cursor.close()
        }

        db.close()

        return characters
    }
}