package com.example.ronbrosh.rocketlauncher.rocketlist.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.ronbrosh.rocketlauncher.R
import com.example.ronbrosh.rocketlauncher.db.RocketData
import kotlinx.android.synthetic.main.recyclerview_item_all_rocket_data.view.*

class RocketListAdapter : ListAdapter<RocketData, RocketListAdapter.RocketListViewHolder>(ItemCallBack) {
    private var listener: RocketListItemClickListener? = null

    private companion object ItemCallBack : DiffUtil.ItemCallback<RocketData>() {
        override fun areItemsTheSame(oldItem: RocketData, newItem: RocketData): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: RocketData, newItem: RocketData): Boolean {
            return oldItem == newItem
        }
    }

    inner class RocketListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(view: View?) {
            if (adapterPosition != RecyclerView.NO_POSITION) {
                listener?.onRocketItemClick(getItem(adapterPosition))
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RocketListViewHolder {
        return RocketListViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_item_all_rocket_data, parent, false))
    }

    override fun onBindViewHolder(holder: RocketListViewHolder, position: Int) {
        val rocketData: RocketData = getItem(position)
        holder.itemView.textViewRocketName.text = rocketData.name
        holder.itemView.textViewRocketCountry.text = rocketData.country
        holder.itemView.textViewRocketEnginesCount.text = String.format(holder.itemView.context.getString(R.string.rocket_data_engines_count_format), rocketData.engine.enginesCount)

    }

    fun setRocketListItemClickListener(rocketListItemClickListener: RocketListItemClickListener?) {
        this.listener = rocketListItemClickListener
    }
}