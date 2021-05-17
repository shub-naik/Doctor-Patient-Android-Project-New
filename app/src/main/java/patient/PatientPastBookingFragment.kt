package patient

import NON_AUTHORIZED_USER
import PATIENT_CREDENTIAL
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.shubham.databasemodule.Database
import com.shubham.doctorpatientandroidappnew.databinding.FragmentPatientPastBookingBinding
import helperFunctions.getLinearLayoutManager
import helperFunctions.getPatientSharedPreferences
import helperFunctions.getToast
import patient.adapters.PatientAppointmentAdapter

class PatientPastBookingFragment : Fragment() {
    private var _binding: FragmentPatientPastBookingBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentPatientPastBookingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentPatientPastBookingBinding.bind(view)

        val patCredential =
            getPatientSharedPreferences(requireActivity()).getString(PATIENT_CREDENTIAL, "")
        if (patCredential != null) {
            val recyclerView =
                binding.PatientPastAppointmentRcyView
            val list = Database.getAllRespectiveAppointmentForPatient(patCredential, 1)

            if (list.isNotEmpty()) {
                val adapter = PatientAppointmentAdapter(list, false)
                val manager = requireActivity().getLinearLayoutManager()
                recyclerView.layoutManager = manager
                recyclerView.adapter = adapter
            } else {
                recyclerView.visibility = View.GONE
                binding.PatientPastAppointmentTxtView.visibility = View.VISIBLE
            }
        } else {
            getToast(requireActivity(), NON_AUTHORIZED_USER).show()
            requireActivity().finish()
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}