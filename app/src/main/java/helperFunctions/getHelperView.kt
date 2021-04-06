package helperFunctions

import android.content.Context
import android.content.SharedPreferences
import android.view.View
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar

fun getToast(context: Context, message: String, duration: Int = Toast.LENGTH_LONG): Toast =
    Toast.makeText(context, message, duration)

fun getSnackBar(layout: View, message: String, duration: Int = Snackbar.LENGTH_LONG): Snackbar =
    Snackbar.make(
        layout,
        message,
        duration
    )

fun getPatientSharedPreferences(context: Context): SharedPreferences =
    context.getSharedPreferences("PatientSharedPref", Context.MODE_PRIVATE)
