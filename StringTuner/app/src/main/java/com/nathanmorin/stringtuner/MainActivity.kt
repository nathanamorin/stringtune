package com.nathanmorin.stringtuner
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.content.Context
import android.app.SearchManager
import android.view.Menu
import android.support.v7.widget.SearchView
import android.util.Log
import android.view.MenuItem
import android.view.SearchEvent
import android.widget.*


class MainActivity : AppCompatActivity() {

    private val adapter: InstrumentAdapter by lazy { InstrumentAdapter(this, getSavedInstruments(applicationContext))}
    private lateinit var menuItem: MenuItem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val listView = findViewById<ListView>(R.id.savedInstruments)
        listView.adapter = adapter
    }


    override fun onResume() {
        Log.d("Main", "Resumed")
        super.onResume()
        if (::menuItem.isInitialized) menuItem.collapseActionView()
        adapter.swapItems(getSavedInstruments(applicationContext))

    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.options_menu, menu)
        // Get the SearchView and set the searchable configuration
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        menuItem = menu.findItem(R.id.search)
        val searchView = menuItem.actionView as SearchView
        // Assumes current activity is the searchable activity
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.setIconifiedByDefault(false) // Do not iconify the widget; expand it by default
//        searchView.focusable = false


        return true
    }


    override fun onSearchRequested(searchEvent: SearchEvent?): Boolean {
        this.onPause()

        return super.onSearchRequested(searchEvent)

    }

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
