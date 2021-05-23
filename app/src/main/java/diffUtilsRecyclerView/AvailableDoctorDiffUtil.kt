package diffUtilsRecyclerView

import androidx.recyclerview.widget.DiffUtil
import entities.DoctorEnt
import models.Doctor

class AvailableDoctorDiffUtil(
    private val oldList: List<Doctor>,
    private val newList: List<Doctor>
) :
    DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldList[oldItemPosition].doctorId == newList[newItemPosition].doctorId

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldList[oldItemPosition].equals(newList[newItemPosition])
}