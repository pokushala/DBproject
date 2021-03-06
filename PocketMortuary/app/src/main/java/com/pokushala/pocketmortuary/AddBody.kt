package com.pokushala.pocketmortuary

import android.content.ContentValues
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_add_body.*

class AddBody : AppCompatActivity() {
    val db_table = "Bodies"
    var id = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_body)


        try{
            var bundle:Bundle = intent.extras
            id = bundle.getInt("ID", 0)
            if (id != 0) {
                etBodyID.setText(bundle.getString("name"))
                etDes.setText(bundle.getString("des"))
            }
        }catch (ex:Exception){}
    }

    fun buAdd(view: View){
        var db_manager = DbManager(this)
        var values = ContentValues()
        values.put("title", etBodyID.text.toString())
        values.put("description", etDes.text.toString())
        if (id == 0) {
            val id = db_manager.insert(values)
            if (id > 0) {
                Toast.makeText(this, "тело добавлено", Toast.LENGTH_LONG).show()
                finish()
            } else {
                Toast.makeText(this, "неверный индекс", Toast.LENGTH_LONG).show()
            }
        }
        else{
            var selection_args = arrayOf(id.toString())
            val id = db_manager.update(values, "ID=?", selection_args)
            if (id > 0) {
                Toast.makeText(this, "тело изменено", Toast.LENGTH_LONG).show()
                finish()
            } else {
                Toast.makeText(this, "неверный индекс", Toast.LENGTH_LONG).show()
            }
        }

    }
}
