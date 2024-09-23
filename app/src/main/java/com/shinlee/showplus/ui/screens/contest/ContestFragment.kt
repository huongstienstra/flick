package com.shinlee.showplus.ui.screens.contest

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.shinlee.showplus.R

class ContestFragment : Fragment() {

    companion object {
        const val TAG = "ContestFragment"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.contest_fragment, container, false)
    }

}