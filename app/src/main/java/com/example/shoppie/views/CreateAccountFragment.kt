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
import com.example.shoppie.data.models.User
import com.example.shoppie.databinding.FragmentCreateAccountBinding
import com.example.shoppie.utils.RegisterValidation
import com.example.shoppie.utils.Resource
import com.example.shoppie.utils.common.ModalProgressLoadingFragment
import com.example.shoppie.utils.requireMainActivity
import com.example.shoppie.utils.setCornerRadius
import com.example.shoppie.viewmodels.RegisterViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

@AndroidEntryPoint
class CreateAccountFragment: Fragment() {
    private lateinit var binding: FragmentCreateAccountBinding
    private val viewModel by viewModels<RegisterViewModel>()
    private lateinit var modalProgressFragment: ModalProgressLoadingFragment
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
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

    private fun setupViews() = binding.apply{
        setupNavigation()
        btnCreateAccount.setCornerRadius(24f)
        llGoogleSection.setCornerRadius(24f)
        llFacebookSection.setCornerRadius(24f)
    }

    private fun setupNavigation() = binding.apply{
        btnCreateAccount.setOnClickListener {
            val user = User(
                userName = etUsername.text.toString().trim(),
                email = etEmail.text.toString().trim(),
                password = etPassword.text.toString(),
                confirmPassword = etConfirmPassword.text.toString(),
                phoneNumber = etPhone.text.toString().trim()
            )

            viewModel.createAccountWithEmailAndPassword(user)
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.register.collect {
                    when(it) {
                        is Resource.Loading -> {
                            modalProgressFragment.showModalProgress()
                        }

                        is Resource.Error -> {
                            Timber.d(it.message.toString())
                            modalProgressFragment.hideModalProgress()
                        }
                        is Resource.Success -> {
                            modalProgressFragment.hideModalProgress()
                            Timber.d(it.data.toString())
                        }
                        else -> Unit
                    }
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.validation.collect { validation ->
                    if (validation.username is RegisterValidation.Failed) {
                        withContext(Dispatchers.Main) {
                            etUsername.apply {
                                requestFocus()
                                error = validation.username.message
                            }
                        }
                    }
                    if (validation.email is RegisterValidation.Failed) {
                        withContext(Dispatchers.Main) {
                            etEmail.apply {
                                requestFocus()
                                error = validation.email.message
                            }
                        }
                    }
                    if (validation.phoneNumber is RegisterValidation.Failed) {
                        withContext(Dispatchers.Main) {
                            etPhone.apply {
                                requestFocus()
                                error = validation.phoneNumber.message
                            }
                        }
                    }

                    if (validation.password is RegisterValidation.Failed) {
                        withContext(Dispatchers.Main) {
                            etPassword.apply {
                                requestFocus()
                                error = validation.password.message
                            }
                        }
                    }

                    if (validation.confirmPassword is RegisterValidation.Failed) {
                        withContext(Dispatchers.Main) {
                            etConfirmPassword.apply {
                                requestFocus()
                                error = validation.confirmPassword.message
                            }
                        }
                    }

                }
            }
        }
    }
}