package com.example.todo

import android.content.Context
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todo.databinding.ActivityMainBinding
import org.json.JSONArray

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val tasks = mutableListOf<Task>()
    private lateinit var adapter: TaskAdapter

    private val PREFS_NAME = "todo_prefs"
    private val KEY_TASKS = "tasks"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadTasks()

        adapter = TaskAdapter(tasks) { position ->
            tasks.removeAt(position)
            adapter.notifyItemRemoved(position)
            saveTasks()
            updateEmptyState()
        }

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter

        binding.buttonAdd.setOnClickListener { addTask() }

        binding.editTextTask.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                addTask()
                true
            } else false
        }

        updateEmptyState()
    }

    private fun addTask() {
        val title = binding.editTextTask.text.toString().trim()
        if (title.isEmpty()) {
            Toast.makeText(this, getString(R.string.error_empty_task), Toast.LENGTH_SHORT).show()
            return
        }
        val task = Task(title = title)
        tasks.add(0, task)
        adapter.notifyItemInserted(0)
        binding.recyclerView.scrollToPosition(0)
        binding.editTextTask.text?.clear()
        hideKeyboard()
        saveTasks()
        updateEmptyState()
    }

    private fun updateEmptyState() {
        binding.textEmpty.visibility =
            if (tasks.isEmpty()) android.view.View.VISIBLE else android.view.View.GONE
    }

    private fun hideKeyboard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.editTextTask.windowToken, 0)
    }

    // Persistence with SharedPreferences + JSON
    private fun saveTasks() {
        val json = JSONArray()
        tasks.forEach { task ->
            val obj = org.json.JSONObject()
            obj.put("id", task.id)
            obj.put("title", task.title)
            obj.put("isDone", task.isDone)
            json.put(obj)
        }
        getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit()
            .putString(KEY_TASKS, json.toString())
            .apply()
    }

    private fun loadTasks() {
        val prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val jsonString = prefs.getString(KEY_TASKS, null) ?: return
        runCatching {
            val json = JSONArray(jsonString)
            for (i in 0 until json.length()) {
                val obj = json.getJSONObject(i)
                tasks.add(
                    Task(
                        id = obj.getLong("id"),
                        title = obj.getString("title"),
                        isDone = obj.getBoolean("isDone")
                    )
                )
            }
        }
    }
}
