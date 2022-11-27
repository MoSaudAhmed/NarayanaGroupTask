package com.example.narayanagrouptask.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.narayanagrouptask.R
import com.example.narayanagrouptask.models.RepoItem
import com.example.narayanagrouptask.ui.callbacks.HomeRepoCLickListener

class RepositoryPagingAdapter(val context: Context, var listener: HomeRepoCLickListener) :
    PagingDataAdapter<RepoItem, RepositoryPagingAdapter.RepoPagingViewHolder>(COMPARATOR) {

    class RepoPagingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tv_name = itemView.findViewById<TextView>(R.id.tv_name)
        val tv_description = itemView.findViewById<TextView>(R.id.tv_description)
        val img_avatar = itemView.findViewById<ImageView>(R.id.img_avatar)
    }

    override fun onBindViewHolder(holder: RepoPagingViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {
            holder.tv_name.text = item.name
            holder.tv_description.text = "${item.description}"
            Glide.with(context).load(item.owner!!.avatarUrl).circleCrop()
                .error(R.drawable.ic_baseline_person_48).into(holder.img_avatar)

            holder.itemView.setOnClickListener {
                listener.onItemClicked(position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepoPagingViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.home_card_row, parent, false)
        return RepoPagingViewHolder(view)
    }

    companion object {
        private val COMPARATOR = object : DiffUtil.ItemCallback<RepoItem>() {
            override fun areItemsTheSame(oldItem: RepoItem, newItem: RepoItem): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: RepoItem, newItem: RepoItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}