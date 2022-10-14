package com.moamedevloper.ToroVash

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.activity.OnBackPressedCallback
import androidx.navigation.Navigation


class HomePageFragment : Fragment() {
    internal lateinit var view: View
    lateinit var soloBtn: Button
    lateinit var multiBtn: Button
    lateinit var dialogBtn: Button
    lateinit var againBtn :Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.fragment_home_page, container, false)



        inisilize()

        (requireActivity() as MainActivity).enable(multiBtn)
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



        /** On back press customize */
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
        dialogBtn.setOnClickListener{
            // Navigation.findNavController(view).navigate(R.id.HomeToMulti)
        }
        againBtn.setOnClickListener{

            /*Navigation.findNavController(view).navigate(R.id.HomeToPlayAgain,Bundle().apply {
                putString("chosenGameCode","zinou".trim())
                putString("playerId","1".trim())
            })*/

        }



        return view
    }



    private fun inisilize() {
        soloBtn = view.findViewById(R.id.solo_btn)
        multiBtn = view.findViewById(R.id.multi_btn)
        dialogBtn = view.findViewById(R.id.rules_btn)
        againBtn = view.findViewById(R.id.info_btn)

    }

}