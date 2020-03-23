package com.treetrunks.fbexample

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.TimeUnit

@RunWith(AndroidJUnit4::class)
class FirestorePhoneAuthTest {

    val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
            super.onCodeSent(p0, p1)
        }

        override fun onVerificationCompleted(p0: PhoneAuthCredential) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun onVerificationFailed(p0: FirebaseException) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }
    }

    @Test fun testPhoneAuth() {
        val phoneNumber = "82+10-2433-0524"


    }
}