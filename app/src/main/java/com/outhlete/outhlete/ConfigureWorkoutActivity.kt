package com.outhlete.outhlete

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.SeekBar
import kotlinx.android.synthetic.main.activity_configure_workout.*

const val EXTRA_LOCATION = "com.outhlete.USER_LOCATION"

class ConfigureWorkoutActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_configure_workout)

        durationSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}

            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val duration = seekBar!!.progress * 15 + 45
                durationTextView.text = "$duration"
            }
        })
    }

    fun createWorkout(view: View) {
        val location = locationEditText.text
        val intent = Intent(this, WorkoutOverviewActivity::class.java).apply {
            putExtra(EXTRA_LOCATION, location)
        }
        startActivity(intent)
    }
}
