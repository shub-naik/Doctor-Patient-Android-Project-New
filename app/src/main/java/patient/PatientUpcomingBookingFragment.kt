package patient

import NON_AUTHORIZED_USER
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import application.ApplicationClass
import com.shubham.doctorpatientandroidappnew.databinding.FragmentPatientUpcomingBookingBinding
import helperFunctions.getLinearLayoutManager
import helperFunctions.getPatientCredentials
import helperFunctions.getToast
import patient.adapters.PatientAppointmentAdapter
import java.time.LocalDate
import java.time.LocalTime

class PatientUpcomingBookingFragment : Fragment() {
    private var _binding: FragmentPatientUpcomingBookingBinding? = null
    private val binding get() = _binding!!

    private val patientDao by lazy { (requireActivity().application as ApplicationClass).patientDao }

    private val TAG = "PatientUpcomingBookingF"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentPatientUpcomingBookingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentPatientUpcomingBookingBinding.bind(view)

        val patCredential = requireActivity().getPatientCredentials()
        if (patCredential > 0) {
            val recyclerView = binding.PatientUpcomingAppointmentRcyView
            val list = patientDao.getUpcomingAppointmentsForPatientById(
                patCredential, LocalDate.now(),
                LocalTime.now()
            )
            list.observe(viewLifecycleOwner, Observer {
                if (it.isNotEmpty()) {
                    val adapter = PatientAppointmentAdapter(it, true)
                    val manager = requireActivity().getLinearLayoutManager()
                    recyclerView.layoutManager = manager
                    recyclerView.adapter = adapter
                } else {
                    recyclerView.visibility = View.GONE
                    binding.PatientUpcomingAppointmentTxtView.visibility = View.VISIBLE
                }
            })
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