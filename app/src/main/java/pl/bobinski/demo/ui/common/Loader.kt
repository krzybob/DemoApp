package pl.bobinski.demo.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun Loader(modifier: Modifier = Modifier) {
    Box(
        modifier = Modifier
            .background(color = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f))
            .then(modifier), contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}