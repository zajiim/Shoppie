package com.example.shoppie.utils.common

import android.content.Context
import android.os.Bundle
import android.view.View
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog

class CustomBottomSheetDialog(context: Context) : BottomSheetDialog(context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setCanceledOnTouchOutside(false)
        setCancelable(false)
        setBottomSheetCallback()
    }

    private fun setBottomSheetCallback() {
        val bottomSheet = findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
        val behavior = bottomSheet?.let { BottomSheetBehavior.from(it) }
        behavior?.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (newState == BottomSheetBehavior.STATE_DRAGGING) {
                    behavior.state = BottomSheetBehavior.STATE_EXPANDED
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
        })
    }
}
