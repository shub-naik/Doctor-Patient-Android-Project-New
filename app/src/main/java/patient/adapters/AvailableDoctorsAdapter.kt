package patient.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.shubham.doctorpatientandroidappnew.R
import com.shubham.doctorpatientandroidappnew.databinding.AvailableDoctorListItemBinding
import diffUtilsRecyclerView.AvailableDoctorDiffUtil
import models.Doctor
import patient.interfaces.AvailableDoctorItemInterface

class AvailableDoctorsAdapter(private val availableDoctorItemInterface: AvailableDoctorItemInterface) :
    RecyclerView.Adapter<AvailableDoctorsAdapter.ViewHolder>(), Filterable {
    private var availableDoctorFilterList = mutableListOf<Doctor>()
    private var availableDoctorsList = mutableListOf<Doctor>()

    fun getAvailableDoctorsList() = availableDoctorFilterList.toList()

    inner class ViewHolder(listItemView: View) : RecyclerView.ViewHolder(listItemView) {
        val doctorProfileImageView: ImageView =
            listItemView.findViewById(R.id.availableDoctorItemImgView)
        val doctorUsernameTxtView: TextView =
            listItemView.findViewById(R.id.availableDoctorItemUserName)
        val doctorDegreeTxtView: TextView =
            listItemView.findViewById(R.id.availableDoctorItemDegree)
        val doctorVideoCallBtn: ImageButton =
            listItemView.findViewById(R.id.availableDoctorItemVideoCallBtn)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = AvailableDoctorListItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(
            binding.root
        )
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val adapterPos = viewHolder.adapterPosition
        val availableDoctor: Doctor = availableDoctorFilterList[adapterPos]

        viewHolder.doctorUsernameTxtView.text = availableDoctor.personName
        viewHolder.doctorDegreeTxtView.text = availableDoctor.doctorDegreeList.joinToString(",")

        viewHolder.doctorVideoCallBtn.setOnClickListener {
            availableDoctorItemInterface.onItemClick(availableDoctor)
        }
    }

    override fun getItemCount(): Int = availableDoctorFilterList.size

    // Search Filter in Recycler View.
    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): FilterResults {
                val charString = charSequence.toString()
                availableDoctorFilterList = if (charString.isEmpty())
                    availableDoctorsList
                else {
                    val filteredList: MutableList<Doctor> = ArrayList()
                    for (doctor in availableDoctorsList)
                        if (doctor.personName.toLowerCase().contains(charString.toLowerCase()))
                            filteredList.add(doctor)
                    filteredList
                }

                val filterResults = FilterResults()
                filterResults.values = availableDoctorFilterList
                return filterResults
            }

            override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {
                val l = filterResults.values
                if (l != null)
                    availableDoctorFilterList = l as MutableList<Doctor>
                notifyDataSetChanged()
//                searchedAdapterData(availableDoctorFilterList)
            }
        }
    }

    // Recycler View Set Data
    fun setAvailableDoctorsAdapterData(availableDoctorList: List<Doctor>) {
        this.availableDoctorsList = availableDoctorList.toMutableList()
        val availableDoctorDiffUtil =
            AvailableDoctorDiffUtil(this.availableDoctorsList, availableDoctorFilterList)
        val diffResults = DiffUtil.calculateDiff(availableDoctorDiffUtil)
        availableDoctorFilterList = this.availableDoctorsList
        diffResults.dispatchUpdatesTo(this)
    }

    fun searchedAdapterData(list: List<Doctor>) {
        try {
            val availableDoctorDiffUtil = AvailableDoctorDiffUtil(availableDoctorsList, list)
            val diffResults = DiffUtil.calculateDiff(availableDoctorDiffUtil)
            availableDoctorFilterList = list.toMutableList()
            diffResults.dispatchUpdatesTo(this)
        } catch (exception: IndexOutOfBoundsException) {

        }
    }
}