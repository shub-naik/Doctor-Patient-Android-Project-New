package patient.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.shubham.doctorpatientandroidappnew.R
import com.shubham.doctorpatientandroidappnew.databinding.AppointmentRowItemBinding
import models.Appointment

class AppointmentHistoryAdapter(private val appointmentList: List<Appointment>) :
    RecyclerView.Adapter<AppointmentHistoryAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AppointmentHistoryAdapter.ViewHolder {
        val binding = AppointmentRowItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.doctorUsername.text = appointmentList[position].doctor.personName
        holder.appointmentDateTxtView.text = appointmentList[position].dateOfAppointment.toString()
        holder.appointmentTimeTxtView.text = appointmentList[position].timeOfAppointment
    }

    override fun getItemCount(): Int = appointmentList.size

    inner class ViewHolder(listItemView: View) : RecyclerView.ViewHolder(listItemView) {
        val doctorUsername: TextView = listItemView.findViewById(R.id.appointmentHistoryDoctorName)
        val appointmentDateTxtView: TextView =
            listItemView.findViewById(R.id.appointmentHistoryDate)
        val appointmentTimeTxtView: TextView =
            listItemView.findViewById(R.id.appointmentHistoryTime)
    }
}