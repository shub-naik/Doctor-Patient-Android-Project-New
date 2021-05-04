package patient

import NON_AUTHORIZED_USER
import PATIENT_CREDENTIAL
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.shubham.databasemodule.DataBase
import com.shubham.doctorpatientandroidappnew.R
import com.shubham.doctorpatientandroidappnew.databinding.FragmentPatientUpcomingBookingBinding
import helperFunctions.getLinearLayoutManager
import helperFunctions.getPatientSharedPreferences
import helperFunctions.getToast
import patient.adapters.PatientAppointmentAdapter

class PatientUpcomingBookingFragment : Fragment() {
    private var _binding: FragmentPatientUpcomingBookingBinding? = null
    private val binding get() = _binding!!

    private val TAG = "PatientUpcomingBookingF"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_patient_upcoming_booking, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentPatientUpcomingBookingBinding.bind(view)

        val patCredential =
            getPatientSharedPreferences(requireActivity()).getString(PATIENT_CREDENTIAL, "")
        if (patCredential != null) {
            val recyclerView = binding.PatientUpcomingAppointmentRcyView
            val list = DataBase.getAllRespectiveAppointmentForPatient(patCredential, 2)
            if (list.isNotEmpty()) {
                val adapter = PatientAppointmentAdapter(list, true)
                recyclerView.adapter = adapter
                val manager = requireActivity().getLinearLayoutManager()
                recyclerView.layoutManager = manager
            } else {
                binding.PatientUpcomingAppointmentTxtView.visibility = View.VISIBLE
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