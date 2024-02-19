package com.example.shoppie.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.transition.TransitionManager
import com.example.shoppie.R
import com.example.shoppie.databinding.FragmentLoginBinding
import com.example.shoppie.utils.Resource
import com.example.shoppie.utils.common.ModalProgressLoadingFragment
import com.example.shoppie.utils.isValidPasswordFormat
import com.example.shoppie.utils.requireMainActivity
import com.example.shoppie.utils.setCornerRadius
import com.example.shoppie.utils.showToast
import com.example.shoppie.utils.viewVisibility
import com.example.shoppie.viewmodels.LoginViewModel
import com.example.shoppie.views.bottomsheets.ForgotPasswordBottomSheet
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment: Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private val forgotPasswordBottomSheet = ForgotPasswordBottomSheet()
    private val viewModel by viewModels<LoginViewModel>()
    private lateinit var modalProgressFragment: ModalProgressLoadingFragment

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        modalProgressFragment = binding.modalProgressContainer.getFragment()
        setupViews()
        setupWindowInsets()
        setupNavigation()
    }

    private fun setupNavigation() = binding.apply{
        tvForgotPass.setOnClickListener {
            forgotPasswordBottomSheet.show(childFragmentManager, "Forgot password")
        }

        btnLogin.setOnClickListener {
            if(checkValidations()) {
                val email = etEmail.text.toString().trim()
                val password = etPassword.text.toString()
                viewModel.login(email, password)
            } else {
                "Validation failed".showToast(this@LoginFragment)
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.login.collect {
                    when(it) {
                        is Resource.Loading -> {
                            modalProgressFragment.showModalProgress()
                        }
                        is Resource.Error -> {
                            modalProgressFragment.hideModalProgress()
                            "Some error occurred".showToast(this@LoginFragment)
                        }
                        is Resource.Success -> {
                            modalProgressFragment.hideModalProgress()
                            findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToMainFragment())
                        }
                        else -> Unit
                    }
                }
            }
        }
    }

    private fun checkValidations(): Boolean {
        var isFormValid = true
        binding.apply {
            try {
                val email = etEmail.text?.toString()?.trim()!!
                val password = etPassword.text?.toString()?.trim()!!


                val emailValid = email.isNotEmpty()
                val passwordValid = isValidPasswordFormat(password)

                when {
                    !emailValid && !passwordValid -> {
                        TransitionManager.beginDelayedTransition(root)
                        tvEmailErr.apply {
                            viewVisibility(true)
                            text = context.getString(R.string.enter_your_email)
                        }
                        etEmail.requestFocus()
                        etEmail.requestFocusFromTouch()

                        tvPasswordErr.apply {
                            viewVisibility(true)
                            text = context.getString(R.string.enter_your_password)
                        }
                        isFormValid = false
                    }

                    !emailValid -> {
                        TransitionManager.beginDelayedTransition(root)
                        tvEmailErr.apply {
                            viewVisibility(true)
                            text = if (email.contains('@')) {
                                context.getString(R.string.enter_valid_email)
                            } else {
                                context.getString(R.string.enter_valid_phone)
                            }
                        }
                        isFormValid = false
                        etEmail.requestFocus()
                        etEmail.requestFocusFromTouch()
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

                    else -> {
                        tvEmailErr.viewVisibility(false)
                        tvPasswordErr.viewVisibility(false)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                isFormValid = false
            }
        }
    return isFormValid
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

    private fun setupViews() = binding.apply{
        btnLogin.setCornerRadius(24f)
        llGoogleSection.setCornerRadius(24f)
        llFacebookSection.setCornerRadius(24f)
    }
}