package com.outhlete.outhlete

import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import com.google.android.gms.maps.model.LatLng
import com.outhlete.outhlete.algorithm.WorkoutCreator
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class WorkoutCreatorTest {
    @Test
    fun fromUSITest() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getTargetContext()
        val start = LatLng(46.010617, 8.958026)
        val workout = WorkoutCreator().makeWorkout(start, 60)
        assertTrue(workout.exercises.size > 0)
        assertTrue(workout.totalDuration < 75)
        assertTrue(workout.totalDuration > 45)
    }

    @Test
    fun fromDanieleHouse() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getTargetContext()
        val start = LatLng(45.998023, 8.942444)
        val workout = WorkoutCreator().makeWorkout(start, 60)
        assertTrue(workout.exercises.size > 0)
        assertTrue(workout.totalDuration < 75)
        assertTrue(workout.totalDuration > 45)
    }

    @Test
    fun fromUSITest75min() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getTargetContext()
        val start = LatLng(46.010617, 8.958026)
        val workout = WorkoutCreator().makeWorkout(start, 75)
        assertTrue(workout.exercises.size > 0)
        assertTrue(workout.totalDuration < 100)
        assertTrue(workout.totalDuration > 60)
    }

    @Test
    fun fromDanieleHouse75min() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getTargetContext()
        val start = LatLng(45.998023, 8.942444)
        val workout = WorkoutCreator().makeWorkout(start, 75)
        assertTrue(workout.exercises.size > 0)
        assertTrue(workout.totalDuration < 100)
        assertTrue(workout.totalDuration > 60)
    }

    @Test
    fun fromUSITest120min() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getTargetContext()
        val start = LatLng(46.010617, 8.958026)
        val workout = WorkoutCreator().makeWorkout(start, 120)
        assertTrue(workout.exercises.size > 0)
        assertTrue(workout.totalDuration < 150)
        assertTrue(workout.totalDuration > 80)
    }

    @Test
    fun fromDanieleHouse120min() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getTargetContext()
        val start = LatLng(45.998023, 8.942444)
        val workout = WorkoutCreator().makeWorkout(start, 120)
        assertTrue(workout.exercises.size > 0)
        assertTrue(workout.totalDuration < 150)
        assertTrue(workout.totalDuration > 80)
    }
}