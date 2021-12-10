package com.example.viewer_2020

import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import androidx.core.content.ContextCompat.startActivity
import java.net.URL
import com.example.viewer_2020.MongoDatabaseStartupActivity.Companion.databaseReference
import com.example.viewer_2020.data.*
import com.google.gson.Gson
import java.io.*
import java.net.HttpURLConnection
import org.json.JSONObject
import java.time.LocalDateTime
import java.util.*

class GetDataFromWebsite(val onCompleted: () -> Unit = {} ,val onError: (error: String) -> Unit = {}) :
    AsyncTask<String, String, String>() {

    override fun doInBackground(vararg p0: String?): String {
        try {
            //Sets the name of the collections on the website
            var listOfCollectionNames: List<String> =
                listOf(
                    "raw_obj_pit",
                    "raw_subj_pit",
                    "obj_tim",
                    "obj_team",
                    "subj_team",
                    "predicted_aim",
                    "predicted_team",
                    "tba_team",
                    "pickability"
                )

            //For each of the collections (make sure to change this number if the number of collections change),
            //pull the data from the website and then add it to the databaseReference variable
            for (x in 0..8) {
                val result =
                    sendRequest("<link to pull collections from Cardinal here>")
                when (x) {
                    0 -> databaseReference?.raw_obj_pit = Gson().fromJson(
                        result.toString(),
                        Array<DatabaseReference.ObjectivePit>::class.java
                    ).toMutableList()
                    1 -> databaseReference?.raw_subj_pit = Gson().fromJson(
                        result.toString(),
                        Array<DatabaseReference.SubjectivePit>::class.java
                    ).toMutableList()
                    2 -> databaseReference?.obj_tim = Gson().fromJson(
                        result.toString(),
                        Array<DatabaseReference.CalculatedObjectiveTeamInMatch>::class.java
                    ).toMutableList()
                    3 -> databaseReference?.obj_team = Gson().fromJson(
                        result.toString(),
                        Array<DatabaseReference.CalculatedObjectiveTeam>::class.java
                    ).toMutableList()
                    4 -> databaseReference?.subj_team = Gson().fromJson(
                        result.toString(),
                        Array<DatabaseReference.CalculatedSubjectiveTeam>::class.java
                    ).toMutableList()
                    5 -> databaseReference?.predicted_aim = Gson().fromJson(
                        result.toString(),
                        Array<DatabaseReference.CalculatedPredictedAllianceInMatch>::class.java
                    ).toMutableList()
                    6 -> databaseReference?.predicted_team = Gson().fromJson(
                        result.toString(),
                        Array<DatabaseReference.CalculatedPredictedTeam>::class.java
                    ).toMutableList()
                    7 -> databaseReference?.tba_team = Gson().fromJson(
                        result.toString(),
                        Array<DatabaseReference.CalculatedTBATeam>::class.java
                    ).toMutableList()
                    8 -> databaseReference?.pickability = Gson().fromJson(
                        result.toString(),
                        Array<DatabaseReference.CalculatedPickAbilityTeam>::class.java
                    ).toMutableList()
                }
            }

            val rawMatchSchedule: MutableMap<String, Website.WebsiteMatch> = Gson().fromJson(
                sendRequest("<link to pull match schedule here>"),
                WebsiteMatchSchedule
            )
            for (i in rawMatchSchedule) {
                val match = Match(i.key)
                for (j in i.value.teams) {
                    when (j.color) {
                        "red" -> {
                            match.redTeams.add(j.number.toString())
                        }
                        "blue" -> {
                            match.blueTeams.add(j.number.toString())
                        }
                    }
                }

                Log.e("parsedmap", match.toString())
                MainViewerActivity.matchCache[i.key] = match
            }
            MainViewerActivity.matchCache =
                MainViewerActivity.matchCache.toList().sortedBy { (k, v) -> v.matchNumber.toInt() }
                    .toMap().toMutableMap()

            MainViewerActivity.teamList = Gson().fromJson(
                sendRequest("<link to pull team list here>"),
                WebsiteTeams
            )

            lastUpdated = Calendar.getInstance().time

            return ("finished")
        } catch (e: Throwable) {
            onError(e.toString())
            return ("error")
        }
    }

    override fun onPostExecute(result: String) {
        onCompleted()
    }
}

fun sendRequest(url: String): String {
    var result = StringBuilder()
    var url =
        URL(url)
    var urlConnection = url.openConnection() as HttpURLConnection

    try {
        val `in`: InputStream = BufferedInputStream(urlConnection.inputStream)

        var reader = BufferedReader(InputStreamReader(`in`))
        var line = reader.readText()
        result.append(line)
    } catch (e: Exception) {
        e.printStackTrace();
    } finally {
        urlConnection.disconnect();
    }
    return result.toString()
}
