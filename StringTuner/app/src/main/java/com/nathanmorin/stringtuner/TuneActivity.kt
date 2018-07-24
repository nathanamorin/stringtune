package com.nathanmorin.stringtuner
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.support.v4.content.ContextCompat
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import net.mabboud.android_tone_player.ContinuousBuzzer
import kotlin.math.ceil

private var currentTone: Double = -1.0

private val numCols = 2

class TuneAdapter(private var tunings: List<String>,
                  private var tuneFrequencies: Map<String,Double>,
                  private var tonePlayer: ContinuousBuzzer = ContinuousBuzzer() ) : RecyclerView.Adapter<TuneAdapter.ViewHolder>() {

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    val buttons = mutableListOf<Button>()
    private var mLastClickTime: Long = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.instrument_tune,parent,false)

        view.minimumHeight = parent.measuredHeight / ceil(tunings.size / numCols.toDouble()).toInt()

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val colorOn = ContextCompat.getColor(holder.view.context,R.color.colorAccent)
        val colorOff = ContextCompat.getColor(holder.view.context,R.color.colorButton)
        val tune = tunings[position]

        val tuningView = holder.view

        val btnTune = tuningView.findViewById<Button>(R.id.tuneAction)
        btnTune.text = tune
        btnTune.height
        btnTune.setBackgroundColor(colorOff)
        btnTune.setOnClickListener {
            if (SystemClock.elapsedRealtime() - mLastClickTime > 1000){
                val freq = tuneFrequencies[tune] as Double

                if (currentTone == freq){
                    tonePlayer.stop()
                    currentTone = -1.0
                    buttons.forEach{ it.setBackgroundColor(colorOff)}
                } else{
                    currentTone = freq
                    tonePlayer.stop()
                    tonePlayer.toneFreqInHz = freq
                    tonePlayer.play()
                    buttons.forEach{ it.setBackgroundColor(colorOff)}
                    btnTune.setBackgroundColor(colorOn)
                }

            }

        }

        buttons.add(btnTune)
    }

    override fun getItemCount() = tunings.size

}



class TuneActivity : AppCompatActivity() {


    private val tonePlayer = ContinuousBuzzer()

    private lateinit var tuneContainer: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tune)

        Log.d("TunActivity","starting")

        val instrument = intent.getParcelableExtra<Instrument>(EXTRA_MESSAGE)

        saveInstrument(applicationContext, instrument)

        val tuningFrequencies = getTuningFrequency(context = applicationContext)

        tonePlayer.pausePeriodSeconds = 600.0
        tonePlayer.volume = 500

        viewManager = GridLayoutManager(applicationContext, numCols)
        viewAdapter = TuneAdapter(instrument.tuning,tuningFrequencies,tonePlayer)

        tuneContainer = findViewById<RecyclerView>(R.id.tuneContainer).apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }


    }

    override fun onDestroy() {
        super.onDestroy()
        tonePlayer.stop()
    }






}
