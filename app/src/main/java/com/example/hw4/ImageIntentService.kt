package com.example.hw4

import android.app.IntentService
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Parcelable
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import org.json.JSONArray
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

class ImageIntentService(name: String = "") : IntentService(name) {
    override fun onHandleIntent(intent: Intent?) {
        val response = Intent(getString(R.string.action))
        response.putParcelableArrayListExtra(getString(R.string.image_list), downloadImages())
        response.putExtra(getString(R.string.type), getString(R.string.list))
        LocalBroadcastManager.getInstance(this).sendBroadcast(response)
    }

    private fun downloadImages(): ArrayList<Parcelable> {
        return try {
            val connection = URL(
                "${getString(R.string.url_query)}${getString(R.string.cherepovets)}&${getString(R.string.client_id)}=${getString(R.string.id)}"
            ).openConnection() as HttpURLConnection
            val images = JSONObject(connection.inputStream.reader().readText()).getJSONArray(getString(
                            R.string.results))
            val result = ArrayList<Parcelable>()
            for (i in 0 until images.length()) {
                val image = images.getJSONObject(i)
                val urls = image.getJSONObject(getString(R.string.urls))
                val description = image.getString(getString(R.string.description))
                val altDescription = image.getString(getString(R.string.alt_description))
                val resultDescription = if (description == getString(R.string.nul1)) {
                    if (altDescription == getString(R.string.nul1)) {
                        getString(R.string.no_description)
                    } else altDescription
                } else description
                val id = image.getString(getString(R.string.id_id))
                result.add(
                    Image(
                        id,
                        resultDescription,
                        id,
                        urls.getString(getString(R.string.mode))
                    )
                )
            }
            result
        } catch (e: Exception) {
            ArrayList()
        }
    }
}