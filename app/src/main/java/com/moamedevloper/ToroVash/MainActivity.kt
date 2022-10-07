package com.moamedevloper.ToroVash

import android.annotation.SuppressLint
import android.net.wifi.p2p.nsd.WifiP2pUpnpServiceRequest.newInstance
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings.Global.putString
import android.provider.Settings.Secure.putString
import android.provider.Settings.System.putString
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.navigation.findNavController
import java.security.KeyStore.Builder.newInstance
import javax.xml.datatype.DatatypeFactory.newInstance
import javax.xml.validation.SchemaFactory.newInstance
import javax.xml.xpath.XPathFactory.newInstance

class MainActivity : AppCompatActivity() {
    @SuppressLint("ResourceType")
    lateinit var testTv:TextView
    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        testTv = findViewById(R.id.testTv)
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