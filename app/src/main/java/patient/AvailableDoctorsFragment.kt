package patient

import AVAILABLE_DOCTORS_SEARCH_LIST_CONSTANT
import SELECTED_DOCTOR
import android.app.Dialog
import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.shubham.databasemodule.DataBase
import com.shubham.doctorpatientandroidappnew.R
import com.shubham.doctorpatientandroidappnew.databinding.AvailableDoctorsFragmentBinding
import com.shubham.doctorpatientandroidappnew.databinding.DoctorFilterDialogBinding
import helperFunctions.getLinearLayoutManager
import helperFunctions.getPatientSharedPreferences
import helperFunctions.getSupportActionBarView
import helperFunctions.getToast
import models.Doctor
import models.FilterModel
import patient.adapters.AvailableDoctorsAdapter
import patient.adapters.FilterDoctorAdapter
import patient.interfaces.AvailableDoctorItemInterface
import patient.viewModels.AvailableDoctorsViewModel

class AvailableDoctorsFragment : Fragment(), AvailableDoctorItemInterface {
    private lateinit var viewModel: AvailableDoctorsViewModel

    private lateinit var searchIconItem: MenuItem

    private var _binding: AvailableDoctorsFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var dialog: Dialog
    private var _filterBinding: DoctorFilterDialogBinding? = null
    private val filterBinding get() = _filterBinding!!

    private lateinit var recyclerView: RecyclerView

    private lateinit var searchView: androidx.appcompat.widget.SearchView
    private val adapter: AvailableDoctorsAdapter by lazy { AvailableDoctorsAdapter(this) }
    private val filterAdapter: FilterDoctorAdapter by lazy {
        FilterDoctorAdapter(
            loadDegreeDetailsForAdapter()
        )
    }

    companion object {
        fun newInstance() = AvailableDoctorsFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _filterBinding = DoctorFilterDialogBinding.inflate(
            inflater,
            container,
            false
        )

        val binding = AvailableDoctorsFragmentBinding.inflate(inflater, container, false)

        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = AvailableDoctorsFragmentBinding.bind(view)

        inflateFilterDoctorDialog()
        initListeners()
    }

    private fun inflateFilterDoctorDialog() {
        // Inflating the Dialog View.
        dialog = Dialog(requireActivity())
        dialog.setContentView(filterBinding.root)

        dialog.window?.apply {
            attributes.windowAnimations = R.style.dialog_animation

            setBackgroundDrawable(
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.dialog_background
                )
            )
            setLayout(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }
    }

    private fun initListeners() {
        filterBinding.ApplyFilterBtn.setOnClickListener {
            viewModel.filterAvailableDoctorData(listOf("Dentist", "MD", "Surgeon"))
                .observe(viewLifecycleOwner, Observer {
                    dialog.cancel()
                    adapter.filteredDoctorsAdapterData(it)
                })
        }

        filterBinding.ClearFilterBtn.setOnClickListener {
            viewModel.restoreAvailableDoctorsList().observe(viewLifecycleOwner, Observer {
                dialog.cancel()
                adapter.filteredDoctorsAdapterData(it)
            })
        }
    }

    private fun loadDegreeDetailsForAdapter() =
        DataBase.doctorDegreeList.map { FilterModel(it) }.toList()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        requireActivity().getSupportActionBarView("Available Doctors", false)

        viewModel = ViewModelProvider(this).get(AvailableDoctorsViewModel::class.java)

        viewModel.getAllAvailableDoctorsList().observe(viewLifecycleOwner, Observer {
            if (it != null && it.size > 0) {
                adapter.setAvailableDoctorsAdapterData(it)
                binding.AvailableDoctorsRecyclerView.adapter = adapter
                val manager = LinearLayoutManager(requireActivity())
                binding.AvailableDoctorsRecyclerView.layoutManager = manager
            } else {
                getToast(requireActivity(), "No Doctors Are Available At this Moment").show()
            }
        })
    }

    override fun onItemClick(doctor: Doctor) {
        searchIconItem.collapseActionView()
        val intent = Intent(context, PatientBookAppointmentActivity::class.java)
        intent.putExtra(SELECTED_DOCTOR, doctor)
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        _filterBinding = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.patient_menu, menu)
        val searchManager =
            requireActivity().getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchIconItem = menu.findItem(R.id.searchIconMenu)
        searchView = searchIconItem.actionView as androidx.appcompat.widget.SearchView
        searchView.setIconifiedByDefault(true)
        searchView.queryHint = getString(R.string.patientSearchViewHint)
        searchView.setSearchableInfo(searchManager.getSearchableInfo(requireActivity().componentName))

        searchView.setOnQueryTextListener(object :
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                val resultData = adapter.getAvailableDoctorsList()
                val intent = Intent(
                    requireActivity(),
                    PatientSearchResultActivity::class.java
                )
                intent.action = Intent.ACTION_SEARCH
                intent.putExtra("query", query)
                intent.putParcelableArrayListExtra(
                    AVAILABLE_DOCTORS_SEARCH_LIST_CONSTANT,
                    ArrayList<Doctor>(resultData)
                )
                startActivity(intent)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.filter.filter(newText)
                return false
            }
        })

        return super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.PatientLogoutMenuItem -> {
                val sharedPref = getPatientSharedPreferences(requireActivity())
                sharedPref.edit().apply {
                    clear()
                    apply()
                }
                getToast(requireActivity(), "User Logout Successfully").show()
                startActivity(Intent(requireActivity(), PatientLoginSignUpActivity::class.java))
                requireActivity().finish()
                true
            }
            R.id.FilterDoctorMenuItem -> {
//                dialog = Dialog(requireActivity())
//                dialog.setContentView(R.layout.doctor_filter_dialog) // doubt
//
//                dialog.window?.apply {
//                    attributes.windowAnimations = R.style.dialog_animation
//
//                    setBackgroundDrawable(
//                        ContextCompat.getDrawable(
//                            requireContext(),
//                            R.drawable.dialog_background
//                        )
//                    )
//                    setLayout(
//                        ViewGroup.LayoutParams.WRAP_CONTENT,
//                        ViewGroup.LayoutParams.WRAP_CONTENT
//                    )
//                }
                recyclerView = filterBinding.FilterDoctorRcyView
                val layoutManager = requireActivity().getLinearLayoutManager()
                recyclerView.layoutManager = layoutManager
                recyclerView.adapter = filterAdapter
                dialog.show()
                true
            }
            else -> false
        }
    }
}