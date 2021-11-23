package com.example.hearthstoneapp.ui.shops

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.hearthstoneapp.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottonSheet : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.bottomsheet_layout, container, false)
    }
}
