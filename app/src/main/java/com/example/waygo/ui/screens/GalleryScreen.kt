package com.example.waygo.ui.screens

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.waygo.ui.states.Trip
import com.example.waygo.utils.copyUriInternal
import com.example.waygo.utils.saveBitmapInternal
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GalleryScreen(
    trip: Trip,
    onBack: () -> Unit,
    onAddImage: (Uri) -> Unit,
    onDeleteImage: (Uri) -> Unit      // ← now only Uri
) {
    val context = LocalContext.current
    var showSheet by remember { mutableStateOf(false) }

    /* Dialog state */
    var zoomUri by remember { mutableStateOf<Uri?>(null) }
    var infoUri by remember { mutableStateOf<Uri?>(null) }
    var pendingDelete by remember { mutableStateOf<Uri?>(null) }

    /* Launchers */
    val takePicture = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicturePreview()
    ) { bmp -> bmp?.let { onAddImage(saveBitmapInternal(context, it)) } }

    val pickImage = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri -> uri?.let { onAddImage(copyUriInternal(context, it)) } }

    /* Scaffold */
    Scaffold(
        topBar = {
            SmallTopAppBar(
                title = { Text(trip.title) },
                navigationIcon = { IconButton(onClick = onBack) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }}
            )
        },
        floatingActionButton = {
            LargeFloatingActionButton(onClick = { showSheet = true }) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        }
    ) { padding ->
        if (trip.images.isEmpty()) {
            Box(Modifier.padding(padding).fillMaxSize(), Alignment.Center) {
                Text("No images yet")
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier.padding(padding).fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(4.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(trip.images) { uri ->
                    Box {
                        /* Thumbnail */
                        AsyncImage(
                            model = uri, contentDescription = null,
                            modifier = Modifier.aspectRatio(1f).fillMaxWidth()
                                .clickable { zoomUri = uri },
                            contentScale = ContentScale.Crop
                        )
                        /* Overlay buttons */
                        Row(
                            Modifier.align(Alignment.TopEnd)
                                .background(MaterialTheme.colorScheme.onSurfaceVariant.copy(.4f))
                        ) {
                            IconButton(onClick = { zoomUri = uri }, modifier = Modifier.size(28.dp)) {
                                Icon(Icons.Default.ZoomIn, contentDescription = "Zoom",
                                    tint = MaterialTheme.colorScheme.surface)
                            }
                            IconButton(onClick = { infoUri = uri }, modifier = Modifier.size(28.dp)) {
                                Icon(Icons.Default.Info, contentDescription = "Info",
                                    tint = MaterialTheme.colorScheme.surface)
                            }
//                            IconButton(onClick = { onDeleteImage(uri) }, modifier = Modifier.size(28.dp)) {
//                                Icon(Icons.Default.Delete, contentDescription = "Delete",
//                                    tint = MaterialTheme.colorScheme.surface)
//                            }
                            IconButton(
                                onClick = { pendingDelete = uri },          // ⬅️ ask first
                                modifier = Modifier.size(28.dp)
                            ) {
                                Icon(Icons.Default.Delete, contentDescription = "Delete",
                                    tint = MaterialTheme.colorScheme.surface)
                            }
                        }
                    }
                }
            }
        }
    }

    /* ---------- Delete confirmation dialog ---------- */
    pendingDelete?.let { uri ->
        AlertDialog(
            onDismissRequest = { pendingDelete = null },
            title  = { Text("Delete image?") },
            text   = { Text("This action cannot be undone.") },
            confirmButton = {
                TextButton(onClick = {
                    onDeleteImage(uri)     // <- real delete
                    pendingDelete = null   // close dialog
                }) { Text("Delete") }
            },
            dismissButton = {
                TextButton(onClick = { pendingDelete = null }) { Text("Cancel") }
            }
        )
    }

    /* Bottom-sheet */
    if (showSheet) {
        ModalBottomSheet(onDismissRequest = { showSheet = false }) {
            ListItem(headlineContent = { Text("Take photo") },
                modifier = Modifier.clickable {
                    takePicture.launch(null); showSheet = false
                })
            ListItem(headlineContent = { Text("Choose from gallery") },
                modifier = Modifier.clickable {
                    pickImage.launch("image/*"); showSheet = false
                })
        }
    }

    /* Zoom dialog */
    zoomUri?.let { uri ->
        AlertDialog(onDismissRequest = { zoomUri = null },
            confirmButton = {},
            text = {
                AsyncImage(
                    model = ImageRequest.Builder(context).data(uri).crossfade(true).build(),
                    contentDescription = null,
                    modifier = Modifier.fillMaxWidth(),
                    contentScale = ContentScale.Fit
                )
            })
    }

    /* Info dialog */
    infoUri?.let { uri ->
        val meta by produceState(initialValue = "Loading…", uri) {
            value = getMeta(context, uri)
        }
        AlertDialog(
            onDismissRequest = { infoUri = null },
            confirmButton = { TextButton(onClick = { infoUri = null }) { Text("Close") } },
            title = { Text("Image info") },
            text  = { Text(meta) }
        )
    }
}

/* -- metadata helper -- */
private suspend fun getMeta(context: Context, uri: Uri): String =
    withContext(Dispatchers.IO) {
        runCatching {
            uri.path?.let { path ->
                val file = File(path)
                val sizeKb = file.length() / 1024
                val date = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
                    .format(Date(file.lastModified()))

                "URI:\n$uri\n\nSize: $sizeKb KB\nDate: $date"
            }
        }.getOrNull() ?: "Could not read metadata"
    }