package patient

import PATIENT_BOOKING_DETAIL_TITLE
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.shubham.doctorpatientandroidappnew.databinding.ActivityPatientAppointmentHistoryBinding
import helperFunctions.getSupportActionBarView
import patient.adapters.AppointmentDetailViewPagerAdapter

class PatientAppointmentHistoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPatientAppointmentHistoryBinding
    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPatientAppointmentHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        this.getSupportActionBarView(PATIENT_BOOKING_DETAIL_TITLE)

        // View Pager for Past and Upcoming Details
        viewPager = binding.PatientAppointmentDetailsViewPager

        tabLayout = binding.PatientAppointmentDetailsTabLayout
        tabLayout.tabGravity = TabLayout.GRAVITY_FILL

        val appointmentViewPagerAdapter = AppointmentDetailViewPagerAdapter(supportFragmentManager)
        viewPager.adapter = appointmentViewPagerAdapter

        tabLayout.setupWithViewPager(viewPager)
        viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean =
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
}