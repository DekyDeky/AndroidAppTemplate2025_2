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
import androidx.navigation.Navigation.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.ifpr.androidapptemplate.R
import com.ifpr.androidapptemplate.baseclasses.TalesCampaign
import com.ifpr.androidapptemplate.baseclasses.TalesGeneralInfo
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

        val allSheetsCampaign = dataCampaing.jogadores.values.flatMap { it.keys }

        loadPlayersOnCampaign(playersContainer, allSheetsCampaign)

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

    private fun loadPlayersOnCampaign(container: LinearLayout, sheetsId: List<String>) {

        val ref = FirebaseDatabase.getInstance().getReference("fichas")

        ref.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {

                container.removeAllViews()

                for (uidSnapshot in snapshot.children) {            // fichas -> uid
                    for (sheetSnapshot in uidSnapshot.children) {  // fichas -> uid -> sheetId

                        val fichaId = sheetSnapshot.key ?: continue

                        // filtra apenas fichas que estão na campanha
                        if (!sheetsId.contains(fichaId)) continue

                        val sheet = sheetSnapshot.getValue(TalesGeneralInfo::class.java) ?: continue

                        val view = LayoutInflater.from(container.context)
                            .inflate(R.layout.item_template, container, false)

                        val imageViewSheet = view.findViewById<ImageView>(R.id.item_image)

                        loadImage(sheet.base64Image, sheet.imageUrl, imageViewSheet)

                        view.findViewById<TextView>(R.id.sheet_charName).text = sheet.name
                        view.findViewById<TextView>(R.id.sheet_charAge).text = sheet.age.toString()
                        view.findViewById<TextView>(R.id.sheet_charType).text = sheet.type
                        view.findViewById<TextView>(R.id.sheet_charDescription).text = sheet.description
                        view.findViewById<TextView>(R.id.sheet_charBody).text = sheet.attributes?.body.toString()
                        view.findViewById<TextView>(R.id.sheet_charTech).text = sheet.attributes?.tech.toString()
                        view.findViewById<TextView>(R.id.sheet_charHeart).text = sheet.attributes?.heart.toString()
                        view.findViewById<TextView>(R.id.sheet_charMind).text = sheet.attributes?.mind.toString()

                        val sheetOpenBtn = view.findViewById<Button>(R.id.sheet_openBtn)

                        sheetOpenBtn.setOnClickListener {
                            openCharSheet(sheet, fichaId)
                        }

                        container.addView(view)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }


    private fun openCharSheet(talesSheet: TalesGeneralInfo, fichaKey: String){
        val bundle = Bundle()
        bundle.putSerializable("charData", talesSheet)
        bundle.putString("fichaId", fichaKey)

        val navController = findNavController(requireActivity(), R.id.nav_host_fragment_activity_main)
        navController.navigate(R.id.navigation_char_sheet, bundle)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }



}