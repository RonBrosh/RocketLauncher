package com.example.ronbrosh.rocketlauncher.rocketlauncher.rocketlist.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.ronbrosh.rocketlauncher.R
import com.example.ronbrosh.rocketlauncher.model.Rocket
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import java.lang.Exception
import java.util.concurrent.atomic.AtomicBoolean

class RocketListAdapter : ListAdapter<Rocket, RocketListAdapter.RocketListViewHolder>(ItemCallBack) {
    private var listener: RocketListAdapterListener? = null
    private var selectedItemPosition = 0

    private companion object ItemCallBack : DiffUtil.ItemCallback<Rocket>() {
        override fun areItemsTheSame(oldItem: Rocket, newItem: Rocket): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Rocket, newItem: Rocket): Boolean {
            return oldItem == newItem
        }
    }

    inner class RocketListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val rocketDetailsContainer: View
        val textViewRocketName: TextView
        val textViewRocketCountry: TextView
        val textViewRocketEnginesCount: TextView
        val imageViewPreview: ImageView
        var isTransitionAlreadyStarted: AtomicBoolean = AtomicBoolean()

        init {
            itemView.setOnClickListener(this)
            rocketDetailsContainer = itemView.findViewById(R.id.rocketDetailsContainer)
            textViewRocketName = itemView.findViewById(R.id.textViewRocketName)
            textViewRocketCountry = itemView.findViewById(R.id.textViewRocketCountry)
            textViewRocketEnginesCount = itemView.findViewById(R.id.textViewRocketEnginesCount)
            imageViewPreview = itemView.findViewById(R.id.imageViewPreview)
        }

        override fun onClick(view: View?) {
            selectedItemPosition = adapterPosition
            if (selectedItemPosition != RecyclerView.NO_POSITION) {
                listener?.onRocketItemClick(rocketDetailsContainer, getItem(selectedItemPosition))
            }
        }

        fun bind(rocket: Rocket) {
            ViewCompat.setTransitionName(rocketDetailsContainer, rocket.rocketId)

            textViewRocketName.text = rocket.name
            textViewRocketCountry.text = rocket.country
            textViewRocketEnginesCount.text = String.format(itemView.context.getString(R.string.rocket_data_engines_count_format), rocket.engine.enginesCount)

            Picasso.get().load(rocket.imageUrlList[0]).placeholder(R.drawable.image_place_holder).into(imageViewPreview, object : Callback {
                override fun onError(e: Exception?) {
                    onLoadFinished()
                }

                override fun onSuccess() {
                    onLoadFinished()
                }
            })
        }

        private fun onLoadFinished() {
            if (selectedItemPosition != adapterPosition)
                return
            else if (isTransitionAlreadyStarted.getAndSet(true))
                return

            listener?.onLastSelectedItemLoadFinished()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RocketListViewHolder {
        return RocketListViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_item_all_rocket_data, parent, false))
    }

    override fun onBindViewHolder(holder: RocketListViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    fun setRocketListItemClickListener(rocketListItemClickListener: RocketListAdapterListener?) {
        this.listener = rocketListItemClickListener
    }

    fun getSelectedItemPosition(): Int {
        return selectedItemPosition
    }

    fun setSelectedItemPosition(selectedItemPosition: Int) {
        this.selectedItemPosition = selectedItemPosition
    }
}