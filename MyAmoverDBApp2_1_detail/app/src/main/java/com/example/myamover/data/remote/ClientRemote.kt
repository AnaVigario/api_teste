package com.example.myamover.data.remote

import kotlinx.serialization.SerialName

data class ClientRemote(
    @SerialName("ID")
    val id: Int,
    val name: String,
    val nif: String,
    val address: String,
    val phone: String,
    val email: String,

)
