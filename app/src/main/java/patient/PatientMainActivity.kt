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

//        fragmentManager.beginTransaction().apply {
//            add(
//                R.id.container,
//                availableDoctorFragment,
//                getString(R.string.available_doctors)
//            ).hide(availableDoctorFragment)
//            add(R.id.container, appointmentFragment, getString(R.string.appointments))
//        }.commit()

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
//                    fragmentManager.beginTransaction().hide(activeFragment)
//                        .show(appointmentFragment).commit()
//                    activeFragment = appointmentFragment
                    fragment = PatientAppointmentDetailsFragment.newInstance()
                }

                R.id.nav_available_doctors -> {
//                    if (fragmentManager.backStackEntryCount >= 1) {
//                        fragmentManager.popBackStack()
//                        fragmentManager.beginTransaction().hide(activeFragment)
//                            .show(availableDoctorFragment).addToBackStack(null).commit()
//                    } else
//                        fragmentManager.beginTransaction().hide(activeFragment)
//                            .show(availableDoctorFragment).commit()
//                    activeFragment = availableDoctorFragment
                    fragment = AvailableDoctorsFragment.newInstance()
                }
            }
            loadFragment(fragment)
        }
    }

//    override fun onBackPressed() {
//        if (fragmentManager.backStackEntryCount >= 1) {
//            bottomNavigationView.selectedItemId = R.id.nav_appointments
//            fragmentManager.popBackStack()
//        } else {
//            super.onBackPressed()
//        }
//    }
}
