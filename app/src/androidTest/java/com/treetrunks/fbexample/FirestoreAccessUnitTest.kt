package com.treetrunks.fbexample

import android.app.Activity
import android.content.Context
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.time.LocalDate
import java.time.LocalDateTime

@RunWith(AndroidJUnit4::class)
class FirestoreAccessUnitTest {
    private lateinit var context: Context

    private lateinit var activity: Activity

    @Before fun setup() {
        FirebaseAuth.getInstance().signOut()

    }

    @Test
    fun testAddNewDocument() {
        val methodName = Object().`class`.enclosingMethod

        val mAuth = FirebaseAuth.getInstance()

        mAuth.signInAnonymously()
            .addOnFailureListener { exception ->
                Assert.fail("$methodName failed. caused by: ${exception.message}")
            }
            .addOnSuccessListener {
                val document = "document_new_${LocalDate.now()}"
                val token_value = LocalDateTime.now().toString()

                setDocument(document, token_value)
            }
    }

    @Test
    fun testUpdateDocument() {
        val methodName = Object().`class`.enclosingMethod

        val mAuth = FirebaseAuth.getInstance()

        mAuth.signInAnonymously()
            .addOnFailureListener { exception ->
                Assert.fail("$methodName failed. caused by: ${exception.message}")
            }
            .addOnSuccessListener {
                val document = "document_update"
                val token_value = LocalDateTime.now().toString()

                setDocument(document, token_value)
            }
    }

    private fun setDocument(document: String, token_value: String) {
        val methodName = Object().`class`.enclosingMethod

        val collectionName = "token_list"
        val data = hashMapOf("token" to token_value)

        val firestore = FirebaseFirestore.getInstance()

        firestore.collection(collectionName)
            .document(document)
            .set(data)
            .addOnCompleteListener {
                it.apply {
                    Assert.assertTrue(isComplete)

                    if (!isSuccessful) {
                        Assert.fail("$methodName failed. caused by: ${exception?.message}")
                    }
                }
            }
    }
}