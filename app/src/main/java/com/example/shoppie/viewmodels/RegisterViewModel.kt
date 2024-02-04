package com.example.shoppie.viewmodels

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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
) : ViewModel() {

    private val _register = MutableStateFlow<Resource<FirebaseUser>>(Resource.Unspecified())
    val register: Flow<Resource<FirebaseUser>> = _register

    private val _validation = Channel<RegisterFieldState>()
    val validation = _validation.receiveAsFlow()
    fun createAccountWithEmailAndPassword(user: User) {
        checkValidation(user)
        if (checkValidation(user)) {
            viewModelScope.launch {
                _register.emit(Resource.Loading())
            }
            firebaseAuth.createUserWithEmailAndPassword(user.email, user.password)
                .addOnSuccessListener {
                    it.user?.let { userVal ->
                        _register.value = Resource.Success(userVal)
                    }
                }
                .addOnFailureListener {
                    _register.value = Resource.Error(it.message.toString())
                }
        } else {
            val registerFieldState = RegisterFieldState(
                username = validateUserName(user.userName),
                email = validateEmail(user.email),
                phoneNumber = validatePhoneNumber(user.phoneNumber),
                password = validatePassword(user.password),
                confirmPassword = validateConfirmPassword(user.password, user.confirmPassword)
            )
            viewModelScope.launch {
                _validation.send(registerFieldState)
            }
        }
    }

    private fun checkValidation(user: User): Boolean {
        val usernameValidation = validateUserName(user.userName)
        val emailValidation = validateEmail(user.email)
        val phoneNumberValidation = validatePhoneNumber(user.phoneNumber)
        val passwordValidation = validatePassword(user.password)
        val confirmPasswordValidation = validateConfirmPassword(user.password, user.confirmPassword)
        return (usernameValidation is RegisterValidation.Success &&
                emailValidation is RegisterValidation.Success && phoneNumberValidation is RegisterValidation.Success
                && passwordValidation is RegisterValidation.Success && confirmPasswordValidation is RegisterValidation.Success)
    }

}