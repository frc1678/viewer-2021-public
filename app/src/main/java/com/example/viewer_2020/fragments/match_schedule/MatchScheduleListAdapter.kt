/*
* MatchScheduleListAdapter.kt
* viewer
*
* Created on 1/26/2020
* Copyright 2020 Citrus Circuits. All rights reserved.
*/

package com.example.viewer_2020.fragments.match_schedule

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.viewer_2020.*
import com.example.viewer_2020.data.Match
import com.example.viewer_2020.constants.Constants
import com.example.viewer_2020.R
import kotlinx.android.synthetic.main.match_details_cell.view.*
import java.lang.Float.parseFloat

// Custom list adapter class with Match object handling to display the custom cell for the match schedule.
class MatchScheduleListAdapter(
    private val context: Context,
    private var matchContents: Map<String, Match>,
    private val ourSchedule: Boolean
) : BaseAdapter() {



    private val inflater = LayoutInflater.from(context)

    // Return the size of the match schedule.
    override fun getCount(): Int {
        return matchContents.size
    }

    // Return the Match object given the match number.
    override fun getItem(position: Int): Match? {
        return if (ourSchedule)
            matchContents[matchContents.keys.toList()[position]]
        else matchContents[(position + 1).toString()]
    }

    // Return the position of the cell.
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    // Populate the elements of the custom cell.
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        Log.e("matchContents", matchContents["1"]?.redTeams.toString())

        val viewHolder: ViewHolder
        val rowView: View?
        val matchNumber: String =
            if (ourSchedule) matchContents.keys.toList()[position]
            else (position + 1).toString()


        if (convertView == null) {
            rowView = inflater.inflate(R.layout.match_schedule_cell, parent, false)
            viewHolder =
                ViewHolder(
                    rowView
                )
            rowView.tag = viewHolder
        } else {
            rowView = convertView
            viewHolder = rowView.tag as ViewHolder
        }

        for (tv in listOf(
            viewHolder.tvRedTeamOne,
            viewHolder.tvRedTeamTwo,
            viewHolder.tvRedTeamThree
        )) {

            tv.text = matchContents[matchNumber]!!.redTeams[listOf(
                        viewHolder.tvRedTeamOne,
                        viewHolder.tvRedTeamTwo,
                        viewHolder.tvRedTeamThree
                    ).indexOf(tv)]
        }
        for (tv in listOf(
            viewHolder.tvBlueTeamOne,
            viewHolder.tvBlueTeamTwo,
            viewHolder.tvBlueTeamThree
        )) {
            tv.text = matchContents[matchNumber]!!.blueTeams[0 +
                    listOf(
                        viewHolder.tvBlueTeamOne,
                        viewHolder.tvBlueTeamTwo,
                        viewHolder.tvBlueTeamThree
                    ).indexOf(tv)]
        }
        viewHolder.tvMatchNumber.text = matchNumber

        if (MainViewerActivity.matchCache[matchNumber]!!.bluePredictedScore != null) {
            viewHolder.tvBluePredictedScore.text =
                MainViewerActivity.matchCache[matchNumber]!!.bluePredictedScore.toString()
        } else {
            val value = getAllianceInMatchObjectByKey(
                Constants.PROCESSED_OBJECT.CALCULATED_PREDICTED_ALLIANCE_IN_MATCH.value,
                Constants.BLUE, matchNumber, "predicted_score"
            )
            if (value != Constants.NULL_CHARACTER) {
                viewHolder.tvBluePredictedScore.text =
                    parseFloat(("%.2f").format(value.toFloat())).toString()
                MainViewerActivity.matchCache[matchNumber]!!.bluePredictedScore =
                    parseFloat(("%.2f").format(value.toFloat()))
            } else {
                viewHolder.tvBluePredictedScore.text =
                    Constants.NULL_PREDICTED_SCORE_CHARACTER
            }
        }

        if (MainViewerActivity.matchCache[matchNumber]!!.redPredictedScore != null) {
            viewHolder.tvRedPredictedScore.text =
                MainViewerActivity.matchCache[matchNumber]!!.redPredictedScore.toString()
        } else {
            val value = getAllianceInMatchObjectByKey(
                Constants.PROCESSED_OBJECT.CALCULATED_PREDICTED_ALLIANCE_IN_MATCH.value,
                Constants.RED, matchNumber, "predicted_score"
            )
            if (value != Constants.NULL_CHARACTER) {
                viewHolder.tvRedPredictedScore.text =
                    parseFloat(("%.2f").format(value.toFloat())).toString()
                MainViewerActivity.matchCache[matchNumber]!!.redPredictedScore =
                    parseFloat(("%.2f").format(value.toFloat()))
            } else {
                viewHolder.tvRedPredictedScore.text =
                    Constants.NULL_PREDICTED_SCORE_CHARACTER
            }
        }

        for (tv in listOf(viewHolder.tvBluePredictedScore, viewHolder.tvRedPredictedScore)) {
            if (tv.text == Constants.NULL_PREDICTED_SCORE_CHARACTER) {
                tv.setTextColor(
                    ContextCompat.getColor(
                        context,
                        R.color.ElectricGreen
                    )
                )
            } else {
                tv.setTextColor(
                    ContextCompat.getColor(
                        context,
                        R.color.Black
                    )
                )
            }
        }
        red_predicted@ for (tv in listOf(
            viewHolder.tvRedPredictedRPOne,
            viewHolder.tvRedPredictedRPTwo
        )) {
            when (listOf(
                viewHolder.tvRedPredictedRPOne,
                viewHolder.tvRedPredictedRPTwo
            ).indexOf(tv)) {
                0 -> {
                    if (MainViewerActivity.matchCache[matchNumber]!!.redPredictedRPOne != null &&
                        MainViewerActivity.matchCache[matchNumber]!!.redPredictedRPOne!!.toDouble() >
                        Constants.PREDICTED_RANKING_POINT_QUALIFICATION
                    ) {
                        tv.setImageResource(R.drawable.shield)
                        continue@red_predicted
                    }
                }
                1 -> {
                    if (MainViewerActivity.matchCache[matchNumber]!!.redPredictedRPTwo != null &&
                        MainViewerActivity.matchCache[matchNumber]!!.redPredictedRPTwo!!.toDouble() >
                        Constants.PREDICTED_RANKING_POINT_QUALIFICATION
                    ) {
                        tv.setImageResource(R.drawable.lightning)
                        continue@red_predicted
                    }
                }
            }
            val value = getAllianceInMatchObjectByKey(
                Constants.PROCESSED_OBJECT.CALCULATED_PREDICTED_ALLIANCE_IN_MATCH.value,
                Constants.RED, matchNumber, "predicted_rp" +
                        "${listOf(
                            viewHolder.tvRedPredictedRPOne,
                            viewHolder.tvRedPredictedRPTwo
                        ).indexOf(tv) + 1}"
            )
            if (value != Constants.NULL_CHARACTER &&
                value.toDouble() > Constants.PREDICTED_RANKING_POINT_QUALIFICATION
            ) {
                when (listOf(
                    viewHolder.tvRedPredictedRPOne,
                    viewHolder.tvRedPredictedRPTwo
                ).indexOf(tv)) {
                    0 -> {
                        MainViewerActivity.matchCache[matchNumber]!!.redPredictedRPOne =
                            parseFloat(("%.2f").format(value.toFloat()))
                    }
                    1 -> {
                        MainViewerActivity.matchCache[matchNumber]!!.redPredictedRPTwo =
                            parseFloat(("%.2f").format(value.toFloat()))
                    }
                }
            } else tv.setImageDrawable(null)
        }
        blue_predicted@ for (tv in listOf(
            viewHolder.tvBluePredictedRPOne,
            viewHolder.tvBluePredictedRPTwo
        )) {
            when (listOf(
                viewHolder.tvBluePredictedRPOne,
                viewHolder.tvBluePredictedRPTwo
            ).indexOf(tv)) {
                0 -> {
                    if (MainViewerActivity.matchCache[matchNumber]!!.bluePredictedRPOne != null &&
                        MainViewerActivity.matchCache[matchNumber]!!.bluePredictedRPOne!!.toDouble() >
                        Constants.PREDICTED_RANKING_POINT_QUALIFICATION
                    ) {
                        tv.setImageResource(R.drawable.shield)
                        continue@blue_predicted
                    }
                }
                1 -> {
                    if (MainViewerActivity.matchCache[matchNumber]!!.bluePredictedRPTwo != null &&
                        MainViewerActivity.matchCache[matchNumber]!!.bluePredictedRPTwo!!.toDouble() >
                        Constants.PREDICTED_RANKING_POINT_QUALIFICATION
                    ) {
                        tv.setImageResource(R.drawable.lightning)
                        continue@blue_predicted
                    }
                }
            }
            val value = getAllianceInMatchObjectByKey(
                Constants.PROCESSED_OBJECT.CALCULATED_PREDICTED_ALLIANCE_IN_MATCH.value,
                Constants.BLUE, matchNumber, "predicted_rp" +
                        "${listOf(
                            viewHolder.tvBluePredictedRPOne,
                            viewHolder.tvBluePredictedRPTwo
                        ).indexOf(tv) + 1}"
            )
            if (value != Constants.NULL_CHARACTER &&
                value.toDouble() > Constants.PREDICTED_RANKING_POINT_QUALIFICATION
            ) {
                when (listOf(
                    viewHolder.tvBluePredictedRPOne,
                    viewHolder.tvBluePredictedRPTwo
                ).indexOf(tv)) {
                    0 -> {
                        MainViewerActivity.matchCache[matchNumber]!!.bluePredictedRPOne =
                            parseFloat(("%.2f").format(value.toFloat()))
                    }
                    1 -> {
                        MainViewerActivity.matchCache[matchNumber]!!.bluePredictedRPTwo =
                            parseFloat(("%.2f").format(value.toFloat()))
                    }
                }
            } else tv.setImageDrawable(null)
        }
        return rowView!!
    }

    // View holder class to handle the elements used in the custom cells.
    private class ViewHolder(view: View?) {
        val tvMatchNumber = view?.findViewById(R.id.tv_match_number) as TextView
        val tvBluePredictedScore = view?.findViewById(R.id.tv_blue_predicted_score) as TextView
        val tvRedPredictedScore = view?.findViewById(R.id.tv_red_predicted_score) as TextView
        val tvBluePredictedRPOne = view?.findViewById(R.id.tv_blue_predicted_rp1) as ImageView
        val tvRedPredictedRPOne = view?.findViewById(R.id.tv_red_predicted_rp1) as ImageView
        val tvBluePredictedRPTwo = view?.findViewById(R.id.tv_blue_predicted_rp2) as ImageView
        val tvRedPredictedRPTwo = view?.findViewById(R.id.tv_red_predicted_rp2) as ImageView
        val tvBlueTeamOne = view?.findViewById(R.id.tv_blue_team_one) as TextView
        val tvBlueTeamTwo = view?.findViewById(R.id.tv_blue_team_two) as TextView
        val tvBlueTeamThree = view?.findViewById(R.id.tv_blue_team_three) as TextView
        val tvRedTeamOne = view?.findViewById(R.id.tv_red_team_one) as TextView
        val tvRedTeamTwo = view?.findViewById(R.id.tv_red_team_two) as TextView
        val tvRedTeamThree = view?.findViewById(R.id.tv_red_team_three) as TextView
    }
}
