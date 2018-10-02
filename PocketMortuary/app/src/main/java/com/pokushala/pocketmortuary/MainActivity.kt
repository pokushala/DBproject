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
        list_of_bodies.add(Body(1,"pupkin vasua", "umer davno kdh hfdkjh"))
        list_of_bodies.add(Body(2,"zalupkin vasua", "kjh hk fdtr dtyfjgk"))
        list_of_bodies.add(Body(3,"lupkin vasua", "ugjhg iiuyi  tyu ttyt uyjh"))

        var my_body_adapter = BodyAdapter(list_of_bodies)
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
                //TODO search database
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
        constructor(list_of_bodies_adapter:ArrayList<Body>):super(){
            this.list_of_bodies_adapter = list_of_bodies_adapter
        }

        override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
            var myView = layoutInflater.inflate(R.layout.ticket, null)
            var myBody = list_of_bodies_adapter[p0]
            myView.tvBodyID.text = myBody.body_name
            myView.tvBodyDes.text = myBody.body_des
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
}
