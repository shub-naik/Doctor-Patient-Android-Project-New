package patient

import PATIENT_CREDENTIAL
import android.content.Intent
import android.database.sqlite.SQLiteConstraintException
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import application.ApplicationClass
import com.shubham.doctorpatientandroidappnew.R
import com.shubham.doctorpatientandroidappnew.databinding.FragmentPatientSignupBinding
import doctorPatientCommon.dialogs.LoadingDialog
import entities.PatientEnt
import helperFunctions.getPatientSharedPreferences
import helperFunctions.getSnackBar
import helperFunctions.getToast
import kotlinx.coroutines.launch
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService

class PatientSignupFragment : Fragment() {
    private var _binding: FragmentPatientSignupBinding? = null
    private val binding: FragmentPatientSignupBinding get() = _binding!!
    private lateinit var backgroundExecutor: ScheduledExecutorService

    private val patientDao by lazy { (requireActivity().application as ApplicationClass).patientDao }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPatientSignupBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeClickListeners()
    }

    companion object {
        fun newInstance(): PatientSignupFragment = PatientSignupFragment()
    }


    private fun initializeClickListeners() {
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
            } else
                addPatientDataToDatabase(name, phone, password)
        }
    }

    private fun addPatientDataToDatabase(
        patientUsername: String,
        patientPhone: String,
        patientPassword: String
    ) {
        val activityContext = requireActivity()
        val loadingDialog = LoadingDialog(requireActivity())
        loadingDialog.startLoadingDialog(binding.linearLayout1)
        lifecycleScope.launch {
            try {
                val check = patientDao.insertPatient(
                    PatientEnt(
                        patientUsername,
                        patientPhone,
                        patientPassword
                    )
                )
                val sharedPreferences = getPatientSharedPreferences(activityContext)
                val editor = sharedPreferences.edit()
                editor.putLong(PATIENT_CREDENTIAL, check)
                editor.apply()
                startActivity(Intent(activity, PatientMainActivity::class.java))
                loadingDialog.stopLoadingDialog()
                activityContext.finish()
            } catch (exception: SQLiteConstraintException) {
                getToast(
                    activityContext,
                    exception.message.toString()
                ).show()
                loadingDialog.stopLoadingDialog()
                binding.PatientSignUpPhoneEt.error = getString(R.string.patient_already_exists)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}