package com.example.projemanag.adapters

import android.content.Context
import android.content.res.Resources
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.projemanag.R
import com.example.projemanag.activities.TaskListActivity
import com.example.projemanag.databinding.ItemBoardBinding
import com.example.projemanag.databinding.ItemTaskBinding
import com.example.projemanag.models.Task
import com.example.projemanag.utils.Constants.TAG
import com.google.rpc.context.AttributeContext

class TaskListItemsAdapter(private val context: Context,
                           private val list: ArrayList<Task>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

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
                        Toast.makeText(context, "Please enter list name.", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    private fun Int.toDp(): Int = (this / Resources.getSystem().displayMetrics.density).toInt()
    private fun Int.toPx(): Int = (this * Resources.getSystem().displayMetrics.density).toInt()

    private class MyViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val binding = ItemTaskBinding.bind(view)
    }
}