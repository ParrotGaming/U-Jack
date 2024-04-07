package com.wompwompstudios.u_jack

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load

class ImageAdapter(
    private val context: Context,
    private val images: MutableList<Image>,
    private val descriptions: MutableList<String>,
    private val values: MutableList<String>,
    private val difficulties: MutableList<String>,
    private val ids: MutableList<String>,
) : RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {

    class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        return ImageViewHolder(LayoutInflater.from(parent.context).inflate(
            R.layout.car_jacking_data_template,
            parent,
            false
        ))
    }

    override fun getItemCount(): Int {
        return images.size
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val curImage = images[position]
        val curDescription = descriptions[position]
        val curValue = values[position]
        val curDifficulty = difficulties[position]
        val curId = ids[position]
        holder.itemView.apply {
            val imageView = findViewById<ImageView>(R.id.imgCar)
            imageView.load(curImage.url)
            val text = findViewById<TextView>(R.id.UserCarDescription)
            text.text = curDescription
            val id = findViewById<TextView>(R.id.UserID)
            id.text = "Id: " + curId
            val valueCar = findViewById<TextView>(R.id.EstimatedCarValue)
            valueCar.text = "Estimated value: $" + curValue
            val difficultyCar = findViewById<TextView>(R.id.DifficultyRatingStatement)
            difficultyCar.text = "Difficulty: " + curDifficulty
        }
    }
    fun addImage(image: Image, description: String, value: String, difficulty: String, id: String) {
        images.add(image)
        descriptions.add(description)
        values.add(value)
        difficulties.add(difficulty)
        ids.add(id)

        notifyItemInserted(images.size - 1)
    }
}
