package com.busra.todolist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.busra.todolist.roomdb.AppDatabase
import com.busra.todolist.roomdb.TaskRepository
import com.busra.todolist.screens.TaskListScreen
import com.busra.todolist.ui.theme.ToDoListTheme
import com.busra.todolist.viewmodel.TaskViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val database = AppDatabase.getDatabase(this)
        val repository = TaskRepository(database.taskDao())
        val viewModel = TaskViewModel(repository)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ToDoListTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {
                        TaskListScreen(viewModel = viewModel)
                    }
                }
            }
        }
    }
}

