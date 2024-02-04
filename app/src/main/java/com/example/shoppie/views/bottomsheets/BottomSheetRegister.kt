package com.example.shoppie.views.bottomsheets

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.fragment.findNavController
import com.example.shoppie.databinding.FragmentBottomSheetBinding
import com.example.shoppie.utils.common.CustomBottomSheetDialog
import com.example.shoppie.utils.requireMainActivity
import com.example.shoppie.utils.setCornerRadius
import com.example.shoppie.views.FragmentVerificationDirections
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomSheetRegister: BottomSheetDialogFragment() {
    private lateinit var binding: FragmentBottomSheetBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return CustomBottomSheetDialog(requireContext())
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
        btnGoToHomepage.setCornerRadius(24f)
        btnGoToHomepage.setOnClickListener {
            navigateToLoginFragment()
        }
    }
    private fun navigateToLoginFragment() {
        dismiss()
        findNavController().navigate(FragmentVerificationDirections.actionFragmentVerificationToLoginFragment())
    }

}