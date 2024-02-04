package com.example.shoppie.views.bottomsheets

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.WindowInsetsCompat
import com.example.shoppie.databinding.CreatePasswordBottomsheetBinding
import com.example.shoppie.databinding.ForgotPasswordBottomsheetBinding
import com.example.shoppie.utils.common.CustomBottomSheetDialog
import com.example.shoppie.utils.requireMainActivity
import com.example.shoppie.utils.setCornerRadius
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ChangePasswordBottomSheet: BottomSheetDialogFragment() {
    private lateinit var binding: CreatePasswordBottomsheetBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = CreatePasswordBottomsheetBinding.inflate(inflater, container, false)
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
            binding.safeBottomGuideline.setGuidelineEnd(rect.bottom)
        }
    }
    private fun setupViews() = binding.apply{
        btnChangePassword.setCornerRadius(24f)
    }
}