package com.example.ronbrosh.rocketlauncher.rocketlist.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.ronbrosh.rocketlauncher.R
import com.example.ronbrosh.rocketlauncher.model.Rocket
import com.squareup.picasso.Picasso

class RocketListAdapter : ListAdapter<Rocket, RocketListAdapter.RocketListViewHolder>(ItemCallBack) {
    private var listener: RocketListItemClickListener? = null

    private companion object ItemCallBack : DiffUtil.ItemCallback<Rocket>() {
        override fun areItemsTheSame(oldItem: Rocket, newItem: Rocket): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Rocket, newItem: Rocket): Boolean {
            return oldItem == newItem
        }
    }

    inner class RocketListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        private val rocketDetailsContainer: View
        val textViewRocketName: TextView
        val textViewRocketCountry: TextView
        val textViewRocketEnginesCount: TextView
        val imageViewPreview: ImageView

        init {
            itemView.setOnClickListener(this)
            rocketDetailsContainer = itemView.findViewById(R.id.rocketDetailsContainer)
            textViewRocketName = itemView.findViewById(R.id.textViewRocketName)
            textViewRocketCountry = itemView.findViewById(R.id.textViewRocketCountry)
            textViewRocketEnginesCount = itemView.findViewById(R.id.textViewRocketEnginesCount)
            imageViewPreview = itemView.findViewById(R.id.imageViewPreview)
        }

        override fun onClick(view: View?) {
            if (adapterPosition != RecyclerView.NO_POSITION) {
                listener?.onRocketItemClick(rocketDetailsContainer, getItem(adapterPosition))
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RocketListViewHolder {
        return RocketListViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_item_all_rocket_data, parent, false))
    }

    override fun onBindViewHolder(holder: RocketListViewHolder, position: Int) {
        val rocket: Rocket = getItem(position)
        holder.textViewRocketName.text = rocket.name
        holder.textViewRocketCountry.text = rocket.country
        holder.textViewRocketEnginesCount.text = String.format(holder.itemView.context.getString(R.string.rocket_data_engines_count_format), rocket.engine.enginesCount)
        Picasso.get().load(rocket.imageUrlList[0]).placeholder(R.drawable.image_place_holder).into(holder.imageViewPreview)
    }

    fun setRocketListItemClickListener(rocketListItemClickListener: RocketListItemClickListener?) {
        this.listener = rocketListItemClickListener
    }
}