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

    fun replaceBlinds(draggedBlind: Blind, targetBlind: Blind): Boolean {
        val db = this.writableDatabase
        val draggedContentValues = ContentValues()
        val targetContentValues = ContentValues()

        draggedContentValues.put(COLUMN_NAME, draggedBlind.name)
        draggedContentValues.put(COLUMN_ITEM_PROGRESSION, draggedBlind.itemProgression)
        draggedContentValues.put(COLUMN_IP, draggedBlind.ip.toString())

        targetContentValues.put(COLUMN_NAME, targetBlind.name)
        targetContentValues.put(COLUMN_ITEM_PROGRESSION, targetBlind.itemProgression)
        targetContentValues.put(COLUMN_IP, targetBlind.ip.toString())

        // Dragged -> Target
        db.update(TABLE_NAME, draggedContentValues, "ID=?", arrayOf(targetBlind.id.toString()))
        // Target -> Dragged
        db.update(TABLE_NAME, targetContentValues, "ID=?", arrayOf(draggedBlind.id.toString()))

        return true
    }

    fun renameBlind(blind: Blind, newName: String) {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(COLUMN_ID, blind.id)
        contentValues.put(COLUMN_NAME, newName)
        contentValues.put(COLUMN_ITEM_PROGRESSION, blind.itemProgression)
        contentValues.put(COLUMN_IP, blind.ip.toString())
        db.update(TABLE_NAME, contentValues, "ID=?", arrayOf(blind.id.toString()))
    }

    fun deleteBlind(id: Int): Int {
        val db = this.writableDatabase
        Log.i("DELETE_BLIND", id.toString())
        return db.delete(TABLE_NAME, "ID=?", arrayOf(id.toString()))
    }
}