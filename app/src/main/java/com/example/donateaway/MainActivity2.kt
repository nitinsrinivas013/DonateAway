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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.donateaway.ui.theme.DonateAwayTheme

class MainActivity2 : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DonateAwayTheme {
                AppNavigation()
            }
        }
    }
}

// --- Navigation Enum ---
enum class AuthScreen {
    LOGIN, SIGNUP_DONOR, SIGNUP_NGO, DASHBOARD_NGO
}

// --- User Roles ---
enum class UserRole {
    DONOR, NGO
}

// --- Donor Sub-Roles ---
enum class DonorType {
    INDIVIDUAL, RESTAURANT
}

@Composable
fun AppNavigation() {
    var currentScreen by remember { mutableStateOf(AuthScreen.LOGIN) }

    // We keep track of the role selected on the login page to know which signup to open
    var lastSelectedRole by remember { mutableStateOf(UserRole.DONOR) }

    when (currentScreen) {
        AuthScreen.LOGIN -> LoginScreen(
            onNavigateToSignup = { role ->
                lastSelectedRole = role
                if (role == UserRole.NGO) {
                    currentScreen = AuthScreen.SIGNUP_NGO
                } else {
                    currentScreen = AuthScreen.SIGNUP_DONOR
                }
            },
            onLoginSuccess = { role ->
                if (role == UserRole.NGO) {
                    currentScreen = AuthScreen.DASHBOARD_NGO
                }
                // Add logic for Donor Dashboard here later if needed
            }
        )
        AuthScreen.SIGNUP_DONOR -> DonorSignupScreen(
            onNavigateToLogin = { currentScreen = AuthScreen.LOGIN }
        )
        AuthScreen.SIGNUP_NGO -> NGOSignupScreen(
            onNavigateToLogin = { currentScreen = AuthScreen.LOGIN },
            onSignUpSuccess = { currentScreen = AuthScreen.DASHBOARD_NGO }
        )
        AuthScreen.DASHBOARD_NGO -> NGODashboardScreen()
    }
}

// --- 1. REUSABLE BASE LAYOUT ---
@Composable
fun AuthLayout(
    title: String,
    subtitle: String,
    topBarContent: @Composable () -> Unit,
    content: @Composable ColumnScope.() -> Unit
) {
    val gradientBrush = Brush.verticalGradient(
        colors = listOf(Color(0xFF4A00E0), Color(0xFF8E2DE2))
    )

    Box(modifier = Modifier.fillMaxSize().background(Color.White)) {
        // Top Gradient Section
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .background(gradientBrush)
                .padding(top = 60.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            topBarContent()
            Spacer(modifier = Modifier.height(30.dp))
            Text("RED SPADE", fontSize = 32.sp, fontWeight = FontWeight.Bold, color = Color.White, letterSpacing = 1.sp)
        }

        // Bottom Card Section
        Box(modifier = Modifier.fillMaxSize().padding(top = 220.dp)) {
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
                    Text(title, fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color(0xFF333333))
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(subtitle, fontSize = 14.sp, color = Color.Gray)
                    Spacer(modifier = Modifier.height(32.dp))

                    content()
                }
            }
        }
    }
}

// --- 2. LOGIN SCREEN (UPDATED LOGIC) ---
@Composable
fun LoginScreen(
    onNavigateToSignup: (UserRole) -> Unit,
    onLoginSuccess: (UserRole) -> Unit
) {
    var selectedRole by remember { mutableStateOf(UserRole.DONOR) }

    // --- STATE FOR INPUTS ---
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    AuthLayout(
        title = if (selectedRole == UserRole.DONOR) "Welcome Back, Donor!" else "NGO Portal Login",
        subtitle = "Enter your details below",
        topBarContent = {
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(painter = painterResource(android.R.drawable.ic_menu_directions), contentDescription = "Back", tint = Color.White)

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Don't have an account?", color = Color.White.copy(alpha = 0.8f), fontSize = 12.sp)
                    Spacer(modifier = Modifier.width(8.dp))
                    SmallPillButton(text = "Get Started", onClick = { onNavigateToSignup(selectedRole) })
                }
            }
        }
    ) {
        // Updated TextField calls with state
        CustomTextField(
            value = email,
            onValueChange = { email = it },
            label = "Email Address",
            placeholder = "example@mail.com"
        )
        Spacer(modifier = Modifier.height(16.dp))
        CustomPasswordField(
            value = password,
            onValueChange = { password = it },
            label = "Password"
        )

        if (errorMessage != null) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = errorMessage!!, color = Color.Red, fontSize = 12.sp)
        }

        Spacer(modifier = Modifier.height(24.dp))

        // --- LOGIN LOGIC ---
        GradientButton(
            text = "Sign In",
            onClick = {
                if (selectedRole == UserRole.NGO) {
                    if (email == "123" && password == "123") {
                        errorMessage = null
                        onLoginSuccess(UserRole.NGO)
                    } else {
                        errorMessage = "Invalid NGO Credentials (Try 123/123)"
                    }
                } else {
                    // For donor, just let them in for now or add similar logic
                    onLoginSuccess(UserRole.DONOR)
                }
            }
        )

        Spacer(modifier = Modifier.height(16.dp))
        Text("Forgot your password?", fontSize = 14.sp, color = Color(0xFF555555), fontWeight = FontWeight.Medium)

        Spacer(modifier = Modifier.height(24.dp))
        OrDivider()
        Spacer(modifier = Modifier.height(24.dp))
        GoogleLoginButton()

        Spacer(modifier = Modifier.weight(1f))
        Spacer(modifier = Modifier.height(24.dp))

        // Main Toggle
        RoleToggleRow(currentRole = selectedRole, onRoleSelected = { selectedRole = it })

        Spacer(modifier = Modifier.height(48.dp))
    }
}

// --- 3. DONOR SIGNUP SCREEN ---
@Composable
fun DonorSignupScreen(onNavigateToLogin: () -> Unit) {
    var donorType by remember { mutableStateOf(DonorType.INDIVIDUAL) }

    // Simple state just so typing works
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    AuthLayout(
        title = "Donor Registration",
        subtitle = "Create your donor account",
        topBarContent = {
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onNavigateToLogin) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Already have an account?", color = Color.White.copy(alpha = 0.8f), fontSize = 12.sp)
                    Spacer(modifier = Modifier.width(8.dp))
                    SmallPillButton(text = "Sign In", onClick = onNavigateToLogin)
                }
            }
        }
    ) {
        // Toggle for Individual vs Restaurant
        SubRoleToggle(current = donorType, onSelected = { donorType = it })
        Spacer(modifier = Modifier.height(24.dp))

        if (donorType == DonorType.INDIVIDUAL) {
            // Individual Flow
            CustomTextField(value = name, onValueChange = { name = it }, label = "Full Name", placeholder = "John Doe")
            Spacer(modifier = Modifier.height(16.dp))
            CustomTextField(value = email, onValueChange = { email = it }, label = "Email Address", placeholder = "john@example.com")
            Spacer(modifier = Modifier.height(16.dp))
            // You can add more state variables for these other fields
            CustomTextField(value = "", onValueChange = {}, label = "Mobile Number", placeholder = "+1 234 567 890", keyboardType = KeyboardType.Phone)
            Spacer(modifier = Modifier.height(16.dp))
            CustomPasswordField(value = password, onValueChange = { password = it }, label = "Password")
            Spacer(modifier = Modifier.height(16.dp))
            CustomPasswordField(value = "", onValueChange = {}, label = "Confirm Password")
            Spacer(modifier = Modifier.height(16.dp))
            OtpField()
        } else {
            // Restaurant Flow
            CustomTextField(value = "", onValueChange = {}, label = "Restaurant Name", placeholder = "Tasty Bites")
            Spacer(modifier = Modifier.height(16.dp))
            CustomTextField(value = "", onValueChange = {}, label = "Branch Location", placeholder = "City Center, Block A", icon = Icons.Filled.LocationOn)
            Spacer(modifier = Modifier.height(16.dp))
            CustomTextField(value = "", onValueChange = {}, label = "Email Address", placeholder = "contact@tastybites.com")
            Spacer(modifier = Modifier.height(16.dp))
            CustomTextField(value = "", onValueChange = {}, label = "Mobile Number", placeholder = "+1 234 567 890", keyboardType = KeyboardType.Phone)
            Spacer(modifier = Modifier.height(16.dp))
            CustomPasswordField(value = "", onValueChange = {}, label = "Password")
        }

        Spacer(modifier = Modifier.height(32.dp))
        GradientButton(text = "Sign Up", onClick = { /* Handle Donor Signup Logic */ })
        Spacer(modifier = Modifier.height(48.dp))
    }
}

// --- 4. NGO SIGNUP SCREEN ---
@Composable
fun NGOSignupScreen(
    onNavigateToLogin: () -> Unit,
    onSignUpSuccess: () -> Unit
) {
    // Simple state just so typing works
    var ngoId by remember { mutableStateOf("") }

    AuthLayout(
        title = "NGO Registration",
        subtitle = "Join our network to help others",
        topBarContent = {
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onNavigateToLogin) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Already have an account?", color = Color.White.copy(alpha = 0.8f), fontSize = 12.sp)
                    Spacer(modifier = Modifier.width(8.dp))
                    SmallPillButton(text = "Sign In", onClick = onNavigateToLogin)
                }
            }
        }
    ) {
        CustomTextField(value = ngoId, onValueChange = { ngoId = it }, label = "NGO ID / Reg Number", placeholder = "NGO-123456")
        Spacer(modifier = Modifier.height(16.dp))
        CustomTextField(value = "", onValueChange = {}, label = "Organization Name", placeholder = "Helping Hands Foundation")
        Spacer(modifier = Modifier.height(16.dp))
        CustomTextField(value = "", onValueChange = {}, label = "Mobile Number", placeholder = "+1 234 567 890", keyboardType = KeyboardType.Phone)
        Spacer(modifier = Modifier.height(16.dp))
        CustomPasswordField(value = "", onValueChange = {}, label = "Password")
        Spacer(modifier = Modifier.height(16.dp))
        CustomPasswordField(value = "", onValueChange = {}, label = "Confirm Password")
        Spacer(modifier = Modifier.height(16.dp))
        OtpField()

        Spacer(modifier = Modifier.height(32.dp))
        // Calling the success callback here triggers the navigation
        GradientButton(text = "Verify & Sign Up", onClick = { onSignUpSuccess() })
        Spacer(modifier = Modifier.height(48.dp))
    }
}

// --- HELPER COMPOSABLE (UPDATED TO ACCEPT STATE) ---

@Composable
fun GradientButton(text: String, onClick: () -> Unit) {
    val gradientBrush = Brush.verticalGradient(
        colors = listOf(Color(0xFF4A00E0), Color(0xFF8E2DE2))
    )
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(gradientBrush)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(text = text, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
    }
}

@Composable
fun SmallPillButton(text: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = Color.White.copy(alpha = 0.2f)),
        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 0.dp),
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier.height(32.dp)
    ) {
        Text(text, fontSize = 12.sp, color = Color.White)
    }
}

@Composable
fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    icon: ImageVector? = null
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(text = label, fontSize = 12.sp, color = Color.Gray)
        Spacer(modifier = Modifier.height(4.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text(placeholder, color = Color.LightGray) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            trailingIcon = if (icon != null) { { Icon(icon, null, tint = Color.Gray) } } else null,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF4A00E0),
                unfocusedBorderColor = Color.LightGray.copy(alpha = 0.5f)
            ),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType)
        )
    }
}

@Composable
fun CustomPasswordField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String
) {
    var passwordVisible by remember { mutableStateOf(false) }
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(text = label, fontSize = 12.sp, color = Color.Gray)
        Spacer(modifier = Modifier.height(4.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text("................", color = Color.LightGray) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val image = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
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

@Composable
fun OtpField() {
    // Simple state for OTP
    var otp by remember { mutableStateOf("") }
    Row(
        verticalAlignment = Alignment.Bottom,
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(modifier = Modifier.weight(1f)) {
            CustomTextField(
                value = otp,
                onValueChange = { otp = it },
                label = "Enter OTP",
                placeholder = "123456",
                keyboardType = KeyboardType.Number
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Button(
            onClick = { /* Send OTP Logic */ },
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.height(56.dp), // Match TextField height approximately
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4A00E0))
        ) {
            Text("Get OTP")
        }
    }
}

@Composable
fun RoleToggleRow(currentRole: UserRole, onRoleSelected: (UserRole) -> Unit) {
    Card(
        shape = RoundedCornerShape(50),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5)),
        modifier = Modifier.fillMaxWidth(0.8f).height(50.dp)
    ) {
        Row(modifier = Modifier.fillMaxSize()) {
            ToggleOption(
                text = "Donor",
                selected = currentRole == UserRole.DONOR,
                onClick = { onRoleSelected(UserRole.DONOR) },
                modifier = Modifier.weight(1f)
            )
            ToggleOption(
                text = "NGO",
                selected = currentRole == UserRole.NGO,
                onClick = { onRoleSelected(UserRole.NGO) },
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun SubRoleToggle(current: DonorType, onSelected: (DonorType) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp)
            .background(Color(0xFFF0F0F0), RoundedCornerShape(20.dp))
            .padding(4.dp)
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .clip(RoundedCornerShape(20.dp))
                .background(if (current == DonorType.INDIVIDUAL) Color(0xFF4A00E0) else Color.Transparent)
                .clickable { onSelected(DonorType.INDIVIDUAL) },
            contentAlignment = Alignment.Center
        ) {
            Text("Individual", color = if (current == DonorType.INDIVIDUAL) Color.White else Color.Gray, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
        }
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .clip(RoundedCornerShape(20.dp))
                .background(if (current == DonorType.RESTAURANT) Color(0xFF4A00E0) else Color.Transparent)
                .clickable { onSelected(DonorType.RESTAURANT) },
            contentAlignment = Alignment.Center
        ) {
            Text("Restaurant", color = if (current == DonorType.RESTAURANT) Color.White else Color.Gray, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
fun ToggleOption(text: String, selected: Boolean, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxHeight()
            .background(if (selected) Color(0xFF4A00E0) else Color.Transparent)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(text = text, color = if (selected) Color.White else Color.Gray, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun OrDivider() {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
        HorizontalDivider(modifier = Modifier.weight(1f), color = Color.LightGray)
        Text("Or sign in with", modifier = Modifier.padding(horizontal = 8.dp), fontSize = 12.sp, color = Color.Gray)
        HorizontalDivider(modifier = Modifier.weight(1f), color = Color.LightGray)
    }
}

@Composable
fun GoogleLoginButton() {
    OutlinedButton(
        onClick = { },
        modifier = Modifier.fillMaxWidth().height(50.dp),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, Color.LightGray)
    ) {
        // Ensure you have R.drawable.ic_google imported, otherwise comment this Image block out
        Image(
            painter = painterResource(id = R.drawable.googlelogo),
            contentDescription = "Google Logo",
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text("Google", color = Color.Black, fontWeight = FontWeight.SemiBold)
    }
}