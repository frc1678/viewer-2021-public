package com.example.viewer_2020

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.core.app.ActivityCompat
import com.example.viewer_2020.data.DatabaseReference

// Splash screen activity that waits for the data to pull from the MongoDB database until it
// begins the other Viewer activities. AKA once MainViewerActivity.databaseReference is not null,
// it will begin the actual viewer activity so ensure that all data is accessible before the viewer
// activity begins.
class MongoDatabaseStartupActivity : ViewerActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.mongodb_database_startup_splash_screen)
        supportActionBar?.hide()

        // Interface to access the DatabaseReference -> CompetitionObject object that
        // should be an exact replica of every WANTED data value from MongoDB.

        // 'response' is a CompetitionObject, so you should be able to access whatever datapoint
        // you want by referencing response. Example: response.raw.qr[0] -> specified value in database.
        // TODO Make not crash when permissions are denied.
        val callback: MongoDatabaseListenerUtil.Callback<DatabaseReference.CompetitionObject> = object :
            MongoDatabaseListenerUtil.Callback<DatabaseReference.CompetitionObject> {
            override fun execute(response: DatabaseReference.CompetitionObject) {
                //'response' is a CompetitionObject. Example: response.[raw].[qr][0] will return the specified value.
                MainViewerActivity.databaseReference = response
                startActivity(Intent(this@MongoDatabaseStartupActivity, MainViewerActivity::class.java))
            }
        }
        MongoDatabaseListenerUtil().getCompetitionInformation(callback)
//        MongoDatabaseListenerUtil().startDatabaseListener()
    }

    override fun onResume() {
        super.onResume()
        if ((ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) or ActivityCompat.checkSelfPermission(this, Manifest.permission.INTERNET))
            != PackageManager.PERMISSION_GRANTED
        ) {
            try {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.INTERNET
                    ),
                    100
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}