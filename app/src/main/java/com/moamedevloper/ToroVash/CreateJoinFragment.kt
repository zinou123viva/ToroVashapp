package com.moamedevloper.ToroVash

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
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



        return view
    }
    private fun creatMethod() {
        choosedGameCode = codeTv.text!!.toString()
        choosedNum = numTv.text!!.toString()
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
        choosedNum = numTv.text!!.toString()
        choosedGameCode = codeTv.text!!.toString()
        dbRef.child(choosedGameCode).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    if (duplicateCount(choosedNum) > 0 || choosedNum.length != 4) {
                        numTv.error = "invalid number"
                    } else {
                        dbRef.child(choosedGameCode).child("numberPl2").setValue(choosedNum)
                        /*
                        val myintent = Intent(this@CreateJoinPage, MultiPlayerMode::class.java)
                        myintent.putExtra("choosedGameCode", choosedGameCode)
                        myintent.putExtra("playerId", "2")
                        startActivity(myintent)
                        finish()

                         */
                        Navigation.findNavController(view).navigate(R.id.CreateToMul,Bundle().apply {
                            putString("chosenGameCode",choosedGameCode)
                            putString("playerId","2")
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

    private fun inisialize() {
        createBtn = view.findViewById(R.id.CreateBtn)
        joinBtn = view.findViewById(R.id.JoinBtn)
        codeTv = view.findViewById(R.id.game_code)
        numTv = view.findViewById(R.id.number_for_opponent)
        dbRef = FirebaseDatabase.getInstance().getReference("gameCodes")
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