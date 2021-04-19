package SampleTestingProject

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ViewModelClass : ViewModel() {
    private val tempList = ArrayList<Int>()
    val list: MutableLiveData<ArrayList<Int>> by lazy {
        MutableLiveData<ArrayList<Int>>()
    }

//    fun initializeData() {
//        for (i in 1..200) {
//            tempList.add(i)
//        }
//        list.value = tempList
//    }

    fun addMoreData() {
        for (i in tempList.size until tempList.size + 10) {
            tempList.add(i)
        }
        list.value = tempList
    }
}