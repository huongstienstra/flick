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
import androidx.fragment.app.viewModels
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
    private val viewModel: PhoneNumberViewModel by sharedViewModel<PhoneNumberViewModel>()

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
                setContent {
                    InputPhoneNumberScreen(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.White),
                        navigateUp = {
                            navigator?.navigateUp()
                            viewModel.resetPhoneNumber()
                        },
                        onNext = {
                            navigator?.navigateInputOTP()
                        },
                        onOpenCountryCode = {
                            CountryCodeBottomSheet().showByTag(
                                childFragmentManager,
                                object : OnSelected {
                                    override fun onCountryCodeSelected(countryCode: String) {
                                        viewModel.updateCountryCode(countryCode)
                                    }
                                })
                        },
                        viewModel = viewModel,
                    )
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.loadCountries()
    }

}