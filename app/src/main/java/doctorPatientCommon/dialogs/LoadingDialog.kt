package doctorPatientCommon.dialogs

import android.app.Activity
import android.app.AlertDialog
import com.shubham.doctorpatientandroidappnew.R

class LoadingDialog(private val activity: Activity) {
    private lateinit var alertDialog: AlertDialog

    fun startLoadingDialog() {
        val builder = AlertDialog.Builder(activity)
        val layoutInflater = activity.layoutInflater
        builder.setView(layoutInflater.inflate(R.layout.custom_progress_dialog, null))
        builder.setCancelable(false)

        alertDialog = builder.create()
        alertDialog.show()
    }

    fun stopLoadingDialog() {
        alertDialog.dismiss()
    }
}