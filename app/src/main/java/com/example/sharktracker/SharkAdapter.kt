package com.example.sharktracker

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.bumptech.glide.Glide
import java.text.SimpleDateFormat
import java.util.*

class SharkAdapter(
    private val onSharkClick: (SharkPing) -> Unit
) : ListAdapter<SharkPing, SharkAdapter.SharkViewHolder>(SharkDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SharkViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_shark, parent, false)
        return SharkViewHolder(view)
    }

    override fun onBindViewHolder(holder: SharkViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class SharkViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val sharkImage: ImageView = itemView.findViewById(R.id.shark_image)
        private val sharkName: TextView = itemView.findViewById(R.id.shark_name)
        private val sharkSpecies: TextView = itemView.findViewById(R.id.shark_species)
        private val lastPingTime: TextView = itemView.findViewById(R.id.last_ping_time)
        private val coordinates: TextView = itemView.findViewById(R.id.coordinates)

        fun bind(sharkPing: SharkPing) {
            sharkName.text = sharkPing.name
            sharkSpecies.text = sharkPing.species

            // Format coordinates
            coordinates.text = "Lat: ${String.format("%.4f", sharkPing.latitude)}, " +
                    "Long: ${String.format("%.4f", sharkPing.longitude)}"

            // Format timestamp
            try {
                val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                val outputFormat = SimpleDateFormat("MMM dd, HH:mm", Locale.getDefault())
                val date = inputFormat.parse(sharkPing.datetime)
                lastPingTime.text = "Last seen: ${outputFormat.format(date ?: Date())}"
            } catch (e: Exception) {
                lastPingTime.text = "Last seen: ${sharkPing.datetime}"
            }

            // Load shark image
            Glide.with(itemView.context)
                .load(sharkPing.profilePhoto)
                .placeholder(R.drawable.ic_shark_placeholder)
                .error(R.drawable.ic_shark_placeholder)
                .circleCrop()
                .into(sharkImage)

            itemView.setOnClickListener {
                onSharkClick(sharkPing)
            }
        }
    }

    class SharkDiffCallback : DiffUtil.ItemCallback<SharkPing>() {
        override fun areItemsTheSame(oldItem: SharkPing, newItem: SharkPing): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: SharkPing, newItem: SharkPing): Boolean {
            return oldItem == newItem
        }
    }
}