package com.example.projemanag.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Task (
    var title: String = "",
    val createdBy: String = ""
): Parcelable