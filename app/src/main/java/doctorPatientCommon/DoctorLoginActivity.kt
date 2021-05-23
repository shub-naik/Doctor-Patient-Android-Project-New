package doctorPatientCommon

import DOCTOR_CREDENTIAL
import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import application.ApplicationClass
import com.shubham.doctorpatientandroidappnew.R
import com.shubham.doctorpatientandroidappnew.databinding.ActivityDoctorPatientLoginBinding
import doctor.DoctorProfileActivity
import doctorPatientCommon.dialogs.LoadingDialog
import helperFunctions.getDoctorSharedPreferences
import helperFunctions.getSnackBar
import helperFunctions.openActivity
import kotlinx.coroutines.launch
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService

class DoctorLoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDoctorPatientLoginBinding
    private lateinit var backgroundExecutor: ScheduledExecutorService
    private val doctorDao by lazy { (this.application as ApplicationClass).doctorDao }
    private val context: Context by lazy { this }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDoctorPatientLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // show Custom Dialog after clicking Login Button
        val loadingDialog = LoadingDialog(this)

        // Request Focus For Phone Number Field Automatically
        binding.DoctorLoginPhoneEt.requestFocus()

        // BackGround and Main Executor
        val mainExecutor: Executor = ContextCompat.getMainExecutor(this)
        backgroundExecutor = Executors.newSingleThreadScheduledExecutor()

        binding.DoctorLoginBtn.setOnClickListener {
            val phone = binding.DoctorLoginPhoneEt.editText!!.text.toString()
            val password = binding.DoctorLoginPasswordEt.editText!!.text.toString()
            if (TextUtils.isEmpty(phone) || TextUtils.isEmpty(password)) {
                getSnackBar(
                    binding.SnackBarLinearLayout,
                    getString(R.string.empty_field_error)
                ).show()
                showEtError(phone, password)
            } else {
                loadingDialog.startLoadingDialog(binding.SnackBarLinearLayout)
//                    val check = Database.checkDoctorLogin(phone, password)
                lifecycleScope.launch {
                    val check = doctorDao.doctorLogin(phone, password)
                    loadingDialog.stopLoadingDialog()
                    when {
                        check.size > 1 || check.isEmpty() -> mainExecutor.execute {
                            getSnackBar(
                                binding.SnackBarLinearLayout,
                                "Invalid Login Found !!!"
                            ).show()
                        }
                        check.size == 1 -> {
                            // Save Data To Doctor Shared Preferences
                            val sharedPreferences = getDoctorSharedPreferences(context)
                            val editor = sharedPreferences.edit()
                            editor.putLong(DOCTOR_CREDENTIAL, check[0])
                            editor.apply()
                            context.openActivity(DoctorProfileActivity::class.java)
                            finish()
                        }
                    }
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        backgroundExecutor = Executors.newSingleThreadScheduledExecutor()
    }

    override fun onStop() {
        super.onStop()
        backgroundExecutor.shutdown()
    }

    override fun onDestroy() {
        super.onDestroy()
        backgroundExecutor.shutdown()
    }

    private fun showEtError(phone: String, password: String) {
        if (phone.isEmpty())
            binding.DoctorLoginPhoneEt.error = "Phone Number is Mandatory"
        if (password.isEmpty())
            binding.DoctorLoginPasswordEt.error =
                "8 Characters Password is Mandatory"
    }
}