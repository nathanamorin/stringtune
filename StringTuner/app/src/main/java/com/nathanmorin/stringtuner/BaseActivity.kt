package com.nathanmorin.stringtuner

import android.app.SearchManager
import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.SearchView
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.SearchEvent

open class BaseActivity : AppCompatActivity() {

    private lateinit var menuItem: MenuItem

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


        return true
    }


    override fun onSearchRequested(searchEvent: SearchEvent?): Boolean {
        this.onPause()

        return super.onSearchRequested(searchEvent)

    }

    override fun onResume() {
        super.onResume()
        if (::menuItem.isInitialized) menuItem.collapseActionView()
    }
}