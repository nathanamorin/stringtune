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

class InstrumentAdapter(context: Context, private var instrumentData: List<Instrument>) : ArrayAdapter<Instrument>(context, 0) , Filterable {

    private var instrumentsFiltered = instrumentData

    override fun getCount(): Int {
        return instrumentsFiltered.size
    }
    override fun getItem(position: Int): Instrument {
        return instrumentsFiltered[position]
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val instrument = getItem(position)

        val instrumentView = convertView ?: LayoutInflater.from(context).inflate(R.layout.instrument_choice, parent, false)

        val tvName = instrumentView.findViewById<TextView>(R.id.name)
        tvName.text = instrument.name
        instrumentView.setOnClickListener{

            val intent = Intent(context, TuneActivity::class.java).apply {
                putExtra(EXTRA_MESSAGE, instrument)
            }
            ContextCompat.startActivity(context, intent, null)
        }
        return instrumentView

    }

    override fun getFilter(): Filter {

        return object : Filter() {
            override fun publishResults(constraint: CharSequence?, results: FilterResults) {
                instrumentsFiltered = results.values as ArrayList<Instrument>
                notifyDataSetChanged()
            }


            override fun performFiltering(constraint: CharSequence): FilterResults {

                val filterResults = FilterResults()

                val instruments = instrumentData.filter {
                    it.name.toLowerCase().contains(constraint.toString().toLowerCase())
                }
                filterResults.count = instruments.size
                filterResults.values = instruments
                return filterResults
            }
        }
    }

}