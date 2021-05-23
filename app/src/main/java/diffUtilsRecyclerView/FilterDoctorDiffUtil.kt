package diffUtilsRecyclerView

import androidx.recyclerview.widget.DiffUtil
import models.FilterModel

class FilterDoctorDiffUtil(
    private val oldList: List<FilterModel>,
    private val newList: List<FilterModel>
) :
    DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldList[oldItemPosition].strText == newList[newItemPosition].strText

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]
        return oldItem == newItem
    }
}