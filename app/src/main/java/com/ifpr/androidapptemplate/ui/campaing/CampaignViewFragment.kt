package com.ifpr.androidapptemplate.ui.campaing

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.ifpr.androidapptemplate.R
import com.ifpr.androidapptemplate.baseclasses.TalesCampaign
import com.ifpr.androidapptemplate.databinding.FragmentCampaingViewBinding
import com.ifpr.androidapptemplate.util.loadImage

class CampaignViewFragment : Fragment() {

    private var _binding: FragmentCampaingViewBinding? = null

    private lateinit var dataCampaing: TalesCampaign
    lateinit var campaingId: String

    private lateinit var campaignNameLabel: TextView
    private lateinit var imageCampaign: ImageView
    private lateinit var campaignTypeLabel : TextView
    private lateinit var campaignCodeLabel: TextView
    private lateinit var campaignCodeCopyBtn: Button
    private lateinit var campaignDescLabel: TextView

    private lateinit var playersContainer: LinearLayout

    private lateinit var auth: FirebaseAuth

    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        dataCampaing = arguments?.getSerializable("campaingData") as TalesCampaign
        campaingId = arguments?.getString("campaingId") ?: ""

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentCampaingViewBinding.inflate(inflater, container, false)
        val binding = _binding!!
        val root: View = binding.root

        val imageBase64 = dataCampaing.base64Image
        val urlImage = dataCampaing.imageUrl
        auth = FirebaseAuth.getInstance()

        campaignNameLabel   = binding.campaignNameLabel
        imageCampaign       = binding.imageCampaign
        campaignTypeLabel   = binding.campaignTypeLabel
        campaignCodeLabel   = binding.campaignCodeLabel
        campaignCodeCopyBtn = binding.campaignCodeCopyBtn
        campaignDescLabel   = binding.campaignDescLabel

        playersContainer    = binding.playersContainer

        campaignNameLabel.setText(dataCampaing.nome)
        loadImage(imageBase64, urlImage, imageCampaign)
        campaignTypeLabel.setText(dataCampaing.type)
        campaignCodeLabel.setText(dataCampaing.code)
        campaignDescLabel.setText(dataCampaing.description)

        campaignCodeCopyBtn.setOnClickListener {

            if(!dataCampaing.code.isNullOrEmpty()){
                copyToClipboard(dataCampaing.code.toString())
            }

        }

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val toolbar = binding.campaignToolbar

        val typedArray = requireContext().theme.obtainStyledAttributes(
            intArrayOf(R.attr.back_arrow)
        )
        val backArrowDrawable = typedArray.getDrawable(0)
        typedArray.recycle()

        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (requireActivity() as AppCompatActivity).supportActionBar?.setHomeAsUpIndicator(backArrowDrawable)

        toolbar.setNavigationOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

    }

    private fun copyToClipboard(text: String) {
        val clipboard = requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("text", text)
        clipboard.setPrimaryClip(clip)
        Toast.makeText(requireContext(), "Copiado!", Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }



}