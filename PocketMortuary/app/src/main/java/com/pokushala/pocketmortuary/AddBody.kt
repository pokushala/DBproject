package com.pokushala.pocketmortuary

import android.content.ContentValues
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_add_body.*

class AddBody : AppCompatActivity() {
    val db_table = "body"
    var id = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_body)


        try{
            var bundle:Bundle = intent.extras
            id = bundle.getInt("id_body", 0)
            if (id != 0) {
                //change
                etBodyID.setText(bundle.getString("surname"))
                etDes.setText(bundle.getString("name"))
                etGender.setText(bundle.getString("sex"))
                etBirth.setText(bundle.getString("date_birth"))
                etDeath.setText(bundle.getString("date_death"))
                etCome.setText(bundle.getString("date_arrived"))
            }
        }catch (ex:Exception){}
    }

    fun chose_male():String{
        /*val radioGroup = findViewById<RadioGroup>(R.id.etGenderOpt)
        var text = "You selected: "
        radioGroup?.setOnCheckedChangeListener {group, checkedId ->
            text += if (R.id.etGenderOpt == checkedId) "male" else "female"
            Toast.makeText(applicationContext, text, Toast.LENGTH_SHORT).show()

        }
        return text
        */
        // Get radio group selected item using on checked change listener
        var text = ""
        etGenderOpt.setOnCheckedChangeListener(
                RadioGroup.OnCheckedChangeListener { group, checkedId ->
                    val radio: RadioButton = findViewById(checkedId)
                    Toast.makeText(applicationContext," On checked change : ${radio.text}",
                            Toast.LENGTH_SHORT).show()
                })


        // Get radio group selected status and text using button click event
        button.setOnClickListener{
            // Get the checked radio button id from radio group
            var id: Int = etGenderOpt.checkedRadioButtonId
            if (id!=-1){ // If any radio button checked from radio group
                // Get the instance of radio button using id
                val radio:RadioButton = findViewById(id)
                text = radio.text.toString()
                Toast.makeText(applicationContext,"On button click : ${radio.text}",
                        Toast.LENGTH_SHORT).show()
            }else{
                // If no radio button checked in this radio group
                text = "не определено"
                Toast.makeText(applicationContext,"On button click : nothing selected",
                        Toast.LENGTH_SHORT).show()
            }
        }
        return text
    }

    // Get the selected radio button text using radio button on click listener
    fun radio_button_click(view: View){
        // Get the clicked radio button instance
        val radio: RadioButton = findViewById(etGenderOpt.checkedRadioButtonId)
        Toast.makeText(applicationContext,"On click : ${radio.text}",
                Toast.LENGTH_SHORT).show()
    }


    fun buAdd(view: View){
        var db_manager = DbManager(this)
        var values = ContentValues()
        //change
        values.put("surname", etBodyID.text.toString())
        values.put("name", etDes.text.toString())
        //values.put("gender", etGender.text.toString())
        values.put("sex", chose_male())
        values.put("date_birth", etBirth.text.toString())
        values.put("date_death", etDeath.text.toString())
        values.put("date_arrived", etCome.text.toString())
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
            val id = db_manager.update(values, "id_body=?", selection_args)
            if (id > 0) {
                Toast.makeText(this, "тело изменено", Toast.LENGTH_LONG).show()
                finish()
            } else {
                Toast.makeText(this, "неверный индекс", Toast.LENGTH_LONG).show()
            }
        }

    }
}
