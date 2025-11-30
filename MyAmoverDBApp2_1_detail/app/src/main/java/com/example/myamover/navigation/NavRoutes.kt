package com.example.myamover.navigation

object NavRoutes {

    const val Login = "login"
    const val Home = "home"
    const val HomeWithArgs = "home/{email}"
    const val Tasks = "tasks"
    const val Map = "map"
    const val Perfil = "perfil"
    const val History = "history"

    const val ViewTaskDetail = "viewTaskDetail"


    fun home(email: String) = "home/$email"
}