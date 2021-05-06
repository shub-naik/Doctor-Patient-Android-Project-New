package doctorPatientCommon.dialogs

import android.app.Activity
import android.app.AlertDialog
import android.view.ViewGroup
import com.shubham.doctorpatientandroidappnew.R

class DoctorFilterDialog(private val activity: Activity) {
    private lateinit var alertDialog: AlertDialog

    fun startLoadingDialog(viewGroup: ViewGroup) {
        val builder = AlertDialog.Builder(activity)
        val layoutInflater = activity.layoutInflater

        builder.setView(layoutInflater.inflate(R.layout.doctor_filter_dialog, viewGroup, false))

        alertDialog = builder.create()
        alertDialog.show()
    }

    fun stopLoadingDialog() {
        alertDialog.dismiss()
    }
}