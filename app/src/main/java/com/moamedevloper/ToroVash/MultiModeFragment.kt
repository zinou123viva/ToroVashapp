package com.moamedevloper.ToroVash

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.Chronometer
import android.widget.TextView
import android.widget.Toolbar
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.database.*
import java.util.*
import kotlin.collections.ArrayList


class MultiModeFragment : Fragment() {
    internal lateinit var view: View
    private lateinit var numberEntredField: TextInputEditText
    private lateinit var recentTryToroField: TextInputEditText
    private lateinit var recentTryVashField: TextInputEditText
    private lateinit var recentTryNumberField: TextInputEditText
    private lateinit var btnView: Button
    private lateinit var toolbar: Toolbar
    private var sinew = true
    private var resultNum: String? = ""
    private var resultT: String? = ""
    private var resultV: String? = ""
    var selectednumber = ""
    var numberEntered = ""
    var toro: Int = 0
    var vash: Int = 0
    var resultNumber: String = ""
    var resultToro: String = ""
    var resultVash: String = ""
    private lateinit var dbRef: DatabaseReference
    lateinit var player:String
    lateinit var choosedGameCode:String
    lateinit var timer:Chronometer
    lateinit var testTv : TextView
    var numberOfTry=1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_multi_mode, container, false)
        inisialise()
        choosedGameCode = requireArguments().getString("chosenGameCode").toString()
        player = requireArguments().getString("playerId").toString()

        gettingTheNumber(player, choosedGameCode)

        btnView.setOnClickListener {

            confirmbtn()
        }


        return view
    }
    private fun confirmbtn() {
        timer.start()
        numberEntered = numberEntredField.text!!.toString()
        if (duplicateCount(numberEntered) > 0 || numberEntered.length != 4) {
            numberEntredField.error = "invalid input"
        } else {
            returnedresult(selectednumber, resultNum!!, resultT!!, resultV!!)
        }
    }

    private fun gettingTheNumber(player: String, choosedGameCode: String) {
        if (player == "1") {
            dbRef.child(choosedGameCode).addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    selectednumber = snapshot.child("numberPl2").value.toString()
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
        } else if (player == "2") {
            dbRef.child(choosedGameCode).addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    selectednumber = snapshot.child("numberPl1").value.toString()
                }

                override fun onCancelled(error: DatabaseError) {
                }

            })
        } else {
            numberEntredField.error = "Retry with an other Code"
        }
    }

    private fun inisialise() {

        numberEntredField = view.findViewById(R.id.number_enterd)
        recentTryToroField = view.findViewById(R.id.Recent_try_answer_toro)
        recentTryVashField = view.findViewById(R.id.Recent_try_answer_vash)
        recentTryNumberField = view.findViewById(R.id.Recent_try_number)
        btnView = view.findViewById(R.id.button)
        dbRef = FirebaseDatabase.getInstance().getReference("gameCodes")
        timer= view.findViewById(R.id.simpleChronometer)
        testTv = view.findViewById(R.id.testTvMul)

    }


    private fun returnedresult(
        selectednumber: String,
        resultNumbe: String,
        resultTor: String,
        resultVas: String
    ) {

        if (!sinew) {
            resultNumber = resultNumbe
            resultToro = resultTor
            resultVash = resultVas
        }
        if (selectednumber!="null") {
            if (numberEntredField.text.toString().isNotEmpty()) {
                numberEntered = numberEntredField.text!!.toString()

                if (selectednumber == numberEntered) {
                    resultVash += " \n\n  "
                    resultToro += " \n 4T "
                    resultNumber += "\n $numberEntered "
                    numberEntredField.clearFocus()
                    if (player == "1") {
                        dbRef.child(choosedGameCode).child("TryPlayer1").setValue(numberOfTry)
                    }else {
                        dbRef.child(choosedGameCode).child("TryPlayer2").setValue(numberOfTry)
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
                        if (selectednumber[count1] == numberEntered[count1]) toro += 1
                        for (count2 in 0..3) {
                            if (selectednumber[count1] == numberEntered[count2] && count1 != count2) vash += 1
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