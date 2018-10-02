package com.pokushala.pocketmortuary

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast

class DbManager{

    val db_name = "MyMorg"
    val db_table = "Bodies"
    val col_id = "id" // title of the table
    val col_title = "title"
    val col_des = "description"
    val db_version = 1
    //CREATE TABLE IF NOT EXISTS Bodies (id INTEGER PRIMARY KEY, title TEXT, description TEXT);
    val sql_create_table = "CREATE TABLE IF NOT EXISTS " + db_table + " (" + col_id +
            " INTEGER PRIMARY KEY," + col_title + " TEXT, " + col_des + " TEXT);"
    var sqlDB:SQLiteDatabase?=null

    constructor(context: Context){
        var db = DataBaseHelperBodies(context)
        sqlDB = db.writableDatabase

    }

    inner class DataBaseHelperBodies:SQLiteOpenHelper{
        var context:Context?=null
        constructor(context: Context):super(context, db_name, null, db_version){
            this.context = context
        }
        override fun onCreate(p0: SQLiteDatabase?) {
            p0!!.execSQL(sql_create_table)
            Toast.makeText(this.context, "база данных сохдана", Toast.LENGTH_LONG).show()
        }

        override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
            p0!!.execSQL("Drop table IF EXISTS" + db_table)
        }

    }

    fun insert(values:ContentValues):Long{
        val id = sqlDB!!.insert(db_table, "", values)
        return id
    }
}