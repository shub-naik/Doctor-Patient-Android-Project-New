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

class AvailableDoctorTimingAdapter(
    private val context: Context,
    private val timingList: List<String>
) :
    RecyclerView.Adapter<AvailableDoctorTimingAdapter.ViewHolder>() {
    private var checkedPosition = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(context)
                .inflate(R.layout.available_doctor_timing_row_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = timingList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(timingList[position])
    }

    inner class ViewHolder(listItemView: View) : RecyclerView.ViewHolder(listItemView) {
        private var textView: TextView = listItemView.findViewById(R.id.availableTimingTxtView)
        private var imageView: ImageView = listItemView.findViewById(R.id.selectedItemImgView)

        fun bind(timing: String) {
            if (checkedPosition == -1) {
                imageView.visibility = View.GONE
                textView.setTextColor(ContextCompat.getColor(context,R.color.black))
            } else {
                if (checkedPosition == adapterPosition) {
                    imageView.visibility = View.VISIBLE
                    textView.setTextColor(ContextCompat.getColor(context, R.color.white))
                } else {
                    imageView.visibility = View.GONE
                    textView.setTextColor(ContextCompat.getColor(context,R.color.black))
                }
            }
            textView.text = timing
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

    fun getSelected(): String? {
        return if (checkedPosition != -1) {
            timingList[checkedPosition]
        } else null
    }

}