package com.example.myamover.screens

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import com.google.maps.android.compose.GoogleMap as MapsGoogleMap


@Composable
fun MapScreen(
    windowSize: WindowWidthSizeClass,
    modifier: Modifier= Modifier,
    points: List<LatLng> = emptyList()
) {
    when (windowSize) {
        WindowWidthSizeClass.Compact -> {
        }

        WindowWidthSizeClass.Medium -> {
        }

        WindowWidthSizeClass.Expanded -> {
        }

        else -> {
        }
    }

    val context = LocalContext.current
    var granted by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) ==
                    PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) ==
                    PackageManager.PERMISSION_GRANTED
        )
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { result ->
        granted = (result[android.Manifest.permission.ACCESS_FINE_LOCATION] == true) ||
                (result[android.Manifest.permission.ACCESS_COARSE_LOCATION] == true)
    }

    LaunchedEffect(Unit) {
        if (!granted) {
            launcher.launch(arrayOf(
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ))
        }
    }

    GoogleMap(
        modifier = modifier.fillMaxSize(),
        startLat = points.firstOrNull()?.latitude ?: 41.3006,
        startLng = points.firstOrNull()?.longitude ?: -7.7441,
        startZoom = 12f,
        showMyLocation = true,
        points = points// <- só ativa se realmente concedida
    )
}

@SuppressLint("UnrememberedMutableState")
@Composable
fun GoogleMap(
    modifier: Modifier = Modifier,
    startLat: Double = -23.55052,
    startLng: Double = -46.63331,
    startZoom: Float = 12f,
    showMyLocation: Boolean = true,
    points: List<LatLng> = emptyList()// desejo
) {
//    val context = LocalContext.current
//    val hasFine = ContextCompat.checkSelfPermission(
//        context,  android.Manifest.permission.ACCESS_FINE_LOCATION
//    ) == PackageManager.PERMISSION_GRANTED
//    val hasCoarse = ContextCompat.checkSelfPermission(
//        context, android.Manifest.permission.ACCESS_COARSE_LOCATION
//    ) == PackageManager.PERMISSION_GRANTED
//    val granted = hasFine || hasCoarse

    val camera = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(startLat, startLng), startZoom)
    }

    val ui = remember( showMyLocation) {
        MapUiSettings(
            myLocationButtonEnabled = showMyLocation,
            compassEnabled = true,
            mapToolbarEnabled = true,
            zoomControlsEnabled = false
        )
    }
    val props = remember( showMyLocation) {
        MapProperties(
            isMyLocationEnabled = showMyLocation
        )
    }

    MapsGoogleMap( // import alias: import com.google.maps.android.compose.GoogleMap as MapsGoogleMap
        modifier = modifier,
        cameraPositionState = camera,
        uiSettings = ui,
        properties = props
    ) {
        //desenhar marcadores
        points.forEachIndexed { index, latLng ->
            Marker(
                state = rememberMarkerState(position = latLng),
                title = "Ponto ${index + 1}"
            )
        }

        // desenhar polyline ligando todos
        if (points.size > 1) {
            com.google.maps.android.compose.Polyline(
                points = points,
                color = androidx.compose.ui.graphics.Color.Blue
            )
        }
    }
}