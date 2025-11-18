package com.ifpr.androidapptemplate.ui.campaing

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.ifpr.androidapptemplate.databinding.FragmentCampaignBinding

class CampaingFragment : Fragment() {
    private var _binding: FragmentCampaignBinding? = null
    private val binding get() = _binding

    private lateinit var campaignViewModel: CampaignViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        campaignViewModel = ViewModelProvider(this).get(CampaignViewModel::class.java)

        _binding = FragmentCampaignBinding.inflate(inflater, container, false)
        val root: View = binding!!.root

        val textView = binding!!.textCampaign
        campaignViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }

        return root

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}