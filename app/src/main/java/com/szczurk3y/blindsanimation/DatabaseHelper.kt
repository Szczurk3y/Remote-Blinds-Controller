package com.szczurk3y.blindsanimation

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log


class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, 1) {
    companion object {
        const val DATABASE_NAME = "blinds.db"
        const val TABLE_NAME = "blinds"
        const val COLUMN_ID = "ID"
        const val COLUMN_NAME = "NAME"
        const val COLUMN_ITEM_PROGRESSION = "ITEM_PROGRESSION"
        const val COLUMN_IP = "IP"
    }

    val data: Cursor
    get() {
        val db = this.writableDatabase
        val result = db.rawQuery("SELECT * FROM $TABLE_NAME", null)
        return result
    }


    override fun onCreate(db: SQLiteDatabase?) {
        db!!.execSQL("""
            CREATE TABLE $TABLE_NAME(
            $COLUMN_ID INTEGER PRIMARY KEY, 
            $COLUMN_NAME TEXT, 
            $COLUMN_ITEM_PROGRESSION INTEGER, 
            $COLUMN_IP TEXT)
        """.trimIndent())
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
    }

    fun insertBlind(blind: Blind) {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(COLUMN_NAME, blind.name)
        contentValues.put(COLUMN_ITEM_PROGRESSION, blind.itemProgression)
        contentValues.put(COLUMN_IP, blind.ip.toString())
        db.insert(TABLE_NAME, null, contentValues)
    }

    fun updateBlind(blind: Blind): Boolean {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(COLUMN_ID, blind.id)
        contentValues.put(COLUMN_NAME, blind.name)
        contentValues.put(COLUMN_ITEM_PROGRESSION, blind.itemProgression)
        contentValues.put(COLUMN_IP, blind.ip.toString())
        db.update(TABLE_NAME, contentValues, "ID=?", arrayOf(blind.id.toString()))

        return true
    }

    fun deleteBlind(blind: Blind): Int {
        val db = this.writableDatabase
        Log.i("DELETE_BLIND", blind.toString())
        return db.delete(TABLE_NAME, "ID=?", arrayOf(blind.id.toString()))
    }
}