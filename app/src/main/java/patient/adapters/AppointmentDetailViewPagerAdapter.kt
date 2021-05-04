package patient.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import patient.PatientPastBookingFragment
import patient.PatientUpcomingBookingFragment

class AppointmentDetailViewPagerAdapter(fragmentManager: FragmentManager) :
    FragmentPagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    private val tabTitles = arrayOf("Upcoming", "Past")

    override fun getPageTitle(position: Int): CharSequence? = tabTitles[position]

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> {
                PatientUpcomingBookingFragment()
            }
            1 -> {
                PatientPastBookingFragment()
            }
            else -> PatientUpcomingBookingFragment()
        }
    }

    override fun getCount(): Int = tabTitles.size
}