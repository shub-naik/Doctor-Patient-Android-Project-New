package application

import APPLICATION_TAG
import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import appDarkModeState
import com.shubham.databasemodule.Db
import daos.DoctorDao
import daos.PatientDao
import helperFunctions.getDarkModeSharedPreferences

class ApplicationClass : Application() {
    private val TAG = APPLICATION_TAG

    private val context: Context by lazy { this }
    val doctorDao: DoctorDao by lazy { Db.getInstance(context).doctorDao }
    val patientDao: PatientDao by lazy { Db.getInstance(context).patientDao }

    override fun onCreate() {
        super.onCreate()

        // Shared Preferences for Light-Dark Mode.
        val darkModeSharedPref = getDarkModeSharedPreferences(this)
        val isNightMode = darkModeSharedPref.getBoolean(appDarkModeState, false)
        if (isNightMode)
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        else
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        // Application Wide Portrait Mode
        registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
            override fun onActivityPaused(activity: Activity) {
            }

            override fun onActivityStarted(activity: Activity) {
            }

            override fun onActivityDestroyed(activity: Activity) {
            }

            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
            }

            override fun onActivityStopped(activity: Activity) {
            }

            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            }

            override fun onActivityResumed(activity: Activity) {
            }

        })
    }
}