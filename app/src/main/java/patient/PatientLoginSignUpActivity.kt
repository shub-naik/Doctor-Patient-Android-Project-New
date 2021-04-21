package patient

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.shubham.doctorpatientandroidappnew.MainActivity
import com.shubham.doctorpatientandroidappnew.databinding.ActivityPatientLoginSignUpBinding
import doctorPatientCommon.DoctorLoginActivity
import patient.adapters.LoginSignUpViewPagerAdapter

class PatientLoginSignUpActivity : AppCompatActivity() {
    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager
    private lateinit var binding: ActivityPatientLoginSignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPatientLoginSignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        handleListeners()

        viewPager = binding.PatientLoginSignUpViewPager

        tabLayout = binding.PatientLoginSignUpTabLayout
        tabLayout.tabGravity = TabLayout.GRAVITY_FILL

        val loginSignUpViewPagerAdapter: LoginSignUpViewPagerAdapter =
            LoginSignUpViewPagerAdapter(supportFragmentManager)
        viewPager.adapter = loginSignUpViewPagerAdapter

        tabLayout.setupWithViewPager(viewPager)
        viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))

    }

    private fun handleListeners() {
        binding.AdminLoginTxtView.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        binding.DoctorLoginTxtView.setOnClickListener {
            startActivity(Intent(this, DoctorLoginActivity::class.java))
        }
    }
}