package com.busra.todolist.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.busra.todolist.model.Task
import com.busra.todolist.viewmodel.TaskViewModel

@Composable
fun TaskListScreen(viewModel: TaskViewModel) {
    val tasks by viewModel.tasks.collectAsState()

    var showDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf<String?>("") }
    var taskToDelete by remember { mutableStateOf<Task?>(null) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                title = ""
                description = null
                showDialog = true
            }) {
                Icon(Icons.Default.Add, contentDescription = "Add Task")
            }
        }
    ) { padding ->
        LazyColumn(contentPadding = padding) {
            items(tasks) { task ->
                TaskItem(
                    task = task,
                    onEditClick = {},
                    onDeleteClick = {
                        taskToDelete = task
                        showDeleteDialog = true
                    },
                    onCheckChange = { isChecked ->
                        viewModel.updateTaskCompletion(task.copy(isCompleted = isChecked))
                    }
                )
            }
        }
    }

    if (showDialog) {
        showTaskDialog(
            title = title,
            description = description,
            onTitleChange = { title = it },
            onDescriptionChange = { description = it },
            onAddTask = {
                if (title.isNotBlank()) {
                    viewModel.addTask(title, description)
                    showDialog = false
                }
            },
            onDismiss = { showDialog = false }
        )
    }

    if (showDeleteDialog && taskToDelete != null) {
        DeleteTaskDialog(
            onConfirmDelete = {
                taskToDelete?.let { viewModel.deleteTask(it) }
                showDeleteDialog = false
                taskToDelete = null
            },
            onDismiss = {
                showDeleteDialog = false
                taskToDelete = null
            }
        )
    }
}

@Composable
fun TaskItem(
    task: Task,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onCheckChange: (Boolean) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.padding(16.dp)
        ) {
            Column {
                Text(text = task.title, style = MaterialTheme.typography.headlineMedium)
                if (!task.description.isNullOrEmpty()) {
                    Text(text = task.description.orEmpty(), style = MaterialTheme.typography.bodyMedium)
                }
            }
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = task.isCompleted,
                    onCheckedChange = onCheckChange
                )
                IconButton(onClick = onEditClick) {
                    Icon(Icons.Default.Edit, contentDescription = "Edit Task")
                }
                IconButton(onClick = onDeleteClick) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete Task")
                }
            }
        }
    }
}

@Composable
fun showTaskDialog(
    title: String,
    description: String?,
    onTitleChange: (String) -> Unit,
    onDescriptionChange: (String?) -> Unit,
    onAddTask: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text(text = "Add New Task") },
        text = {
            Column {
                OutlinedTextField(
                    value = title,
                    onValueChange = { onTitleChange(it) },
                    label = { Text("Title") }
                )
                OutlinedTextField(
                    value = description.orEmpty(),
                    onValueChange = { onDescriptionChange(it) },
                    label = { Text("Description") }
                )
            }
        },
        confirmButton = {
            Button(onClick = onAddTask) {
                Text("Add Task")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun DeleteTaskDialog(onConfirmDelete: () -> Unit, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Görev Silme İşlemi") },
        text = { Text("Görevi silmek istiyor musunuz?") },
        confirmButton = {
            Button(onClick = onConfirmDelete) {
                Text("Evet")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Hayır")
            }
        }
    )
}
