package com.example.narayanagrouptask.ui.adapters

import android.content.Context
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.narayanagrouptask.R
import com.example.narayanagrouptask.models.Owner


class ContributorsAdapter(
    var context: Context,
    var repoItems: ArrayList<Owner>,
) : RecyclerView.Adapter<ContributorsAdapter.MViewHolder>() {
    public fun updateList(repoItems: List<Owner>) {
        this.repoItems.addAll(repoItems)
        //COuld be improved with DiffUtils or some other way to notify only newly added items
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MViewHolder {
        var view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.home_card_row, parent, false)
        return MViewHolder(view)
    }

    override fun onBindViewHolder(holder: MViewHolder, position: Int) {
        Glide.with(context).load(repoItems.get(position).avatarUrl).circleCrop()
            .error(R.drawable.ic_baseline_person_48).into(holder.img_avatar)

        holder.tv_name.text = "${repoItems.get(position).login}"
        holder.tv_description.text = "${repoItems.get(position).htmlUrl}"
        holder.tv_description.setPaintFlags(holder.tv_description.getPaintFlags() or Paint.UNDERLINE_TEXT_FLAG)

/*        holder.itemView.setOnClickListener {
            listener.onItemClicked(position)
        }*/
    }

    override fun getItemCount(): Int {
        return repoItems.size
    }

    inner class MViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tv_name: TextView
        var tv_description: TextView
        var img_avatar: ImageView

        init {
            tv_name = itemView.findViewById(R.id.tv_name)
            tv_description = itemView.findViewById(R.id.tv_description)
            img_avatar = itemView.findViewById(R.id.img_avatar)

        }
    }
}