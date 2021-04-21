package patient.adapters

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import patient.PatientLoginFragment
import patient.PatientSignupFragment

class LoginSignUpViewPagerAdapter(
    fragmentManager: FragmentManager
) : FragmentPagerAdapter(fragmentManager) {

    private val tabTitles = arrayOf("Patient Login", "Patient SignUp")

    override fun getPageTitle(position: Int): CharSequence? = tabTitles[position]

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> {
                Log.e("Pos", "getItem: 1")
                PatientLoginFragment()
            }
            1 -> {
                Log.e("Pos", "getItem: 2")
                PatientSignupFragment()
            }
            else -> PatientLoginFragment()
        }
    }

    override fun getCount(): Int = tabTitles.size
}