package com.shinlee.showplus.ui.screens.profile

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import com.shinlee.showplus.R


class ProfileFragment : Fragment() {

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.profile_fragment, container, false)
        val composeView = view.findViewById<ComposeView>(R.id.compose_view)
        composeView.apply {
            // Dispose of the Composition when the view's LifecycleOwner
            // is destroyed
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                // In Compose world
//                AlertDialogExample(
//                    onDismissRequest = { false },
//                    onConfirmation = {
//                        activity?.let{
//                            val intent = Intent (it, AuthenticationActivityV2::class.java)
//                            it.startActivity(intent)
//                        } // Add logic here to handle confirmation.
//                    },
//                    dialogTitle = "You have to login?",
//                    dialogText = "Login to the app to continue to use this feature",
//                    icon = Icons.Default.Info
//                )
            }
        }
        return view
    }

}