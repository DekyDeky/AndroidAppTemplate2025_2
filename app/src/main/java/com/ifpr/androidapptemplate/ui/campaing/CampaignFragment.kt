package com.ifpr.androidapptemplate.ui.campaing

import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation.findNavController
import com.bumptech.glide.Glide
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.ifpr.androidapptemplate.databinding.FragmentCampaignBinding
import com.ifpr.androidapptemplate.R
import com.ifpr.androidapptemplate.baseclasses.TalesCampaign

class CampaignFragment : Fragment() {
    private var _binding: FragmentCampaignBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_campaign, container, false)

        val containerCampaign = view.findViewById<LinearLayout>(R.id.itemContainerCampaigns)
        carregarCampanhas(containerCampaign)

        val createCampaignBtn = view.findViewById<FloatingActionButton>(R.id.campaignCreateBtn)

        createCampaignBtn.setOnClickListener {
            val navController = findNavController(requireActivity(), R.id.nav_host_fragment_activity_main)
            navController.navigate(R.id.navigation_create_campaigns)
        }

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun carregarCampanhas(container: LinearLayout){
        val uid = FirebaseAuth.getInstance().currentUser!!.uid

        val databaseRef = FirebaseDatabase.getInstance().getReference("campanhas")

        databaseRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                container.removeAllViews()

                for (itemSnapshot in snapshot.children) {
                    val item = itemSnapshot.getValue(TalesCampaign::class.java) ?: continue

                    if(item.ownerUid != uid) continue

                    val itemView = LayoutInflater.from(container.context)
                        .inflate(R.layout.item_campaing_template, container, false)

                    val imageView = itemView.findViewById<ImageView>(R.id.image_campaign)
                    val campaignNameLabel = itemView.findViewById<TextView>(R.id.campaignNameLabel)
                    val campaignTypeLabel = itemView.findViewById<TextView>(R.id.campaingTypeLabel)
                    val campaingCodeLabel = itemView.findViewById<TextView>(R.id.campaingCodeLabel)
                    val campaingDescLabel = itemView.findViewById<TextView>(R.id.campaingDescLabel)

                    val campaignOpenBtn = itemView.findViewById<Button>(R.id.campaignOpenBtn)

                    campaignNameLabel.text = item.nome ?: " Não informado!"
                    campaignTypeLabel.text = item.type ?: " Não informado!"
                    campaingCodeLabel.text = item.code ?: " Não criado!"
                    campaingDescLabel.text = item.description ?: " Não informado!"

                    if (!item.imageUrl.isNullOrEmpty()) {
                        Glide.with(container.context).load(item.imageUrl).into(imageView)
                    } else if (!item.base64Image.isNullOrEmpty()) {
                        try {
                            val bytes = Base64.decode(item.base64Image, Base64.DEFAULT)
                            val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                            imageView.setImageBitmap(bitmap)
                        } catch (_: Exception) {}
                    }

                    campaignOpenBtn.setOnClickListener {
                        val campaignKey = snapshot.key

                        openCampaignView(item, campaignKey!!)
                    }

                    container.addView(itemView)
                }

            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(container.context, "Erro ao carregar dados", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun openCampaignView(talesCampaign: TalesCampaign, campaingKey: String){
        val bundle = Bundle()
        bundle.putSerializable("campaingData", talesCampaign)
        bundle.putString("campaingId", campaingKey)

        val navController = findNavController(requireActivity(), R.id.nav_host_fragment_activity_main)
        navController.navigate(R.id.navigation_campaign_view, bundle)
    }

}