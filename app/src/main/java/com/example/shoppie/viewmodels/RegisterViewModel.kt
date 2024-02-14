package com.example.shoppie.viewmodels

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoppie.data.models.User
import com.example.shoppie.utils.RegisterFieldState
import com.example.shoppie.utils.RegisterValidation
import com.example.shoppie.utils.Resource
import com.example.shoppie.utils.validateConfirmPassword
import com.example.shoppie.utils.validateEmail
import com.example.shoppie.utils.validatePassword
import com.example.shoppie.utils.validatePhoneNumber
import com.example.shoppie.utils.validateUserName
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val db: FirebaseFirestore
) : ViewModel() {

    private val _register = MutableStateFlow<Resource<FirebaseUser>>(Resource.Unspecified())
    val register: Flow<Resource<FirebaseUser>> = _register

    private val _validation = Channel<RegisterFieldState>()
    val validation = _validation.receiveAsFlow()
    private val _verificationId = MutableStateFlow<String?>(null)
    private val _otpSent = MutableStateFlow(false)
    val otpSent: StateFlow<Boolean> = _otpSent

    fun createAccountWithEmailAndPassword(user: User, password: String) {
//        checkValidation(user, password)
//        if (checkValidation(user, password)) {
            viewModelScope.launch {
                _register.emit(Resource.Loading())
            }
            firebaseAuth.createUserWithEmailAndPassword(user.email, password)
                .addOnSuccessListener {
                    it.user?.let { userVal ->
                        _register.value = Resource.Success(userVal)
                    }
                }
                .addOnFailureListener {
                    _register.value = Resource.Error(it.message.toString())
                }
        }
//    else {
//            val registerFieldState = RegisterFieldState(
//                username = validateUserName(user.userName),
//                email = validateEmail(user.email),
//                phoneNumber = validatePhoneNumber(user.phoneNumber),
//                password = validatePassword(password),
////                confirmPassword = validateConfirmPasswordassword(user.password, user.confirmPassword)
//            )
//            viewModelScope.launch {
//                _validation.send(registerFieldState)
//            }
//        }
//    }

//    private fun checkValidation(user: User, password: String): Boolean {
//        val usernameValidation = validateUserName(user.userName)
//        val emailValidation = validateEmail(user.email)
//        val phoneNumberValidation = validatePhoneNumber(user.phoneNumber)
//        val passwordValidation = validatePassword(password)
////        val confirmPasswordValidation = validateConfirmPassword(user.password, user.confirmPassword)
//        return (usernameValidation is RegisterValidation.Success &&
//                emailValidation is RegisterValidation.Success && phoneNumberValidation is RegisterValidation.Success
//                && passwordValidation is RegisterValidation.Success
//                //                && confirmPasswordValidation is RegisterValidation.Success
//                )
//    }


    fun sendOtp(userNumber: String, activity: Activity) {
        val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {

            }

            override fun onVerificationFailed(e: FirebaseException) {


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

}