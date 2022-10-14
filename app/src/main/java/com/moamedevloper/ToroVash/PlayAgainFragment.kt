package com.moamedevloper.ToroVash

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import androidx.activity.OnBackPressedCallback
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.DialogFragment
import androidx.navigation.Navigation
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class PlayAgainFragment : DialogFragment() {
    internal lateinit var view:View
    lateinit var numTv: TextInputEditText
    lateinit var player: String
    lateinit var enterBtn: Button
    lateinit var choosedGameCode: String
    private lateinit var dbRef: DatabaseReference
    var choosedNum = ""
    lateinit var playAgainCon : ConstraintLayout
    var playerId = ""
    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_play_again, container, false)


        inisialise()

        // On back press customize
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
                            Navigation.findNavController(view).navigate(R.id.PlayAgainToHome)
                            // remove value -choosedGameCode- from the realtime data base
                            dbRef.child(choosedGameCode).removeValue()
                        }
                        builder.setCancelable(true)
                        builder.create()
                        builder.show()
                    }
                }
                )
        }


        playerId = if (player == "1") {
            "numberPl1"
        }else{
            "numberPl2"
        }
        enterBtn.setOnClickListener{
            choosedNum = numTv.text!!.toString()
            // testTv.text = "chosenGameCode = $choosedGameCode \n choosedNum = $choosedNum \n playerId = $player"
            dbRef.child(choosedGameCode).child(playerId).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (!snapshot.exists()) {
                        if (duplicateCount(choosedNum) > 0 || choosedNum.length != 4) {
                            numTv.error = "invalid number"
                        } else {
                            dbRef.child(choosedGameCode).child(playerId).setValue(choosedNum)
                            Navigation.findNavController(view).navigate(R.id.PlayAgainToMulti,Bundle().apply {
                                putString("chosenGameCode",choosedGameCode)
                                putString("playerId",player)
                            })
                            /*
                           val myintent = Intent(this@CreateJoinPage, MultiPlayerMode::class.java)
                           myintent.putExtra("choosedGameCode", choosedGameCode)
                           myintent.putExtra("playerId", "1")
                           startActivity(myintent)
                           finish()
                            */
                        }
                    }else{
                        numTv.error="Sorry you can't replay with the same room name"
                    }
                }

                override fun onCancelled(p0: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })}
        friendQuit()
        playAgainCon.setOnClickListener {
            // hide soft keyboard on rot layout click
            // it hide soft keyboard on edit text outside root layout click
            requireActivity().hideSoftKeyboard()

            // remove focus from edit text
            numTv.clearFocus()
        }


        return view
    }
    private fun Activity.hideSoftKeyboard(){
        (getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).apply {
            hideSoftInputFromWindow(currentFocus?.windowToken, 0)
        }
    }
    private fun friendQuit() {
        delay = 100L
        /** fun to repeat something every -delay- 1000L ==> 1 sec */
        MultiModeFragment.RepeatHelper.repeatDelayed {
            dbRef.child(choosedGameCode)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            if (delay == 100L){
                                val builder = AlertDialog.Builder(activity)
                                builder.setMessage("Your friend don't want to play again")
                                builder.setPositiveButton("Ok") { _, _ ->
                                    Navigation.findNavController(view) .navigate(R.id.PlayAgainToHome)
                                }
                                builder.setCancelable(false)
                                builder.create()
                                builder.show()
                            }
                            delay =0L
                        }
                    }
                    override fun onCancelled(error: DatabaseError) {
                    }

                })

        }
    }
    private fun inisialise() {
        dbRef = FirebaseDatabase.getInstance().getReference("gameCodes")

        numTv = view.findViewById(R.id.game_code)

        enterBtn = view.findViewById(R.id.BackToGame)

        /** get the argument sent from the previous fragment */
        choosedGameCode = requireArguments().getString("chosenGameCode").toString()


        /** access and change an activity variable from fragment  */
        (requireActivity() as MainActivity).gameCode = choosedGameCode

        player = requireArguments().getString("playerId").toString()

        playAgainCon = view.findViewById(R.id.PlayAgainCon)
    }
    private fun duplicateCount(text: String): Int {
        val invalid = ArrayList<Char>()

        for (i in text.indices) {
            val c = text[i].lowercaseChar()
            if (invalid.contains(c))
                continue

            for (j in i + 1 until text.length) {
                if (c == text[j].lowercaseChar()) {
                    invalid.add(c)
                    break
                }
            }
        }

        return invalid.size
    }

}