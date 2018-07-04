package com.nathanmorin.stringtuner
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import net.mabboud.android_tone_player.ContinuousBuzzer

private var currentTone: Double = -1.0

class TuneAdapter(context: Context,
                  private var tunings: List<String>,
                  private var tuneFrequencies: Map<String,Double>,
                  private var tonePlayer: ContinuousBuzzer = ContinuousBuzzer() ) : ArrayAdapter<String>(context, 0, tunings) {


    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val tune = getItem(position)

        val tuningView = convertView ?: LayoutInflater.from(context).inflate(R.layout.instrument_tune,null)

        val btnTune = tuningView.findViewById<Button>(R.id.tuneAction)
        btnTune.text = tune
        btnTune.setOnClickListener {

            val freq = tuneFrequencies[tune]

            if (currentTone == freq){
                tonePlayer.stop()
                currentTone = -1.0
            } else{
                tonePlayer.stop()
                tonePlayer.toneFreqInHz = freq!!
                tonePlayer.play()
                currentTone = freq
            }

        }
        return tuningView

    }

}



class Tune : AppCompatActivity() {


    private val tonePlayer = ContinuousBuzzer()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tune)

        val instrument = intent.getParcelableExtra<Instrument>(EXTRA_MESSAGE)

        val tuneContainer = findViewById<GridView>(R.id.tuneContainer)

        val tuningFrequencies = getTuningFrequency(context = applicationContext)

        tonePlayer.pausePeriodSeconds = 600.0
        tonePlayer.volume = 500

        tuneContainer.adapter = TuneAdapter(applicationContext,instrument.tuning, tuningFrequencies,tonePlayer)


//        instrument.tuning.forEach {
//            val tune = it
//            val instrumentView = layoutInflater.inflate(R.layout.instrument_tune,null)
//
//            val btnTune = instrumentView.findViewById<Button>(R.id.tuneAction)
//            btnTune.text = tune
//            btnTune.setOnClickListener {
//
//                val freq = tuningFrequencies[tune]
//
//                if (currentTone == freq){
//                    tonePlayer.stop()
//                    currentTone = -1.0
//                } else{
//                    tonePlayer.stop()
//                    tonePlayer.toneFreqInHz = freq!!
//                    tonePlayer.play()
//                    currentTone = freq
//                }
//
//            }
//
//            tuneContainer.addView(instrumentView)
//        }


    }

    override fun onDestroy() {
        super.onDestroy()
        tonePlayer.stop()
    }






}
