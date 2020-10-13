package com.example.hw4

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_image.*
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.ConcurrentHashMap

class ImageActivity : AppCompatActivity() {
    companion object {
        val idToImage = ConcurrentHashMap<String, Bitmap>()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image)

        val id = intent.getStringExtra("id")
        if (idToImage[id] == null) {
            ImageAsyncTask(this).execute(intent.getStringExtra("id"), intent.getStringExtra("ref"))
        } else {
            image.setImageBitmap(idToImage[id])
        }
    }

    class ImageAsyncTask(val activity: ImageActivity) : AsyncTask<String, Unit, Bitmap>() {
        override fun doInBackground(vararg params: String?): Bitmap? {
            val id = params[0] ?: ""
            val ref = params[1]
            return try {
                val url = URL(ref)
                val connection: HttpURLConnection = url
                    .openConnection() as HttpURLConnection
                connection.doInput = true
                connection.connect()
                val input: InputStream = connection.inputStream
                val bitmap = BitmapFactory.decodeStream(input)
                idToImage[id] = bitmap
                bitmap
            } catch (e: IOException) {
                e.printStackTrace()
                null
            }
        }

        override fun onPostExecute(result: Bitmap?) {
            super.onPostExecute(result)
            if (result == null) {
                Toast.makeText(
                    activity,
                    activity.getString(R.string.error_connect),
                    Toast.LENGTH_LONG
                ).show()
            } else activity.image.setImageBitmap(result)
        }
    }
}