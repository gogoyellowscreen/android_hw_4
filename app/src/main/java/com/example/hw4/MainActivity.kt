package com.example.hw4

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private var images = ArrayList<Image>()
    private lateinit var imageReceiver: BroadcastReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        savedInstanceState ?: run {
            try {
                downloadList()
            } catch (e: Exception) {
            }
        }
        val viewManager = LinearLayoutManager(this)
        myRecyclerView.apply {
            layoutManager = viewManager
            adapter = ImageAdapter(images) {
                val intent = Intent(
                    this.context,
                    ImageActivity::class.java
                ).apply {
                    putExtra(getString(R.string.id_id), it.id)
                    putExtra(getString(R.string.ref), it.ref)
                }
                startActivity(intent)
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(getString(R.string.array), images)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        images.addAll(savedInstanceState.getParcelableArrayList(getString(R.string.array)) ?: emptyList())
        if (images.isEmpty()) {
            downloadList()
        }
    }

    private fun downloadList() {
        val intent = Intent().apply {
            setClass(this@MainActivity, ImageIntentService::class.java)
            putExtra(getString(R.string.type), getString(R.string.list))
        }
        startService(intent)
    }

    override fun onStart() {
        setReceiver()
        super.onStart()
    }

    override fun onStop() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(imageReceiver)
        super.onStop()
    }

    private fun setReceiver() {
        imageReceiver = ImageReceiver()
        val intentFilter = IntentFilter().apply {
            addAction(getString(R.string.action))
        }
        LocalBroadcastManager.getInstance(this).registerReceiver(imageReceiver, intentFilter)
    }

    private inner class ImageReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val type = intent.getStringExtra(getString(R.string.type))
            if (type == getString(R.string.list)) {
                images.addAll(
                    intent.getParcelableArrayListExtra(getString(R.string.image_list))
                        ?: emptyList()
                )
                myRecyclerView.adapter?.notifyDataSetChanged()

                if (images.isEmpty()) {
                    Toast.makeText(
                        this@MainActivity,
                        getString(R.string.error_connect),
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }
}