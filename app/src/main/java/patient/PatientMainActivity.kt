package patient

import AVAILABLE_DOCTORS_LIST_CONSTANT
import AVAILABLE_DOCTORS_SEARCH_LIST_CONSTANT
import SELECTED_DOCTOR
import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.shubham.databasemodule.DataBase
import com.shubham.doctorpatientandroidappnew.R
import com.shubham.doctorpatientandroidappnew.databinding.ActivityPatientMainBinding
import helperFunctions.getPatientSharedPreferences
import helperFunctions.getToast
import models.Doctor
import patient.adapters.AvailableDoctorsAdapter
import patient.interfaces.AvailableDoctorItemInterface

class PatientMainActivity : AppCompatActivity(), AvailableDoctorItemInterface {
    private lateinit var searchIconItem: MenuItem
    private lateinit var binding: ActivityPatientMainBinding
    private lateinit var searchView: androidx.appcompat.widget.SearchView
    private val availableDoctorsList: ArrayList<Doctor> = ArrayList()

    private val adapter: AvailableDoctorsAdapter by lazy { AvailableDoctorsAdapter(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPatientMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState?.getParcelableArrayList<Doctor>(AVAILABLE_DOCTORS_LIST_CONSTANT) != null) {
            availableDoctorsList.clear()
            availableDoctorsList.addAll(
                savedInstanceState.getParcelableArrayList<Doctor>(AVAILABLE_DOCTORS_LIST_CONSTANT)!!
            )
        }

        val availableDoctorsRecyclerView = binding.AvailableDoctorsRecyclerView

        if (DataBase.getRegisteredDoctorList().isNotEmpty()) {
            availableDoctorsList.clear()
            availableDoctorsList.addAll(ArrayList(DataBase.getRegisteredDoctorList()))
        } else
            getToast(this, "No Doctors Are Available At this Moment").show()

        adapter.setAvailableDoctorsAdapterData(availableDoctorsList)
        availableDoctorsRecyclerView.adapter = adapter
        val manager = LinearLayoutManager(this)
        availableDoctorsRecyclerView.layoutManager = manager
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(
            AVAILABLE_DOCTORS_LIST_CONSTANT,
            availableDoctorsList
        )
    }

    // Options Menu
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.patient_menu, menu)
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchIconItem = menu.findItem(R.id.searchIconMenu)
        searchView = searchIconItem.actionView as androidx.appcompat.widget.SearchView
        searchView.setIconifiedByDefault(true)
        searchView.queryHint = getString(R.string.patientSearchViewHint)
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))

        searchView.setOnQueryTextListener(object :
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                val resultData = adapter.getAvailableDoctorsList()
                val intent = Intent(
                    this@PatientMainActivity,
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

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.PatientLogoutMenuItem -> {
                val sharedPref = getPatientSharedPreferences(this)
                sharedPref.edit().apply {
                    clear()
                    apply()
                }
                getToast(this, "User Logout Successfully").show()
                startActivity(Intent(this, PatientLoginSignUpActivity::class.java))
                finish()
                true
            }
            R.id.PatientAppointmentCheckMeuItem -> {
                startActivity(Intent(this, PatientAppointmentHistoryActivity::class.java))
                true
            }
            R.id.searchIconMenu -> {
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onItemClick(doctor: Doctor) {
        searchIconItem.collapseActionView()
        val intent = Intent(this, PatientBookAppointmentActivity::class.java)
        intent.putExtra(SELECTED_DOCTOR, doctor)
        startActivity(intent)
    }
}
