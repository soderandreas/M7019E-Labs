package com.ltu.m7019e.moviedb.v24.worker

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters

private const val TAG = "ConnectionWorker"

class CheckConnectionWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {
    override suspend fun doWork(): Result {
        Log.d(TAG, "GET HERE?")
        return if (isConnected()) {
            Result.success()
        } else {
            Result.retry()
        }
    }

    private val connectivityManager = context.getSystemService(ConnectivityManager::class.java)

    // https://medium.com/@meytataliti/obtaining-network-connection-info-with-flow-in-android-af2e6b760dfd
    private fun isConnected(): Boolean {
        // Network class represents one of the networks that the device is connected to.
        val activeNetwork = connectivityManager.activeNetwork
        return if (activeNetwork == null) {
            false // if there is no active network, then simply no internet connection.
        } else {
            // NetworkCapabilities object contains information about properties of a network
            val netCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork)
            (netCapabilities != null
                    // indicates that the network is set up to access the internet
                    && netCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                    // indicates that the network provides actual access to the public internet when it is probed
                    && netCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED))
        }
    }
}