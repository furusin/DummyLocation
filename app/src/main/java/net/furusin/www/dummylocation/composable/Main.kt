@file:OptIn(ExperimentalMaterial3Api::class)

package net.furusin.www.dummylocation.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import net.furusin.www.dummylocation.MainViewModel

@Composable
fun Main(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier,
    setButtonClicked: () -> Unit,
    checkButtonClicked: () -> Unit
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Longitude(viewModel)
        Latitude(viewModel)
        Button(onClick = { setButtonClicked() }) {
            Text(text = "SET")
        }
        Button(onClick = { checkButtonClicked() }) {
            Text(text = "CHECK")
        }
    }
}


@Composable
fun Longitude(viewModel: MainViewModel) {
    val text = viewModel.longitude.collectAsState()

    TextField(
        value = text.value,
        onValueChange = { viewModel.updateLongitude(it) },
        label = { Text("Longitude") },
        modifier = Modifier
            .padding(20.dp)
            .fillMaxWidth()
    )
}

@Composable
fun Latitude(viewModel: MainViewModel) {
    var text = viewModel.latitude.collectAsState()

    TextField(
        value = text.value,
        onValueChange = { viewModel.updateLatitude(it) },
        label = { Text(text = "Latitude") },
        modifier = Modifier
            .padding(20.dp)
            .fillMaxWidth()
    )
}