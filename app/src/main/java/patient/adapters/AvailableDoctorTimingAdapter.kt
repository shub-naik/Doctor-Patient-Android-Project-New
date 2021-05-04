package patient.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.shubham.doctorpatientandroidappnew.R
import java.time.LocalTime

class AvailableDoctorTimingAdapter(
    private val availableTimingList: List<LocalTime>
) :
    RecyclerView.Adapter<AvailableDoctorTimingAdapter.ViewHolder>() {

    private lateinit var context: Context
    private var checkedPosition = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        if (!this::context.isInitialized)
            context = parent.context
        val view =
            LayoutInflater.from(context)
                .inflate(R.layout.available_doctor_timing_row_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = availableTimingList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val pos = holder.adapterPosition
        holder.bind(availableTimingList[pos])
    }

    inner class ViewHolder(listItemView: View) : RecyclerView.ViewHolder(listItemView) {
        private var textView: TextView = listItemView.findViewById(R.id.availableTimingTxtView)
        private var imageView: ImageView = listItemView.findViewById(R.id.selectedItemImgView)

        fun bind(timing: LocalTime) {
            if (checkedPosition == -1) {
                imageView.visibility = View.GONE
                textView.setTextColor(ContextCompat.getColor(context, R.color.black))
            } else {
                if (checkedPosition == adapterPosition) {
                    imageView.visibility = View.VISIBLE
                    textView.setTextColor(ContextCompat.getColor(context, R.color.white))
                } else {
                    imageView.visibility = View.GONE
                    textView.setTextColor(ContextCompat.getColor(context, R.color.black))
                }
            }
            textView.text = timing.toString()

            itemView.setOnClickListener {
                imageView.visibility = View.VISIBLE
                textView.setTextColor(ContextCompat.getColor(context, R.color.white))
                if (checkedPosition != adapterPosition) {
                    notifyItemChanged(checkedPosition) // Used for Hiding Previous Selected Item
                    checkedPosition = adapterPosition
                }
            }
        }
    }

    fun getSelected(): LocalTime? {
        return if (checkedPosition != -1) {
            availableTimingList[checkedPosition]
        } else null
    }
}