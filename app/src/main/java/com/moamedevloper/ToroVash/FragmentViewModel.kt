package com.moamedevloper.ToroVash

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class FragmentViewModel : ViewModel() {
    private val data = MutableLiveData<String>()
    val storedData : LiveData<String> = data

    fun setData (newData : String){
        data.value = newData
    }
}