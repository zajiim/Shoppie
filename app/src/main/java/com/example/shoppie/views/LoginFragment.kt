package com.example.shoppie.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.shoppie.databinding.FragmentLoginBinding
import com.example.shoppie.utils.requireMainActivity
import com.example.shoppie.utils.setCornerRadius
import com.example.shoppie.views.bottomsheets.ForgotPasswordBottomSheet

class LoginFragment: Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private val forgotPasswordBottomSheet = ForgotPasswordBottomSheet()

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
        setupViews()
        setupWindowInsets()
        setupNavigation()
    }

    private fun setupNavigation() = binding.apply{
        tvForgotPass.setOnClickListener {
            forgotPasswordBottomSheet.show(childFragmentManager, "Forgot password")
        }

        btnLogin.setOnClickListener {
            findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToMainFragment())
        }
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