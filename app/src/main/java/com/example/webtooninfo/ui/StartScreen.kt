package com.example.webtooninfo.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.webtooninfo.R

@Composable
fun HomeScreen(viewModel: WebtoonViewModel, browseWebtoons:() -> Unit){
    val progress by viewModel.progress.collectAsState()
    val isLoaded by viewModel.isLoaded.collectAsState()
    Column(modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(modifier = Modifier
            .fillMaxWidth()
            .height(140.dp))
        Image(painter = painterResource(id = R.drawable.loading_screen_image),
            contentDescription = "",
            modifier = Modifier.size(200.dp))
        Spacer(modifier = Modifier
            .fillMaxWidth()
            .height(75.dp))
        Text(text = "Fantasy", fontSize = 32.sp,
            fontWeight = FontWeight.Bold)
        Text(text = "Webtoon Info", fontSize = 22.sp)
        Spacer(modifier = Modifier
            .fillMaxWidth()
            .height(110.dp))
        Box(modifier = Modifier
            .size(60.dp)
            .clickable { if (isLoaded) browseWebtoons() }
            .background(
                color = if(!isLoaded)Color(150, 200, 150, 255)
                        else Color(46, 171, 43, 255),
                shape = RoundedCornerShape(8.dp)
            ),
            contentAlignment = Alignment.Center) {
            if(isLoaded)
            Icon(imageVector = Icons.Filled.ArrowForward, contentDescription = "",
                tint = Color.White,
                modifier = Modifier.size(35.dp))
            else
                CircularProgressIndicator(progress = progress,color = Color.White)
        }
    }
}