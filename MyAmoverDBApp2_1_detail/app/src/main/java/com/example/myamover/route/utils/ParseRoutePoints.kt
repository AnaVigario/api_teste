package com.example.myamover.route.utils

import com.example.myamover.route.VrptwPayload
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson

fun parseRoutePoints(json: String, includeStations: Boolean = false, returnDepot: Boolean = false): List<LatLng> {
    val payload = Gson().fromJson(json, VrptwPayload::class.java)
    val nodes = payload?.nodes ?: emptyList()
    if (nodes.isEmpty()) return emptyList()

    val depot = nodes.firstOrNull { it.is_depot == true } ?: nodes.first()
    val stops = nodes.filter {
        when {
            it.is_depot == true -> false
            it.is_charging_station == true && !includeStations -> false
            else -> true
        }
    }.toMutableList()

    if (returnDepot) stops.add(depot)

    val ordered = listOf(depot) + stops
    return ordered.map { LatLng(it.y, it.x) } // y=lat, x=lon
}