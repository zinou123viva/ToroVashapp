package com.moamedevloper.ToroVash

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation


class HomePageFragment : Fragment() {
    internal lateinit var view: View
    lateinit var soloBtn: Button
    lateinit var multiBtn: Button
    lateinit var dialogBtn: Button
    private var isConnected: Boolean = false
    lateinit var testTv:TextView
    val fragmentViewModel: FragmentViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home_page, container, false)
        inisilize()
        /*isConnected = arguments?.getBoolean("inConnected")
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
        soloBtn.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.HomeToSolo)
        }
        multiBtn.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.HomeToCreate)
        }






        return view
    }

    private fun inisilize() {
        soloBtn = view.findViewById(R.id.solo_btn)
        multiBtn = view.findViewById(R.id.multi_btn)
        dialogBtn = view.findViewById(R.id.rules_btn)
        testTv = view.findViewById(R.id.testTv1)

    }

}