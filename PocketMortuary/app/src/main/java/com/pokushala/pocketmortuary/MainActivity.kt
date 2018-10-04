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
        //list_of_bodies.add(Body(1,"pupkin vasua", "umer davno kdh hfdkjh"))
        //list_of_bodies.add(Body(2,"zalupkin vasua", "kjh hk fdtr dtyfjgk"))
        //list_of_bodies.add(Body(3,"lupkin vasua", "ugjhg iiuyi  tyu ttyt uyjh"))



        //load from db
        load_query("%")

    }

    override fun onResume(){
        super.onResume()
        load_query("%")

    }


    fun load_query(title:String){
        var db_manager = DbManager(this)
        val projections = arrayOf("id", "title", "description")
        val selection_args = arrayOf(title)
        val cursor = db_manager.query(projections, "title like ?", selection_args, "title")
        list_of_bodies.clear()
        if (cursor.moveToFirst()){
            do{
                val id = cursor.getInt(cursor.getColumnIndex("id"))
                val title = cursor.getString(cursor.getColumnIndex("title"))
                val description = cursor.getString(cursor.getColumnIndex("description"))
                list_of_bodies.add(Body(id,title, description))
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
            myView.tvBodyID.text = myBody.body_name
            myView.tvBodyDes.text = myBody.body_des
            myView.tvDelete.setOnClickListener(View.OnClickListener {
                val db_manager = DbManager(this.context!!)
                val selection_args = arrayOf(myBody.body_id.toString())
                db_manager.delete("ID=?", selection_args)
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
    fun go_to_update(body: Body){
        var intent = Intent(this, AddBody::class.java)
        intent.putExtra("ID", body.body_id)
        intent.putExtra("name", body.body_name)
        intent.putExtra("des", body.body_des)
        startActivity(intent)
    } //because can't use context inside
}
