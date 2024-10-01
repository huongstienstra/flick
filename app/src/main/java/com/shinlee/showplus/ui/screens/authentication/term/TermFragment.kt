package com.shinlee.showplus.ui.screens.authentication.term

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import com.shinlee.showplus.R
import com.shinlee.showplus.ui.screens.authentication.Destination
import com.shinlee.showplus.ui.screens.authentication.FragmentNavigation
import org.koin.androidx.viewmodel.ext.android.viewModel

class TermFragment : Fragment() {

    private val viewModel: TermViewModel by viewModel()
    private lateinit var navigator: FragmentNavigation

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is FragmentNavigation) {
            navigator = context
        } else {
            throw ClassCastException("$context must implement FragmentNavigation")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_term, container, false).apply {
            findViewById<ComposeView>(R.id.compose_view).apply {
                setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
                setContent {
                    TermScreen(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.White),
                        viewModel = viewModel,
                        onNextClick = {
                            navigator.navigateTo(Destination.FirstStepSignup)

                        }
                    )
                }
            }
        }
    }
}