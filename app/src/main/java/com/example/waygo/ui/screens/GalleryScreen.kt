package com.example.waygo.ui.screens

import android.content.Context
import android.net.Uri
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.waygo.R
import com.example.waygo.utils.copyUriInternal
import com.example.waygo.utils.saveBitmapInternal
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GalleryScreen(
    trip: com.example.waygo.domain.model.Trip,
    onBack: () -> Unit,
    onAddImage: (Uri) -> Unit,
    onDeleteImage: (Uri) -> Unit
) {
    val context = LocalContext.current
    var showSheet by remember { mutableStateOf(false) }

    var zoomUri by remember { mutableStateOf<Uri?>(null) }
    var infoUri by remember { mutableStateOf<Uri?>(null) }
    var pendingDelete by remember { mutableStateOf<Uri?>(null) }

    val takePicture = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        bitmap?.let { bmp ->
            val savedUri = saveBitmapInternal(context, bmp)
            onAddImage(savedUri)
        }
    }

    val pickImage = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let { pickedUri ->
            val copiedUri = copyUriInternal(context, pickedUri)
            onAddImage(copiedUri)
        }
    }

    Scaffold(
        topBar = {
            SmallTopAppBar(
                title = { Text(trip.name) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        floatingActionButton = {
            LargeFloatingActionButton(onClick = { showSheet = true }) {
                Icon(Icons.Default.Add, contentDescription = "Add Image")
            }
        }
    ) { padding ->
        if (trip.images.isEmpty()) {
            Box(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = stringResource(id = R.string.no_images_yet))
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(4.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(trip.images) { uri ->
                    Box {
                        AsyncImage(
                            model = uri,
                            contentDescription = "Trip image",
                            modifier = Modifier
                                .aspectRatio(1f)
                                .clickable { zoomUri = uri },
                            contentScale = ContentScale.Crop
                        )
                        Row(
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .background(MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f))
                        ) {
                            IconButton(onClick = { zoomUri = uri }, modifier = Modifier.size(28.dp)) {
                                Icon(
                                    Icons.Default.ZoomIn,
                                    contentDescription = "Zoom image",
                                    tint = MaterialTheme.colorScheme.surface
                                )
                            }
                            IconButton(onClick = { infoUri = uri }, modifier = Modifier.size(28.dp)) {
                                Icon(
                                    Icons.Default.Info,
                                    contentDescription = "Image info",
                                    tint = MaterialTheme.colorScheme.surface
                                )
                            }
                            IconButton(onClick = { pendingDelete = uri }, modifier = Modifier.size(28.dp)) {
                                Icon(
                                    Icons.Default.Delete,
                                    contentDescription = "Delete image",
                                    tint = MaterialTheme.colorScheme.surface
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    pendingDelete?.let { uri ->
        AlertDialog(
            onDismissRequest = { pendingDelete = null },
            title = { Text("Delete image?") },
            text = { Text("This action cannot be undone.") },
            confirmButton = {
                TextButton(onClick = {
                    onDeleteImage(uri)
                    pendingDelete = null
                }) { Text("Delete") }
            },
            dismissButton = {
                TextButton(onClick = { pendingDelete = null }) { Text("Cancel") }
            }
        )
    }

    if (showSheet) {
        ModalBottomSheet(onDismissRequest = { showSheet = false }) {
            ListItem(
                headlineContent = { Text("Take photo") },
                modifier = Modifier.clickable {
                    takePicture.launch(null)
                    showSheet = false
                }
            )
            ListItem(
                headlineContent = { Text("Choose from gallery") },
                modifier = Modifier.clickable {
                    pickImage.launch("image/*")
                    showSheet = false
                }
            )
        }
    }

    zoomUri?.let { uri ->
        AlertDialog(
            onDismissRequest = { zoomUri = null },
            confirmButton = {},
            text = {
                AsyncImage(
                    model = ImageRequest.Builder(context).data(uri).crossfade(true).build(),
                    contentDescription = "Zoomed image",
                    modifier = Modifier.fillMaxWidth(),
                    contentScale = ContentScale.Fit
                )
            }
        )
    }

    infoUri?.let { uri ->
        val meta by produceState(initialValue = "Loading…", uri) {
            value = getMeta(context, uri)
        }
        AlertDialog(
            onDismissRequest = { infoUri = null },
            confirmButton = { TextButton(onClick = { infoUri = null }) { Text("Close") } },
            title = { Text("Image info") },
            text = { Text(meta) }
        )
    }
}

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

@Composable
fun GalleryScreenWrapper(
    viewModel: com.example.waygo.ui.viewmodel.TripViewModel,
    tripId: Int,
    onBack: () -> Unit
) {
    val trips by viewModel.trips.collectAsState()

    val trip = trips.find { it.id == tripId }

    if (trip != null) {
        GalleryScreen(
            trip = trip,
            onBack = onBack,
            onAddImage = { uri -> viewModel.addImageToTrip(tripId, uri) },
            onDeleteImage = { uri -> viewModel.removeImageFromTrip(tripId, uri) }
        )
    } else {
        // Show a loading or empty state while trip is not loaded
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Loading trip...")
        }
    }
}
