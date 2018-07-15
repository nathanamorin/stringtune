package com.nathanmorin.stringtuner
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.content.Context
import android.app.SearchManager
import android.content.Intent
import android.support.v4.content.ContextCompat
import android.view.Menu
import android.support.v7.widget.SearchView
import android.util.Log
import android.view.LayoutInflater
import android.widget.*


class MainActivity : AppCompatActivity() {

    private var savedInstruments: List<Instrument> = emptyList()
    private val adapter: ArrayAdapter<Instrument> by lazy { InstrumentAdapter(this, savedInstruments)}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        savedInstruments  = getSavedInstruments(applicationContext)
        val listView = findViewById<ListView>(R.id.savedInstruments)
        listView.adapter = adapter
    }


    override fun onResume() {
        savedInstruments = getSavedInstruments(applicationContext)
        adapter.notifyDataSetChanged()
        super.onResume()
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.options_menu, menu)
        // Get the SearchView and set the searchable configuration
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.search).actionView as SearchView
        // Assumes current activity is the searchable activity
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.setIconifiedByDefault(false) // Do not iconify the widget; expand it by default
//        searchView.focusable = false


        return true
    }


//    override fun onSearchRequested(searchEvent: SearchEvent?): Boolean {
//        Log.d("Main", "InstrumentSearch!")
//
//        return super.onSearchRequested(searchEvent)
//
//    }
//
//    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
//        Log.d("Main", "on option selected")
//        if (item?.itemId == R.id.menu_search) {
//            onSearchRequested(searchEvent = null)
//            return true
//        }
//
//        return super.onOptionsItemSelected(item)
//    }
}
