package com.shinlee.showplus.ui.screens.authentication.signup.phone_number

import android.content.Context
import android.os.Bundle
import android.util.Log
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
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.shinlee.showplus.R
import com.shinlee.showplus.ui.screens.authentication.nav.AuthNavigator
import com.shinlee.showplus.ui.screens.authentication.signup.SignupViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.concurrent.TimeUnit


class ThirdStepSignupFragment : Fragment() {
    private val viewModel: SignupViewModel by viewModel()
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
        return inflater.inflate(R.layout.fragment_third_step_signup, container, false).apply {
            findViewById<ComposeView>(R.id.compose_view).apply {
                setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
               // setupCallbacks()
                setContent {
                    ThirdStepSignupScreen(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.White),
                        viewModel = viewModel,
                        onBackClick = {
                            navigator?.onBackPress()
                        },
                        onSignUpSuccess = {

                        },
                        onGetOtpClick = {
                            //sendVerificationCode(it)
                        }
                    )
                }
            }
        }
    }

//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        arguments?.getString("token")?.let { value ->
//            viewModel.token.value = value
//            Log.d("khanh", viewModel.token.value)
//        }
//    }
//
//    private fun sendVerificationCode(phoneNumber: String) {
//        val options = PhoneAuthOptions.newBuilder(FirebaseAuth.getInstance())
//            .setPhoneNumber(phoneNumber)
//            .setTimeout(60L, TimeUnit.SECONDS)
//            .setActivity(requireActivity())
//            .setCallbacks(callbacks)
//            .build()
//        PhoneAuthProvider.verifyPhoneNumber(options)
//    }
//
//    private fun setupCallbacks() {
//        callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
//            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
//                // Auto verification completed
////                signInWithPhoneAuthCredential(credential)
//            }
//
//            override fun onVerificationFailed(e: FirebaseException) {
//                Log.d("khanh", e.message+"")
//                Toast.makeText(requireContext(), e.message, Toast.LENGTH_SHORT).show()
//            }
//
//            override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
//
//            }
//        }
//    }
}