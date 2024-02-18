package com.example.shoppie.views


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.shoppie.R
import com.example.shoppie.data.models.User
import com.example.shoppie.databinding.FragmentVerificationLayoutBinding
import com.example.shoppie.utils.common.ModalProgressLoadingFragment
import com.example.shoppie.utils.requireMainActivity
import com.example.shoppie.utils.setCornerRadius
import com.example.shoppie.utils.showToast
import com.example.shoppie.viewmodels.RegisterViewModel
import com.example.shoppie.views.bottomsheets.BottomSheetRegister
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FragmentVerification : Fragment() {
    private lateinit var binding: FragmentVerificationLayoutBinding
    private val registerSuccessBottomSheet = BottomSheetRegister()
    private val viewModel by viewModels<RegisterViewModel>()
    private val args by navArgs<FragmentVerificationArgs>()
    private lateinit var modalProgressFragment: ModalProgressLoadingFragment

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentVerificationLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        modalProgressFragment = binding.modalProgressContainer.getFragment()
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

    private fun setupViews() = binding.apply {
        setupNavigation()
        btnSubmit.setCornerRadius(24f)
        setSubmitButtonColor()
        btnSubmit.setOnClickListener {
            if (otpInputLayout.text.isNotEmpty()) {
                val otp = otpInputLayout.text
                modalProgressFragment.showModalProgress()
                otpInputLayout.text.showToast(this@FragmentVerification)
//                viewModel.signInWithOtp(args.verificationId, otp)
                viewModel.signInWithOtp(args.verificationId, otp, getUserInfo())
            }
        }


        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                modalProgressFragment.showModalProgress()
                viewModel.otpVerified.collect { isVerified ->
                    if (isVerified) {
                        modalProgressFragment.hideModalProgress()
                        findNavController().navigate(FragmentVerificationDirections.actionFragmentVerificationToLoginFragment())
                    } else {
                        modalProgressFragment.hideModalProgress()
                    }
                }
            }
        }
    }

    private fun getUserInfo(): User {
        val phoneNumber = args.phoneNumber
        val userName = args.userName
        return User(userName = userName, phoneNumber = phoneNumber)
    }

    private fun setupNavigation() = binding.apply {
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