package com.example.shoppie.viewmodels

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoppie.data.models.User
import com.example.shoppie.utils.Constants
import com.example.shoppie.utils.Resource
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val db: FirebaseFirestore
) : ViewModel() {

    private val _register = MutableStateFlow<Resource<User>>(Resource.Unspecified())
    val register: Flow<Resource<User>> = _register

//    private val _validation = Channel<RegisterFieldState>()
//    val validation = _validation.receiveAsFlow()
    private val  _verificationId = MutableStateFlow<String?>(null)
    val verificationId: StateFlow<String?> = _verificationId
    private val _otpSent = MutableStateFlow(false)
    val otpSent: StateFlow<Boolean> = _otpSent

    private val _otpVerified = MutableStateFlow(false)
    val otpVerified: StateFlow<Boolean> = _otpVerified

    fun createAccountWithEmailAndPassword(user: User, password: String) {
            viewModelScope.launch {
                _register.emit(Resource.Loading())
            }
        user.email?.let {
            firebaseAuth.createUserWithEmailAndPassword(it, password)
                .addOnSuccessListener {
                    it.user?.let { userVal ->
                        saveUserInfo(userVal.uid, user)
    //                        _register.value = Resource.Success(userVal)
                    }
                }
                .addOnFailureListener {
                    _register.value = Resource.Error(it.message.toString())
                }
        }
        }

    private fun saveUserInfo(userUid: String, user: User) {
        db.collection(Constants.USER_COLLECTION).document(userUid)
            .set(user)
            .addOnSuccessListener {
                _register.value = Resource.Success(user)
            }
            .addOnFailureListener {
                _register.value = Resource.Error(it.message.toString())
            }
    }


    fun sendOtp(userNumber: String, activity: Activity) {
        val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {}

            override fun onVerificationFailed(e: FirebaseException) {
                _otpSent.value = false
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken,
            ) {
                _verificationId.value = verificationId
                _otpSent.value = true
            }
        }

        val options = PhoneAuthOptions.newBuilder(firebaseAuth)
            .setPhoneNumber("+91$userNumber")
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(activity)
            .setCallbacks(callbacks)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }


//    fun signInWithOtpAndSaveUserInfo(verificationId: String, otp: String, user: User) {
//        val credential = PhoneAuthProvider.getCredential(verificationId, otp)
//        firebaseAuth.signInWithCredential(credential)
//            .addOnCompleteListener { task ->
//                if (task.isSuccessful) {
//                    val firebaseUser = task.result?.user
//                    firebaseUser?.let {
//                        saveUserInfoAfterOTPVerification(it.uid, user)
//                    }
//                } else {
//                    _otpVerified.value = false
//                }
//            }
//    }
//
//    private fun saveUserInfoAfterOTPVerification(uid: String, user: User) {
//        db.collection(Constants.USER_COLLECTION).document(uid)
//            .set(user)
//            .addOnSuccessListener {
//                _register.value = Resource.Success(user)
//            }
//            .addOnFailureListener {
//                _register.value = Resource.Error(it.message.toString())
//            }
//    }



    fun signInWithOtp(verificationId: String, otp: String, user: User) {
        val credential = PhoneAuthProvider.getCredential(verificationId, otp)
        signInWithPhoneAuthCredential(credential, user)
    }
    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential, user: User) {
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                _otpVerified.value = task.isSuccessful
                saveUserInfo(task.result.user!!.uid, user)
            }
    }


}