package patient.adapters

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.shubham.doctorpatientandroidappnew.R
import com.shubham.doctorpatientandroidappnew.databinding.AppointmentDateItemBinding
import com.shubham.doctorpatientandroidappnew.databinding.AppointmentDetailRowItemBinding
import java.time.LocalDate

class AppointmentHistoryAdapter(
    private val appointmentListItems: List<AppointmentListItem>,
    private val context: Context
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when (viewType) {
            0 -> {
                val binding = AppointmentDateItemBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
                return DateItemViewHolder(binding)
            }
            1 -> {
                val binding = AppointmentDetailRowItemBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
                return AppointmentDetailsItemViewHolder(binding)
            }
            else -> {
                val binding = AppointmentDateItemBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
                return DateItemViewHolder(binding)
            }
        }
    }

    override fun getItemViewType(position: Int): Int = appointmentListItems[position].getType()

    override fun getItemCount(): Int = appointmentListItems.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            0 -> {
                val appointmentDateItem: AppointmentDateItem =
                    appointmentListItems[position] as AppointmentDateItem
                val dateItemViewHolder: DateItemViewHolder = holder as DateItemViewHolder
                dateItemViewHolder.dateTextView.text =
                    appointmentDateItem.appointmentDate.dateOfAppointment
            }
            1 -> {
                val appointmentDetailItem: AppointmentDetailItem =
                    appointmentListItems[position] as AppointmentDetailItem
                val appointmentDetailItemViewHolder: AppointmentDetailsItemViewHolder =
                    holder as AppointmentDetailsItemViewHolder

                if (if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        LocalDate.now() > appointmentDetailItem.appointmentDetails.dateOfAppointment
                    } else {
                        TODO("VERSION.SDK_INT < O")
                    }
                ) {
                    holder.appointmentHistoryImgView.setImageResource(R.drawable.ic_baseline_history_24)
                    holder.appointmentHistoryCardView.setBackgroundColor(
                        ContextCompat.getColor(
                            context,
                            R.color.disabled
                        )
                    )
                } else
                    holder.appointmentHistoryImgView.setImageResource(R.drawable.ic_baseline_access_time_24)

                appointmentDetailItemViewHolder.doctorUsernameTextView.text =
                    appointmentDetailItem.appointmentDetails.doctor.personName
                appointmentDetailItemViewHolder.appointmentTimeTxtView.append(
                    appointmentDetailItem.appointmentDetails.timeOfAppointment
                )
            }
        }
    }

    inner class DateItemViewHolder(appointmentDateItemBinding: AppointmentDateItemBinding) :
        RecyclerView.ViewHolder(appointmentDateItemBinding.root) {
        val dateTextView: TextView = appointmentDateItemBinding.DateOfAppointmentHistoryRowTxtView
    }

    inner class AppointmentDetailsItemViewHolder(appointmentDetailRowItemBinding: AppointmentDetailRowItemBinding) :
        RecyclerView.ViewHolder(appointmentDetailRowItemBinding.root) {
        val appointmentHistoryCardView: CardView =
            appointmentDetailRowItemBinding.AppointmentHistoryCardBackground
        val appointmentHistoryImgView: ImageView =
            appointmentDetailRowItemBinding.appointmentHistoryImgView
        val doctorUsernameTextView: TextView =
            appointmentDetailRowItemBinding.appointmentHistoryDoctorName
        val appointmentTimeTxtView: TextView =
            appointmentDetailRowItemBinding.appointmentHistoryTime
    }
}