package patient

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.shubham.doctorpatientandroidappnew.R
import com.shubham.doctorpatientandroidappnew.databinding.ActivityPatientMainBinding

class PatientMainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPatientMainBinding

    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPatientMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        bottomNavigationView = binding.bottomNavigationView

        loadFragment(PatientAppointmentDetailsFragment.newInstance())
        initListeners()
        bottomNavigationView.itemIconTintList = null
    }

    private fun loadFragment(fragment: Fragment?): Boolean {
        if (fragment != null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
            return true
        }
        return false
    }

    private fun initListeners() {
        bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
            var fragment: Fragment? = null
            when (menuItem.itemId) {
                R.id.nav_appointments -> {
                    fragment = PatientAppointmentDetailsFragment.newInstance()
                }

                R.id.nav_available_doctors -> {
                    fragment = AvailableDoctorsFragment.newInstance()
                }
            }
            loadFragment(fragment)
        }
    }
}
