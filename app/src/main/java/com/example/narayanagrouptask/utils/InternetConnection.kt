package com.example.narayanagrouptask.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities

fun checkInternet(context: Context): Boolean {
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    val networks = connectivityManager.allNetworks
    var hasInternet = false
    if (networks.isNotEmpty()) {
        for (network in networks) {
            val nc = connectivityManager.getNetworkCapabilities(network)
            if (nc!!.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)) hasInternet = true
        }
    }
    return hasInternet
}