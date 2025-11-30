package com.example.myamover.route.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import com.example.myamover.route.VrptwNode
import com.example.myamover.route.VrptwPayload
import com.google.gson.Gson
import java.net.URLEncoder
import java.nio.charset.StandardCharsets


//Abrir rota no Google Maps
private fun enc(s: String) = URLEncoder.encode(s, StandardCharsets.UTF_8.toString())
private fun latLng(n: VrptwNode) = "${n.y},${n.x}" // y = lat, x = lon


/**
 * Lê um JSON no formato VrptwPayload e abre a rota no Google Maps:
 * - origem: primeiro nó com is_depot=true (ou o primeiro nó caso não exista)
 * - paragens: nós que não são depósito (ignora estações por padrão)
 * - destino: última paragem
 */
fun startRouteFromNodesJson(
    context: Context,
    json: String,
    includeChargingStationsAsStops: Boolean = true,
    returnToDepot: Boolean = true
) {
    try {
        val payload = Gson().fromJson(json, VrptwPayload::class.java)
        val nodes = payload?.nodes ?: emptyList()
        if (nodes.isEmpty()) return

        val depot = nodes.firstOrNull { it.is_depot == true } ?: nodes.first()

        // filtrar paragens
        val stops = nodes.filter {
            when {
                it.is_depot == true -> false
                it.is_charging_station == true && !includeChargingStationsAsStops -> false
                else -> true
            }
        }.toMutableList()

        // se for para voltar ao depósito, adiciona ele no fim como destino
        if (returnToDepot) stops.add(depot)

        val origin = depot
        val destination = stops.lastOrNull() ?: depot
        val waypoints = if (stops.size > 1) stops.dropLast(1) else emptyList()

        val originStr = enc("${origin.y},${origin.x}")
        val destStr = enc("${destination.y},${destination.x}")
        val waypointsStr = waypoints
            .take(23)
            .joinToString("|") { "${it.y},${it.x}" }
            .let { if (it.isNotEmpty()) enc(it) else "" }

        val url = StringBuilder("https://www.google.com/maps/dir/?api=1")
            .append("&origin=").append(originStr)
            .append("&destination=").append(destStr)
            .append("&travelmode=driving")
        if (waypointsStr.isNotEmpty()) url.append("&waypoints=").append(waypointsStr)

        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url.toString()))
            .apply { setPackage("com.google.android.apps.maps") }
        if (intent.resolveActivity(context.packageManager) == null) intent.setPackage(null)
        context.startActivity(intent)

    } catch (t: Throwable) {
        Toast.makeText(context, "Falha ao abrir rota: ${t.message}", Toast.LENGTH_SHORT).show()
    }
}