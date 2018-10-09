package com.pokushala.pocketmortuary

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.database.sqlite.SQLiteQueryBuilder
import android.widget.Toast
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.sql.SQLException



class DbManager{
// база данных MyMorg таблица тел Bodies
companion object {
    const val DB_NAME :String = "morgue_miem_new1.db"
    const val DB_VERSION = 1
}
    val db_name = "morgue_miem_new1"

    //______________________________first table___________________
    val db_table = "body"
    val col_id = "id_body"
    //change
    val col_surname = "surname"
    val col_name = "name"
    val col_sex = "sex"

    val col_birth = "date_birth"
    val col_death = "date_death"
    val col_arrived = "date_arrived"
    val col_insur = "id_insurable"
    //______________________________second table________________________


    //val col_reason = "reason"
    //val col_doc = "doc"
    //val col_emer = "emer"

    val db_version = 1
    //CREATE TABLE IF NOT EXISTS Bodies (id INTEGER PRIMARY KEY, surname TEXT, name TEXT);
    //change
    val sql_create_table = "CREATE TABLE IF NOT EXISTS " + db_table + " (" + col_id +
            " INTEGER PRIMARY KEY," + col_surname + " TEXT, " + col_name + " TEXT, " +
            col_sex + " TEXT, " + col_birth + " DATE, " + col_arrived + " DATE, " + col_death + " DATE);"
   // + col_reason + " TEXT, " + col_doc + " TEXT, " + col_emer + ""

    var sqlDB:SQLiteDatabase?=null

    constructor(context: Context){
        var db = DataBaseHelperBodies(context)
        try {
            db.updateDataBase()
        } catch (mIOException: IOException) {
            throw Error("UnableToUpdateDatabase")
        }

        sqlDB = db.writableDatabase
    }

    inner class DataBaseHelperBodies:SQLiteOpenHelper{
        val DB_PATH:String

        private var mDataBase: SQLiteDatabase? = null
        private var mContext: Context? = null
        private var mNeedUpdate = false

        var context:Context?=null
        constructor(context: Context):super(context, DB_NAME, null, DB_VERSION){
            this.context = context
            if (android.os.Build.VERSION.SDK_INT >= 17)
                DB_PATH = context.getApplicationInfo().dataDir + "/databases/"
            else
                DB_PATH = "/data/data/" + context.getPackageName() + "/databases/"
            this.mContext = context

            copyDataBase()

            this.getReadableDatabase()
        }

        @Throws(IOException::class)
        fun updateDataBase() {
            if (mNeedUpdate) {
                val dbFile = File(DB_PATH + DB_NAME)
                if (dbFile.exists())
                    dbFile.delete()

                copyDataBase()

                mNeedUpdate = false
            }
        }

        private fun checkDataBase(): Boolean {
            val dbFile = File(DB_PATH + DB_NAME)
            return dbFile.exists()
        }

        private fun copyDataBase() {
            if (!checkDataBase()) {
                this.readableDatabase
                this.close()
                try {
                    copyDBFile()
                } catch (mIOException: IOException) {
                    throw Error("ErrorCopyingDataBase")
                }

            }
        }

        @Throws(IOException::class)
        private fun copyDBFile() {
            val mInput = mContext?.getAssets()?.open(DB_NAME)
            //InputStream mInput = mContext.getResources().openRawResource(R.raw.info);
            val mOutput = FileOutputStream(DB_PATH + DB_NAME)
            val mBuffer = ByteArray(1024)
            var mLength: Int=0
            while ({mLength = mInput!!.read(mBuffer); mLength}() > 0)
                mOutput.write(mBuffer, 0, mLength)
            mOutput.flush()
            mOutput.close()
            mInput?.close()
        }

        @Throws(SQLException::class)
        fun openDataBase(): Boolean {
            mDataBase = SQLiteDatabase.openDatabase(DB_PATH + DB_NAME, null, SQLiteDatabase.CREATE_IF_NECESSARY)
            return mDataBase != null
        }

        @Synchronized override fun close() {
            if (mDataBase != null)
                mDataBase?.close()
            super.close()
        }

        override fun onCreate(db: SQLiteDatabase?) {
            db!!.execSQL(sql_create_table)
            Toast.makeText(this.context, "база данных создана", Toast.LENGTH_LONG).show()
        }

        override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
            if (newVersion > oldVersion) {
                db!!.execSQL("Drop table IF EXISTS" + db_table)
                mNeedUpdate = true
            }
        }

    }

    fun insert(values:ContentValues):Long{
        val id = sqlDB!!.insert(db_table, "", values)
        return id
    }

    fun query(projection:Array<String>, selection:String, selection_args:Array<String>, sort_order:String):Cursor{
        val qb = SQLiteQueryBuilder()
        qb.tables = db_table
        val cursor = qb.query(sqlDB, projection, selection, selection_args,null, null, sort_order)
        return cursor
    }

    fun delete(selection: String, selection_args: Array<String>):Int{
        val count = sqlDB!!.delete(db_table, selection, selection_args)
        return count
    }

    fun update(values: ContentValues, selection:String, selection_args: Array<String>):Int{
        val count = sqlDB!!.update(db_table, values, selection, selection_args)
        return count
    }

}


