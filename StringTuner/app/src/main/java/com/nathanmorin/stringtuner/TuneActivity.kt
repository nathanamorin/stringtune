package com.nathanmorin.stringtuner
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
        val tune = tunings[position]

        val tuningView = holder.view

        val btnTune = tuningView.findViewById<Button>(R.id.tuneAction)
        btnTune.text = tune
        btnTune.height
        btnTune.setBackgroundResource(R.drawable.btn_tune_off)
        btnTune.setOnClickListener {
            if (SystemClock.elapsedRealtime() - mLastClickTime > 300){
                val freq = tuneFrequencies[tune] as Double

                if (currentTone == freq){
                    Log.d("STOPPING",freq.toString())
                    tonePlayer.stop()
                    currentTone = -1.0
                    buttons.forEach{ it.setBackgroundResource(R.drawable.btn_tune_off)}
                } else{
                    Log.d("STARTING",freq.toString())
                    currentTone = freq
                    tonePlayer.stop()
                    tonePlayer.toneFreqInHz = freq
                    tonePlayer.play()
                    buttons.forEach{ it.setBackgroundResource(R.drawable.btn_tune_off)}
                    btnTune.setBackgroundResource(R.drawable.btn_tune_on)
                    Thread.sleep(300)

                }

                mLastClickTime = SystemClock.elapsedRealtime()

            }

        }

        buttons.add(btnTune)
    }

    override fun getItemCount() = tunings.size

}



class TuneActivity : BaseActivity() {


    private val tonePlayer = ContinuousBuzzer()

    private lateinit var tuneContainer: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tune)

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

        title = instrument.name


    }

    override fun onDestroy() {
        super.onDestroy()
        tonePlayer.stop()
    }






}
