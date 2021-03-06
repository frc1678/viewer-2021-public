package com.example.viewer_2020.fragments.pickability

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.fragment.app.Fragment
import com.example.viewer_2020.*
import com.example.viewer_2020.constants.Constants
import com.example.viewer_2020.fragments.team_details.TeamDetailsFragment
import kotlinx.android.synthetic.main.fragment_pickability.view.*
import kotlinx.android.synthetic.main.fragment_ranking.view.*
import java.lang.ClassCastException
import java.util.Comparator

class PickabilityFragment(val mode: PickabilityMode) : IFrag() {
    private val teamDetailsFragment = TeamDetailsFragment()
    private val teamDetailsFragmentArguments = Bundle()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_pickability, container, false)
        root.tv_pickability_header.text = mode.toString().toLowerCase().capitalize() + " Pickability"
        val map : Map<String, Float> = updateMatchScheduleListView(root)

        root.lv_pickability.setOnItemClickListener { _, _, position, _ ->
            val list : List<String> = map.keys.toList()
            val pickabilityFragmentTransaction = this.fragmentManager!!.beginTransaction()
            teamDetailsFragmentArguments.putString(Constants.TEAM_NUMBER,
                list[position]
            )
            teamDetailsFragment.arguments = teamDetailsFragmentArguments
            pickabilityFragmentTransaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
            pickabilityFragmentTransaction.addToBackStack(null).replace(
                (view!!.parent as ViewGroup).id,
                teamDetailsFragment
            ).commit()
        }

        return root
    }

    private fun updateMatchScheduleListView(root: View) : Map<String, Float>{
        var map = mutableMapOf<String, Float>()
        val rawTeamNumbers = convertToFilteredTeamsList(
            Constants.PROCESSED_OBJECT.CALCULATED_PREDICTED_TEAM.value,
            MainViewerActivity.teamList
        )

        rawTeamNumbers.forEach { e -> map[e] = try {
            getTeamDataValue(
                e,
                (if (mode == PickabilityMode.FIRST) "first_pickability" else "second_pickability")
            ).toFloat()
        } catch (e: Exception) {
            (-1000).toFloat()
        } }

        map = map.toList().sortedBy {(k, v) ->


            (v)
        }.reversed().toMap().toMutableMap()
        adapter = PickabilityListAdapter(
            items = map,
            context = activity!!,
            mode = mode
        )
        root.lv_pickability.adapter = adapter
        return map
    }
}
enum class PickabilityMode {
    FIRST,
    SECOND
}