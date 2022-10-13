package com.moamedevloper.ToroVash

import android.app.Activity
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.Navigation
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.database.*


class CreateJoinFragment : Fragment() {
    internal lateinit var view:View
     lateinit var createBtn: Button
     lateinit var joinBtn: Button
     lateinit var codeTv: TextInputEditText
     lateinit var numTv: TextInputEditText
     var choosedNum = ""
    lateinit var constraintLayout  : ConstraintLayout
    var choosedGameCode = ""
    private lateinit var dbRef: DatabaseReference
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_create_join, container, false)
        inisialize()


        createBtn.setOnClickListener {
            creatMethod()
        }
        joinBtn.setOnClickListener {
            joinMethod()
        }

        constraintLayout.setOnClickListener {
            // hide soft keyboard on rot layout click
            // it hide soft keyboard on edit text outside root layout click
            requireActivity().hideSoftKeyboard()

            // remove focus from edit text
            codeTv.clearFocus()
            numTv.clearFocus()
        }


        return view
    }
    private fun creatMethod() {
        choosedGameCode = codeTv.text!!.toString().trim()
        choosedNum = numTv.text!!.toString().trim()
        dbRef.child(choosedGameCode).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (!snapshot.exists()) {
                    if (duplicateCount(choosedNum) > 0 || choosedNum.length != 4) {
                        numTv.error = "invalid number"
                    } else {
                        dbRef.child(choosedGameCode).child("numberPl1").setValue(choosedNum)
                        Navigation.findNavController(view).navigate(R.id.CreateToMul,Bundle().apply {
                            putString("chosenGameCode",choosedGameCode)
                            putString("playerId","1")
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
                    codeTv.error="The chosen code already exist"
                }
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }


        })

    }

    private fun joinMethod() {
        choosedNum = numTv.text!!.toString().trim()
        choosedGameCode = codeTv.text!!.toString().trim()
        dbRef.child(choosedGameCode).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    if (duplicateCount(choosedNum) > 0 || choosedNum.length != 4) {
                        numTv.error = "invalid number"
                    } else {
                        dbRef.child(choosedGameCode).child("numberPl2")
                            .addListenerForSingleValueEvent(object : ValueEventListener {
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    if (!snapshot.exists()) {
                                        dbRef.child(choosedGameCode).child("numberPl2")
                                            .setValue(choosedNum)

                                        /*
                                        val myintent = Intent(this@CreateJoinPage, MultiPlayerMode::class.java)
                                        myintent.putExtra("choosedGameCode", choosedGameCode)
                                        myintent.putExtra("playerId", "2")
                                        startActivity(myintent)
                                        finish()

                                         */

                                        Navigation.findNavController(view)
                                            .navigate(R.id.CreateToMul, Bundle().apply {
                                                putString("chosenGameCode", choosedGameCode)
                                                putString("playerId", "2")
                                            })
                                    } else {
                                        codeTv.error = "You cant join this game"
                                    }

                                }

                                override fun onCancelled(error: DatabaseError) {
                                    TODO("Not yet implemented")
                                }

                            })
                    }
                } else {
                    codeTv.error = "invalid game code"
                }

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    fun Activity.hideSoftKeyboard(){
        (getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).apply {
            hideSoftInputFromWindow(currentFocus?.windowToken, 0)
        }
    }

    private fun inisialize() {
        createBtn = view.findViewById(R.id.CreateBtn)
        joinBtn = view.findViewById(R.id.JoinBtn)
        codeTv = view.findViewById(R.id.game_code)
        numTv = view.findViewById(R.id.number_for_opponent)
        dbRef = FirebaseDatabase.getInstance().getReference("gameCodes")
        constraintLayout = view.findViewById(R.id.CreatJoinCon)
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