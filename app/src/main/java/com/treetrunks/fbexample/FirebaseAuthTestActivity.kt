package com.treetrunks.fbexample

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.time.LocalDateTime

class FirebaseAuthTestActivity : AppCompatActivity() {
    val LOG_TAG = "FIREBASE_TEST"
    lateinit var textView:TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_firebase_auth_test)

        textView = findViewById(R.id.logview)
    }

    override fun onStart() {
        super.onStart()

        val mAuth = FirebaseAuth.getInstance()

        mAuth.signInAnonymously()
            .addOnCompleteListener { Log.i(LOG_TAG, "sign in completed!") }
            .addOnSuccessListener {
                val newDoc = hashMapOf("token" to "new data_${LocalDateTime.now()}")
                addNewDocInFirestore(newDoc)

                val updateDoc = hashMapOf("token" to "changed data_${LocalDateTime.now()}")
                updateDocInFirestore("document01", updateDoc)
            }
            .addOnFailureListener { exception ->
                Log.e(LOG_TAG, "sign in failed.. ${exception.message}")
            }
    }

    private fun addNewDocInFirestore(doc: HashMap<String,String>) {
        val firestore = FirebaseFirestore.getInstance()

        firestore.collection("token_list")
            .add(doc)
            .addOnCompleteListener { Log.i(LOG_TAG, "completed!") }
            .addOnSuccessListener { showOnTextView("success!!")}
            .addOnFailureListener { exception ->
                Log.i(LOG_TAG, "failed.. caused by ${exception.message}")
            }
    }

    private fun updateDocInFirestore(docName: String, value: HashMap<String,String>) {
        val firestore = FirebaseFirestore.getInstance()

        firestore.collection("token_list")
            .document(docName)
            .set(value)
            .addOnCompleteListener { Log.i(LOG_TAG, "completed!") }
            .addOnSuccessListener { showOnTextView("success!!")}
            .addOnFailureListener { exception ->
                Log.i(LOG_TAG, "failed.. caused by ${exception.message}")
            }
    }

    private fun showOnTextView(log: String) {
        textView.text = log
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }
}
