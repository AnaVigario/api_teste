package com.example.myamover.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Moped
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector

sealed class NavigationItem (var route: String, var icon: ImageVector, var title: String) {
    object Home : NavigationItem("home/{email}", Icons.Filled.Home, "Home")
    object Tasks : NavigationItem("tasks", Icons.Filled.Moped, "Tasks")
    object Map : NavigationItem("map", Icons.Filled.Map, "Map")
    object Perfil : NavigationItem("perfil", Icons.Filled.Person, "Perfil")
    object History : NavigationItem("history", Icons.Filled.History, "History")

}