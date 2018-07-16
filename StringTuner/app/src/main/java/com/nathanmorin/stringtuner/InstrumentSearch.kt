package com.nathanmorin.stringtuner

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*



class InstrumentSearch : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("InstrumentSearch", "Start")
        super.onCreate(savedInstanceState)
        if (Intent.ACTION_SEARCH == intent.action){
            val query: String = intent.getStringExtra(SearchManager.QUERY)

            showResults(query)
        }
    }


    private fun showResults(query: String){
        setContentView(R.layout.activity_instrument_search)
        Log.d("InstrumentSearch", query)
        val instruments: List<Instrument> = getInstruments(context = applicationContext)
        val adapter = InstrumentAdapter(this, instruments)
        adapter.filter.filter(query)
        val listView = findViewById<ListView>(R.id.list)
        listView.adapter = adapter
    }

}

