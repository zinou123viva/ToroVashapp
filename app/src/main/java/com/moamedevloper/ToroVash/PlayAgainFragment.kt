package com.moamedevloper.ToroVash

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.navigation.Navigation
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.database.*


class PlayAgainFragment : Fragment() {
    internal lateinit var view:View
    lateinit var numTv: TextInputEditText
    lateinit var player: String
    lateinit var enterBtn: Button
    lateinit var choosedGameCode: String
    lateinit var testTv: TextView
    private lateinit var dbRef: DatabaseReference
    var choosedNum = ""
    var playerId = ""
    @SuppressLint("SetTextI18n")
    override fun onCreateView(

        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        view  = inflater.inflate(R.layout.fragment_play_again, container, false)
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
                            Navigation.findNavController(view).navigate(R.id.AgainToHome)
                        }
                        builder.setCancelable(true)
                        builder.create()
                        builder.show()
                    }
                }
                )
        }


        inisialise()


        playerId = if (player == "1") {
            "numberPl1"
        }else{
            "numberPl2"
        }
        enterBtn.setOnClickListener{
            choosedNum = numTv.text!!.toString()
            testTv.text = "chosenGameCode = $choosedGameCode \n choosedNum = $choosedNum \n playerId = $player"
            dbRef.child(choosedGameCode).addListenerForSingleValueEvent(object :
                ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (!snapshot.exists()) {
                        if (duplicateCount(choosedNum) > 0 || choosedNum.length != 4) {
                            numTv.error = "invalid number"
                        } else {
                            dbRef.child(choosedGameCode).child(playerId).setValue(choosedNum)
                            Navigation.findNavController(view).navigate(R.id.AgainToMulti,Bundle().apply {
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
                        numTv.error="Sorry you can't replay with the same room name "
                    }
                }

                override fun onCancelled(p0: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })}


        return view
    }
    private fun inisialise() {
        dbRef = FirebaseDatabase.getInstance().getReference("gameCodes")

        numTv = view.findViewById(R.id.game_code)

        testTv = view.findViewById(R.id.testTvAgain)

        enterBtn = view.findViewById(R.id.BackToGame)

        choosedGameCode = requireArguments().getString("chosenGameCode").toString()

        player = requireArguments().getString("playerId").toString()
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
