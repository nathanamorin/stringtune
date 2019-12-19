package com.nathanmorin.stringtuner
import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.InputStream

private lateinit var instruments: Map<Int,Instrument>

fun getInstrument(context: Context, id: Int): Instrument?{

    return getInstrumentMap(context)[id]
}

fun getInstrumentMap(context: Context): Map<Int,Instrument> {
    val inputStream: InputStream = context.resources.openRawResource(R.raw.instrument_tunings_parsed)

    if (!::instruments.isInitialized){
        val instrumentsRead = mutableListOf<Instrument>()
        inputStream.bufferedReader().useLines { l -> l.forEach {
            val col = it.split(",")
            instrumentsRead.add(Instrument(col[0].toInt(),col[1],col[4].split(":"))) }

        }

        instruments = instrumentsRead.map{ it.id to it}.toMap()
    }

    return instruments
}
fun getInstruments(context: Context): List<Instrument> {
    return getInstrumentMap(context).values.toList()
}

fun getSavedInstruments(context: Context) : List<Instrument> {
    val sharedPref = context.getSharedPreferences(
            context.getString(R.string.preference_file_key), Context.MODE_PRIVATE)

    val savedRaw = sharedPref.getString(context.getString(R.string.instrument_key),null)

    return savedRaw?.split(",")?.mapNotNull { getInstrument(context,id = it.toInt()) } ?: emptyList()

}

fun writeSavedInstruments(context: Context, instruments: List<Instrument?>) {
    val editor = context.getSharedPreferences(
            context.getString(R.string.preference_file_key), Context.MODE_PRIVATE).edit()
//    val connectionsJSONString = Gson().toJson(instruments)
    editor.putString(context.getString(R.string.instrument_key),
            instruments.mapNotNull { it?.id.toString() }.reduce{ i1, i2 -> "$i1,$i2" })

    editor.apply()
}

fun saveInstrument(context: Context, instrument: Instrument) {
    val savedInstruments =  getSavedInstruments(context).toMutableList()
    if (!savedInstruments.contains(instrument)) savedInstruments.add(instrument)
    writeSavedInstruments(context,savedInstruments)
}

fun removeInstrument(context: Context, instrument: Instrument) {
    val savedInstruments =  getSavedInstruments(context).toMutableList()
    savedInstruments.remove(instrument)
    writeSavedInstruments(context,savedInstruments)
}


fun getTunings(context: Context): List<List<String>> {
    val inputStream: InputStream = context.resources.openRawResource(R.raw.tune_frequencies)

    val tuning = mutableListOf<List<String>>()
    inputStream.bufferedReader().useLines { l -> l.forEach {
        val col = it.split(",")
        tuning.add(col)
        }
    }

    return tuning
}


fun getTuningFrequency(context: Context): Map<String,Double> {

    val reMap = mutableMapOf<String,Double>()

    getTunings(context).forEach {
        reMap.put(it[0],it[2].toDouble())
    }

    return reMap;

}