package com.nathanmorin.stringtuner

import android.app.SearchManager
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.widget.ListView


class InstrumentSearch : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        if (Intent.ACTION_SEARCH == intent.action){
            val query: String = intent.getStringExtra(SearchManager.QUERY)

            showResults(query)
        } else if (Intent.ACTION_VIEW == intent.action) {
            val detailUri = intent.data
            val id = detailUri.lastPathSegment.toInt()
            val detailsIntent = Intent(applicationContext, TuneActivity::class.java).apply {
                val instrument = getInstrument(applicationContext,id)
                if (instrument == null) finish()
                putExtra(EXTRA_MESSAGE, instrument)
            }
            ContextCompat.startActivity(applicationContext, detailsIntent, null)
            finish()
        }
    }


    private fun showResults(query: String){
        setContentView(R.layout.activity_instrument_search)
        val instruments: List<Instrument> = getInstruments(context = applicationContext)
        val adapter = InstrumentAdapter(this, instruments)
        adapter.filter.filter(query)
        val listView = findViewById<ListView>(R.id.list)
        listView.adapter = adapter
    }

}

