package com.outhlete.outhlete

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.view.View
import android.widget.Toast
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PolylineOptions
import com.google.maps.DirectionsApiRequest
import com.google.maps.GeoApiContext
import com.google.maps.PendingResult
import com.google.maps.android.PolyUtil
import com.google.maps.model.DirectionsResult
import com.google.maps.model.TravelMode
import com.outhlete.outhlete.R.id.map
import com.outhlete.outhlete.algorithm.WorkoutCreator
import com.outhlete.outhlete.domain.Exercise
import org.joda.time.DateTime
import java.util.concurrent.TimeUnit

class WorkoutOverviewActivity : FragmentActivity(), OnMapReadyCallback {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_workout_overview)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager.findFragmentById(map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        val location = intent.getParcelableExtra<LatLng>(ConfigureWorkoutActivity.EXTRA_LOCATION)
        val duration = intent.getIntExtra(ConfigureWorkoutActivity.EXTRA_DURATION, 60)
        val workout = WorkoutCreator().makeWorkout(location, duration)

        if (workout.exercises.isEmpty()) { // FIXME Move the workout creation in the first view
            return
        }

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
        directionsApi.origin(workout.exercises.first().encodedStarPosition())
        directionsApi.destination(workout.exercises.first().encodedStarPosition())

        val waypoints = workout.wayPoints
        waypoints.removeAt(0)
        waypoints.removeAt(waypoints.lastIndex)
        directionsApi.waypoints(*waypoints.map { encodeLatLng(it) }.toTypedArray())

        val route = directionsApi.await().routes[0]
        val path = PolyUtil.decode(route.overviewPolyline.encodedPath)
        googleMap.addPolyline(PolylineOptions().addAll(path).color(Color.rgb(0, 102, 255)))
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 14f))
    }

    fun startWorkout(view: View) {
        val intent = Intent(this, ExerciseActivity::class.java)
        startActivity(intent)
    }

    private fun Exercise.encodedStarPosition() = encodeLatLng(this.startPosition)

    private fun Exercise.encodedEndPosition() = encodeLatLng(this.endPosition)

    private fun encodeLatLng(latLng: LatLng) = "${latLng.latitude},${latLng.longitude}"
}
