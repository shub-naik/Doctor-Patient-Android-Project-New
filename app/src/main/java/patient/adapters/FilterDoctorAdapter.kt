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

class FilterDoctorAdapter(
    private val filters: MutableList<FilterModel>,
    private val oldList: List<FilterModel>
) :
    RecyclerView.Adapter<FilterDoctorAdapter.ViewHolder>() {
    private val checkedList = ArrayList<String>()

    fun clearCheckedTextView() {
        checkedList.clear()
        // Method 1
        filters.mapIndexed { index, filterModel ->
            if (filterModel.checkedStatus) {
                filterModel.checkedStatus = false
                notifyItemChanged(index)
            } else {
                filterModel.checkedStatus = false
            }
        }

        // Method 2
        updateDataSet(filters, oldList)
    }

    private fun updateDataSet(oldList: List<FilterModel>, newList: List<FilterModel>) {
        val filterDoctorDiffUtil = FilterDoctorDiffUtil(oldList, newList)
        val diffResults = DiffUtil.calculateDiff(filterDoctorDiffUtil)
        filters.clear()
        filters.addAll(newList)
        diffResults.dispatchUpdatesTo(this)
    }

    fun getCheckedResult() = checkedList.toList()

    override fun getItemCount(): Int {
        return filters.size
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FilterDoctorAdapter.ViewHolder {
        val binding = FilterDoctorItemBinding.inflate(LayoutInflater.from(parent.context))
        return ViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: FilterDoctorAdapter.ViewHolder, position: Int) {
        holder.bind(holder.adapterPosition)
    }

    inner class ViewHolder(listItemView: View) : RecyclerView.ViewHolder(listItemView) {
        val binding = FilterDoctorItemBinding.bind(listItemView)
        private val checkedTxtView: CheckedTextView = binding.checkedTextView

        fun bind(position: Int) {
            val checkStatus = filters[position].checkedStatus
            val checkText = filters[position].strText
            checkedTxtView.isChecked = checkStatus
            checkedTxtView.text = checkText
            checkedTxtView.setOnClickListener {
                if (!checkStatus) {
                    checkedList.add(checkText)
                    checkedTxtView.isChecked = true
                    filters[position].checkedStatus = true
                } else {
                    checkedList.remove(checkText)
                    checkedTxtView.isChecked = false
                    filters[position].checkedStatus = false
                }
            }
        }
    }
}