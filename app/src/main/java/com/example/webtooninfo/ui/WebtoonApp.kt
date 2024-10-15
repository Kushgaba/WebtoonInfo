package com.example.webtooninfo.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.webtooninfo.R
import com.example.webtooninfo.data.toData

enum class Screens{StartScreen,
    BrowseWebtoons,
    WebtoonInfo,
    FavoriteWebtoons}
var canNavigateBack = false

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WebtoonApp(navController: NavHostController = rememberNavController(), viewModel: WebtoonViewModel = viewModel()){
    val backStackEntry by navController.currentBackStackEntryAsState()
    val goToApp = remember{ mutableStateOf(false) }
    val viewFavorites = { navController.navigate(Screens.FavoriteWebtoons.name){
        popUpTo(Screens.StartScreen.name)
    } }
    val goBackToHome = { navController.navigate(Screens.StartScreen.name){
        popUpTo(0)
    } }
    val currentScreen = Screens.valueOf(
        backStackEntry?.destination?.route ?: Screens.StartScreen.name
    )
    canNavigateBack = navController.previousBackStackEntry != null
    if(!goToApp.value){
        HomeScreen(
            viewModel,
            browseWebtoons = {goToApp.value = true})
    }
    else{
        Scaffold(topBar = {
            TopAppBar(title = {
                Row(modifier = Modifier
                    .padding(15.dp),
                    verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(id = R.drawable.book), contentDescription = "",
                        tint = Color(46, 171, 43, 255),
                        modifier = Modifier.size(40.dp)
                    )
                    Text(text = "Webtoon Info", fontSize = 22.sp)
                }
            }, navigationIcon = { if(canNavigateBack){
                IconButton(onClick = { navController.navigateUp() }) {
                    Icon(imageVector = Icons.Outlined.ArrowBack, contentDescription = "Back")
                }
            }})
        }, bottomBar = {
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround) {
                Column(horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.clickable{goBackToHome()}) {
                    Icon(imageVector = Icons.Filled.Home, contentDescription = "",
                        tint = Color(46, 171, 43, 255),
                        modifier = Modifier.size(33.dp))
                    Text(text = "Home", fontSize = 14.sp)
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.clickable{viewFavorites() }) {
                    Icon(imageVector = Icons.Filled.Favorite, contentDescription = "",
                        tint = Color(46, 171, 43, 255),
                        modifier = Modifier.size(33.dp))
                    Text(text = "Favourites", fontSize = 14.sp)
                }

            }
        },
            modifier = Modifier.fillMaxSize().windowInsetsPadding(WindowInsets.systemBars)) { it ->
            NavHost(navController = navController, startDestination = Screens.BrowseWebtoons.name,
                modifier = Modifier
                    .padding(it)
            ) {
                composable(route = Screens.BrowseWebtoons.name) {
                    BrowseWebtoons(viewModel = viewModel,
                        viewDetails = {
                            viewModel.checkFavorite(it)
                            navController.navigate(Screens.WebtoonInfo.name)
                        })
                }
                composable(route = Screens.WebtoonInfo.name) {
                    WebtoonDetails(viewModel)
                }
                composable(route = Screens.FavoriteWebtoons.name) {
                    viewModel.fetchWebtoon()
                    FavoriteWebtoons(viewModel,
                        viewDetails = {
                            viewModel.checkFavorite(it.toData())
                            navController.navigate(Screens.WebtoonInfo.name)
                        })
                }
            }
        }
    }
}

