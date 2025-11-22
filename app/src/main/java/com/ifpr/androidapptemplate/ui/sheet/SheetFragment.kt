package com.ifpr.androidapptemplate.ui.sheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.R
import androidx.fragment.app.Fragment
import com.ifpr.androidapptemplate.baseclasses.TalesGeneralInfo

class SheetFragment : Fragment() {

    private lateinit var data: TalesGeneralInfo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        data = arguments?.getSerializable("charData") as TalesGeneralInfo
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(com.ifpr.androidapptemplate.R.layout.fragment_sheet, container, false)


    }

}