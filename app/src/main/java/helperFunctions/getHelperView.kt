package helperFunctions

import android.content.Context
import android.content.SharedPreferences
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar

fun getToast(context: Context, message: String, duration: Int = Toast.LENGTH_LONG): Toast =
    Toast.makeText(context, message, duration)

fun getSnackBar(layout: View, message: String, duration: Int = Snackbar.LENGTH_LONG): Snackbar =
    Snackbar.make(
        layout,
        message,
        duration
    )

fun Context.getSupportActionBarView(title: String) {
    val actionBarView = (this as AppCompatActivity).supportActionBar
    actionBarView?.apply {
        setDisplayHomeAsUpEnabled(true)
        this.title = title
    }
}

fun showSoftKeyboard(view: View) {
    if (view.requestFocus()) {
        val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
    }
}

fun getPatientSharedPreferences(context: Context): SharedPreferences =
    context.getSharedPreferences("PatientSharedPref", Context.MODE_PRIVATE)

fun getDoctorSharedPreferences(context: Context): SharedPreferences =
    context.getSharedPreferences("DoctorSharedPref", Context.MODE_PRIVATE)

fun getDarkModeSharedPreferences(context: Context): SharedPreferences =
    context.getSharedPreferences("DarkModeSharedPref", Context.MODE_PRIVATE)
