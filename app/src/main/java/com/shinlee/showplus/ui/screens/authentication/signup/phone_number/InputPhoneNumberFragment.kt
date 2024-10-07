package com.shinlee.showplus.ui.screens.authentication.signup.phone_number

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.media3.common.util.Log
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.shinlee.showplus.R
import com.shinlee.showplus.ui.screens.authentication.nav.AuthNavigator
import com.shinlee.showplus.ui.screens.authentication.signup.LoginViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import java.util.concurrent.TimeUnit


class InputPhoneNumberFragment : Fragment() {
    private val viewModel: LoginViewModel by sharedViewModel<LoginViewModel>()
    private lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks

    private var navigator: AuthNavigator? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is AuthNavigator) {
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
        setupCallbacks()
        return inflater.inflate(R.layout.fragment_third_step_signup, container, false).apply {
            findViewById<ComposeView>(R.id.compose_view).apply {
                setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
                setContent {
                    InputPhoneNumberScreen(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.White),
                        navigateUp = {
                            navigator?.navigateUp()
                        }, onNext = { phoneNumber ->
                            sendVerificationCode(phoneNumber = "+84969283845")
                        },
                        viewModel = viewModel
                    )
                }
            }
        }
    }


    private fun sendVerificationCode(phoneNumber: String) {
        val options = PhoneAuthOptions.newBuilder(FirebaseAuth.getInstance())
            .setPhoneNumber(phoneNumber)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(requireActivity())
            .setCallbacks(callbacks)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private fun setupCallbacks() {
        callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                // Auto verification completed
//                signInWithPhoneAuthCredential(credential)
            }

            override fun onVerificationFailed(e: FirebaseException) {
                Toast.makeText(requireContext(), e.message, Toast.LENGTH_SHORT).show()
                Log.e("phone", "$e")
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {

            }
        }
    }
}