package com.example.projemanag.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Board (
    val name: String = "",
    val image: String = "",
    val createdBy: String = "",
    val assignedTo: ArrayList<String> = ArrayList()
    ): Parcelable