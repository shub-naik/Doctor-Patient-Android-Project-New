package patient

import AVAILABLE_DOCTORS_LIST_CONSTANT
import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.shubham.databasemodule.DataBase
import com.shubham.doctorpatientandroidappnew.MainActivity
import com.shubham.doctorpatientandroidappnew.R
import com.shubham.doctorpatientandroidappnew.databinding.ActivityPatientMainBinding
import helperFunctions.getPatientSharedPreferences
import helperFunctions.getToast
import models.Doctor
import patient.adapters.AvailableDoctorsAdapter
import patient.interfaces.AvailableDoctorItemInterface
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService

class PatientMainActivity : AppCompatActivity(), AvailableDoctorItemInterface {
    private var isScrolling = false // Used for recycler view scrolling

    private lateinit var binding: ActivityPatientMainBinding
    private lateinit var searchView: androidx.appcompat.widget.SearchView
    private var availableDoctorsList: ArrayList<Doctor> = ArrayList()

    private val adapter: AvailableDoctorsAdapter by lazy { AvailableDoctorsAdapter(this) }
    private val backgroundExecutor: ScheduledExecutorService by lazy { Executors.newSingleThreadScheduledExecutor() }
    private val mainExec: Executor by lazy { ContextCompat.getMainExecutor(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPatientMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState?.getParcelableArrayList<Doctor>(AVAILABLE_DOCTORS_LIST_CONSTANT) != null)
            availableDoctorsList =
                savedInstanceState.getParcelableArrayList<Doctor>(AVAILABLE_DOCTORS_LIST_CONSTANT)!!

        val availableDoctorsRecyclerView = binding.AvailableDoctorsRecyclerView

        if (DataBase.getRegisteredDoctorList().isNotEmpty())
            availableDoctorsList = ArrayList(DataBase.getRegisteredDoctorList())
        else
            getToast(this, "No Doctors Are Available At this Moment").show()

        adapter.setAvailableDoctorsAdapterData(availableDoctorsList)
        availableDoctorsRecyclerView.adapter = adapter
        val manager = LinearLayoutManager(this)
        availableDoctorsRecyclerView.layoutManager = manager

        // Recycler View On Scroll Listener
//        availableDoctorsRecyclerView.addOnScrollListener(object : OnScrollListener() {
//            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
//                super.onScrolled(recyclerView, dx, dy)
//                val currentItemsCount = manager.childCount
//                val totalItemCount = manager.itemCount
//                val scrollOutItemsCount = manager.findFirstVisibleItemPosition()
//
//                if (isScrolling && (totalItemCount == currentItemsCount + scrollOutItemsCount)) {
//                    // Fetch data remotely
//                    isScrolling = false
//                    fetchMoreData()
//                }
//            }
//
//            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
//                super.onScrollStateChanged(recyclerView, newState)
//                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL)
//                    isScrolling = true
//            }
//        })
    }

//    private fun fetchMoreData() {
//        if (availableDoctorsList.size >= 30)
//            return
//        binding.AvailableDoctorsRecyclerViewProgressBar.visibility = View.VISIBLE
//
//        backgroundExecutor.schedule({
//            availableDoctorsList.addAll(getDummyAvailableDoctorData())
//            mainExec.execute {
//                adapter.setAvailableDoctorsAdapterData(
//                    availableDoctorsList
//                )
//
//                // Recycler View Position State Starts Here
//                val recyclerViewState: Parcelable? =
//                    binding.AvailableDoctorsRecyclerView.layoutManager?.onSaveInstanceState()
//                binding.AvailableDoctorsRecyclerView.layoutManager?.onRestoreInstanceState(
//                    recyclerViewState
//                )
//                // Recycler View Position State Ends Here
//
//                binding.AvailableDoctorsRecyclerViewProgressBar.visibility = View.GONE
//            }
//        }, 3, TimeUnit.SECONDS)
//    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(
            AVAILABLE_DOCTORS_LIST_CONSTANT,
            availableDoctorsList
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        backgroundExecutor.shutdown()
    }

    // Options Menu
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.patient_menu, menu)
        // Associate searchable configuration with the SearchView
        val searchManager =
            getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView =
            menu.findItem(R.id.searchIconMenu).actionView as androidx.appcompat.widget.SearchView
        searchView.setSearchableInfo(
            searchManager
                .getSearchableInfo(componentName)
        )
        searchView.maxWidth = Int.MAX_VALUE

        searchView.setOnQueryTextListener(object :
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                adapter.filter.filter(query)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.filter.filter(newText)
                return false
            }
        })
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.PatientLogoutMenuItem -> {
                val sharedPref = getPatientSharedPreferences(this)
                sharedPref.edit().apply {
                    clear()
                    apply()
                }
                getToast(this, "User Logout Selected").show()
                startActivity(Intent(this, MainActivity::class.java))
                finish()
                true
            }
            R.id.PatientProfileMenuItem -> {
                getToast(this, "User Profile Selected").show()
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

    override fun onItemClick(position: Int) {
        if (position >= 0 && position <= availableDoctorsList.size - 1) {
            val intent = Intent(this, PatientBookAppointmentActivity::class.java)
            intent.putExtra("SelectedDoctor", availableDoctorsList[position])
            startActivity(intent)
        } else
            getToast(this, "Some Error Occurred in Available List").show()
    }
}
