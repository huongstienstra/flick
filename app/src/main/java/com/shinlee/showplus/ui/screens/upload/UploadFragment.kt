package com.shinlee.showplus.ui.screens.upload

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.shinlee.showplus.R

class UploadFragment : Fragment() {

    companion object {
        const val TAG = "UploadFragment"
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.upload_fragment, container, false)
    }

}