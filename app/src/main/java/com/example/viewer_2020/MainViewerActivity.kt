/*
* MainViewerActivity.kt
* viewer
*
* Created on 1/26/2020
* Copyright 2020 Citrus Circuits. All rights reserved.
*/

package com.example.viewer_2020

import android.os.Bundle
import android.view.MenuItem
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.viewer_2020.data.*
import kotlinx.android.synthetic.main.activity_main.*

// Main activity class that handles the dual fragment view.
class MainViewerActivity : ViewerActivity() {

    companion object {
        var currentRankingMenuItem: MenuItem? = null
        var databaseReference: DatabaseReference.CompetitionObject? = null
        var teamCache: HashMap<String, Team> = HashMap()
        var matchCache: HashMap<String, Match> = HashMap()
    }

    // Populates the menu items and fragment items with the corresponding fragment IDs.
    private fun setupNavigationController(host: Int) {
        // This is where you put the main activity's menu options. The ranking navigation bar
        // does not have a navigation controller connected to a fragment because its only usage
        // is to return a position value to set the correct collection of data for the ranking adapter.
        val appBarConfiguration = AppBarConfiguration(setOf(
            R.id.navigation_match_schedule, R.id.navigation_ranking))
        setupActionBarWithNavController(findNavController(host), appBarConfiguration)
        nav_view.setupWithNavController(findNavController(host))
    }

    // Override the onBackPressed to disable the back button as everything is inside fragments.
    override fun onBackPressed() { }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()
        setupNavigationController(R.id.nav_host_fragment)
    }
}