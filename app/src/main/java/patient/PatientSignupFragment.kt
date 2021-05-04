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
import com.shubham.doctorpatientandroidappnew.databinding.FragmentPatientSignupBinding
import doctorPatientCommon.dialogs.LoadingDialog
import exceptions.Exceptions
import helperFunctions.getPatientSharedPreferences
import helperFunctions.getSnackBar
import helperFunctions.getToast
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit

class PatientSignupFragment : Fragment() {
    private lateinit var binding: FragmentPatientSignupBinding
    private lateinit var backgroundExecutor: ScheduledExecutorService

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPatientSignupBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeClickListeners()
    }

    private fun initializeClickListeners() {
        val mainExecutor: Executor = ContextCompat.getMainExecutor(requireActivity())
        backgroundExecutor = Executors.newSingleThreadScheduledExecutor()

        binding.PatientSignUpBtn.setOnClickListener {
            val phone = binding.PatientSignUpPhoneEt.text.toString()
            val password = binding.PatientSignUpPasswordEt.text.toString()
            val name = binding.PatientSignUpNameEt.text.toString()
            if (phone.isEmpty() || password.isEmpty() || name.isEmpty()) {
                if (TextUtils.isEmpty(phone) || TextUtils.isEmpty(password) || TextUtils.isEmpty(
                        name
                    )
                ) {
                    getSnackBar(
                        binding.linearLayout1,
                        getString(R.string.empty_field_error)
                    ).show()
                    if (phone.isEmpty())
                        binding.PatientSignUpPhoneEt.error = "Phone Number Required"
                    if (password.isEmpty())
                        binding.PatientSignUpPasswordEt.error = "8 Characters Password Required"
                    if (name.isEmpty())
                        binding.PatientSignUpNameEt.error = "Username Required"
                }
            } else {
                val loadingDialog = LoadingDialog(requireActivity())
                loadingDialog.startLoadingDialog(binding.linearLayout1)
                val check = addPatientDataToDatabase(name, phone, password, loadingDialog)

                backgroundExecutor.schedule({
                    loadingDialog.stopLoadingDialog()
                    if (!check)
                        mainExecutor.execute {
                            getSnackBar(
                                binding.linearLayout1,
                                "Invalid SignUp Found !!!"
                            ).show()
                        }
                    else {
//                         Save Data To Patient Shared Preferences
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

    private fun addPatientDataToDatabase(
        patientUsername: String,
        patientPhone: String,
        patientPassword: String,
        loadingDialog: LoadingDialog
    ): Boolean {
        val activityContext = requireActivity()

        try {
            return DataBase.addPatientToRegisteredList(
                mapOf(
                    "PatientUsername" to patientUsername,
                    "PatientPhone" to patientPhone,
                    "PatientPassword" to patientPassword
                )
            )
        } catch (exception: IllegalStateException) {
            getToast(
                activityContext,
                exception.message.toString()
            ).show()
            loadingDialog.stopLoadingDialog()
            Log.e("CheckValue", "addPatientDataToDatabase: 2 ")

        } catch (exception: Exceptions) {
            getToast(
                activityContext,
                exception.message.toString()
            ).show()
            loadingDialog.stopLoadingDialog()

            binding.PatientSignUpPhoneEt.error =
                "User Already Exists With This Phone Number - $patientPhone"
            Log.e("CheckValue", "addPatientDataToDatabase: 3 ")
        }
        return false
    }
}