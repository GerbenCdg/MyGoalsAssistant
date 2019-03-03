package com.gmail.gerbencdg.mygoalsassistant.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent


class NetworkBroadcastReceiver(private val networkCallback : NetworkAvailableCallBack) : BroadcastReceiver() {

    companion object {
        // Thanks to this variable, we ensure not to call onNetworkAvailable
        // When we switch e.g. from a mobile data network to Wifi
        private var wasConnectedPreviously : Boolean = false
    }

    override fun onReceive(context: Context, intent: Intent) {

        if (intent.action == "android.net.conn.CONNECTIVITY_CHANGE") {

            wasConnectedPreviously = if (NetworkUtils.isConnected(context) && !wasConnectedPreviously) {
                networkCallback.onNetworkAvailable()
                true
            } else {
                false
            }
        }
    }

    interface NetworkAvailableCallBack{
        fun onNetworkAvailable()
    }
}