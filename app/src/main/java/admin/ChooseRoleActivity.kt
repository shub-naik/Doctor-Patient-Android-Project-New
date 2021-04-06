package admin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.shubham.doctorpatientandroidappnew.R
import com.shubham.doctorpatientandroidappnew.databinding.ActivityChooseRoleBinding

class ChooseRoleActivity : AppCompatActivity() {
    private lateinit var binding:ActivityChooseRoleBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_role)

        binding = ActivityChooseRoleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.registerDoctorBtn.setOnClickListener {
            Log.e("DoctorRoleFound", "Doctor Role Found")
            val intent = Intent(this, RegisterRoleActivity::class.java)
            intent.putExtra("register_type", "doctor")
            startActivity(intent)
        }

        binding.registerPatientBtn.setOnClickListener {
            Log.e("UserRoleFound", "User Patient Role Found")
            val intent = Intent(this, RegisterRoleActivity::class.java)
            intent.putExtra("register_type", "patient")
            startActivity(intent)
        }

    }
}