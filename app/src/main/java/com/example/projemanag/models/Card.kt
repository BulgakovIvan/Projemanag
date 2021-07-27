package com.example.projemanag.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Card(
    val name: String = "",
    val createBy: String = "",
    val assignedTo: ArrayList<String> = ArrayList(),
    val labelColor: String = ""
): Parcelable