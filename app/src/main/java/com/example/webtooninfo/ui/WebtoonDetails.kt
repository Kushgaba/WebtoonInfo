package com.example.webtooninfo.ui

import android.icu.text.DecimalFormat
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.webtooninfo.data.toData
import com.example.webtooninfo.data.toLocal

@Composable
fun WebtoonDetails(viewModel: WebtoonViewModel){
    val isFavorite by viewModel.fetchLocal.collectAsState()
    val showDialog by viewModel.showDialog.collectAsState()
    if(isFavorite){
        val item by viewModel.chosenWebtoonLocal.collectAsState()
        if (showDialog) {
            item?.rating?.let {
                RatingDialog(
                    currentRating = it.toFloat(),
                    onDismiss = { viewModel.setDialogVisible(false) },
                    onSave = { newRating ->
                        viewModel.saveRating(item!!.title,newRating)
                    }
                )
            }
        }
        LazyColumn(modifier = Modifier
            .fillMaxSize()
            .padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(15.dp)) {
            item {
                AsyncImage(model = item?.image, contentDescription = "",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(280.dp),
                    contentScale = ContentScale.Crop)
            }
            item {
                item?.let {
                    Text(text = it.title, fontSize = 28.sp,
                        fontWeight = FontWeight.SemiBold)
                }
            }
            item {
                item?.let { Text(text = it.description, fontSize = 16.sp) }
            }
            item {
                Row(horizontalArrangement = Arrangement.spacedBy(25.dp),
                    verticalAlignment = Alignment.CenterVertically) {
                    Button(onClick = { viewModel.setDialogVisible(true) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(46, 171, 43, 255)),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(text = "Rate this webtoon", color = Color.White, fontSize = 16.sp)
                    }
                    Text(text = "Average Rating: ${item?.rating}", fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                }
            }
            item {
                Button(
                    onClick = { item?.let {
                        viewModel.setChosenWebtoon(it.toData())
                        viewModel.deleteWebtoon(it)
                    }
                        viewModel.setFetchLocal(false)
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor =Color(150, 200, 150, 255)
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(text = "Remove from Favorites", color = Color.Black, fontSize = 16.sp)
                }
            }
        }
    }
    else{
        val item by viewModel.chosenWebtoon.collectAsState()
        if (showDialog) {
            item?.rating?.let {
                RatingDialog(
                    currentRating = it.toFloat(),
                    onDismiss = { viewModel.setDialogVisible(false) },
                    onSave = { newRating ->
                        viewModel.saveRating(item!!.title,newRating)
                    }
                )
            }
        }
        LazyColumn(modifier = Modifier
            .fillMaxSize()
            .padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(15.dp)) {
            item {
                AsyncImage(model = item?.image, contentDescription = "",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(280.dp),
                    contentScale = ContentScale.Crop)
            }
            item {
                item?.title?.let {
                    Text(text = it, fontSize = 28.sp,
                        fontWeight = FontWeight.SemiBold)
                }
            }
            item {
                item?.let { Text(text = it.description, fontSize = 16.sp) }
            }
            item {
                val roundedRating = item?.let {
                    kotlin.math.round(it.rating * 10) / 10
                } ?: 0f
                Row(horizontalArrangement = Arrangement.spacedBy(25.dp),
                    verticalAlignment = Alignment.CenterVertically) {
                    Button(onClick = { viewModel.setDialogVisible(true) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(46, 171, 43, 255)),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(text = "Rate this webtoon", color = Color.White, fontSize = 16.sp)
                    }
                    Text(text = "Average Rating: $roundedRating", fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                }
            }
            item {
                Button(
                    onClick = { item?.let {
                        viewModel.setChosenWebtoonLocal(it.toLocal())
                        viewModel.saveWebtoon(it)
                    }
                        viewModel.setFetchLocal(true)
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(46,171,43,255)
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(text = "Add to Favorites", color = Color.White, fontSize = 16.sp)
                }
            }
        }
    }
}

@Composable
fun RatingDialog(
    currentRating: Float,
    onDismiss: () -> Unit,
    onSave: (Float) -> Unit
) {
    val decimalFormat = DecimalFormat("#.#")
    var rating by remember { mutableFloatStateOf(currentRating) }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Rate This Webtoon") },
        text = {
            Column {
                Text(text = "Select a rating:")
                Slider(
                    value = rating,
                    onValueChange = { rating = (kotlin.math.round(it * 10) / 10) },
                    valueRange = 0f..5f,
                    steps = 48,
                    colors = SliderDefaults.colors(
                        thumbColor = Color(46, 171, 43, 255),
                        activeTrackColor = Color(46, 171, 43, 255),
                        activeTickColor = Color.Transparent,
                        inactiveTrackColor = Color(211, 211, 211, 1),
                        inactiveTickColor = Color.Transparent
                    )
                )
                Text(text = rating.toString())
            }
        },
        confirmButton = {
            Button(onClick = {
                onSave((kotlin.math.round(rating * 10) / 10))
                onDismiss()
            }, colors = ButtonDefaults.buttonColors(containerColor =  Color(46, 171, 43, 255))) {
                Text("Save")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor =  Color.White,
                    contentColor = Color.Black)){
                Text("Cancel")
            }
        }
    )
}



