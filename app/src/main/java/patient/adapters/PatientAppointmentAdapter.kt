package patient.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.shubham.doctorpatientandroidappnew.R
import com.shubham.doctorpatientandroidappnew.databinding.AppointmentDetailRowItemBinding
import models.Appointment

class PatientAppointmentAdapter(
    private val appointmentList: List<Appointment>,
    private val pastOrUpcomingStatus: Boolean = false // Past means false and Upcoming means true
) :
    RecyclerView.Adapter<PatientAppointmentAdapter.ViewHolder>() {
    private lateinit var context: Context

    inner class ViewHolder(listItemView: View) : RecyclerView.ViewHolder(listItemView) {
        val binding = AppointmentDetailRowItemBinding.bind(listItemView)
        val imgView: ImageView = binding.appointmentTypeImgView
        val docNameTxtView: TextView = binding.appointmentDoctorName
        val dateTimeTxtView: TextView = binding.appointmentDateTime
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        if (!this::context.isInitialized)
            context = parent.context
        val binding = AppointmentDetailRowItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(
            binding.root
        )
    }

    override fun getItemCount(): Int = appointmentList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val adapterPos = holder.adapterPosition
        val currentAppointment = appointmentList[adapterPos]
        if (pastOrUpcomingStatus)
            holder.imgView.setImageResource(R.drawable.ic_baseline_access_time_24)
        else
            holder.imgView.setImageResource(R.drawable.ic_baseline_history_24)
        holder.docNameTxtView.text = context.resources.getString(
            R.string.doctor_name,
            currentAppointment.appointmentDetails.doctor.personName
        )
        holder.dateTimeTxtView.text =
            context.getString(
                R.string.date_time_of_appointment,
                currentAppointment.appointmentDetails.dateOfAppointment.toString(),
                currentAppointment.appointmentDetails.timeOfAppointment.toString()
            )
    }
}