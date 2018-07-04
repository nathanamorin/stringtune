package com.nathanmorin.stringtuner
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.content.Context
import android.support.v4.content.ContextCompat.startActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.text.Editable
import android.text.TextWatcher




const val EXTRA_MESSAGE = "com.nathanmorin.stringtuner.INSTRUMENT_SELECT"


class InstrumentAdapter(context: Context, private var instrumentData: List<Instrument>) : ArrayAdapter<Instrument>(context, 0) , Filterable{

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

            val intent = Intent(context, Tune::class.java).apply {
                putExtra(EXTRA_MESSAGE, instrument)
            }
            startActivity(context, intent, null)
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



class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val instruments: List<Instrument> = getInstruments(context = applicationContext)

        val adapter = InstrumentAdapter(this, instruments)
        val listView = findViewById<ListView>(R.id.instrumentContainer)
        listView.adapter = adapter


        val textView = findViewById<AutoCompleteTextView>(R.id.searchInput)

        textView.addTextChangedListener(object : TextWatcher {

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                // Call back the Adapter with current character to Filter
                adapter.filter.filter(s.toString())
                Log.d("Search", s.toString())
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun afterTextChanged(s: Editable) {}
        })

    }


}
