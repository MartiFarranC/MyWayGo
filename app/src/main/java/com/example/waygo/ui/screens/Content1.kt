package com.example.waygo.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.waygo.R

@Composable
fun Content1(navController: NavController, paddingValues: PaddingValues) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp) // Afegeix un petit marge
    ) {
        Row(
            modifier = Modifier
                .align(Alignment.BottomEnd), // Alinea el contingut a baix a la dreta
            verticalAlignment = Alignment.CenterVertically // Centra verticalment el text i el botó
        ) {
            Button(onClick = {
                navController.navigate("about")
            }) {
                Text(text = stringResource(id = R.string.about_us))
            }
        }
    }

    Box(modifier = Modifier.padding(paddingValues)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = stringResource(id = R.string.welcome))
            Spacer(modifier = Modifier.height(20.dp))
            Button(onClick = { navController.navigate(route="profile")}) {
                Text(text = stringResource(id = R.string.profile))
            }
        }
    }
}