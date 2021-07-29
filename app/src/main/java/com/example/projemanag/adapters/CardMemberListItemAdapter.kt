package com.example.projemanag.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.projemanag.R
import com.example.projemanag.activities.CardDetailsActivity
import com.example.projemanag.activities.TaskListActivity
import com.example.projemanag.databinding.ItemCardSelectedMemberBinding
import com.example.projemanag.models.SelectedMembers
import com.example.projemanag.utils.Constants.TAG

class CardMemberListItemAdapter(
    private val context: Context,
    private val list: ArrayList<SelectedMembers>,
    private val assignedMembers: Boolean
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var onClickListener: OnClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context).inflate(
            R.layout.item_card_selected_member,
            parent,
            false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]

        if (holder is MyViewHolder) {
            if (position == list.size - 1 && assignedMembers) {
                holder.binding.ivAddMember.visibility = View.VISIBLE
                holder.binding.ivSelectedMemberImage.visibility = View.GONE

                if (context is CardDetailsActivity) {
                    holder.itemView.setOnClickListener {
                        if (onClickListener != null) {
                            onClickListener!!.onClick()
                        }
                    }
                }

            } else {
                holder.binding.ivAddMember.visibility = View.GONE
                holder.binding.ivSelectedMemberImage.visibility = View.VISIBLE

                Glide
                    .with(context)
                    .load(model.image)
                    .centerCrop()
                    .placeholder(R.drawable.ic_user_place_holder)
                    .into(holder.binding.ivSelectedMemberImage)
            }

            if (context is TaskListActivity) {
                holder.itemView.setOnClickListener {
                    if (onClickListener != null) {
                        onClickListener!!.onClick()
                    }
                }
            }

        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val binding = ItemCardSelectedMemberBinding.bind(view)
    }

    interface OnClickListener{
        fun onClick()
    }

    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }
}