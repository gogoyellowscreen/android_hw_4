package com.example.hw4

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.list_item.view.*

class ImageAdapter(
    private val images: List<Image>,
    private val onClick: (Image) -> Unit
) : RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {
    class ImageViewHolder(val root: View) : RecyclerView.ViewHolder(root) {
        fun bind(image: Image) {
            with(root) {
                description.text = image.description
                ref.text = image.ref
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val holder = ImageViewHolder(
            LayoutInflater.
            from(parent.context).
            inflate(R.layout.list_item, parent, false)
        )
        holder.root.setOnClickListener {
            onClick(images[holder.adapterPosition])
        }
        return holder
    }

    override fun getItemCount(): Int = images.size

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.bind(images[position])
    }
}