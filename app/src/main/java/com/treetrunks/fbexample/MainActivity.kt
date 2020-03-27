package com.treetrunks.fbexample

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button

class MainActivity : AppCompatActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val fcmButton = findViewById<Button>(R.id.fcm_btn)
        fcmButton.setOnClickListener(this)

        val authButton = findViewById<Button>(R.id.firestore_auth_btn)
        authButton.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        var intent: Intent? = null
        when(v?.id) {
            R.id.fcm_btn -> {
                intent = Intent(this, CloudMessagingActivity::class.java)
            }
            R.id.firestore_auth_btn -> {
                intent = Intent(this, FirebaseAuthTestActivity::class.java)
            }
        }
        startActivity(intent)
    }
}
