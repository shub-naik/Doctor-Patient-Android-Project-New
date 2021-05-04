package patient

import PATIENT_CREDENTIAL
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.shubham.databasemodule.DataBase
import com.shubham.doctorpatientandroidappnew.R
import com.shubham.doctorpatientandroidappnew.databinding.FragmentPatientLoginBinding
import doctorPatientCommon.dialogs.LoadingDialog
import helperFunctions.getPatientSharedPreferences
import helperFunctions.getSnackBar
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit

class PatientLoginFragment : Fragment() {
    private lateinit var binding: FragmentPatientLoginBinding
    private lateinit var backgroundExecutor: ScheduledExecutorService

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPatientLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeClickListeners()
        Log.e("FragmentDes", "onViewCreated: Login Created")
    }

    private fun initializeClickListeners() {
        // BackGround and Main Executor
        val mainExecutor: Executor = ContextCompat.getMainExecutor(activity)
        backgroundExecutor = Executors.newSingleThreadScheduledExecutor()

        binding.PatientLoginBtn.setOnClickListener {
            val phone = binding.PatientLoginPhoneEt.editText!!.text.toString()
            val password = binding.PatientLoginPasswordEt.editText!!.text.toString()
            if (phone.isEmpty() || password.isEmpty()) {
                if (TextUtils.isEmpty(phone) || TextUtils.isEmpty(password)) {
                    getSnackBar(
                        binding.SnackBarLinearLayout,
                        getString(R.string.empty_field_error)
                    ).show()
                    if (phone.isEmpty())
                        binding.PatientLoginPhoneEt.editText!!.error = "Phone Number Required"
                    if (password.isEmpty())
                        binding.PatientLoginPasswordEt.editText!!.error =
                            "8 Characters Password Required"
                }
            } else {
                val loadingDialog = LoadingDialog(requireActivity())
                loadingDialog.startLoadingDialog(binding.SnackBarLinearLayout)
                backgroundExecutor.schedule({
                    // Call Backend Method For Patient Login Check
                    val check = DataBase.checkPatientLogin(phone, password)
                    loadingDialog.stopLoadingDialog()

                    if (!check)
                        mainExecutor.execute {
                            getSnackBar(
                                binding.SnackBarLinearLayout,
                                "Invalid Login Found !!!"
                            ).show()
                        }
                    else {
                        // Save Data To Patient Shared Preferences
                        val sharedPreferences = getPatientSharedPreferences(requireActivity())
                        val editor = sharedPreferences.edit()
                        editor.putString(PATIENT_CREDENTIAL, phone)
                        editor.apply()
                        startActivity(Intent(activity, PatientMainActivity::class.java))
                        requireActivity().finish()
                    }
                }, 3, TimeUnit.SECONDS)
            }
        }
    }
}