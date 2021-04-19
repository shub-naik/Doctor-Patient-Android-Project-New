package SampleTestingProject

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.shubham.doctorpatientandroidappnew.databinding.ActivityConstraintLayoutBinding


class ConstraintLayoutActivity : AppCompatActivity() {

    private lateinit var binding: ActivityConstraintLayoutBinding
    private lateinit var viewModelClass: ViewModelClass
    private val l = ArrayList<Int>()
    private val TAG = "ConstraintLayoutAct"

    override fun onCreate(savedInstanceState: Bundle?) {
//        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES) // lifecycle methods are called only once
//        delegate.localNightMode = AppCompatDelegate.MODE_NIGHT_YES // lifecycle methods are called twice

        super.onCreate(savedInstanceState)
        binding = ActivityConstraintLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.e(TAG, "ConstraintLayoutActivity: ConstraintLayoutActivity Created Again")
        binding.ToActivity2.setOnClickListener {
            startActivity(Intent(this, Constraintlayout2::class.java))
        }

//        viewModelClass = ViewModelProvider(this).get(ViewModelClass::class.java)
//
//        viewModelClass.list.observe(this, Observer {
//            l.addAll(it)
//        })
//
//
//        binding.addDataBtn.setOnClickListener {
//            viewModelClass.addMoreData()
//        }
    }

    override fun onPause() {
        super.onPause()
        Log.e("ConstraintLayoutAct", "onPause: onPause once")
    }

    override fun onStop() {
        super.onStop()
        Log.e("ConstraintLayoutAct", "onStop: onStop once")
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        Log.e("ConstraintLayoutAct", "onSaveInstanceState: onSaveInstanceState once")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e("ConstraintLayoutAct", "onDestroy: onDestroy once")
    }
}