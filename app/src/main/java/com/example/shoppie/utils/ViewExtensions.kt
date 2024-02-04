package com.example.shoppie.utils

import android.app.AlertDialog
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment


//private var dialog: AlertDialog? = null
fun View.setCornerRadius(radius: Float = Float.MAX_VALUE) {
    val outline = outlineProvider as? RoundedRectOutlineProvider ?: RoundedRectOutlineProvider(radius)
    outline.radiusPx = radius
    outlineProvider = outline
    clipToOutline = true
}

fun String.showToast(fragment: Fragment) =
    run { Toast.makeText(fragment.requireContext(), this, Toast.LENGTH_SHORT).show() }

//fun String.showDialog(context: Context) {
//    val progress = ProgressDialogBinding.inflate(LayoutInflater.from(context))
//    progress.tvMessage.text = this
//    dialog = AlertDialog.Builder(context).setView(progress.root).setCancelable(false).create()
//    dialog?.apply {
//        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//        dialog?.show()
//    }
//}

fun View.show() {
    View.VISIBLE
}
fun View.hide() {
    View.INVISIBLE
}

fun View.gone() {
    View.GONE
}