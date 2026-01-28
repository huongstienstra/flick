package com.flick.app.ui.screens.authentication.signup.phone_number

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.flick.app.R
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

interface OnSelected {
    fun onCountryCodeSelected(countryCode: String)
}

class CountryCodeBottomSheet : BottomSheetDialogFragment() {

    private val viewModel: PhoneNumberViewModel by sharedViewModel<PhoneNumberViewModel>()

    private var listener: OnSelected? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.country_code_bottomsheet, container, false)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        dialog.setOnShowListener { dialogInterface ->
            val bottomSheetDialog = dialogInterface as BottomSheetDialog
            setupRatio(bottomSheetDialog)
        }
        return dialog
    }

    private fun setupRatio(bottomSheetDialog: BottomSheetDialog) {
        val bottomSheet =
            bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as FrameLayout?
        val behavior = BottomSheetBehavior.from(bottomSheet!!)
        behavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
        behavior.skipCollapsed = true
        behavior.isDraggable = true

        bottomSheet.background = null
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.apply {
            setBackgroundDrawableResource(android.R.color.transparent)
            setDimAmount(0.5f)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<ComposeView>(R.id.compose_view).setContent {
            val countries by viewModel.countries.collectAsStateWithLifecycle()
            CountriesBottomSheetContent(
                modifier = Modifier.fillMaxWidth(),
                countries = countries,
                onCountrySelected = { phoneCode ->
                    listener?.onCountryCodeSelected(phoneCode.phoneCode)
                    dismissAllowingStateLoss()
                },
                onDismiss = {
                    dismissAllowingStateLoss()
                }
            )

        }
    }


    fun showByTag(manager: FragmentManager, listener: OnSelected) {
        this.listener = listener
        super.show(manager, CountryCodeBottomSheet::class.java.name)
    }

}