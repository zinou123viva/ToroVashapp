package com.moamedevloper.ToroVash

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.google.android.material.textfield.TextInputEditText
import kotlin.random.Random

class SoloModeFragment : Fragment() {
     internal lateinit var view : View
     var selectednumber = ""
     var numberEntered = ""
     var toro: Int = 0
     var vash: Int = 0
     var resultNumber:String=""
     var resultToro:String=""
     var resultVash:String=""
    private lateinit var numberEntredField: TextInputEditText
    private lateinit var recentTryToroField: TextInputEditText
    private lateinit var recentTryVashField: TextInputEditText
    private lateinit var recentTryNumberField: TextInputEditText
    private lateinit var grnBtn: Button
    private lateinit var confBtn: Button
    private var sinew=true
    private var num:String? =null
    private var resultNum:String?=""
    private var resultT:String?=""
    private var resultV:String?=""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_solo_mode, container, false)
        
        initialise()
        
        newgame(sinew)
        
        num= selectednumber


        grnBtn.setOnClickListener {
            newgame(true)
            num= selectednumber
        }
        confBtn.setOnClickListener {
            numberEntered = numberEntredField.text!!.toString()

            if (duplicateCount(numberEntered) > 0 || numberEntered.length!=4) {
                numberEntredField.error = "invalid input"
            } else {
                returnedresult(num!!, resultNum!!, resultT!!,resultV!!)
            }
        }


        return view
    }

    private fun newgame(new: Boolean) {
        if (new) {
            do{
                val randomNumber= Random.nextInt(0,10000)
                selectednumber= randomNumber.toString()
            }while (duplicateCount(selectednumber)!=0 || selectednumber.length!=4)
            resultNumber=""
            resultToro=""
            resultVash=""
            recentTryNumberField.setText(resultNumber)
            recentTryToroField.setText(resultToro)
            recentTryVashField.setText(resultVash)
        }
    }

    private fun initialise() {
        /** initialise in fragment */
        numberEntredField = view.findViewById(R.id.number_enterd)
        recentTryToroField = view.findViewById(R.id.Recent_try_answer_toro)
        recentTryVashField = view.findViewById(R.id.Recent_try_answer_vash)
        recentTryNumberField = view.findViewById(R.id.Recent_try_number)
        confBtn = view.findViewById(R.id.button)
        grnBtn= view.findViewById(R.id.generate)

    }

    private fun returnedresult(selectednumber : String,  resultNumbe :String,  resultTor : String,  resultVas : String) {

        if (!sinew) {
            resultNumber = resultNumbe
            resultToro = resultTor
            resultVash = resultVas
        }
        if (selectednumber.isNotEmpty()) {
            if (numberEntredField.text.toString().isNotEmpty()) {
                numberEntered = numberEntredField.text!!.toString()

                if (selectednumber == numberEntered) {
                    resultVash += " \n\n  "
                    resultToro += " \n 4T "
                    resultNumber += "\n $numberEntered"
                    // clear focus from the edit text fields
                    numberEntredField.clearFocus()

                    /*
                    //to force hide keyboard

                    this.numberEntredField.let { view ->
                        val imm =
                            getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                        imm?.hideSoftInputFromWindow(view.windowToken, 0)
                    }
                    AlertDialog.Builder(this)
                        .setTitle("You Win")
                        .setMessage("Congratulation that was so smart ")
                        .setPositiveButton("Replay") { _, _ ->
                            finish()
                            startActivity(intent)
                        }.show()

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
                sinew=true

                /*
          set line through
            if (toro == 0 && vash == 0) {
                recentTryNumberField.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG))
            }*/

                toro = 0
                vash = 0
                numberEntredField.text!!.clear()
            } else {
                // set an error message in the text fields
                numberEntredField.error = "number required"
            }
        } else {
            numberEntredField.error = "number not generated"
        }
    }
    /** Detect if the field contain duplicated character */
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