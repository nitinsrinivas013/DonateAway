package com.example.donateaway

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ExitToApp
import androidx.compose.material.icons.automirrored.outlined.Help
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// --- Enums for Dashboard Screens ---
enum class NGOScreen {
    FEED, PICKUPS, HISTORY, PROFILE
}

// --- Data Models ---
data class DonationItem(
    val id: Int,
    val donorName: String,
    val foodItems: String,
    val quantity: String,
    val distance: String,
    val timeAgo: String,
    val isVeg: Boolean,
    val imageColor: Color
)

data class PickupItem(
    val id: Int,
    val donorName: String,
    val address: String,
    val status: String, // "Accepted", "On the way"
    val contact: String
)

data class HistoryItem(
    val id: Int,
    val donorName: String,
    val date: String,
    val impact: String // e.g., "Fed 20 people"
)

// --- MAIN CONTAINER ---
@Composable
fun NGODashboardScreen() {
    var currentScreen by remember { mutableStateOf(NGOScreen.FEED) }

    Scaffold(
        bottomBar = {
            NGO_BottomNavigation(
                currentScreen = currentScreen,
                onScreenSelected = { currentScreen = it }
            )
        },
        floatingActionButton = {
            if (currentScreen == NGOScreen.FEED) {
                FloatingActionButton(
                    onClick = { /* Refresh */ },
                    containerColor = Color(0xFF4A00E0),
                    contentColor = Color.White
                ) {
                    Icon(Icons.Filled.Refresh, "Refresh")
                }
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            when (currentScreen) {
                NGOScreen.FEED -> NGOFeedContent()
                NGOScreen.PICKUPS -> NGOPickupsContent()
                NGOScreen.HISTORY -> NGOHistoryContent()
                NGOScreen.PROFILE -> NGOProfileContent()
            }
        }
    }
}

// ==========================================
// 1. FEED SCREEN (The Original Dashboard)
// ==========================================
@Composable
fun NGOFeedContent() {
    val brandGradient = Brush.verticalGradient(
        colors = listOf(Color(0xFF4A00E0), Color(0xFF8E2DE2))
    )
    val donations = listOf(
        DonationItem(1, "Tasty Bites Restaurant", "Fried Rice, Manchurian", "Serves 15", "1.2 km", "10 min ago", true, Color(0xFFFFCC80)),
        DonationItem(2, "John Doe", "Homemade Pasta", "Serves 4", "0.5 km", "25 min ago", false, Color(0xFFEF9A9A)),
        DonationItem(3, "City Bakery", "Breads & Pastries", "5 kg", "3.0 km", "1 hr ago", true, Color(0xFFCE93D8)),
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
    ) {
        // Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .clip(RoundedCornerShape(bottomStart = 30.dp, bottomEnd = 30.dp))
                .background(brandGradient)
                .padding(24.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Hello, Helping Hands!", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Filled.LocationOn, null, tint = Color.White.copy(0.7f), modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("New York, USA", color = Color.White.copy(0.7f), fontSize = 12.sp)
                        }
                    }
                    IconButton(onClick = {}) { Icon(Icons.Filled.Notifications, null, tint = Color.White) }
                }
                Spacer(modifier = Modifier.height(24.dp))
                Row(modifier = Modifier.fillMaxWidth()) {
                    StatCard("12", "Pending", Modifier.weight(1f))
                    Spacer(modifier = Modifier.width(12.dp))
                    StatCard("850", "Lives Touched", Modifier.weight(1f))
                }
            }
        }

        PaddingLabel("Available Donations Nearby")

        LazyColumn(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(donations) { donation -> DonationCard(donation) }
            item { Spacer(modifier = Modifier.height(24.dp)) }
        }
    }
}

// ==========================================
// 2. PICKUPS SCREEN (Active Tasks)
// ==========================================
@Composable
fun NGOPickupsContent() {
    val pickups = listOf(
        PickupItem(1, "Wedding Banquet Hall", "123 Event St, Downtown", "Accepted", "555-0123"),
        PickupItem(2, "Star Coffee House", "45 Park Ave", "On the way", "555-0199")
    )

    Column(modifier = Modifier.fillMaxSize().background(Color(0xFFF5F5F5))) {
        SimpleHeader(title = "Active Pickups")

        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(pickups) { item ->
                PickupCard(item)
            }
        }
    }
}

@Composable
fun PickupCard(item: PickupItem) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                Column {
                    Text(item.donorName, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Text(item.address, color = Color.Gray, fontSize = 13.sp)
                }
                Box(
                    modifier = Modifier
                        .background(Color(0xFFE8EAF6), RoundedCornerShape(8.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(item.status, color = Color(0xFF3F51B5), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row {
                Button(
                    onClick = {},
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE0F2F1), contentColor = Color(0xFF00796B))
                ) {
                    Icon(Icons.Filled.Call, null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Call")
                }
                Spacer(modifier = Modifier.width(12.dp))
                Button(
                    onClick = {},
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4A00E0))
                ) {
                    Icon(Icons.Filled.Navigation, null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Navigate")
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            OutlinedButton(
                onClick = { /* Mark complete logic */ },
                modifier = Modifier.fillMaxWidth(),
                border = BorderStroke(1.dp, Color(0xFF4CAF50)),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF4CAF50))
            ) {
                Text("Mark as Collected")
            }
        }
    }
}

// ==========================================
// 3. HISTORY SCREEN (Past Donations)
// ==========================================
@Composable
fun NGOHistoryContent() {
    val history = listOf(
        HistoryItem(1, "Fresh Mart", "Oct 24, 2023", "Fed 45 people"),
        HistoryItem(2, "Pizza Hut", "Oct 22, 2023", "Fed 12 people"),
        HistoryItem(3, "Community Event", "Oct 20, 2023", "Fed 100 people")
    )

    Column(modifier = Modifier.fillMaxSize().background(Color(0xFFF5F5F5))) {
        SimpleHeader(title = "Donation History")

        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(history) { item ->
                HistoryCard(item)
            }
        }
    }
}

@Composable
fun HistoryCard(item: HistoryItem) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(1.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(Color(0xFFF5F5F5), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Outlined.Check, null, tint = Color.Green)
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(item.donorName, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text(item.date, color = Color.Gray, fontSize = 12.sp)
            }
            Text(item.impact, color = Color(0xFF4A00E0), fontWeight = FontWeight.Bold, fontSize = 14.sp)
        }
    }
}

// ==========================================
// 4. PROFILE SCREEN (Settings & Info)
// ==========================================
@Composable
fun NGOProfileContent() {
    val brandGradient = Brush.verticalGradient(
        colors = listOf(Color(0xFF4A00E0), Color(0xFF8E2DE2))
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(rememberScrollState())
    ) {
        // Header overlapping style
        Box(modifier = Modifier.height(280.dp)) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(brandGradient)
            )

            // Profile Card overlapping
            Card(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(horizontal = 24.dp)
                    .fillMaxWidth()
                    .height(140.dp),
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(8.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text("Helping Hands Foundation", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                    Text("ID: NGO-882190", color = Color.Gray, fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Filled.Phone, null, modifier = Modifier.size(16.dp), tint = Color.Gray)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("+1 234 567 8900", color = Color.Gray, fontSize = 14.sp)
                    }
                }
            }

            // Avatar on top of card
            Box(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 130.dp) // Halfway logic
                    .size(80.dp)
                    .border(4.dp, Color.White, CircleShape)
                    .background(Color.LightGray, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text("H", fontSize = 32.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Menu Options
        Column(modifier = Modifier.padding(horizontal = 24.dp)) {
            ProfileOptionItem(icon = Icons.Outlined.Person, title = "Edit Profile")
            ProfileOptionItem(icon = Icons.Outlined.Notifications, title = "Notifications")
            ProfileOptionItem(icon = Icons.Outlined.Settings, title = "Settings")
            ProfileOptionItem(icon = Icons.AutoMirrored.Outlined.Help, title = "Help & Support")

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { /* Logout logic */ },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFEBEE), contentColor = Color.Red),
                modifier = Modifier.fillMaxWidth().height(50.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.AutoMirrored.Outlined.ExitToApp, null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Log Out")
            }

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Composable
fun ProfileOptionItem(icon: ImageVector, title: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { }
            .padding(vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, null, tint = Color(0xFF555555))
        Spacer(modifier = Modifier.width(16.dp))
        Text(title, fontSize = 16.sp, color = Color(0xFF333333), modifier = Modifier.weight(1f))
        Icon(Icons.Filled.ChevronRight, null, tint = Color.LightGray)
    }
    HorizontalDivider(Modifier, DividerDefaults.Thickness, color = Color(0xFFF5F5F5))
}


// ==========================================
// SHARED COMPONENTS
// ==========================================

@Composable
fun SimpleHeader(title: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .background(Brush.verticalGradient(colors = listOf(Color(0xFF4A00E0), Color(0xFF8E2DE2))))
            .padding(start = 24.dp, bottom = 24.dp),
        contentAlignment = Alignment.BottomStart
    ) {
        Text(title, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 24.sp)
    }
}

@Composable
fun NGO_BottomNavigation(currentScreen: NGOScreen, onScreenSelected: (NGOScreen) -> Unit) {
    NavigationBar(
        containerColor = Color.White,
        tonalElevation = 8.dp
    ) {
        NavigationBarItem(
            icon = { Icon(if(currentScreen == NGOScreen.FEED) Icons.Filled.Home else Icons.Outlined.Home, null) },
            label = { Text("Feed") },
            selected = currentScreen == NGOScreen.FEED,
            onClick = { onScreenSelected(NGOScreen.FEED) },
            colors = NavigationBarItemDefaults.colors(selectedIconColor = Color(0xFF4A00E0), indicatorColor = Color(0xFFEDE7F6))
        )
        NavigationBarItem(
            icon = { Icon(if(currentScreen == NGOScreen.PICKUPS) Icons.Filled.LocalShipping else Icons.Outlined.LocalShipping, null) },
            label = { Text("Pickups") },
            selected = currentScreen == NGOScreen.PICKUPS,
            onClick = { onScreenSelected(NGOScreen.PICKUPS) },
            colors = NavigationBarItemDefaults.colors(selectedIconColor = Color(0xFF4A00E0), indicatorColor = Color(0xFFEDE7F6))
        )
        NavigationBarItem(
            icon = { Icon(if(currentScreen == NGOScreen.HISTORY) Icons.Filled.History else Icons.Outlined.History, null) },
            label = { Text("History") },
            selected = currentScreen == NGOScreen.HISTORY,
            onClick = { onScreenSelected(NGOScreen.HISTORY) },
            colors = NavigationBarItemDefaults.colors(selectedIconColor = Color(0xFF4A00E0), indicatorColor = Color(0xFFEDE7F6))
        )
        NavigationBarItem(
            icon = { Icon(if(currentScreen == NGOScreen.PROFILE) Icons.Filled.Person else Icons.Outlined.Person, null) },
            label = { Text("Profile") },
            selected = currentScreen == NGOScreen.PROFILE,
            onClick = { onScreenSelected(NGOScreen.PROFILE) },
            colors = NavigationBarItemDefaults.colors(selectedIconColor = Color(0xFF4A00E0), indicatorColor = Color(0xFFEDE7F6))
        )
    }
}

// --- MISSING COMPONENTS RESTORED ---

@Composable
fun StatCard(number: String, label: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.2f))
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = number, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 20.sp)
            Text(text = label, color = Color.White.copy(alpha = 0.8f), fontSize = 12.sp)
        }
    }
}

@Composable
fun PaddingLabel(text: String) {
    Text(
        text = text,
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp,
        color = Color(0xFF333333),
        modifier = Modifier.padding(start = 24.dp, top = 24.dp, bottom = 12.dp)
    )
}

@Composable
fun DonationCard(item: DonationItem) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Row 1: Image Placeholder + Title + Time
            Row(verticalAlignment = Alignment.Top) {
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(item.imageColor),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = item.donorName.first().toString(), fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(text = item.donorName, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color(0xFF333333))
                    Spacer(modifier = Modifier.height(4.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(12.dp)
                                .border(1.dp, if(item.isVeg) Color.Green else Color.Red, CircleShape)
                                .padding(2.dp)
                                .background(if(item.isVeg) Color.Green else Color.Red, CircleShape)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = if(item.isVeg) "Veg" else "Non-Veg", fontSize = 12.sp, color = Color.Gray)

                        Text(text = " • ", color = Color.Gray)
                        Text(text = item.timeAgo, fontSize = 12.sp, color = Color.Gray)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(text = item.foodItems, fontWeight = FontWeight.Medium, fontSize = 14.sp, color = Color.Black)
            Text(text = "Quantity: ${item.quantity}", fontSize = 13.sp, color = Color.Gray)

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(thickness = 1.dp, color = Color(0xFFF0F0F0))
            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Filled.NearMe, contentDescription = null, tint = Color(0xFF4A00E0), modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = item.distance, color = Color(0xFF4A00E0), fontWeight = FontWeight.Bold, fontSize = 14.sp)
                }

                Button(
                    onClick = { /* Handle Claim */ },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4A00E0)),
                    shape = RoundedCornerShape(20.dp),
                    contentPadding = PaddingValues(horizontal = 24.dp, vertical = 8.dp),
                    modifier = Modifier.height(36.dp)
                ) {
                    Text("Claim", fontSize = 12.sp)
                }
            }
        }
    }
}