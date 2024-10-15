package com.example.webtooninfo.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.webtooninfo.data.WebtoonData

@Composable
fun BrowseWebtoons(viewModel: WebtoonViewModel, viewDetails: (WebtoonData) -> Unit){
    val webtoons by viewModel.webtoonsDownloaded.collectAsState()
    LazyColumn(modifier = Modifier.padding(15.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)) {
            items(webtoons){
                WebtoonCard(item = it, viewDetails)
            }
        }
}

@Composable
fun WebtoonCard(item: WebtoonData,
                viewDetails:(WebtoonData) -> Unit){
    Card(modifier = Modifier
        .padding(horizontal = 10.dp)
        .clickable { viewDetails(item) },
        border = BorderStroke(2.dp,Color(46, 171, 43, 255))
    ) {
        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)){
            AsyncImage(model = item.image, contentDescription = "",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .clip(RoundedCornerShape(5.dp)),
                contentScale = ContentScale.Crop)
            Text(text = item.title, fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold)
            Text(text = "By ${item.author}", fontSize = 14.sp)
        }
    }
}