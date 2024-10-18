package com.shinlee.showplus.ui.screens.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.shinlee.showplus.ui.MainViewModel

@Composable
fun ProfileScreen(
    modifier: Modifier,
    viewModel: MainViewModel
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        TopBar(
            modifier= Modifier,
            userName = uiState.userName ?: "---",
            onNotificationClick = { /* TODO */ },
            onAddClick = { /* TODO */ },
            onMenuClick = { /* TODO */ }
        )
        ProfileInfo(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp),
            numberOfPosts = "0",
            numberOfEvents = "999,999",
            numberOfFollowers = "999,999",
            highLight = "a funny person in the world."
        )
        ShowPlusCandyComponent(
            modifier = Modifier.fillMaxWidth(),
            candyCount = "999,999"
        )
        ProfileActionButtons(
            modifier = Modifier.padding(top = 8.dp),
            onEditProfile = { /* TODO */ },
            onRecharge = { /* TODO */ },
            onAddPerson = { /* TODO */ }
        )
        ProfileTabs(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(top = 12.dp),
            onTabSelected = {

            }
        )
        NoPosts()
    }
}

@Composable
fun TopBar(
    modifier: Modifier,
    userName: String,
    onNotificationClick: () -> Unit = {},
    onAddClick: () -> Unit = {},
    onMenuClick: () -> Unit = {}
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier
                .weight(1f)
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = userName,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Spacer(modifier = Modifier.width(8.dp))
            Icon(
                modifier = Modifier
                    .size(12.dp),
                painter = painterResource(com.shinlee.common.R.drawable.ic_drop_down),
                contentDescription = "Dropdown",
            )
        }
        Row {
            Icon(
                modifier = Modifier
                    .size(20.dp)
                    .clickable {
                        onNotificationClick()
                    },
                painter = painterResource(id = com.shinlee.common.R.drawable.ic_notification),
                contentDescription = "Notifications"
            )
            Spacer(modifier = Modifier.width(12.dp))
            Icon(
                modifier = Modifier
                    .size(20.dp)
                    .clickable {
                        onAddClick()
                    },
                painter = painterResource(id = com.shinlee.common.R.drawable.ic_add),
                contentDescription = "Add"
            )
            Spacer(modifier = Modifier.width(12.dp))
            Icon(
                modifier = Modifier
                    .size(20.dp)
                    .clickable {
                        onMenuClick()
                    },
                painter = painterResource(id = com.shinlee.common.R.drawable.ic_menu),
                contentDescription = "Menu"
            )
        }
    }
}

@Composable
fun ProfileInfo(
    modifier: Modifier,
    numberOfPosts: String,
    numberOfEvents: String,
    numberOfFollowers: String,
    highLight: String
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = com.shinlee.common.R.drawable.avatar),
            contentDescription = "Profile Picture",
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            StatsItem(count = numberOfPosts, label = "posts")
            StatsItem(count = numberOfEvents, label = "events")
            StatsItem(count = numberOfFollowers, label = "followers")
        }
    }
    Text(text = highLight, modifier = Modifier.padding(vertical = 12.dp))
}

@Composable
fun StatsItem(count: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(count, fontWeight = FontWeight.Bold)
        Text(label)
    }
}

@Composable
fun ShowPlusCandyComponent(
    candyCount: String,
    modifier: Modifier
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0xFFF0F0F0))
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "ShowPlus Candy",
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            color = Color.Black
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                painter = painterResource(com.shinlee.common.R.drawable.pink_star_icon),
                contentDescription = "Star",
                tint = Color(0xFFFF4081),
                modifier = Modifier.size(20.dp)
            )
            Text(
                text = candyCount,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFFF4081)
            )
        }
    }
}

@Composable
fun ProfileActionButtons(
    onEditProfile: () -> Unit,
    onRecharge: () -> Unit,
    onAddPerson: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Button(
            onClick = onEditProfile,
            modifier = Modifier.weight(1f),
            colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text("Edit Profile", color = Color.Black)
        }

        Button(
            onClick = onRecharge,
            modifier = Modifier.weight(1f),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF6B6B)),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text("Recharge", color = Color.White)
        }

        IconButton(
            onClick = onAddPerson,
            modifier = Modifier.size(48.dp),
            colors = IconButtonDefaults.iconButtonColors(containerColor = Color.White),
        ) {
            Icon(
                painter = painterResource(com.shinlee.common.R.drawable.ic_add_friend),
                contentDescription = "Add Person",
                tint = Color.Black
            )
        }
    }
}

@Composable
fun ProfileTabs(
    modifier: Modifier = Modifier,
    onTabSelected: (Int) -> Unit
) {
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val tabs = listOf("Participate", "Sponsorship", "Subscription")

    Column(modifier = modifier) {
        TabRow(
            selectedTabIndex = selectedTabIndex,
            containerColor = Color.Transparent,
            contentColor = Color(0xFFFF4081),
            indicator = { tabPositions ->
                TabRowDefaults.Indicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                    height = 3.dp,
                    color = Color(0xFFFF4081)
                )
            }
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = {
                        selectedTabIndex = index
                        onTabSelected(index)
                    },
                    text = {
                        Text(
                            text = title,
                            fontSize = 12.sp,
                            fontWeight = if (selectedTabIndex == index) FontWeight.Bold else FontWeight.Normal,
                            color = if (selectedTabIndex == index) Color(0xFFFF4081) else Color.Gray
                        )
                    }
                )
            }
        }
        Divider(color = Color.LightGray, thickness = 1.dp)
    }
}

@Composable
fun NoPosts() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            painter = painterResource(id = com.shinlee.common.R.drawable.ic_camera),
            contentDescription = "Camera",
            modifier = Modifier.size(80.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text("No posts yet", style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 20.sp))
    }
}

//@Preview()
//@Composable
//fun ProfileScreenPreview() {
//    ProfileScreen(
//        userName = "Enzo",
//        modifier = Modifier
//            .fillMaxWidth()
//            .background(Color.White)
//    )
//}