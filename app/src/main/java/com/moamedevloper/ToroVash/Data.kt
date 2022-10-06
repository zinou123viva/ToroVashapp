package com.moamedevloper.ToroVash

import android.os.Parcelable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.parcelize.Parcelize

@Parcelize
data class Data(
    val isConnected: String
) : Parcelable