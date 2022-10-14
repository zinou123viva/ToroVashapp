package com.moamedevloper.ToroVash

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class MainActivity : AppCompatActivity() {
    @SuppressLint("ResourceType")
    lateinit var testTv:TextView
    private lateinit var dbRef: DatabaseReference
    var gameCode = ""
    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        dbRef = FirebaseDatabase.getInstance().getReference("gameCodes")
        testTv = findViewById(R.id.testTv)
        hideSystemBars()

       /* val homeFrament : Fragment =HomePageFragment()
        val bundle = Bundle()
        bundle.putBoolean("isConnected",true)
        homeFrament.arguments = bundle

        val networkConnection= NetworkConnection(applicationContext)
        networkConnection.observe(this) { isConnected ->
            if (isConnected) {

            } else {

                AlertDialog.Builder(this)
                    .setView(R.layout.connection_check)
                    .setCancelable(false)
                    .setPositiveButton("Online"){_,_->

                    }
                    .setNeutralButton("Offline"){_,_->
                    }
                    .show()
            }
        }*/

    }
    override fun onDestroy() {
        /** do something before activity destroyed */
        dbRef.child(gameCode).removeValue()
        super.onDestroy()
    }

    @Suppress("DEPRECATION")
    /** Hide the System Bars (Back Button, Home Button ...) */
    private fun hideSystemBars() {
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        window.decorView.apply {
            // Hide both the navigation bar and the status bar.
            // SYSTEM_UI_FLAG_FULLSCREEN is only available on Android 4.1 and higher, but as
            // a general rule, you should design your app to hide the status bar whenever you
            // hide the navigation bar.
            systemUiVisibility =
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_FULLSCREEN
        }
    }
}