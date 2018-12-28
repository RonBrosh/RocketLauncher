package com.example.ronbrosh.rocketlauncher.rocketlauncher.rocketlist.view

import android.os.Build
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
import kotlinx.android.synthetic.main.layout_rocket_details.view.*
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
        private var isTransitionAlreadyStarted: AtomicBoolean = AtomicBoolean()

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(view: View?) {
            selectedItemPosition = adapterPosition
            if (selectedItemPosition != RecyclerView.NO_POSITION) {
                listener?.onRocketItemClick(this, getItem(selectedItemPosition))
            }
        }

        fun bind(rocket: Rocket) {
            if (Build.VERSION.SDK_INT >= 21) {
                itemView.imageViewPreview.transitionName = itemView.resources.getString(R.string.rocket_item_image_transition_name, rocket.rocketId)
                itemView.textContainer.transitionName = itemView.resources.getString(R.string.rocket_item_text_container_transition_name, rocket.rocketId)
            }
            itemView.textViewRocketName.text = rocket.name
            itemView.textViewRocketCountry.text = rocket.country
            itemView.textViewRocketEnginesCount.text = String.format(itemView.context.getString(R.string.rocket_data_engines_count_format), rocket.engine.enginesCount)

            Picasso.get().load(rocket.imageUrlList[0]).placeholder(R.drawable.image_place_holder).into(itemView.imageViewPreview, object : Callback {
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