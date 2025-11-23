package com.example.donateaway

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.donateaway.ui.theme.DonateAwayTheme



class MainActivity2 : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DonateAwayTheme {
                AppLoginScreen()

            }
        }
    }
}
// --- Enum for the User Type ---
enum class UserRole {
    DONOR, NGO
}

@Composable
fun AppLoginScreen() {
    // State for the Toggle (Default is Donor as requested)
    var selectedRole by remember { mutableStateOf(UserRole.DONOR) }

    // Colors from the image
    val gradientBrush = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF4A00E0), // Deep Purple
            Color(0xFF8E2DE2)  // Lighter Violet
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // 1. Top Background Section
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp) // Covers top ~35%
                .background(gradientBrush)
                .padding(top = 60.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header Navigation (Top right)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = android.R.drawable.ic_menu_directions), // Placeholder for Back Arrow
                    contentDescription = "Back",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Don't have an account?",
                        color = Color.White.copy(alpha = 0.8f),
                        fontSize = 12.sp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = { /* Handle Get Started */ },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.White.copy(alpha = 0.2f)),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 0.dp),
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier.height(32.dp)
                    ) {
                        Text("Get Started", fontSize = 12.sp, color = Color.White)
                    }
                }
            }

            Spacer(modifier = Modifier.height(30.dp))

            // Logo Text
            Text(
                text = "DONATE AWAY",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                letterSpacing = 1.sp
            )
        }

        // 2. Bottom Card Section (Overlapping)
        // We use a Box to align it to the bottom, but allow it to slide up over the header
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 220.dp) // This creates the overlap effect
        ) {
            Surface(
                modifier = Modifier.fillMaxSize(),
                shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp),
                color = Color.White,
                shadowElevation = 10.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 24.dp, vertical = 32.dp)
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    // Dynamic Title based on Toggle
                    Text(
                        text = if (selectedRole == UserRole.DONOR) "Welcome Back, Donor!" else "NGO Portal Login",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF333333)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Enter your details below",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    // Form Fields
                    CustomTextField(
                        label = "Email Address",
                        placeholder = "nicholas@ergemla.com"
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    CustomPasswordField(
                        label = "Password"
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Sign In Button with Gradient
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(gradientBrush)
                            .clickable { /* Handle Login */ },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Sign In",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Forgot your password?",
                        fontSize = 14.sp,
                        color = Color(0xFF555555),
                        fontWeight = FontWeight.Medium
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // "Or sign in with" Divider
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        HorizontalDivider(modifier = Modifier.weight(1f), color = Color.LightGray)
                        Text(
                            text = "Or sign in with",
                            modifier = Modifier.padding(horizontal = 8.dp),
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                        HorizontalDivider(modifier = Modifier.weight(1f), color = Color.LightGray)
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Google Button Only
                    OutlinedButton(
                        onClick = { /* Handle Google Login */ },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, Color.LightGray)
                    ) {
                        // In a real app, use R.drawable.ic_google
                        // Using a placeholder Icon for now
                        Image(
                            painter = painterResource(id = R.drawable.googlelogo), // This now works!
                            contentDescription = "Google Logo",
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Google",
                            color = Color.Black,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Spacer(modifier = Modifier.weight(1f)) // Pushes content up if screen is tall
                    Spacer(modifier = Modifier.height(24.dp))

                    // --- THE REQUESTED TOGGLE ROW ---
                    RoleToggleRow(
                        currentRole = selectedRole,
                        onRoleSelected = { role -> selectedRole = role }
                    )

                    Spacer(modifier = Modifier.height(48.dp))
                }
            }
        }
    }
}

// --- Helper Composable ---

@Composable
fun RoleToggleRow(
    currentRole: UserRole,
    onRoleSelected: (UserRole) -> Unit
) {
    Card(
        shape = RoundedCornerShape(50),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5)),
        modifier = Modifier
            .fillMaxWidth(0.8f) // Take up 80% of width
            .height(50.dp)

    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Donor Toggle Item
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .background(
                        if (currentRole == UserRole.DONOR) Color(0xFF4A00E0) else Color.Transparent,
                        shape = RoundedCornerShape(topStart = 50.dp, bottomStart = 50.dp)
                    )
                    .clickable { onRoleSelected(UserRole.DONOR) },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Donor",
                    color = if (currentRole == UserRole.DONOR) Color.White else Color.Gray,
                    fontWeight = FontWeight.Bold
                )
            }

            // NGO Toggle Item
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .background(
                        if (currentRole == UserRole.NGO) Color(0xFF4A00E0) else Color.Transparent,
                        shape = RoundedCornerShape(topEnd = 50.dp, bottomEnd = 50.dp)
                    )
                    .clickable { onRoleSelected(UserRole.NGO) },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "NGO",
                    color = if (currentRole == UserRole.NGO) Color.White else Color.Gray,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun CustomTextField(label: String, placeholder: String) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(text = label, fontSize = 12.sp, color = Color.Gray)
        Spacer(modifier = Modifier.height(4.dp))
        OutlinedTextField(
            value = "",
            onValueChange = {},
            placeholder = { Text(placeholder, color = Color.LightGray) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF4A00E0),
                unfocusedBorderColor = Color.LightGray.copy(alpha = 0.5f)
            ),
            singleLine = true
        )
    }
}

@Composable
fun CustomPasswordField(label: String) {
    var passwordVisible by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(text = label, fontSize = 12.sp, color = Color.Gray)
        Spacer(modifier = Modifier.height(4.dp))
        OutlinedTextField(
            value = "",
            onValueChange = {},
            placeholder = { Text("................", color = Color.LightGray) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val image = if (passwordVisible)
                    Icons.Filled.Visibility
                else Icons.Filled.VisibilityOff

                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(imageVector = image, contentDescription = null, tint = Color.Gray)
                }
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF4A00E0),
                unfocusedBorderColor = Color.LightGray.copy(alpha = 0.5f)
            ),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )
    }
}

@Preview
@Composable
fun PreviewLogin() {
    AppLoginScreen()
}