package com.example.viewer_2020

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.viewer_2020.data.DatabaseReference
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.mongodb_database_startup_splash_screen.*

// Splash screen activity that waits for the data to pull from the MongoDB database until it
// begins the other Viewer activities. AKA once MainViewerActivity.databaseReference is not null,
// it will begin the actual viewer activity so ensure that all data is accessible before the viewer
// activity begins.
class MongoDatabaseStartupActivity : ViewerActivity() {
    var buttonClickable = false
    companion object {
        var databaseReference: DatabaseReference.CompetitionObject? =
            DatabaseReference.CompetitionObject()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.mongodb_database_startup_splash_screen)
        supportActionBar?.hide()

        // Interface to access the DatabaseReference -> CompetitionObject object that
        // should be an exact replica of every WANTED data value from MongoDB.

        // 'response' is a CompetitionObject, so you should be able to access whatever datapoint
        // you want by referencing response. Example: response.raw.qr[0] -> specified value in database.
        // TODO Make not crash when permissions are denied.

        getData()
    }

    override fun onResume() {
        super.onResume()
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.INTERNET)
            != PackageManager.PERMISSION_GRANTED
        ) {
            try {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(

                        Manifest.permission.INTERNET
                    ),
                    100
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    private fun getData(){
        buttonClickable = false
        GetDataFromWebsite({
            ContextCompat.startActivity(this, Intent(this, MainViewerActivity::class.java), null)

        }) {
            Log.e("error", it)
            runOnUiThread {
                // Stuff that updates the UI
                Snackbar.make(splash_screen_layout, "Data Failed to load", 2500).show()
                refresh_button.visibility = View.VISIBLE
                refresh_button.isEnabled = true
                buttonClickable = true
            }

        }.execute()

    }

    fun refreshClick(view: View) {
        if(buttonClickable){
            Snackbar.make(splash_screen_layout, "Refreshing Data", 2500).show()
            refresh_button.isEnabled = false
            getData()
        }

    }
}