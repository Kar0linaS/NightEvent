package com.dziubi.nolio.services


import com.dziubi.nolio.data.models.EventModel
import com.dziubi.nolio.data.models.LocationModel
import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.IBinder
import android.os.Looper
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import com.google.android.gms.location.*
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt


class LocalizationBackgroundService : Service() {

    private val LOCATION_SERVICE_NOTIF_ID = 1
    private val LOCATION_SERVICE_DANAGER_NOTIF_ID = 2

    private val LOC_CHANNEL_ID = "LOC_CHANNEL_ID"
    private val UPDATE_INTERVAL_IN_MILIS: Long = 1_000 * 10
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            super.onLocationResult(locationResult)

            val locationModel = LocationModel(
                locationResult.lastLocation?.latitude,
                locationResult.lastLocation?.longitude
            )

            CoroutineScope(Dispatchers.IO).launch {
                if (isNearEvent(locationModel))
                    createWarningNotification()
            }
        }
    }

    private fun createWarningNotification() {

        val builder = NotificationCompat.Builder(this, LOC_CHANNEL_ID)
            .setContentTitle("Uważaj! Jesteś w pobliżu incydentu")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setSmallIcon(com.dziubi.nolio.R.drawable.ic_launcher_foreground)
            .build()

        val channel = NotificationChannel(
            LOC_CHANNEL_ID,
            "LOC_SERVICE",
            NotificationManager.IMPORTANCE_DEFAULT
        )

        val manger = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manger.createNotificationChannel(channel)
        manger.notify(LOCATION_SERVICE_DANAGER_NOTIF_ID, builder)
    }

    private suspend fun isNearEvent(locationModel: LocationModel): Boolean {

        db.collection("events")
            .get()
            .await()
            .toObjects(EventModel::class.java)
            .forEach { inc ->
                if (calculateDistance(locationModel, inc.location) <= 1) return true
            }

        return false
    }

    private fun calculateDistance(userLocation: LocationModel, location: LocationModel?): Double {

        userLocation.lat ?: return -1.0
        userLocation.lng ?: return -1.0

        val userLat = userLocation.lat
        val userLng = userLocation.lng

        location?.lat ?: return -1.0
        location?.lng ?: return -1.0

        val venueLat = location.lat
        val venueLng = location.lng

        val latDistance = Math.toRadians(userLat - venueLat)
        val lngDistance = Math.toRadians(userLng - venueLng)

        val a = (sin(latDistance / 2) * sin(latDistance / 2)
                + (cos(Math.toRadians(userLat)) * cos(Math.toRadians(venueLat))
                * sin(lngDistance / 2) * sin(lngDistance / 2)))

        val c = 2 * atan2(sqrt(a), sqrt(1 - a))

        val result = 6371 * c
        Log.d("LOC_D", "Distance: $result")
        return result
    }

    private val db = FirebaseFirestore.getInstance()

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        initData()
    }

    private fun initData() {
        locationRequest = LocationRequest.create()
            .apply {
                interval = UPDATE_INTERVAL_IN_MILIS
                priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(applicationContext)
    }

    override fun stopService(name: Intent?): Boolean {
        fusedLocationClient.removeLocationUpdates(locationCallback)
        return super.stopService(name)
    }

    override fun onDestroy() {
        super.onDestroy()
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        intent?.action?.let {
            if (it == "STOP_SERVICE") stopSelf()
        }

        startLocationUpdate()
        prepareForegroundNotification()
        return START_NOT_STICKY
    }

    private fun startLocationUpdate() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.myLooper()!!
        )
    }

    private fun prepareForegroundNotification() {
        val serviceChannel = NotificationChannel(
            LOC_CHANNEL_ID,
            "Location Service Channel",
            NotificationManager.IMPORTANCE_DEFAULT
        )

        val manger = getSystemService(NotificationManager::class.java)
        manger.createNotificationChannel(serviceChannel)

        val stopSelfIntent = Intent(applicationContext, LocalizationBackgroundService::class.java)
            .apply { action = "STOP_SERVICE" }

        val pendingStopIntent = PendingIntent.getService(
            applicationContext,
            0,
            stopSelfIntent,
            PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(this, LOC_CHANNEL_ID)
            .setSmallIcon(com.dziubi.nolio.R.drawable.ic_launcher_foreground)
            .setContentTitle("Serwis lokalizujacy dziala w tle! Jestes bezpieczny!")
            .addAction(com.dziubi.nolio.R.drawable.ic_launcher_foreground, "Stop service", pendingStopIntent)
            .setOngoing(true)
            .build()

        startForeground(LOCATION_SERVICE_NOTIF_ID, notification)
    }


}