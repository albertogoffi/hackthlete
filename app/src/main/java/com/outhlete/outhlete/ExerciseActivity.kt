package com.outhlete.outhlete

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Environment
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.maps.DirectionsApiRequest
import com.google.maps.GeoApiContext
import com.google.maps.android.PolyUtil
import com.google.maps.model.TravelMode
import com.outhlete.outhlete.domain.Exercise
import com.outhlete.outhlete.domain.Workout
import kotlinx.android.synthetic.main.activity_exercise.*
import org.joda.time.DateTime
import java.security.AccessController.getContext
import java.util.concurrent.TimeUnit

class ExerciseActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var workout: Workout
    private var index = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exercise)

        workout = (this.application as App).workout
        index = intent.getIntExtra("index", 0)
        if (index == workout.exercises.lastIndex) {
            buttonNext.text = "Done! Get another workout!"
            buttonNext.setOnClickListener {
                val intent = Intent(this@ExerciseActivity, ConfigureWorkoutActivity::class.java)
                startActivity(intent)
            }
        }

        val exercise = workout.exercises[index]
        val exerciseImage = exercise.image.substringBefore(".")
        val id = this.resources.getIdentifier(exerciseImage, "drawable", this.packageName)

        imageView.setImageDrawable(getDrawable(id))

        exerciseNameView.text = exercise.name
        exerciseTargetView.text = "${exercise.goal.description} (${exercise.goal.bodyPart})"
        exerciseDescriptionView.text = exercise.description

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
}

    override fun onMapReady(googleMap: GoogleMap) {
        val exercise = workout.exercises[index]

        val geoApiContext = GeoApiContext.Builder()
                .queryRateLimit(3)
                .apiKey("AIzaSyBq6e5OnxObqIWurfzay99fkCZQDvsVjOU")
                .connectTimeout(1, TimeUnit.SECONDS)
                .readTimeout(1, TimeUnit.SECONDS)
                .writeTimeout(1, TimeUnit.SECONDS)
                .build()

        val directionsApi = DirectionsApiRequest(geoApiContext)
        directionsApi.mode(TravelMode.WALKING)
        directionsApi.alternatives(false)
        directionsApi.departureTime(DateTime.now())
        directionsApi.origin(exercise.encodedStartPosition())
        directionsApi.destination(exercise.encodedEndPosition())
        val route = directionsApi.await().routes[0]
        val path = PolyUtil.decode(route.overviewPolyline.encodedPath)
        googleMap.addPolyline(PolylineOptions().addAll(path).color(Color.rgb(0, 102, 255)))

        val bounds = if (path.first().latitude > path.last().latitude) {
            LatLngBounds(path.last(), path.first())
        } else {
            LatLngBounds(path.first(), path.last())
        }
        googleMap.setLatLngBoundsForCameraTarget(bounds)
        googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 40))

        if (path.first() == path.last()) {
            googleMap.addMarker(MarkerOptions().position(path.last()))
            googleMap.moveCamera(CameraUpdateFactory.zoomBy(-3.0f))
        }
    }

    fun moveToNextExercise(view: View) {
        val intent = Intent(this, ExerciseActivity::class.java).apply {
            putExtra("index", index + 1)
        }
        startActivity(intent)
    }

    private fun Exercise.encodedStartPosition() = encodeLatLng(this.startPosition)

    private fun Exercise.encodedEndPosition() = encodeLatLng(this.endPosition)

    private fun encodeLatLng(latLng: LatLng) = "${latLng.latitude},${latLng.longitude}"
}
