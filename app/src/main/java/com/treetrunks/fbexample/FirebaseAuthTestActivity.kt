package com.treetrunks.fbexample

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.time.LocalDateTime
import java.time.ZoneId

class FirebaseAuthTestActivity : AppCompatActivity(), View.OnClickListener {
    var instance_id:String = DEFAULT_ANONYMOUS_ID
    lateinit var doc_name_edit:EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_firebase_auth_test)

        findViewById<Button>(R.id.sign_in_btn).setOnClickListener(this)
        findViewById<Button>(R.id.write_doc_btn).setOnClickListener(this)

        doc_name_edit = findViewById(R.id.doc_name_edit)
    }

    override fun onStart() {
        super.onStart()

        if (isLoggedin()) {
            // caching user info
            instance_id = FirebaseAuth.getInstance().currentUser?.uid ?: DEFAULT_ANONYMOUS_ID
        }
    }

    private fun signInAnonymously() {
        val mAuth = FirebaseAuth.getInstance()

        mAuth.signInAnonymously()
            .addOnCompleteListener { Log.i(LOG_TAG, "sign in completed!") }
            .addOnSuccessListener {
                instance_id = mAuth.currentUser?.uid ?: DEFAULT_ANONYMOUS_ID
                Log.d(LOG_TAG, "Sign in Successed! instance id is $instance_id")

                Toast.makeText(this, "Sign in Successed!", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Sign in Failed..", Toast.LENGTH_SHORT).show()
                Log.e(LOG_TAG, "fail cause.. ${exception.message}")
            }
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.sign_in_btn -> signInAnonymously()
            R.id.write_doc_btn -> {
                if (doc_name_edit.text.isNullOrBlank()) {
                    Toast.makeText(this, "Document 이름을 넣어주세요", Toast.LENGTH_SHORT).show()
                    doc_name_edit.requestFocus()
                    return
                }

                if (!isLoggedin()) {
                    Toast.makeText(this, "로그인 먼저하세요", Toast.LENGTH_SHORT).show()
                    return
                }

                val docName = doc_name_edit.text.toString()
                val tokenValue = "token__${LocalDateTime.now(ZoneId.of("Asia/Seoul"))}"

                val data = hashMapOf("token" to tokenValue)
                writeDocInFireStore(docName, data)
                doc_name_edit.text.clear()
            }
        }
    }

    private fun isLoggedin(): Boolean {
        val mAuth = FirebaseAuth.getInstance()
        return mAuth.currentUser?.uid != null
    }

    private fun writeDocInFireStore(docName: String, data: HashMap<String, String>) {
        val firestore = FirebaseFirestore.getInstance()
        val collectionPath = "token_list"

        firestore.collection(collectionPath)
            .document(docName)
            .set(data)
            .addOnCompleteListener { Log.i(LOG_TAG, "writing completed!") }
            .addOnSuccessListener {
                Log.i(LOG_TAG, "Writing success!!")
                Toast.makeText(this, "Writing success!!", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "writging Failed..", Toast.LENGTH_SHORT).show()
                Log.i(LOG_TAG, "fail caused.. ${exception.message}")
            }
    }

    companion object {
        const val DEFAULT_ANONYMOUS_ID = "default_id"
        const val LOG_TAG = "FIREBASE_TEST"
    }
}
