package com.example.shoppie.views

import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.transition.TransitionManager
import com.example.shoppie.R
import com.example.shoppie.data.models.User
import com.example.shoppie.databinding.FragmentCreateAccountBinding
import com.example.shoppie.utils.Resource
import com.example.shoppie.utils.common.ModalProgressLoadingFragment
import com.example.shoppie.utils.isValidPasswordFormat
import com.example.shoppie.utils.requireMainActivity
import com.example.shoppie.utils.setCornerRadius
import com.example.shoppie.utils.showToast
import com.example.shoppie.utils.viewVisibility
import com.example.shoppie.viewmodels.RegisterViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
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
            if (validateForms()) {
                val userName = etUsername.text.toString().trim()
                val emailOrPhone = etEmailOrPhone.text.toString().trim()
                val password = etPassword.text.toString().trim()
                val confirmPassword = etConfirmPassword.text.toString().trim()

                if (emailOrPhone.contains('@')) {
                    if (emailOrPhone.isNotEmpty() && password.isNotEmpty()) {
                        val user = User(userName = userName, email = emailOrPhone, phoneNumber = "")
                        viewModel.createAccountWithEmailAndPassword(user, password)
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Email and password cannot be empty",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    viewModel.sendOtp(emailOrPhone, requireActivity())
                }
                modalProgressFragment.showModalProgress()
            } else {
                "Verification failed".showToast(this@CreateAccountFragment)
            }
        }

        lifecycleScope.launch {
            viewModel.register.collect { resource ->
                when (resource) {
                    is Resource.Loading -> {
//                        modalProgressFragment.showModalProgress()
                    }

                    is Resource.Error -> {
                        Timber.d(resource.message.toString())
                        resource.message.toString().showToast(this@CreateAccountFragment)
                        modalProgressFragment.hideModalProgress()
                    }

                    is Resource.Success -> {
                        modalProgressFragment.hideModalProgress()
                        Timber.d(resource.data.toString())
                    }

                    else -> Unit
                }
            }
        }

        lifecycleScope.launch {
            viewModel.otpSent.collect { otp ->
                if (otp) {
                    val verificationId = viewModel.verificationId.value
                    modalProgressFragment.hideModalProgress()
                    findNavController().navigate(
                        CreateAccountFragmentDirections.actionCreateAccountFragmentToFragmentVerification(
                            verificationId.toString()
                        )
                    )
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
        etEmailOrPhone.doOnTextChanged { text, _, _, _ ->
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
                val emailOrPhone = etEmailOrPhone.text?.toString()?.trim()!!
                val password = etPassword.text?.toString()?.trim()!!
                val confirmPassword = etConfirmPassword.text?.toString()?.trim()!!

                val userNameValid = userName.isNotEmpty()
                val emailOrPhoneValid =
                    emailOrPhone.isNotEmpty() && isValidEmailOrPhone(emailOrPhone)
                val passwordValid = isValidPasswordFormat(password)
                val confirmPasswordValid = (confirmPassword == password)

                when {
                    !userNameValid && !emailOrPhoneValid && !passwordValid -> {
                        TransitionManager.beginDelayedTransition(root)
                        tvUsernameErr.apply {
                            viewVisibility(true)
                            text = context.getString(R.string.enter_username)
                        }
                        etUsername.requestFocus()
                        etUsername.requestFocusFromTouch()

                        tvEmailErr.apply {
                            viewVisibility(true)
                            text = context.getString(R.string.enter_your_email)
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

                    !emailOrPhoneValid -> {
                        TransitionManager.beginDelayedTransition(root)
                        tvEmailErr.apply {
                            viewVisibility(true)
                            text = if (emailOrPhone.contains('@')) {
                                context.getString(R.string.enter_valid_email)
                            } else {
                                context.getString(R.string.enter_valid_phone)
                            }
                        }
                        isFormValid = false
                        etEmailOrPhone.requestFocus()
                        etEmailOrPhone.requestFocusFromTouch()
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
                        }
                        isFormValid = false
                        etConfirmPassword.requestFocus()
                        etConfirmPassword.requestFocusFromTouch()
                    }

                    else -> {
                        tvUsernameErr.viewVisibility(false)
                        tvEmailErr.viewVisibility(false)
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

    private fun isValidEmailOrPhone(input: String): Boolean {
        return if (input.contains('@')) {
            Patterns.EMAIL_ADDRESS.matcher(input).matches()
        } else {
            input.length == 10 && input.matches(Regex("\\d+"))
        }
    }
}