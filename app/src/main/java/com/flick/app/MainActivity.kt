package com.flick.app

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.flick.app.ui.MainViewModel
import com.flick.app.ui.FlickApp
import com.flick.app.ui.screens.authentication.AuthenticationActivity
import com.flick.app.ui.screens.search.SearchViewModel
import com.flick.app.ui.screens.show.ShowViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {

    private val mainViewModel: MainViewModel by viewModel()
    private val showViewModel: ShowViewModel by viewModel()
    private val searchViewModel: SearchViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mainViewModel.checkLoginStatus()

        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    FlickApp(
                        mainViewModel = mainViewModel,
                        showViewModel = showViewModel,
                        searchViewModel = searchViewModel,
                        onNavigateToLogin = {
                            startActivity(Intent(this, AuthenticationActivity::class.java))
                        }
                    )
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        showViewModel.releaseAllPlayers()
    }
}
