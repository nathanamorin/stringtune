package com.nathanmorin.stringtuner
import android.os.Bundle
import android.widget.ListView
import android.widget.TextView


class MainActivity : BaseActivity() {

    private val adapter: InstrumentAdapter by lazy { InstrumentAdapter(this, getSavedInstruments(applicationContext))}

    private lateinit var tvSavedInstrument: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val listView = findViewById<ListView>(R.id.savedInstruments)
        tvSavedInstrument = findViewById(R.id.tv_saved_instrument_message)
        listView.adapter = adapter
    }


    fun clearMessages() {
        tvSavedInstrument.text = ""
    }


    override fun onResume() {
        super.onResume()
        clearMessages()
        val instruments = getSavedInstruments(applicationContext)

        if (instruments.isEmpty()){
            findViewById<TextView>(R.id.tv_saved_instrument_message).text = "No recent instruments.  Search for instruments above."
        }
        adapter.swapItems(instruments)

    }
}
