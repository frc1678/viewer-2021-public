package com.example.viewer_2020

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import java.io.File

// First activity to run. Shows a blank screen so it can ask permissions without crashing.
class ViewerStartupActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.viewer_startup_activity)
    }

    override fun onResume() {
        super.onResume()

        // Requests storage read permissions until you accept them
        // (in Android 11, it will only ask twice)
        if ((ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED)
        ) {
            try {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    ),
                    100
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        // Gives an alert bubble when file(s) are missing and specifies the missing file(s)
        while (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
            val matchScheduleCSV =
                File("/storage/emulated/0/${Environment.DIRECTORY_DOWNLOADS}/match_schedule.csv")
            val teamListCSV = File("/storage/emulated/0/${Environment.DIRECTORY_DOWNLOADS}/team_list.csv")
            if (matchScheduleCSV.exists() && teamListCSV.exists()) {
                startActivity(Intent(this@ViewerStartupActivity, MongoDatabaseStartupActivity::class.java))
                return
            } else if (teamListCSV.exists()) {
                AlertDialog.Builder(this).setMessage("You are missing the match_schedule CSV").show()
                return
            } else if (matchScheduleCSV.exists()) {
                AlertDialog.Builder(this).setMessage("You are missing the team_list CSV").show()
                return
            } else {
                AlertDialog.Builder(this).setMessage("You are missing the match_schedule and team_list CSVs").show()
                return
            }
        }
    }
}