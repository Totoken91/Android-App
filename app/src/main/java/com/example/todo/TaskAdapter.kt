package com.example.todo

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.todo.databinding.ItemTaskBinding

class TaskAdapter(
    private val tasks: MutableList<Task>,
    private val onDelete: (Int) -> Unit
) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    inner class TaskViewHolder(val binding: ItemTaskBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val binding = ItemTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TaskViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = tasks[position]
        with(holder.binding) {
            checkboxTask.isChecked = task.isDone
            textTask.text = task.title
            updateStrikethrough(this, task.isDone)

            checkboxTask.setOnCheckedChangeListener { _, isChecked ->
                task.isDone = isChecked
                updateStrikethrough(this, isChecked)
            }

            buttonDelete.setOnClickListener {
                onDelete(holder.adapterPosition)
            }
        }
    }

    private fun updateStrikethrough(binding: ItemTaskBinding, isDone: Boolean) {
        binding.textTask.paintFlags = if (isDone) {
            binding.textTask.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        } else {
            binding.textTask.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
        }
        binding.textTask.alpha = if (isDone) 0.4f else 1.0f
    }

    override fun getItemCount() = tasks.size
}
