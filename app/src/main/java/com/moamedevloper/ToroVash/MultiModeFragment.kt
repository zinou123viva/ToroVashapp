package com.moamedevloper.ToroVash

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.activity.OnBackPressedCallback
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.database.*
import java.util.*


var delay = 0L

class MultiModeFragment : Fragment() {
    internal lateinit var view: View
    private lateinit var numberEntredField: TextInputEditText
    private lateinit var recentTryToroField: TextInputEditText
    private lateinit var recentTryVashField: TextInputEditText
    private lateinit var recentTryNumberField: TextInputEditText
    private lateinit var btnView: Button
    private var sinew = true
    private var resultNum: String? = ""
    private var resultT: String? = ""
    private var resultV: String? = ""
    lateinit var constraintLayout  : ConstraintLayout
    lateinit var constraintLayoutScroll: ConstraintLayout
    var selectednumber :String? = null
    var numberEntered = ""
    var toro: Int = 0
    var vash: Int = 0
    var resultNumber: String = ""
    var resultToro: String = ""
    var resultVash: String = ""
    private lateinit var dbRef: DatabaseReference
    var player: String? = null
    var choosedGameCode: String? = null
     var quite = "0"
    var conct = 1
    lateinit var timer: Chronometer
    lateinit var waitFried: TextView
    var friendsTry = "null"
    var playerId = ""
    var tryId = ""

    var numberOfTry = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.fragment_multi_mode, container, false)

        inisialise()

        (requireActivity() as MainActivity).fragment = "multiMode"
        (requireActivity() as MainActivity).view = view


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
                             Navigation.findNavController(view).navigate(R.id.MultiToHomePage)
                            dbRef.child(choosedGameCode!!).removeValue()
                            quite = player!!
                            conct = 0
                        }
                        builder.setCancelable(true)
                        builder.create()
                        builder.show()
                    }
                }
                )
        }


        /*val callback = object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                TODO("Not yet implemented")

            }

        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,callback)
       */




        gettingTheNumber()

        btnView.setOnClickListener {

            confirmbtn()
        }
        friendQuit()
        /** hide the key board when the user click outside the edit text fields */
        // root constraint layout click listener
        constraintLayout.setOnClickListener {
            // hide soft keyboard on rot layout click
            // it hide soft keyboard on edit text outside root layout click
            requireActivity().hideSoftKeyboard()

            // remove focus from edit text
            numberEntredField.clearFocus()
        }
        constraintLayoutScroll.setOnClickListener {
            // hide soft keyboard on rot layout click
            // it hide soft keyboard on edit text outside root layout click
            requireActivity().hideSoftKeyboard()

            // remove focus from edit text
            numberEntredField.clearFocus()
        }

            return view
    }
    fun Activity.hideSoftKeyboard(){
        (getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager).apply {
            hideSoftInputFromWindow(currentFocus?.windowToken, 0)
        }
    }
    private fun friendQuit() {
        delay = 1000L
        if (conct == 1 ){
            quite = player.toString()
        }
        RepeatHelper.repeatDelayed {
            dbRef.child(choosedGameCode!!)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (!snapshot.exists()) {
                            if (delay == 1000L && quite != player){
                                val buildera = AlertDialog.Builder(activity)
                                buildera.setMessage("Your friend quite the game \n You win ")
                                buildera.setPositiveButton("Ok") { _, _ ->
                                    Navigation.findNavController(view) .navigate(R.id.MultiToHomePage)
                                }
                                buildera.setCancelable(false)
                                buildera.create()
                                buildera.show()
                            }
                            delay =0L
                        }
                    }
                    override fun onCancelled(error: DatabaseError) {
                    }

                })

        }
    }

    private fun confirmbtn() {
        numberEntered = numberEntredField.text!!.toString()
        if (duplicateCount(numberEntered) > 0 || numberEntered.length != 4) {
            numberEntredField.error = "invalid input"
        } else {
            returnedresult(selectednumber.toString(), resultNum!!, resultT!!, resultV!!)
        }
    }

    private fun gettingTheNumber() {

        when (player) {
            "1" -> {
                dbRef.child(choosedGameCode!!).addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        selectednumber = snapshot.child("numberPl2").value.toString()
                    }

                    override fun onCancelled(error: DatabaseError) {
                    }

                })
            }
            "2" -> {
                dbRef.child(choosedGameCode!!).addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        selectednumber = snapshot.child("numberPl1").value.toString()
                    }

                    override fun onCancelled(error: DatabaseError) {
                    }

                })
            }
            else -> {
                numberEntredField.error = "Retry with an other Code"
            }
        }
        /** when (player) {
        "1" -> {
        dbRef.child(choosedGameCode!!).child("numberPl2").addListenerForSingleValueEvent(object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
        if (snapshot.exists()) {
        dbRef.child(choosedGameCode!!).addValueEventListener(object :ValueEventListener{
        override fun onDataChange(p0: DataSnapshot) {
        selectednumber = p0.child("numberPl2").value.toString()
        }

        override fun onCancelled(p0: DatabaseError) {
        }

        })
        }
        }
        override fun onCancelled(p0: DatabaseError) {
        TODO("Not yet implemented")
        }
        })
        }
        "2" -> {
        dbRef.child(choosedGameCode!!).child("numberPl1").addListenerForSingleValueEvent(object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
        if (snapshot.exists()) {
        dbRef.child(choosedGameCode!!).addValueEventListener(object :ValueEventListener{
        override fun onDataChange(p0: DataSnapshot) {
        selectednumber = p0.child("numberPl1").value.toString()
        }
        override fun onCancelled(p0: DatabaseError) {
        }

        })
        }

        }

        override fun onCancelled(p0: DatabaseError) {
        TODO("Not yet implemented")
        }
        }
        )
        }
        else -> {
        numberEntredField.error = "Retry with an other Code"
        }
        }

         */
    }




    private fun inisialise() {

        numberEntredField = view.findViewById(R.id.number_enterd)
        recentTryToroField = view.findViewById(R.id.Recent_try_answer_toro)
        recentTryVashField = view.findViewById(R.id.Recent_try_answer_vash)
        recentTryNumberField = view.findViewById(R.id.Recent_try_number)
        btnView = view.findViewById(R.id.button)
        dbRef = FirebaseDatabase.getInstance().getReference("gameCodes")
        timer = view.findViewById(R.id.simpleChronometer)
        waitFried = view.findViewById(R.id.waitFriend)
        constraintLayout = view.findViewById(R.id.container)
        constraintLayoutScroll = view.findViewById(R.id.scrollViewCon)
        choosedGameCode = requireArguments().getString("chosenGameCode").toString()
        player = requireArguments().getString("playerId").toString()
        if (player == "1") {
            playerId = "numberPl1"
            tryId = "tryPl1"
        }else{
            playerId = "numberPl2"
            tryId = "tryPl1"
        }
        /** access a activity variable from fragment */
        (requireActivity() as MainActivity).gameCode = choosedGameCode!!
    }


    @SuppressLint("SetTextI18n")
    private fun returnedresult(
        selectednumber: String,
        resultNumbe: String,
        resultTor: String,
        resultVas: String,
    ) {

        if (!sinew) {
            resultNumber = resultNumbe
            resultToro = resultTor
            resultVash = resultVas
        }
        if (selectednumber != "null") {
            if (numberEntredField.text.toString().isNotEmpty()) {
                numberEntered = numberEntredField.text!!.toString()

                if (selectednumber == numberEntered) {
                    resultVash += " \n\n  "
                    resultToro += " \n 4T "
                    resultNumber += "\n $numberEntered "
                    numberEntredField.clearFocus()
                    numberEntredField.isEnabled = false
                    if (player == "1") {
                        dbRef.child(choosedGameCode!!).child("tryPl1").setValue(numberOfTry)
                        theWinner()
                    } else {
                        dbRef.child(choosedGameCode!!).child("tryPl2").setValue(numberOfTry)
                        theWinner()
                    }

                    /*to force hide keyboard
                            this.numberEntredField.let { view ->
                                val imm =
                                    getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                                imm?.hideSoftInputFromWindow(view.windowToken, 0)
                            }
                            AlertDialog.Builder(this)
                                .setTitle("You Found It!!")
                                .setMessage("\nCongratulation that was so smart \n\n    Try : $numberOfTry  " +
                                        "\n\n    The Number Was : $selectednumber\n")
                                .show()
                             */

                } else {
                    for (count1 in 0..3) {
                        if (selectednumber[count1] == numberEntered[count1]) toro ++
                        for (count2 in 0..3) {
                            if (selectednumber[count1] == numberEntered[count2] && count1 != count2) vash ++
                        }
                    }
                    if (toro == 0 && vash == 0) {
                        resultVash += " \n X "
                        resultToro += " \n X "
                        resultNumber += " \n $numberEntered "

                    } else if (vash == 0) {
                        resultVash += " \n "
                        resultNumber += "\n  $numberEntered "
                        resultToro += " \n $toro T"
                    } else if (toro == 0) {
                        resultNumber += " \n  $numberEntered "
                        resultVash += " \n $vash V"
                        resultToro += " \n "
                    } else {
                        resultNumber += "\n  $numberEntered "
                        resultVash += " \n $vash V"
                        resultToro += " \n $toro T"
                    }

                }
                recentTryNumberField.setText(resultNumber)
                recentTryToroField.setText(resultToro)
                recentTryVashField.setText(resultVash)
                toro = 0
                vash = 0
                numberOfTry++
                numberEntredField.text!!.clear()
            } else {
                numberEntredField.error = "number required"
            }
        } else {
            numberEntredField.error = "Waiting your friend to choose a number"
        }
    }

    @SuppressLint("SetTextI18n")
    private fun theWinner() {
        delay = 100L
        friendQuit()
        RepeatHelper.repeatDelayed {
            gettingTry()
            if (friendsTry == "null") {
                waitFried.isVisible = true
                waitFried.text = "Friend Still Trying To Find Out The Number ..."

            } else if (friendsTry.toInt() > (numberOfTry - 1)) {
                waitFried.isVisible = false
                /*val msg = "friendsTry = $friendsTry\n your try = $numberOfTry"
                                testTv.isVisible = true
                                testTv.text = msg
                Toast.makeText(activity, "You win", Toast.LENGTH_SHORT).show()
                 */
                val buildera = AlertDialog.Builder(activity)
                buildera.setMessage("You win")
                buildera.setPositiveButton("Play Again") { _, _ ->
                    dbRef.child(choosedGameCode!!).child(playerId)
                        .addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                if (snapshot.exists()) {
                                    dbRef.child(choosedGameCode!!).child(playerId).removeValue()
                                    dbRef.child(choosedGameCode!!).child(tryId).removeValue()                                }
                            }
                            override fun onCancelled(error: DatabaseError) {
                                TODO("Not yet implemented")
                            }
                        })
                    Navigation.findNavController(view).navigate(R.id.MultiToPlayAgain,Bundle().apply {
                        putString("chosenGameCode",choosedGameCode)
                        putString("playerId",player)
                    })

                }
                buildera.setNeutralButton("Exit"){_,_ ->
                    dbRef.child(choosedGameCode!!).removeValue()

                    Navigation.findNavController(view).navigate(R.id.MultiToHomePage)
                }
                buildera.setCancelable(false)
                buildera.create()
                buildera.show()
                delay = 0L
            } else if (friendsTry.toInt() < (numberOfTry - 1)) {
                waitFried.isVisible = false
                /*val msg = "friendsTry = $friendsTry\n your try = $numberOfTry"
                                testTv.isVisible = true
                                testTv.text = msg
                Toast.makeText(activity, "You lost", Toast.LENGTH_SHORT).show()
                */

                /** alert message */
                val builder = AlertDialog.Builder(activity)
                builder.setMessage("You lose")
                builder.setPositiveButton("Play Again") { _, _ ->
                    dbRef.child(choosedGameCode!!).child(playerId)
                        .addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                if (snapshot.exists()) {
                                    dbRef.child(choosedGameCode!!).child(playerId).removeValue()
                                    dbRef.child(choosedGameCode!!).child(tryId).removeValue()
                                }
                            }
                            override fun onCancelled(error: DatabaseError) {
                                TODO("Not yet implemented")
                            }
                        })
                    Navigation.findNavController(view).navigate(R.id.MultiToPlayAgain,Bundle().apply {
                    putString("chosenGameCode",choosedGameCode)
                    putString("playerId",player)
                })

                }
                builder.setNeutralButton("Exit"){_,_ ->
                    dbRef.child(choosedGameCode!!).removeValue()

                    Navigation.findNavController(view).navigate(R.id.MultiToHomePage)
                }
                builder.setCancelable(false)
                builder.create()
                builder.show()
                delay = 0L
            } else {
                waitFried.isVisible = false
                /*val msg = "friendsTry = $friendsTry\n your try = $numberOfTry"
                                testTv.isVisible = true
                                testTv.text = msg
                Toast.makeText(activity, "Draw", Toast.LENGTH_SHORT).show()
                 */
                val builder = AlertDialog.Builder(activity)
                builder.setMessage("Draw")
                builder.setPositiveButton("Play Again") { _, _ ->
                    dbRef.child(choosedGameCode!!).child(playerId)
                        .addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                if (snapshot.exists()) {
                                    dbRef.child(choosedGameCode!!).child(playerId).removeValue()
                                    dbRef.child(choosedGameCode!!).child(tryId).removeValue()
                                }
                            }
                            override fun onCancelled(error: DatabaseError) {
                                TODO("Not yet implemented")
                            }
                        })
                    Navigation.findNavController(view).navigate(R.id.MultiToPlayAgain,Bundle().apply {
                        putString("chosenGameCode",choosedGameCode)
                        putString("playerId",player)
                    })
                }
                builder.setNeutralButton("Exit"){_,_ ->
                    dbRef.child(choosedGameCode!!).removeValue()
                    Navigation.findNavController(view).navigate(R.id.MultiToHomePage)
                }
                builder.setCancelable(false)
                builder.create()
                builder.show()
                delay = 0L
            }

        }
    }
    /** fun to repeat something every -delay- 1000L ==> 1 sec */
    object RepeatHelper {
        @Suppress("DEPRECATION")
        fun repeatDelayed(todo: () -> Unit) {
            val handler = Handler()
            handler.postDelayed(object : Runnable {
                override fun run() {
                    todo()
                    if (delay != 0L) {
                        handler.postDelayed(this, delay)
                    }
                }
            }, delay)
        }
    }


    /** getting a value from the realtime data base  */
    private fun gettingTry() {
        if (player == "1") {
            dbRef.child(choosedGameCode!!).addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    friendsTry = snapshot.child("tryPl2").value.toString()
                }

                override fun onCancelled(error: DatabaseError) {
                }

            })
        } else if (player == "2") {
            dbRef.child(choosedGameCode!!).addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    friendsTry = snapshot.child("tryPl1").value.toString()
                }

                override fun onCancelled(error: DatabaseError) {
                }

            })
        }
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
