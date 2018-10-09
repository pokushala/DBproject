package com.pokushala.pocketmortuary

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.SearchView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.ticket.view.*

class MainActivity : AppCompatActivity() {

    var list_of_bodies = ArrayList<Body>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //add dummy data
        //list_of_bodies.add(Body(1,"pupkin vasua", "umer davno kdh hfdkjh", "pol"))
        //list_of_bodies.add(Body(2,"zalupkin vasua", "kjh hk fdtr dtyfjgk"))
        //list_of_bodies.add(Body(3,"lupkin vasua", "ugjhg iiuyi  tyu ttyt uyjh"))

        //load from db
        load_query("%")

    }

    override fun onResume(){
        super.onResume()
        load_query("%")
    }
/*
    fun chose_male():String{
        val radioGroup = findViewById<RadioGroup>(R.id.etGenderOpt)
        var text = "You selected: "
        radioGroup?.setOnCheckedChangeListener {group, checkedId ->
            text += if (R.id.etGenderOpt == checkedId) "male" else "female"
            Toast.makeText(applicationContext, text, Toast.LENGTH_SHORT).show()
        }
        return text
    }
*/
    fun load_query(surname:String){
        var db_manager = DbManager(this)
        val projections = arrayOf("id_body", "surname", "name", "sex", "date_arrived", "date_birth", "date_death")
        val selection_args = arrayOf(surname)
        val cursor = db_manager.query(projections, "surname like ?", selection_args, "surname")
        list_of_bodies.clear()
        if (cursor.moveToFirst()){
            do{
                val id = cursor.getInt(cursor.getColumnIndex("id_body"))
                val surname = cursor.getString(cursor.getColumnIndex("surname"))
                val name = cursor.getString(cursor.getColumnIndex("name"))
                val sex = cursor.getString(cursor.getColumnIndex("sex"))
                val date_arrived = cursor.getString(cursor.getColumnIndex("date_arrived"))
                val date_birth = cursor.getString(cursor.getColumnIndex("date_birth"))
                val date_death = cursor.getString(cursor.getColumnIndex("date_death"))
                list_of_bodies.add(Body(id, surname, name, sex, date_arrived, date_birth, date_death))
            }while(cursor.moveToNext())
        }
        var my_body_adapter = BodyAdapter(this, list_of_bodies)
        lvBodies.adapter = my_body_adapter
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)

        var sv = menu!!.findItem(R.id.app_bar_search).actionView as SearchView
        val sm = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        sv.setSearchableInfo(sm.getSearchableInfo(componentName))
        sv.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                Toast.makeText(applicationContext, query, Toast.LENGTH_LONG).show()
                load_query("%" + query + "%")
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                return false
            }

        })

        return super.onCreateOptionsMenu(menu) 
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item!=null) {
            when (item.itemId) {
                R.id.addBody -> {
                    //got to add page
                    var intent = Intent(this, AddBody::class.java)
                    startActivity(intent)
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    inner class BodyAdapter:BaseAdapter{
        var list_of_bodies_adapter = ArrayList<Body>()
        var context:Context?=null
        constructor(context: Context, list_of_bodies_adapter:ArrayList<Body>):super(){
            this.list_of_bodies_adapter = list_of_bodies_adapter
            this.context = context
        }

        override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
            var myView = layoutInflater.inflate(R.layout.ticket, null)
            var myBody = list_of_bodies_adapter[p0]
            myView.tvBodyID.text = myBody.body_surname + " " +
                    myBody.body_name + " " +
                    myBody.body_sex
            myView.tvBodyDes.text = myBody.body_birth + "-" + myBody.body_death +
                    "\n Поступление: " + myBody.body_arrived
            myView.tvDelete.setOnClickListener(View.OnClickListener {
                val db_manager = DbManager(this.context!!)
                val selection_args = arrayOf(myBody.body_id.toString())
                db_manager.delete("id_body=?", selection_args)
                load_query("%")
            })
            myView.tvEdit.setOnClickListener(View.OnClickListener {
                go_to_update(myBody)
            })
            return myView
        }

        override fun getItem(p0: Int): Any {
            return list_of_bodies_adapter[p0]
        }

        override fun getItemId(p0: Int): Long {
            return p0.toLong()
        }

        override fun getCount(): Int {
            return list_of_bodies_adapter.size
        }
    }

    // после нажатия на редактирования заполненные ранее поля в активити для изменения
    fun go_to_update(body: Body){
        var intent = Intent(this, AddBody::class.java)
        //change
        intent.putExtra("id_body", body.body_id)
        intent.putExtra("surname", body.body_surname)
        intent.putExtra("name", body.body_name)
        intent.putExtra("sex", body.body_sex)
        intent.putExtra("date_birth", body.body_birth)
        intent.putExtra("date_death", body.body_death)
        intent.putExtra("date_arrived", body.body_arrived)
        startActivity(intent)
    } //because can't use context inside
}
