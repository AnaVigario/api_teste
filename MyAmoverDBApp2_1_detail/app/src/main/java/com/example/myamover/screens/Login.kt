package com.example.myamover.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.myamover.R
import com.example.myamover.model.LoginViewModel
import com.example.myamover.navigation.NavRoutes
import com.example.myamover.ui.theme.MyAmoverTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    windowSize: WindowWidthSizeClass,
    modifier: Modifier = Modifier,
    navController: NavController, // <-- já não criamos aqui, recebemos de fora
) {
    // --- VIEWMODEL & STATE ---
    val viewModel: LoginViewModel = viewModel()
    val uiState by viewModel.ui.collectAsState()

    // --- ESTADO LOCAL DOS CAMPOS ---
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    var emailError by remember { mutableStateOf("") }
    var passwordError by remember { mutableStateOf("") }

    // ✅ se o utilizador autenticou com sucesso -> navega para home/{email}
    LaunchedEffect(uiState.loggedInUser) {
        uiState.loggedInUser?.let { user ->
            // protegemos o email para meter na rota (nav routes não gostam de caracteres especiais tipo '@')
            val encodedEmail = user.email.replace("@", "%40")

            navController.navigate("home/$encodedEmail") {
                // remove o login do histórico para não voltar atrás com back
                popUpTo(NavRoutes.Login) { inclusive = true }
                launchSingleTop = true
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // HEADER verde + logo + nome app
        Box(modifier = Modifier.fillMaxWidth()) {
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .background(colorScheme.primary)
            )
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 82.dp),
                color = colorScheme.background
            ) {
                Row(
                    modifier = Modifier
                        .padding(
                            start = 16.dp, end = 16.dp,
                            top = 4.dp, bottom = 16.dp
                        )
                ) {
                    Spacer(Modifier.size(96.dp))
                    Card(
                        modifier = Modifier
                            .width(120.dp)
                            .height(120.dp)
                            .offset(x = 50.dp, y = 5.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = colorScheme.background
                        ),
                    ) {
                        Text(
                            "AmoVeR",
                            style = MaterialTheme.typography.titleLarge
                        )
                        Text(
                            "Logistic",
                            style = MaterialTheme.typography.bodyMedium,
                            color = colorScheme.onTertiaryContainer
                        )
                    }
                }
            }

            // LOGO
            Card(
                modifier = Modifier
                    .size(128.dp)
                    .align(Alignment.TopStart)
                    .offset(x = 16.dp, y = 20.dp),
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                colors = CardDefaults.cardColors(
                    containerColor = colorScheme.surfaceContainerLowest
                )
            ) {
                Image(
                    painter = painterResource(R.drawable.logo_verde),
                    contentDescription = "Logo AmoVeR",
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(20.dp)),
                    contentScale = ContentScale.Fit,
                    alignment = Alignment.Center
                )
            }
        }

        Text(
            text = "Login",
            style = MaterialTheme.typography.bodyLarge,
            color = colorScheme.onTertiaryContainer,
            fontSize = 35.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 46.dp)
        )

        Spacer(modifier = Modifier.height(40.dp))

        // CAMPO EMAIL
        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it
                if (emailError.isNotEmpty()) emailError = ""
            },
            label = {
                Text(
                    emailError.ifEmpty { "Email" },
                    color = if (emailError.isNotEmpty()) colorScheme.error else Color.Unspecified
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = "Email"
                )
            },
            modifier = Modifier
                .padding(horizontal = 26.dp)
                .fillMaxWidth(),
            shape = MaterialTheme.shapes.medium,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = colorScheme.surfaceContainerLowest,
                disabledContainerColor = Color.LightGray,
                errorContainerColor = colorScheme.errorContainer,
            ),
            singleLine = true,
            isError = emailError.isNotEmpty()
        )

        Spacer(modifier = Modifier.height(20.dp))

        // CAMPO PASSWORD
        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
                if (passwordError.isNotEmpty()) passwordError = ""
            },
            label = {
                Text(
                    passwordError.ifEmpty { "Password" },
                    color = if (passwordError.isNotEmpty()) {
                        colorScheme.error
                    } else Color.Unspecified
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = "Password"
                )
            },
            visualTransformation = if (passwordVisible) {
                VisualTransformation.None
            } else {
                PasswordVisualTransformation()
            },
            trailingIcon = {
                val image = if (passwordVisible)
                    painterResource(id = R.drawable.visibility_24)
                else painterResource(id = R.drawable.visibility_off_24)

                Icon(
                    painter = image,
                    contentDescription = if (passwordVisible) "Hide password" else "Show password",
                    modifier = Modifier.clickable { passwordVisible = !passwordVisible }
                )
            },
            modifier = Modifier
                .padding(horizontal = 26.dp)
                .fillMaxWidth(),
            shape = MaterialTheme.shapes.medium,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = colorScheme.surfaceContainerLowest,
                disabledContainerColor = Color.LightGray,
                errorContainerColor = colorScheme.errorContainer,
            ),
            singleLine = true,
            isError = passwordError.isNotEmpty()
        )

        Spacer(modifier = Modifier.height(30.dp))

        // BOTÃO LOGIN
        Button(
            onClick = {
                emailError = if (email.isBlank()) "Email is required" else ""
                passwordError = if (password.isBlank()) "Password is required" else ""

                if (emailError.isEmpty() && passwordError.isEmpty()) {
                    viewModel.login(email, password)
                }
            },
            modifier = Modifier
                .padding(horizontal = 36.dp)
                .fillMaxWidth(),
            enabled = !uiState.loading
        ) {
            if (uiState.loading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(18.dp),
                    strokeWidth = 2.dp,
                )
                Spacer(modifier = Modifier.width(8.dp))
            }
            Text(
                text = "Login",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        }

        // ERRO vindo da ViewModel (ex. auth falhou)
        if (uiState.message != null && uiState.loggedInUser == null) {
            Text(
                text = uiState.message!!,
                color = colorScheme.error,
                fontSize = 15.sp,
                modifier = Modifier
                    .padding(horizontal = 26.dp, vertical = 16.dp)
            )
        }

        Spacer(modifier = Modifier.height(30.dp))

        // FORGOT PASSWORD
        Text(
            text = "Forgot password?",
            color = colorScheme.onTertiaryContainer,
            fontSize = 15.sp,
            modifier = Modifier
                .padding(horizontal = 26.dp)
                .clickable {
                    // navController.navigate("forgotPassword")
                }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun LoginPreview() {
    MyAmoverTheme {
        MyAmoverTheme() {
            LoginScreen(
                windowSize = WindowWidthSizeClass.Compact,
                navController = NavHostController(LocalContext.current)
            )

        }
    }
}