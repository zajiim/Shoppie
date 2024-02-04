package com.example.shoppie.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.shoppie.R
import com.example.shoppie.databinding.FragmentVerificationLayoutBinding
import com.example.shoppie.views.bottomsheets.BottomSheetRegister
import com.example.shoppie.utils.requireMainActivity
import com.example.shoppie.utils.setCornerRadius

class FragmentVerification: Fragment() {
    private lateinit var binding: FragmentVerificationLayoutBinding
    private val registerSuccessBottomSheet = BottomSheetRegister()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentVerificationLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        setupWindowInsets()
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
        btnSubmit.setCornerRadius(24f)
        setSubmitButtonColor()
    }

    private fun setupNavigation() = binding.apply{
        btnBack.setOnClickListener {
            findNavController().navigateUp()
        }
        btnSubmit.setOnClickListener {
            registerSuccessBottomSheet.show(childFragmentManager, "Register success")
        }

    }

    private fun setSubmitButtonColor() = binding.apply {
        otpInputLayout.onInputChanged = { otpVal ->
            if (otpVal.length < 6) {
                btnSubmit.setBackgroundColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.submit_btn_inactive
                    )
                )
                btnSubmit.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.submit_btn_inactive_text_color
                    )
                )
            } else if (otpVal.length == 6) {
                btnSubmit.setBackgroundColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.primary_violet
                    )
                )
                btnSubmit.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.white
                    )
                )
            }
        }
    }
}