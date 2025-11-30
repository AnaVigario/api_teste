package com.example.myamover.route.utils

import android.content.Context



//Ler o json
fun loadJsonFromAssets(context: Context, fileName: String): String {
    return context.assets.open(fileName).bufferedReader().use { it.readText() }
}