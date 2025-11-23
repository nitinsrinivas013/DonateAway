package com.example.donateaway

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import com.example.donateaway.ui.theme.DonateAwayTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DonateAwayTheme {
                MyApp()

            }
        }
    }
}

@Composable
fun MyApp(){
    DonorLoginScreen()

}

@Composable
fun DonorLoginScreen(){

    Text(
        "WELCOME TO THE DONATE AWAY APP",
        fontSize = 20.sp,
        fontWeight = FontWeight.SemiBold,
        modifier = Modifier.fillMaxWidth().padding(top = 100.dp, start = 15.dp, end = 15.dp)
    )

    var emailId by remember { mutableStateOf("") }
    var password by remember {mutableStateOf("")}

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ){
        TextField(emailId , {emailId = it} , label = {Text("EMAIL ID")})
        Spacer(Modifier.height(20.dp))
        TextField(password,
            {password = it},
            label = {Text("PASSWORD")},
            visualTransformation = PasswordVisualTransformation()
        )
        Spacer(Modifier.height(40.dp))
        Button({}) {
            Text("SIGN IN")
        }
        Spacer(Modifier.height(20.dp))
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ){
            Text("New to the app? Sign up")
            Text("Forgot password?")
        }
        Spacer(Modifier.height(20.dp))
        ButtonHandling()




    }


}

@Composable
fun ButtonHandling(){

    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ){

        Button(
            {},
            shape = RectangleShape
            ) {
            Text("DONOR")
        }
        Button(
            {},
            shape = RectangleShape
        ) {
            Text("NGO")
        }
    }
}

@Preview
@Composable
fun DonorLoginScreenPreview(){
    DonateAwayTheme {
        DonorLoginScreen()
    }
}







