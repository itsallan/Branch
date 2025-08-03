package io.branch

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.branch.view.theme.BranchTheme
import io.branch.navigation.BranchNavHost
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    BranchTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
          //  color = MaterialTheme.colorScheme.background
        ) {
            BranchNavHost()
        }
    }
}