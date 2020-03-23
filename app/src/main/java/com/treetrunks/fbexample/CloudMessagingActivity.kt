package com.treetrunks.fbexample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessaging

class CloudMessagingActivity : AppCompatActivity(), View.OnClickListener {
    private val LOG_TAG = javaClass.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cloud_messaging)

        val tokenBtn = findViewById<Button>(R.id.token_btn)
        tokenBtn.setOnClickListener(this)

        val subscribeBtn = findViewById<Button>(R.id.subscribe_btn)
        subscribeBtn.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.token_btn ->{
                FirebaseInstanceId.getInstance().instanceId
                    .addOnCompleteListener { task->
                        if (!task.isSuccessful) {
                            Log.d(LOG_TAG, "get instanceid failed.. ${task.exception}")
                        } else {
                            val token = task.result?.token

                            val msg = "InstanceID token $token"
                            Log.d(LOG_TAG, msg)
                            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
                        }
                    }
            }
            R.id.subscribe_btn -> {
                FirebaseMessaging.getInstance().subscribeToTopic("strack")
                    .addOnCompleteListener { task ->
                        if (!task.isSuccessful) {
                            Log.d(LOG_TAG, "subscribe to topic failed.. ${task.exception}")
                        } else {
                            val msg = "subscribe to topic success!!"
                            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }
    }
}
