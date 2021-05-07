package patient.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckedTextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.shubham.doctorpatientandroidappnew.databinding.FilterDoctorItemBinding
import diffUtilsRecyclerView.FilterDoctorDiffUtil
import models.FilterModel
import java.util.*

class FilterDoctorAdapter(private val filters: List<FilterModel>) :
    RecyclerView.Adapter<FilterDoctorAdapter.ViewHolder>() {
    private val checkedList = ArrayList<String>()
    private val oldList = arrayListOf<FilterModel>()

    fun getCheckedResult() = checkedList.toList()

    fun clearCheckedTextView() {
        checkedList.clear()
        oldList.clear()
        oldList.addAll(filters)
        filters.mapIndexed { index, element ->
            element.checkedStatus = false
            notifyItemChanged(index)
        }
//        updateDataSet(oldList, filters)
    }

    inner class ViewHolder(listItemView: View) : RecyclerView.ViewHolder(listItemView) {
        val binding = FilterDoctorItemBinding.bind(listItemView)
        private val checkedTxtView: CheckedTextView = binding.checkedTextView

//        override fun onClick(v: View) {
//            val adapterPosition = adapterPosition
//            if (!filters[adapterPosition].checkedStatus) {
//                checkedTxtView.setCheckMarkDrawable(R.drawable.ic_baseline_check_circle_24)
//                checkedList.add(checkedTxtView.text.toString())
//                checkedTxtView.isChecked = true
//                filters[adapterPosition].checkedStatus = true
//            } else {
//                checkedList.remove(checkedTxtView.text.toString())
//                checkedTxtView.isChecked = false
//                filters[adapterPosition].checkedStatus = false
//            }
//        }

        fun bind(position: Int) {
            checkedTxtView.isChecked = filters[position].checkedStatus
            checkedTxtView.text = filters[position].strText
            checkedTxtView.setOnClickListener {
                val adapterPosition = adapterPosition
                if (!filters[adapterPosition].checkedStatus) {
                    checkedList.add(checkedTxtView.text.toString())
                    checkedTxtView.isChecked = true
                    filters[adapterPosition].checkedStatus = true
                } else {
                    checkedList.remove(checkedTxtView.text.toString())
                    checkedTxtView.isChecked = false
                    filters[adapterPosition].checkedStatus = false
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return filters.size
    }

    override fun onBindViewHolder(holder: FilterDoctorAdapter.ViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FilterDoctorAdapter.ViewHolder {
        val binding = FilterDoctorItemBinding.inflate(LayoutInflater.from(parent.context))
        return ViewHolder(binding.root)
    }

//    private fun updateDataSet(oldList: List<FilterModel>, newList: List<FilterModel>) {
//        val filterDoctorDiffUtil = FilterDoctorDiffUtil(oldList, newList)
//        val diffResults = DiffUtil.calculateDiff(filterDoctorDiffUtil)
//        diffResults.dispatchUpdatesTo(this)
//    }
}