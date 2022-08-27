package com.moamedevloper.ToroVash

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.navigation.findNavController

class MainActivity : AppCompatActivity() {
    @SuppressLint("ResourceType")
    lateinit var mulBtn:Button
    lateinit var testTv:TextView
    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // hideSystemBars()
        testTv = findViewById(R.id.testTv)
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
        }
    }
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