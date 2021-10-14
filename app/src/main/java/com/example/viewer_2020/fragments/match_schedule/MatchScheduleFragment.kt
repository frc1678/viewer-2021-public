/*
* MatchScheduleFragment.kt
* viewer
*
* Created on 1/26/2020
* Copyright 2020 Citrus Circuits. All rights reserved.
*/

package com.example.viewer_2020

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.viewer_2020.constants.Constants
import com.example.viewer_2020.data.Match
import com.example.viewer_2020.fragments.match_schedule.MatchScheduleListAdapter
import com.example.viewer_2020.fragments.match_schedule.match_details.MatchDetailsFragment
import kotlinx.android.synthetic.main.fragment_match_schedule.view.*
import kotlinx.android.synthetic.main.team_details.view.*
import android.util.Log

//The fragment of the match schedule 'view' that is one of the options of the navigation bar.
class MatchScheduleFragment : Fragment() {

    private val matchDetailsFragment = MatchDetailsFragment()
    private val matchDetailsFragmentArguments = Bundle()
    private var scheduleSelected : String? = null
    private var csvFile : MutableList<String> = csvFileRead("match_schedule.csv", false)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_match_schedule, container, false)
        scheduleSelected = arguments?.getString("selection")

        updateMatchScheduleListView(root, "$scheduleSelected")

        val matchDetailsFragmentTransaction = this.fragmentManager!!.beginTransaction()
        // When an item click occurs, go to the MatchDetails fragment of the match item clicked.

        root.lv_match_schedule.setOnItemClickListener { _, _, position, _ ->
            when (scheduleSelected) {
                "Our Schedule" -> {
                    matchDetailsFragmentArguments.putInt(Constants.MATCH_NUMBER,
                        csvFile.filter { it.matches(Regex(".*[B|R]-${Constants.MY_TEAM_NUMBER}( .*)?")) }
                            .sortedBy { it.trim().split(" ")[0].toInt() } [position]
                            .trim().split(" ")[0].toInt())
                }
                "Match Schedule" -> {
                    matchDetailsFragmentArguments.putInt(Constants.MATCH_NUMBER, position + 1)
                }
            }
            matchDetailsFragment.arguments = matchDetailsFragmentArguments
            matchDetailsFragmentTransaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
            matchDetailsFragmentTransaction.addToBackStack(null).replace(
                (view!!.parent as ViewGroup).id,
                matchDetailsFragment
            ).commit()
        }
        return root
    }

    private fun updateMatchScheduleListView(root: View, scheduleSelected: String) {
        root.lv_match_schedule.adapter =
            MatchScheduleListAdapter(
                activity!!,
                (convertMatchScheduleListToMap(
                    csvFile,
                    isFiltered = false,
                    matchNumber = null
                )!!
                        ),
                scheduleSelected
            )
    }
}