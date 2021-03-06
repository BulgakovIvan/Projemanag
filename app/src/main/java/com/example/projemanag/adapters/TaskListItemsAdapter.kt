package com.example.projemanag.adapters

import android.app.AlertDialog
import android.content.Context
import android.content.res.Resources
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projemanag.R
import com.example.projemanag.activities.TaskListActivity
import com.example.projemanag.databinding.ItemBoardBinding
import com.example.projemanag.databinding.ItemTaskBinding
import com.example.projemanag.models.Task
import com.example.projemanag.utils.Constants.TAG
import com.google.rpc.context.AttributeContext
import java.util.*
import kotlin.collections.ArrayList

class TaskListItemsAdapter(private val context: Context,
                           private val list: ArrayList<Task>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var mPositionDraggedFrom = -1
    private var mPositionDraggedTo = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val view = LayoutInflater
            .from(context)
            .inflate(R.layout.item_task, parent, false)

        // Here the layout params are converted dynamically according to the screen size
        // as width is 70% and height is wrap_content.
        val layoutParams = LinearLayout.LayoutParams(
            (parent.width * 0.7).toInt(),
            LinearLayout.LayoutParams.WRAP_CONTENT
        )

        // Here the dynamic margins are applied to the view.
        layoutParams.setMargins((15.toDp()).toPx(), 0, (40.toDp()).toPx(), 0)
        view.layoutParams = layoutParams

        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]

        if (holder is MyViewHolder) {
            with(holder.binding) {
                if (position == list.size - 1) {
                    tvAddTaskList.visibility = View.VISIBLE
                    llTaskItem.visibility = View.GONE
                } else {
                    tvAddTaskList.visibility = View.GONE
                    llTaskItem.visibility = View.VISIBLE
                }

                tvTaskListTitle.text = model.title

                tvAddTaskList.setOnClickListener {
                    tvAddTaskList.visibility = View.GONE
                    cvAddTaskListName.visibility = View.VISIBLE
                }

                ibCloseListName.setOnClickListener {
                    tvAddTaskList.visibility = View.VISIBLE
                    cvAddTaskListName.visibility = View.GONE
                }

                ibDoneListName.setOnClickListener {
                    val listName = etTaskListName.text.toString()
                    if (listName.isNotEmpty()) {
                        if (context is TaskListActivity) {
                            context.createTaskList(listName)
                        }
                    } else {
                        Toast.makeText(context, "Please enter a list name.", Toast.LENGTH_SHORT)
                            .show()
                    }
                }

                ibEditListName.setOnClickListener {
                    etEditTaskListName.setText(model.title)
                    llTitleView.visibility = View.GONE
                    cvEditTaskListName.visibility = View.VISIBLE
                }

                ibCloseEditableView.setOnClickListener {
                    llTitleView.visibility = View.VISIBLE
                    cvEditTaskListName.visibility = View.GONE
                }

                ibDoneEditListName.setOnClickListener {
                    val listName = etEditTaskListName.text.toString()
                    if (listName.isNotEmpty()) {
                        if (context is TaskListActivity) {
                            context.updateTaskList(position, listName, model)
                        }
                    } else {
                        Toast.makeText(context, "Please enter a list name.", Toast.LENGTH_SHORT)
                            .show()
                    }
                }

                ibDeleteList.setOnClickListener {
                    alertDialogForDeleteList(position, model.title)
                }

                tvAddCard.setOnClickListener {
                    tvAddCard.visibility = View.GONE
                    cvAddCard.visibility = View.VISIBLE
                }

                ibCloseCardName.setOnClickListener {
                    tvAddCard.visibility = View.VISIBLE
                    cvAddCard.visibility = View.GONE
                }

                ibDoneCardName.setOnClickListener {
                    val cardName = etCardName.text.toString()
                    if (cardName.isNotEmpty()) {
                        if (context is TaskListActivity) {
                            context.addCardToTaskList(position, cardName)
                        }
                    } else {
                        Toast.makeText(context, "Please enter a card name.", Toast.LENGTH_SHORT)
                            .show()
                    }
                }

                rvCardList.layoutManager = LinearLayoutManager(context)
                rvCardList.setHasFixedSize(true)

                val adapter = CardListItemsAdapter(context, model.cards)
                rvCardList.adapter = adapter

                adapter.setOnClickListener(
                    object : CardListItemsAdapter.OnClickListener{
                        override fun onClick(cardPosition: Int) {
                            if (context is TaskListActivity) {
                                context.cardDetails(position, cardPosition)
                            }
                        }
                    }
                )

                val dividerItemDecoration = DividerItemDecoration(
                    context,
                    DividerItemDecoration.VERTICAL)

                rvCardList.addItemDecoration(dividerItemDecoration)

                val helper = ItemTouchHelper(
                    object: ItemTouchHelper.SimpleCallback(
                        ItemTouchHelper.UP or ItemTouchHelper.DOWN, 0
                    ){
                        override fun onMove(
                            recyclerView: RecyclerView,
                            dragged: RecyclerView.ViewHolder,
                            target: RecyclerView.ViewHolder
                        ): Boolean {
                            Log.e(TAG, "move")

                            val draggedPosition = dragged.adapterPosition
                            val targetPosition = target.adapterPosition

                            if (mPositionDraggedFrom == -1)
                                mPositionDraggedFrom = draggedPosition

                            mPositionDraggedTo = targetPosition

                            Collections.swap(list[position].cards, draggedPosition, targetPosition)
                            adapter.notifyItemMoved(draggedPosition, targetPosition)
                            return false
                        }

                        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                        }

                        override fun clearView(
                            recyclerView: RecyclerView,
                            viewHolder: RecyclerView.ViewHolder
                        ) {
                            super.clearView(recyclerView, viewHolder)

                            if (mPositionDraggedFrom != -1 && mPositionDraggedTo != -1
                                && mPositionDraggedFrom != mPositionDraggedTo
                            ) {
                                (context as TaskListActivity).updateCardsInTaskList(
                                    position,
                                    list[position].cards
                                )
                            }

                            mPositionDraggedFrom = -1
                            mPositionDraggedTo = -1

                        }

                    }
                )

                helper.attachToRecyclerView(rvCardList)
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    private fun Int.toDp(): Int = (this / Resources.getSystem().displayMetrics.density).toInt()
    private fun Int.toPx(): Int = (this * Resources.getSystem().displayMetrics.density).toInt()

    private fun alertDialogForDeleteList(position: Int, title: String) {
        val builder = AlertDialog.Builder(context)

        builder.setTitle("Alert")
        builder.setMessage("Are you sure you want to delete $title.")
        builder.setIcon(android.R.drawable.ic_dialog_alert)

        builder.setPositiveButton("Yes") { dialogInterface, which ->
            dialogInterface.dismiss()

            if (context is TaskListActivity) {
                context.deleteTaskList(position)
            }
        }

        builder.setNegativeButton("No") { dialogInterface, _ ->
            dialogInterface.dismiss()
        }

        val alertDialog: AlertDialog = builder.create()

        alertDialog.setCancelable(false)
        alertDialog.show()
    }

    private class MyViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val binding = ItemTaskBinding.bind(view)
    }
}