package SampleTestingProject

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.shubham.doctorpatientandroidappnew.databinding.ActivityConstraintlayout2Binding

class Constraintlayout2 : AppCompatActivity() {
    private lateinit var binding: ActivityConstraintlayout2Binding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConstraintlayout2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.ToActivity3.setOnClickListener {
//            startActivity(Intent(this, Constraintlayout3::class.java))
//            finish()
//            val callIntent = Intent(Intent.ACTION_DIAL)
//            callIntent.data = Uri.parse("tel:" + 8104076494) //change the number
//
//            startActivity(callIntent)
//            val sendIntent = Intent().apply {
//                action = Intent.ACTION_SENDTO
//                putExtra(Intent.EXTRA_TEXT, "This is my text to send.")
//                type = "text/plain"
//            }
//
//            val shareIntent = Intent.createChooser(sendIntent, null)
//            startActivity(shareIntent)
//            val uriText = "mailto:naikshub8412@gmail.com" +
//                    "?subject=" + Uri.encode("some subject text here") +
//                    "&body=" + Uri.encode("some text here")
//
//            val uri: Uri = Uri.parse(uriText)

            val uriText = "mailto:naikshub8412@gmail.com" +
                    "?subject=" + Uri.encode("some subject text here") +
                    "&body=" + Uri.encode("some text here")

            val uri = Uri.parse(uriText)

            val sendIntent = Intent(Intent.ACTION_SENDTO)
            sendIntent.data = uri
            startActivity(Intent.createChooser(sendIntent, "Send email"))
        }

        Log.e("ConstraintL2", "onCreate: again once ")

//        when {
//            intent?.action == Intent.ACTION_SEND -> {
//                if ("text/plain" == intent.type) {
//                    handleSendText(intent) // Handle text being sent
//                } else if (intent.type?.startsWith("image/") == true) {
//                    handleSendImage(intent) // Handle single image being sent
//                }
//            }
//            intent?.action == Intent.ACTION_SEND_MULTIPLE
//                    && intent.type?.startsWith("image/") == true -> {
//                handleSendMultipleImages(intent) // Handle multiple images being sent
//            }
//            else -> {
//                // Handle other intents, such as being started from the home screen
//            }
//        }
//
//        binding.btnThemeGroup.addOnButtonCheckedListener { _, checkedId, isChecked ->
//            if (isChecked) {
//                val theme = when (checkedId) {
//                    binding.BtnDefault.id -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
//                    binding.BtnDark.id -> AppCompatDelegate.MODE_NIGHT_YES
//                    else -> AppCompatDelegate.MODE_NIGHT_NO
//                }
//                AppCompatDelegate.setDefaultNightMode(theme)
//            }
//        }
//
//        findViewById<Button>(R.id.ToActivity3).setOnClickListener {
//            val intent = Intent(this, Constraintlayout3::class.java)
//            startActivity(intent)
//        }

    }
//
//    override fun onPause() {
//        super.onPause()
//        Log.e("ConstraintLayoutAct2", "onPause: onPause once")
//
//    }
//
//    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
//        super.onSaveInstanceState(outState, outPersistentState)
//        Log.e("ConstraintLayoutAct2", "onSaveInstanceState: onSaveInstanceState once")
//    }

    override fun onStop() {
        super.onStop()
        Log.e("ConstraintL2", "onStop: called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e("ConstraintL2", "onDestroy: called")
    }

}