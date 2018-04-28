package com.outhlete.outhlete

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.SeekBar
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.places.ui.PlacePicker
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.synthetic.main.activity_configure_workout.*

class ConfigureWorkoutActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_LOCATION = "com.outhlete.LOCATION"
        const val EXTRA_DURATION = "com.outhlete.DURATION"
    }

    private val placePickerRequestCode = 1
    private var duration = 60
    private var startEndPosition: LatLng? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_configure_workout)

        durationSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}

            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                duration = seekBar!!.progress * 15 + 45
                durationTextView.text = "$duration minutes"
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == placePickerRequestCode && data != null) {
            if (resultCode != PlacePicker.RESULT_ERROR) {
                val place = PlacePicker.getPlace(this, data)
                locationTextView.text = place.address
                startEndPosition = place.latLng
            }
        }
    }

    fun pickPlace(view: View) {
        val builder = PlacePicker.IntentBuilder()
        startActivityForResult(builder.build(this), placePickerRequestCode)
    }

    fun createWorkout(view: View) {
        if (startEndPosition == null) {
            val locationProvider = LocationServices.getFusedLocationProviderClient(this)

            if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                locationProvider.lastLocation.addOnSuccessListener {
                    startEndPosition = LatLng(it.latitude, it.longitude)
                    val intent = Intent(this, WorkoutOverviewActivity::class.java).apply {
                        putExtra(EXTRA_LOCATION, startEndPosition)
                        putExtra(EXTRA_DURATION, duration)
                    }
                    startActivity(intent)
                }
            }
        } else {
            val intent = Intent(this, WorkoutOverviewActivity::class.java).apply {
                putExtra(EXTRA_LOCATION, startEndPosition)
                putExtra(EXTRA_DURATION, duration)
            }
            startActivity(intent)
        }
    }
}
