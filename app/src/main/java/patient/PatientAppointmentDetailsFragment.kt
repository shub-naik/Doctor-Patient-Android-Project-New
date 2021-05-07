package patient

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.shubham.doctorpatientandroidappnew.databinding.PatientAppointmentDetailsFragmentBinding
import helperFunctions.getSupportActionBarView
import patient.adapters.AppointmentDetailViewPagerAdapter
import patient.viewModels.PatientAppointmentDetailsViewModel

class PatientAppointmentDetailsFragment : Fragment() {
    private var _binding: PatientAppointmentDetailsFragmentBinding? = null
    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager

    private val binding get() = _binding!!

    companion object {
        fun newInstance() = PatientAppointmentDetailsFragment()
    }

    private lateinit var viewModel: PatientAppointmentDetailsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = PatientAppointmentDetailsFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        Log.e("DetailsFragment", "onResume: 1")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = PatientAppointmentDetailsFragmentBinding.bind(view)

        Log.e("DetailsFragment", "onViewCreated: 1")

        // View Pager for Past and Upcoming Details
        viewPager = binding.PatientAppointmentDetailsViewPager

        tabLayout = binding.PatientAppointmentDetailsTabLayout
        tabLayout.tabGravity = TabLayout.GRAVITY_FILL

        val appointmentViewPagerAdapter =
            AppointmentDetailViewPagerAdapter(childFragmentManager)
        viewPager.adapter = appointmentViewPagerAdapter

        tabLayout.setupWithViewPager(viewPager)
        viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(PatientAppointmentDetailsViewModel::class.java)

        requireActivity().getSupportActionBarView("Appointments", false)
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e("DetailsFragment", "onDestroy: 1")
    }
}