package com.nathanmorin.stringtuner

import android.content.Context
import android.content.Intent
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView

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
                    it.matchesSearch(constraint.toString())
                }
                filterResults.count = instruments.size
                filterResults.values = instruments
                return filterResults
            }
        }
    }

    fun swapItems(newInstruments: List<Instrument>) {

        this.instrumentData = newInstruments
        this.instrumentsFiltered = newInstruments

        notifyDataSetChanged()
    }

}