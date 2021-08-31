package com.example.ngsi_image_uploader

import android.content.Context
import android.database.Cursor
import android.database.DatabaseErrorHandler
import android.database.sqlite.SQLiteOpenHelper
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteDatabase.CursorFactory
import android.content.ContentValues
import android.util.Log
import android.widget.Toast
import com.example.ngsi_image_uploader.data.Registration


class dbHandler(
    context: Context?,
    name: String?,
    factory: CursorFactory?,
    version: Int,
    errorHandler: DatabaseErrorHandler?
) : SQLiteOpenHelper(context, name, factory, version, errorHandler) {

    //information of database
    private val DATABASE_VERSION = 1
    private val DATABASE_NAME = "imageUploader.db"
    val TABLE_NAME = "Registration"
    val COLUMN_ID = "regID"
    val COLUMN_TRANSACTION = "regTransactionNumber"
    val COLUMN_BASE64_CODE = "regBase64Img"
    val COLUMN_NAME = "regName"
    val COLUMN_EMAIL = "regEmail"
    val COLUMN_CONTACT = "regContactNo"


    override fun onCreate(db: SQLiteDatabase) {
        val CREATE_TABLE = ("CREATE TABLE " + TABLE_NAME + "(" + COLUMN_ID +
                " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
                + COLUMN_TRANSACTION + " TEXT, "
                + COLUMN_NAME + " TEXT, "
                + COLUMN_EMAIL + " TEXT, "
                + COLUMN_BASE64_CODE + " TEXT,"
                + COLUMN_CONTACT + " TEXT )")
        db.execSQL(CREATE_TABLE)
    }
    override fun onUpgrade(db: SQLiteDatabase?, i: Int, i1: Int) {}
    fun loadHandler(): String? {
        var result = ""
        val query = "Select * FROM $TABLE_NAME"
        val db = this.writableDatabase
        val cursor: Cursor = db.rawQuery(query, null)
        while (cursor.moveToNext()) {
            val result_0: Int = cursor.getInt(0)
            val result_1: String = cursor.getString(1)
            result += result_0.toString() + " " + result_1 +
                    System.getProperty("line.separator")
        }
        cursor.close()
        db.close()
        Log.e("LoadHandler", " " + result)
        return result
    }
    fun addHandler(registration: Registration) {
        val values = ContentValues()
        values.put(COLUMN_TRANSACTION, registration.regTransactionNumber)
        values.put(COLUMN_NAME, registration.regName)
        values.put(COLUMN_EMAIL, registration.regEmail)
        values.put(COLUMN_BASE64_CODE, registration.regBase64Img)
        values.put(COLUMN_CONTACT, registration.regContactNo)
        Log.e("Checking", " " + values[COLUMN_TRANSACTION])
        val db = this.writableDatabase
        db.insert("Registration", null, values)
        db.close()
    }
    fun getListContents(): Cursor? {
        val db = this.writableDatabase
        return db.rawQuery("SELECT * FROM Registration", null)
    }
}