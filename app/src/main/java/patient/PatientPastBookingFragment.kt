package patient

import NON_AUTHORIZED_USER
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import application.ApplicationClass
import com.shubham.doctorpatientandroidappnew.databinding.FragmentPatientPastBookingBinding
import helperFunctions.getLinearLayoutManager
import helperFunctions.getPatientCredentials
import helperFunctions.getToast
import patient.adapters.PatientAppointmentAdapter
import java.time.LocalDate
import java.time.LocalTime

class PatientPastBookingFragment : Fragment() {
    private var _binding: FragmentPatientPastBookingBinding? = null
    private val binding get() = _binding!!
    private val patientDao by lazy { (requireActivity().application as ApplicationClass).patientDao }

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

        val patCredential = requireActivity().getPatientCredentials()
        if (patCredential > 0) {
            val recyclerView = binding.PatientPastAppointmentRcyView
            val list = patientDao.getPastAppointmentByPatientId(
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
                    binding.PatientPastAppointmentTxtView.visibility = View.VISIBLE
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