package com.nathanmorin.stringtuner
import android.content.Context
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