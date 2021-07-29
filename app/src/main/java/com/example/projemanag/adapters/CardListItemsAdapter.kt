package com.example.projemanag.adapters

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projemanag.R
import com.example.projemanag.activities.TaskListActivity
import com.example.projemanag.models.Card
import com.example.projemanag.models.SelectedMembers
import com.example.projemanag.utils.Constants.TAG

open class CardListItemsAdapter(
    private val context: Context,
    private var list: ArrayList<Card>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var onClickListener: OnClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.item_card,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]

        if (holder is MyViewHolder) {
            if (model.labelColor.isNotEmpty()) {
                holder.viewLabelColor.visibility = View.VISIBLE
                holder.viewLabelColor.setBackgroundColor(Color.parseColor(model.labelColor))
            } else {
                holder.viewLabelColor.visibility = View.GONE
            }

            holder.cardName.text = model.name

            if ((context as TaskListActivity).mAssignedMemberDetailList.size > 0) {
                val selectedMembersList: ArrayList<SelectedMembers> = ArrayList()

                for (i in context.mAssignedMemberDetailList.indices) {
                    for (j in model.assignedTo) {
                        if (context.mAssignedMemberDetailList[i].id == j) {
                            val selectedMember = SelectedMembers(
                                context.mAssignedMemberDetailList[i].id,
                                context.mAssignedMemberDetailList[i].image
                            )

                            selectedMembersList.add(selectedMember)
                        }
                    }
                }

                if (selectedMembersList.size > 0) {
                    if (selectedMembersList.size == 1
                        && selectedMembersList[0].id == model.createBy
                    ) {
                        holder.recyclerView.visibility = View.GONE

                    } else {
                        holder.recyclerView.visibility = View.VISIBLE

                        holder.recyclerView.layoutManager = GridLayoutManager(context, 4)
                        val adapter = CardMemberListItemAdapter(
                            context, selectedMembersList, false
                        )
                        holder.recyclerView.adapter = adapter

                        adapter.setOnClickListener(
                            object : CardMemberListItemAdapter.OnClickListener {
                                override fun onClick() {
                                    if (onClickListener != null) {
                                        onClickListener!!.onClick(position)
                                    }
                                }

                            }
                        )
                    }
                } else {
                    holder.recyclerView.visibility = View.GONE
                }
            }

            holder.itemView.setOnClickListener {
                if (onClickListener != null) {
                    onClickListener!!.onClick(position) // cardPosition
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }

    interface OnClickListener {
        fun onClick(position: Int)
    }

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val cardName: TextView = view.findViewById(R.id.tv_card_name)
        val membersName: TextView = view.findViewById(R.id.tv_members_name)
        val viewLabelColor: View = view.findViewById(R.id.view_label_color)
        val recyclerView: RecyclerView = view.findViewById(R.id.rv_card_selected_members_list)
    }
}