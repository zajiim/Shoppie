package com.example.shoppie.utils

import android.app.AlertDialog
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import java.util.regex.Pattern


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

fun View.viewVisibility(visible: Boolean = true) {
    visibility = if (visible) View.VISIBLE else View.GONE
}

fun String.isValidEmail() = !isNullOrEmpty() && Patterns.EMAIL_ADDRESS.matcher(this).matches()

fun isValidPasswordFormat(password: String): Boolean {
    val passwordREGEX = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$");
    return passwordREGEX.matcher(password).matches()
}

