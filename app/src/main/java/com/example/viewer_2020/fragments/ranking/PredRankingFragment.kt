package com.example.viewer_2020.fragments.ranking

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.fragment.app.Fragment
import com.example.viewer_2020.*
import com.example.viewer_2020.constants.Constants
import com.example.viewer_2020.constants.Translations
import com.example.viewer_2020.convertToFilteredTeamsList
import com.example.viewer_2020.fragments.team_details.TeamDetailsFragment
import kotlinx.android.synthetic.main.fragment_ranking.view.*

class PredRankingFragment : IFrag() {
    private val teamDetailsFragment = TeamDetailsFragment()
    private val teamDetailsFragmentArguments = Bundle()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Usage of counter variable to increment at every iteration of the menu item forEach statement.
        // Used to find the index of the current selected menu in the menu item list because the
        // MenuItem object does not have an indexOf() return function.
        val root = inflater.inflate(R.layout.fragment_ranking, container, false)
        // This is a listener for the navigation bar onClick.
        // When a menu item of the navigation bar is clicked, it sends the MenuItem to
        // the handler activity (MainViewerActivity) to set the adapter of this fragment's
        // resource layout to the correct adapter given the new menu item that should be displayed.

        root.tv_ranking_header.text = "Pred. Rankings"
        root.btn_toggle.text = "To Rankings"

        root.tv_datapoint_two.text =
            Translations.ACTUAL_TO_HUMAN_READABLE[Constants.FIELDS_TO_BE_DISPLAYED_RANKING[1]]
        root.tv_datapoint_three.text =
            Translations.ACTUAL_TO_HUMAN_READABLE[Constants.FIELDS_TO_BE_DISPLAYED_RANKING[2]]
        root.tv_datapoint_four.text =
            Translations.ACTUAL_TO_HUMAN_READABLE[Constants.FIELDS_TO_BE_DISPLAYED_RANKING[3]]
        root.tv_datapoint_five.text =
            Translations.ACTUAL_TO_HUMAN_READABLE[Constants.FIELDS_TO_BE_DISPLAYED_RANKING[0]]

        adapter = PredRankingListAdapter(activity!!, convertToPredFilteredTeamsList(
            Constants.PROCESSED_OBJECT.CALCULATED_PREDICTED_TEAM.value,
            MainViewerActivity.teamList
        ))

        root.lv_ranking.adapter = adapter

        root.lv_ranking.setOnItemClickListener { _, _, position, _ ->
            val rankingFragmentTransaction = this.fragmentManager!!.beginTransaction()
            teamDetailsFragmentArguments.putString(
                Constants.TEAM_NUMBER, convertToPredFilteredTeamsList(
                    Constants.PROCESSED_OBJECT.CALCULATED_PREDICTED_TEAM.value,
                    MainViewerActivity.teamList
                )[position]
            )
            teamDetailsFragment.arguments = teamDetailsFragmentArguments
            rankingFragmentTransaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
            rankingFragmentTransaction.addToBackStack(null).replace(
                (view!!.parent as ViewGroup).id,
                teamDetailsFragment
            ).commit()
        }
        root.btn_toggle.setOnClickListener {
            toggletoRanking()
        }
        return root
    }
    fun toggletoRanking() {
        val rankingFragment = RankingFragment()
        val ft = fragmentManager!!.beginTransaction()
        fragmentManager!!.beginTransaction()
            .addToBackStack(null)
            .replace(R.id.nav_host_fragment, rankingFragment, "rankings")
            .commit()
        return
    }
}