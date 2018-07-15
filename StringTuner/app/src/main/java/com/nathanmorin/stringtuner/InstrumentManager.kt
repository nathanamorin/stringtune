package com.nathanmorin.stringtuner
import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.InputStream

fun getInstruments(context: Context): List<Instrument> {

    val inputStream: InputStream = context.resources.openRawResource(R.raw.instrument_tunings_parsed)

    val instruments = mutableListOf<Instrument>()
    inputStream.bufferedReader().useLines { l -> l.forEach {
        val col = it.split(",")
        instruments.add(Instrument(col[0],col[3].split(":"))) }

    }

    return instruments

}

fun getSavedInstruments(context: Context) : List<Instrument> {
    val sharedPref = context.getSharedPreferences(
            context.getString(R.string.preference_file_key), Context.MODE_PRIVATE)

    val savedRaw = sharedPref.getString(context.getString(R.string.instrument_key),null)

    return if (savedRaw != null){
        val type = object : TypeToken<List<Instrument>>() {}.type

        Gson().fromJson<List<Instrument>>(savedRaw,type)
    } else {
        emptyList()
    }

}

fun writeSavedInstruments(context: Context, instruments: List<Instrument>) {
    val editor = context.getSharedPreferences(
            context.getString(R.string.preference_file_key), Context.MODE_PRIVATE).edit()
    val connectionsJSONString = Gson().toJson(instruments)
    editor.putString(context.getString(R.string.instrument_key), connectionsJSONString)

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