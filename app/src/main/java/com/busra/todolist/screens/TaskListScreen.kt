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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
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
import androidx.compose.ui.text.style.TextOverflow
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
                    onDeleteClick = {
                        taskToDelete = task
                        showDeleteDialog = true
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
    onDeleteClick: () -> Unit
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.padding(16.dp)
        ) {
            // Başlık ve açıklama text'leri alt alta, açıklama kelime sınırıyla
            Column(
                modifier = Modifier
                    .weight(1f)  // Sağdaki silme butonunun yerini etkilememek için
                    .padding(start = 8.dp)
            ) {
                Text(text = task.title, style = MaterialTheme.typography.headlineMedium)

                // Description text, kelime sınırı ve taşma ayarları
                if (!task.description.isNullOrEmpty()) {
                    Text(
                        text = task.description.orEmpty(),
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = 2,  // İki satıra kadar göster
                        overflow = TextOverflow.Ellipsis  // Taşarsa '...' ekle
                    )
                }
            }

            // Sağdaki silme butonu
            IconButton(onClick = onDeleteClick) {
                Icon(Icons.Default.Delete, contentDescription = "Delete Task")
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
        title = { Text(text = "Yeni Bir Görev Ekle") },
        text = {
            Column {
                OutlinedTextField(
                    value = title,
                    onValueChange = { onTitleChange(it) },
                    label = { Text("Başlık") }
                )
                OutlinedTextField(
                    value = description.orEmpty(),
                    onValueChange = { onDescriptionChange(it) },
                    label = { Text("Açıklama") }
                )
            }
        },
        confirmButton = {
            Button(onClick = onAddTask) {
                Text("Görevi Ekle")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("İptal Et")
            }
        }
    )
}

@Composable
fun DeleteTaskDialog(onConfirmDelete: () -> Unit, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Görev Sil") },
        text = { Text("Görevi silmek istiyor musunuz?") },
        confirmButton = {
            Button(onClick = onConfirmDelete) {
                Text("Evet")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Hayır") }
        }
    )
}
