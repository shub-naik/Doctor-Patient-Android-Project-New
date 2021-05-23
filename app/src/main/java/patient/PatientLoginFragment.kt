package patient

import PATIENT_CREDENTIAL
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import application.ApplicationClass
import com.shubham.doctorpatientandroidappnew.R
import com.shubham.doctorpatientandroidappnew.databinding.FragmentPatientLoginBinding
import doctorPatientCommon.dialogs.LoadingDialog
import helperFunctions.getPatientSharedPreferences
import helperFunctions.getSnackBar
import kotlinx.coroutines.launch

class PatientLoginFragment : Fragment() {
    private var _binding: FragmentPatientLoginBinding? = null
    private val binding: FragmentPatientLoginBinding get() = _binding!!

    private val patientDao by lazy { (requireActivity().application as ApplicationClass).patientDao }

    companion object {
        fun newInstance() = PatientLoginFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPatientLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeClickListeners()
    }

    private fun initializeClickListeners() {
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
                        binding.PatientLoginPhoneEt.editText!!.error =
                            getString(R.string.phone_required)
                    if (password.isEmpty())
                        binding.PatientLoginPasswordEt.editText!!.error =
                            getString(R.string.password_required)
                }
            } else
                patientLoginCheck(phone, password)
        }
    }

    private fun patientLoginCheck(phone: String, password: String) {
        lifecycleScope.launch {
            val loadingDialog = LoadingDialog(requireActivity())
            loadingDialog.startLoadingDialog(binding.SnackBarLinearLayout)
            val check = patientDao.patientLogin(phone, password)
            if (check.isNotEmpty() && check[0] > 0) {
                val sharedPreferences = getPatientSharedPreferences(requireActivity())
                val editor = sharedPreferences.edit()
                editor.putLong(PATIENT_CREDENTIAL, check[0])
                editor.apply()
                startActivity(Intent(activity, PatientMainActivity::class.java))
                requireActivity().finish()
            } else getSnackBar(
                binding.SnackBarLinearLayout,
                getString(R.string.invalid_login_found)
            ).show()
            loadingDialog.stopLoadingDialog()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}