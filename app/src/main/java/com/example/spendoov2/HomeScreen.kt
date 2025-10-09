//package com.example.spendoov2
//
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.size
//import androidx.compose.foundation.layout.width
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.unit.dp
//import androidx.navigation.NavController
//import com.example.spendoov2.ui.theme.interTextStyle
//import java.time.format.TextStyle
//
//@Composable
//fun HomeScreen(navController: NavController) {
//    var contentType by remember { mutableStateOf("all") }
//
//    Column(modifier = Modifier.fillMaxSize()) {
//        HomeTopBanner(
//            currentTab = contentType,
//            onTabSelected = { newTab -> contentType = newTab },
//            onSearchClicked = { navController.navigate("search") }
//        )
//        // Pass the tab content to the home page body
//        HomePageContent(content = contentType)
//
//        BottomNav(
//            onClick = { action ->
//                contentType = action
//            }
//        )
//    }
//}
//
//@Composable
//fun HomeTopBanner(
//    onNavigateToPage:
//    textStyle: TextStyle = interTextStyle,
//    modifier: Modifier = Modifier) {
//    Row(
//        horizontalArrangement = Arrangement.SpaceBetween,
//        verticalAlignment = Alignment.CenterVertically,
//        modifier = modifier
//            .fillMaxWidth()
//    ) {
//        LogoContainer(textStyle)
//        Row(
//            verticalAlignment = Alignment.CenterVertically,
//            horizontalArrangement = Arrangement.SpaceBetween,
//            modifier = modifier
//                .width(50.dp)
//                .size(38.dp)
//        ) {
//            Image(
//                painter = painterResource(R.drawable.search_icon),
//                contentDescription = "Search",
//                modifier = modifier
//                    .clickable { onNavigateToPage("search") }
//            )
//            Image(
//                painter = painterResource(R.drawable.dot_option),
//                contentDescription = null
//            )
//        }
//    }
//    Spacer(modifier = modifier.padding(12.dp))
//    NavBarHome(
//        currentTab = contentTabs,
//        onClick = onClick
//    )
//}