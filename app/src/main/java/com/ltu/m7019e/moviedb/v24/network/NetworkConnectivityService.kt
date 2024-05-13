package com.ltu.m7019e.moviedb.v24.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.produceState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn

sealed class NetworkStatus {
    object Unknown: NetworkStatus()
    object Connected: NetworkStatus()
    object Disconnected: NetworkStatus()
}

class NetworkConnectivityService (
    context: Context
) {
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    val currentConnectivityState: NetworkStatus
        get() {
            return getCurrentConnectivityState(connectivityManager)
        }

    private fun getCurrentConnectivityState(
        connectivityManager: ConnectivityManager
    ): NetworkStatus {
        val connected = connectivityManager.allNetworks.any { network ->
            connectivityManager.getNetworkCapabilities(network)
                ?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                ?: false
        }

        return if (connected) NetworkStatus.Connected else NetworkStatus.Disconnected
    }

    @ExperimentalCoroutinesApi
    fun observeConnectivityAsFlow() = callbackFlow {
        val callback = NetworkCallback { networkStatus: NetworkStatus -> trySend(networkStatus) }

        val networkRequest = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            .build()

        connectivityManager.registerNetworkCallback(networkRequest, callback)

        // Set current state
        val currentState = getCurrentConnectivityState(connectivityManager)
        trySend(currentState)

        // Remove callback when not used
        awaitClose {
            // Remove listeners
            connectivityManager.unregisterNetworkCallback(callback)
        }
    }

    fun NetworkCallback(callback: (NetworkStatus) -> Unit): ConnectivityManager.NetworkCallback {
        return object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                Log.d("ONAVIL", "The network is available")
                //callback(NetworkStatus.Connected)
            }

            override fun onCapabilitiesChanged(
                network: Network,
                networkCapabilities: NetworkCapabilities
            ) {
                super.onCapabilitiesChanged(network, networkCapabilities)
                val unmetered = networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_NOT_METERED)
                Log.d("ONCAPACITY", "The network capacity changed $unmetered")
                if (unmetered) {
                    callback(NetworkStatus.Connected)
                }
            }

            override fun onLost(network: Network) {
                Log.d("ONLOST", "The network is lost")
                callback(NetworkStatus.Disconnected)
            }
        }
    }
}