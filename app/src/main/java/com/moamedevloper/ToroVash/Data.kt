package com.moamedevloper.ToroVash

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Data(
    val isConnected: String
) : Parcelable