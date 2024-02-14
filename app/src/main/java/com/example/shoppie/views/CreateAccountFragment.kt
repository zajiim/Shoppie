package com.example.shoppie.views

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.transition.TransitionManager
import com.example.shoppie.R
import com.example.shoppie.data.models.User
import com.example.shoppie.databinding.FragmentCreateAccountBinding
import com.example.shoppie.utils.RegisterValidation
import com.example.shoppie.utils.Resource
import com.example.shoppie.utils.common.ModalProgressLoadingFragment
import com.example.shoppie.utils.isValidEmail
import com.example.shoppie.utils.isValidPasswordFormat
import com.example.shoppie.utils.requireMainActivity
import com.example.shoppie.utils.setCornerRadius
import com.example.shoppie.utils.show
import com.example.shoppie.utils.showToast
import com.example.shoppie.utils.viewVisibility
import com.example.shoppie.viewmodels.RegisterViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

@AndroidEntryPoint
class CreateAccountFragment : Fragment() {
    private lateinit var binding: FragmentCreateAccountBinding
    private val viewModel by viewModels<RegisterViewModel>()
    private lateinit var modalProgressFragment: ModalProgressLoadingFragment
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentCreateAccountBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        modalProgressFragment = binding.modalProgressContainer.getFragment()
        setupWindowInsets()
        setupViews()
    }

    private fun setupWindowInsets() {
        requireMainActivity().windowsInsetsLiveData.observe(viewLifecycleOwner) { insets ->
            val rect = insets.getInsets(
                WindowInsetsCompat.Type.systemBars() or
                        WindowInsetsCompat.Type.displayCutout()
            )
            binding.safeTopGuideline.setGuidelineBegin(rect.top)
            binding.safeBottomGuideline.setGuidelineEnd(rect.bottom)
        }
    }

    private fun setupViews() = binding.apply {
        setupNavigation()
        validateTextInputs()
        btnCreateAccount.setCornerRadius(24f)
        llGoogleSection.setCornerRadius(24f)
        llFacebookSection.setCornerRadius(24f)
    }

    private fun setupNavigation() = binding.apply {
        btnCreateAccount.setOnClickListener {

//            Log.e("CreateAccount", "validations: ${validateForms()}")
            if (validateForms()) {
                val user = User(
                    userName = etUsername.text.toString().trim(),
                    email = etEmail.text.toString().trim(),
                    phoneNumber = etPhone.text.toString().trim()
                )
                val password = etPassword.text.toString()
                val confirmPassword = etConfirmPassword.text.toString()

                if (user.email.isNotEmpty() && password.isNotEmpty()) {
                    viewModel.createAccountWithEmailAndPassword(user, password)
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Email and password cannot be empty",
                        Toast.LENGTH_SHORT
                    ).show()
                }

//                viewModel.createAccountWithEmailAndPassword(user, password, confirmPassword)
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.register.collect {
                    when (it) {
                        is Resource.Loading -> {
                            modalProgressFragment.showModalProgress()
                        }

                        is Resource.Error -> {
                            Timber.d(it.message.toString())
                            modalProgressFragment.hideModalProgress()
                        }

                        is Resource.Success -> {
                            sendOtp()
                            modalProgressFragment.hideModalProgress()
                            Timber.d(it.data.toString())
                        }

                        else -> Unit
                    }
                }
            }
        }
//
//        lifecycleScope.launch {
//            repeatOnLifecycle(Lifecycle.State.STARTED) {
//                viewModel.validation.collect { validation ->
//                    if (validation.username is RegisterValidation.Failed) {
//                        withContext(Dispatchers.Main) {
//                            tvUsernameErr.apply {
//                                text = validation.username.message
//                            viewVisibility(true)
//                            etUsername.requestFocus()
//                        }
//                    }
//                    if (validation.email is RegisterValidation.Failed) {
//                        withContext(Dispatchers.Main) {
////                            etEmail.apply {
////                                requestFocus()
////                                error = validation.email.message
////                            }
//                            tvEmailErr.apply {
//                                text = validation.email.message
//                                viewVisibility(true)
//                                etEmail.requestFocus()
//                            }
//
//                        }
//                    }
//                    if (validation.phoneNumber is RegisterValidation.Failed) {
//                        withContext(Dispatchers.Main) {
////                            etPhone.apply {
////                                requestFocus()
////                                error = validation.phoneNumber.message
////                            }
//                            tvPhonenumberErr.apply {
//                                text = validation.phoneNumber.message
//                                viewVisibility(true)
//                                etEmail.requestFocus()
//                            }
//                        }
//                    }
//
//                    if (validation.password is RegisterValidation.Failed) {
//                        withContext(Dispatchers.Main) {
////                            etPassword.apply {
////                                requestFocus()
////                                error = validation.password.message
////                            }
//                            tvPassword.apply {
//                                text = validation.password.message
//                                viewVisibility(true)
//                                etEmail.requestFocus()
//                            }
//                        }
//                    }
//
////                    if (validation.confirmPassword is RegisterValidation.Failed) {
////                        withContext(Dispatchers.Main) {
//////                            etConfirmPassword.apply {
//////                                requestFocus()
//////                                error = validation.confirmPassword.message
//////                            }
////                            tvConfirmPasswordErr.text = validation.confirmPassword.message
////                            tvConfirmPasswordErr.show()
////                            etConfirmPassword.requestFocus()
////                        }
//                    }
//
//                }
//            }
//        }
    }

    private fun sendOtp() {
        viewModel.apply {
            sendOtp(binding.etPhone.text.toString(), requireActivity())
            lifecycleScope.launch {
                otpSent.collect { otp ->
                    if (otp) {
                        findNavController().navigate(CreateAccountFragmentDirections.actionCreateAccountFragmentToFragmentVerification())
                    }
                }
            }
        }
    }


    private fun validateTextInputs() = binding.apply {
        etUsername.doOnTextChanged { text, _, _, _ ->
            tvUsernameErr.apply {
                if (isVisible) viewVisibility(text.isNullOrEmpty())
            }
        }
        etPassword.doOnTextChanged { text, _, _, _ ->
            tvPasswordErr.apply { if (isVisible) viewVisibility(text.isNullOrEmpty()) }
        }
        etConfirmPassword.doOnTextChanged { text, _, _, _ ->
            tvConfirmPasswordErr.apply { if (isVisible) viewVisibility(text.isNullOrEmpty()) }
        }
        etEmail.doOnTextChanged { text, _, _, _ ->
            tvEmailErr.apply { if (isVisible) viewVisibility(text.isNullOrEmpty()) }
        }
        etPhone.doOnTextChanged { text, _, _, _ ->
            tvPhonenumberErr.apply { if (isVisible) viewVisibility(text.isNullOrEmpty()) }
        }
    }


    private fun validateForms(): Boolean {
        var isFormValid = true
        binding.apply {
            try {
                val userName = etUsername.text?.toString()?.trim()!!
                val email = etEmail.text?.toString()?.trim()!!
                val password = etPassword.text?.toString()?.trim()!!
                val confirmPassword = etConfirmPassword.text?.toString()?.trim()!!
                val mobile = etPhone.text?.toString()?.trim()!!

                val userNameValid =
                    userName.isNotEmpty()
                val emailValid =
                    email.isNotEmpty() && email.isValidEmail()
                val mobileValid = mobile.isNotEmpty()

                val passwordValid = isValidPasswordFormat(password)
                val confirmPasswordValid = (confirmPassword == password)

                when {
                    !userNameValid && !emailValid && !mobileValid && !passwordValid -> {
                        TransitionManager.beginDelayedTransition(root)
                        tvUsernameErr.apply {
                            viewVisibility(true)
                            text = context.getString(R.string.enter_username)
                        }
                        etUsername.requestFocus()
                        etUsername.requestFocusFromTouch()


                        tvEmailErr.apply {
                            viewVisibility(true)
                            text = context.getString(R.string.enter_email)
                        }

                        tvPhonenumberErr.apply {
                            viewVisibility(true)
                            text = context.getString(R.string.enter_phone_number)
                        }


                        tvPasswordErr.apply {
                            viewVisibility(true)
                            text = context.getString(R.string.enter_your_password)
                        }
                        isFormValid = false
                    }


                    !userNameValid -> {
                        TransitionManager.beginDelayedTransition(root)
                        tvUsernameErr.apply {
                            viewVisibility(true)
                            text = context.getString(R.string.enter_your_username)
                        }
                        isFormValid = false
                        etUsername.requestFocus()
                        etUsername.requestFocusFromTouch()
                    }

                    !emailValid -> {
                        TransitionManager.beginDelayedTransition(root)
                        tvEmailErr.apply {
                            viewVisibility(true)
                            text = context.getString(R.string.enter_valid_email)
                        }
                        isFormValid = false
                        etEmail.requestFocus()
                        etEmail.requestFocusFromTouch()
                    }

                    !mobileValid -> {
                        TransitionManager.beginDelayedTransition(root)
                        tvPhonenumberErr.apply {
                            viewVisibility(true)
                            text = context.getString(R.string.enter_your_mobile)
                        }
                        isFormValid = false
                        etPhone.requestFocus()
                        etPhone.requestFocusFromTouch()
                    }

                    !passwordValid -> {
                        TransitionManager.beginDelayedTransition(root)
                        tvPasswordErr.apply {
                            viewVisibility(true)
                            text = context.getString(R.string.enter_your_password)
                        }
                        isFormValid = false
                        etPassword.requestFocus()
                        etPassword.requestFocusFromTouch()
                    }


                    !confirmPasswordValid -> {
                        TransitionManager.beginDelayedTransition(root)
                        tvConfirmPasswordErr.apply {
                            viewVisibility(true)
                            text = context.getString(R.string.confirm_your_password)
                            isFormValid = false
                            etConfirmPassword.requestFocus()
                            etConfirmPassword.requestFocusFromTouch()
                        }
                    }

                    else -> {
                        tvUsernameErr.viewVisibility(false)
                        tvEmailErr.viewVisibility(false)
                        tvPhonenumberErr.viewVisibility(false)
                        tvPasswordErr.viewVisibility(false)
                        tvConfirmPasswordErr.viewVisibility(false)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                isFormValid = false
            }
        }
        return isFormValid
    }


//    private fun validations(): Boolean {
//        watchers()
//        return if (binding.etEmail.text?.isEmpty() == true && binding.etPassword.text?.isEmpty() == true) {
//            TransitionManager.beginDelayedTransition(binding.root)
//            binding.passwordErrorText.visibility = View.VISIBLE
//            binding.emailErrorText.visibility = View.VISIBLE
//            false
//        } else if (binding.etEmail.text?.isEmpty() == true) {
//            TransitionManager.beginDelayedTransition(binding.root)
//            binding.emailErrorText.visibility = View.VISIBLE
//            false
//        } else if (binding.etPassword.text?.isEmpty() == true) {
//            TransitionManager.beginDelayedTransition(binding.root)
//            binding.passwordErrorText.visibility = View.VISIBLE
//            false
//        } else {
//            binding.passwordErrorText.visibility = View.GONE
//            binding.emailErrorText.visibility = View.GONE
//            true
//        }
//    }
//
//    private fun watchers() = binding.apply {
//        etEmail.addTextChangedListener(object : TextWatcher {
//            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
//            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
//                clEmailValue.apply {
//                    if (p1 == 0) emailErrorText.visibility = View.VISIBLE
//                    else emailErrorText.visibility = View.GONE
//                }
//            }
//
//            override fun afterTextChanged(p0: Editable?) {}
//        })
//        etPassword.addTextChangedListener(object : TextWatcher {
//            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
//            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
//                clPasswordValue.apply {
//                    if (p1 == 0) passwordErrorText.visibility = View.VISIBLE
//                    else passwordErrorText.visibility = View.GONE
//                }
//            }
//
//            override fun afterTextChanged(p0: Editable?) {}
//        })
//    }
}