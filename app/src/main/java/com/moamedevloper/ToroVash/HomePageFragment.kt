package com.moamedevloper.ToroVash

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import androidx.core.content.res.TypedArrayUtils.getBoolean
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import java.lang.Boolean.getBoolean
import java.lang.reflect.Array.getBoolean


class HomePageFragment : Fragment() {
    internal lateinit var view: View
    lateinit var soloBtn: Button
    lateinit var multiBtn: Button
    lateinit var dialogBtn: Button
    lateinit var testTv:TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.fragment_home_page, container, false)

        inisilize()

        /*
        isConnected = arguments?.getBoolean("isConnected").toString()
        testTv.text = isConnected
        isConnected = arguments?.getBoolean("inConnected")
        testTv.text = isConnected.toString()
        if (isConnected) {
            multiBtn.isEnabled = false
            multiBtn.isClickable = false
        } else {
            multiBtn.isEnabled = true
            multiBtn.isClickable = true
        }

        val isConnectd = fragmentViewModel.data.toString()
        if (isConnectd == "true"){
            multiBtn.isEnabled = true
                multiBtn.isClickable = true
        }else {
            multiBtn.isEnabled = false
            multiBtn.isClickable = false
        }


         */

        activity?.let {
            requireActivity()
                .onBackPressedDispatcher
                .addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
                    override fun handleOnBackPressed() {
                        // Do custom work here
                        val builder = AlertDialog.Builder(activity)
                        builder.setMessage("Are you sure to quite the game")
                        builder.setPositiveButton("Cancel"){ _, _ ->
                        }
                        builder.setNeutralButton("Sure") { _, _ ->
                            activity!!.finish()
                        }
                        builder.setCancelable(true)
                        builder.create()
                        builder.show()
                    }
                }
                )
        }

        soloBtn.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.HomeToSolo)
        }

        multiBtn.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.HomeToCreate)
        }

        return view
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun isOnline(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (connectivityManager != null) {
            val capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                    Log.d("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    Log.d("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                    Log.d("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                    return true
                }
            }
        }
        return false
    }

    private fun inisilize() {
        soloBtn = view.findViewById(R.id.solo_btn)
        multiBtn = view.findViewById(R.id.multi_btn)
        dialogBtn = view.findViewById(R.id.rules_btn)
        testTv = view.findViewById(R.id.testTv1)

    }

}