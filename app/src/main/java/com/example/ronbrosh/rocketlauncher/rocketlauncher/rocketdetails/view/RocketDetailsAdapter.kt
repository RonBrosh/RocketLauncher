package com.example.ronbrosh.rocketlauncher.rocketlauncher.rocketdetails.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ronbrosh.rocketlauncher.R
import com.example.ronbrosh.rocketlauncher.model.Launch
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.layout_rocket_details.view.*
import kotlinx.android.synthetic.main.recyclerview_item_launch_data.view.*
import java.text.SimpleDateFormat
import java.util.*

class RocketDetailsAdapter : RecyclerView.Adapter<RocketDetailsAdapter.LaunchViewHolder>() {

    private var data: List<Launch>? = null

    inner class LaunchViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(launch: Launch) {
            itemView.name.text = launch.name
            itemView.date.text = SimpleDateFormat.getDateTimeInstance().format(Date(launch.timeStamp))
            if (launch.isSuccessful)
                itemView.isSuccessful.text = itemView.context.getString(R.string.launch_successful)
            else
                itemView.isSuccessful.text = itemView.context.getString(R.string.launch_failed)

            Picasso.get().load(launch.patchImage?.smallImageURL).into(itemView.patch,object : Callback {
                override fun onError(e: Exception?) {
                    itemView.imagePlaceHolder.visibility = View.VISIBLE
                }

                override fun onSuccess() {
                    itemView.imagePlaceHolder.visibility = View.GONE
                }
            })
        }
    }

    fun setData(data: List<Launch>) {
        this.data = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LaunchViewHolder {
        return LaunchViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_item_launch_data, parent, false))
    }

    override fun getItemCount(): Int {
        data?.let { list ->
            return list.size
        }
        return 0
    }

    override fun onBindViewHolder(holder: LaunchViewHolder, position: Int) {
        data?.let { list ->
            holder.bind(list[position])
        }
    }
}