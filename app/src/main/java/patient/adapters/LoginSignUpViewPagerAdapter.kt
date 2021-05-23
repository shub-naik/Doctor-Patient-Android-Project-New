package patient.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import daos.PatientDao
import patient.PatientLoginFragment
import patient.PatientSignupFragment

class LoginSignUpViewPagerAdapter(
    fragmentManager: FragmentManager
) : FragmentPagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private val tabTitles = arrayOf("Patient Login", "Patient SignUp")

    override fun getPageTitle(position: Int): CharSequence? = tabTitles[position]

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> {
                PatientLoginFragment.newInstance()
            }
            1 -> {
                PatientSignupFragment.newInstance()
            }
            else -> PatientLoginFragment.newInstance()
        }
    }

    override fun getCount(): Int = tabTitles.size
}