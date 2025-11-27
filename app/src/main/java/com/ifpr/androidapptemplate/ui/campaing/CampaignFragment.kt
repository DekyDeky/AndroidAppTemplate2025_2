package com.ifpr.androidapptemplate.ui.campaing

import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation.findNavController
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.ifpr.androidapptemplate.databinding.FragmentCampaignBinding
import com.ifpr.androidapptemplate.R
import com.ifpr.androidapptemplate.baseclasses.TalesCampaign
import com.ifpr.androidapptemplate.baseclasses.TalesGeneralInfo

class CampaignFragment : Fragment() {
    private var _binding: FragmentCampaignBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val fichaNomes = mutableListOf<String>()
        val fichaIds = mutableListOf<String>()

        val view = inflater.inflate(R.layout.fragment_campaign, container, false)

        val containerCampaign = view.findViewById<LinearLayout>(R.id.itemContainerCampaigns)
        loadCampanhas(containerCampaign)

        val containerPlayingCampaign = view.findViewById<LinearLayout>(R.id.itemContainerPlayingCampaigns)
        loadPlayingCampaigns(containerPlayingCampaign)

        val createCampaignBtn = view.findViewById<FloatingActionButton>(R.id.campaignCreateBtn)

        val campaignEnterBtn = view.findViewById<Button>(R.id.campaignEnterBtn)

        createCampaignBtn.setOnClickListener {
            val navController = findNavController(requireActivity(), R.id.nav_host_fragment_activity_main)
            navController.navigate(R.id.navigation_create_campaigns)
        }

        val dialog = BottomSheetDialog(requireContext())
        val viewDialog = layoutInflater.inflate(R.layout.dialog_enter_campaign, null)

        val codeInput = viewDialog.findViewById<EditText>(R.id.editCampaignCode)
        val joinButton = viewDialog.findViewById<Button>(R.id.buttonJoinCampaign)
        val spinnerSheets = viewDialog.findViewById<Spinner>(R.id.spinnerFicha)

        spinnerSheets.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                spinnerSheets.tag = fichaIds[position]   // <-- ARMAZENA O ID REAL
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        loadUserSheets(spinnerSheets)

        joinButton.setOnClickListener {
            val code = codeInput.text.toString().trim()

            if (code.isEmpty()) {
                codeInput.error = "Digite um código"
                return@setOnClickListener
            }

            val sheetId = spinnerSheets.tag as String

            // Função que tenta entrar na campanha
            enterCampaign(code, sheetId)

            dialog.dismiss()
        }

        campaignEnterBtn.setOnClickListener {
            dialog.setContentView(viewDialog)
            dialog.show()
        }

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun loadCampanhas(container: LinearLayout){
        val uid = FirebaseAuth.getInstance().currentUser!!.uid

        val databaseRef = FirebaseDatabase.getInstance().getReference("campanhas")

        databaseRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                container.removeAllViews()

                for (itemSnapshot in snapshot.children) {
                    val item = itemSnapshot.getValue(TalesCampaign::class.java) ?: continue

                    if(item.ownerUid != uid) continue

                    val jogadoresNode = itemSnapshot.child("jogadores")

                    item.jogadores.clear()

                    for (uidJogador in jogadoresNode.children) {
                        val uid = uidJogador.key ?: continue

                        val sheetMap = mutableMapOf<String, Boolean>()

                        for (sheetNode in uidJogador.children) {
                            val sheetId = sheetNode.key ?: continue
                            sheetMap[sheetId] = true
                        }

                        item.jogadores[uid] = sheetMap


                    }

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

    fun loadPlayingCampaigns(container: LinearLayout){
        val uid = FirebaseAuth.getInstance().currentUser!!.uid
        val ref = FirebaseDatabase.getInstance().getReference("campanhas")

        ref.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {

                container.removeAllViews()

                for (campaignSnapshot in snapshot.children) {

                    // caminho: campanhas -> id -> jogadores -> uid
                    val jogadoresNode = campaignSnapshot.child("jogadores")

                    // Verifica se existe jogadores/<uid>
                    if (!jogadoresNode.hasChild(uid)) {
                        continue
                    }

                    // Converter para classe
                    val campaign = campaignSnapshot.getValue(TalesCampaign::class.java) ?: continue

                    // Inflate do card
                    val itemView = LayoutInflater.from(container.context)
                        .inflate(R.layout.item_campaing_template, container, false)



                    val imageView = itemView.findViewById<ImageView>(R.id.image_campaign)
                    val name = itemView.findViewById<TextView>(R.id.campaignNameLabel)
                    val type = itemView.findViewById<TextView>(R.id.campaingTypeLabel)
                    val code = itemView.findViewById<TextView>(R.id.campaingCodeLabel)
                    val desc = itemView.findViewById<TextView>(R.id.campaingDescLabel)
                    val openBtn = itemView.findViewById<Button>(R.id.campaignOpenBtn)

                    name.text = campaign.nome
                    type.text = campaign.type
                    code.text = campaign.code
                    desc.text = campaign.description

                    if (!campaign.imageUrl.isNullOrEmpty()) {
                        Glide.with(container.context).load(campaign.imageUrl).into(imageView)
                    } else if (!campaign.base64Image.isNullOrEmpty()) {
                        val bytes = Base64.decode(campaign.base64Image, Base64.DEFAULT)
                        val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                        imageView.setImageBitmap(bitmap)
                    }

                    val campaignId = campaignSnapshot.key!!

                    openBtn.setOnClickListener {
                        openCampaignView(campaign, campaignId)
                    }

                    container.addView(itemView)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), "Falha ao carregar campanhas: ${error.toString()}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun openCampaignView(talesCampaign: TalesCampaign, campaingKey: String?){
        val bundle = Bundle()
        bundle.putSerializable("campaingData", talesCampaign)
        bundle.putString("campaingId", campaingKey)

        val navController = findNavController(requireActivity(), R.id.nav_host_fragment_activity_main)
        navController.navigate(R.id.navigation_campaign_view, bundle)
    }

    private fun enterCampaign(code: String, sheetId: String) {
        val ref = FirebaseDatabase.getInstance().getReference("campanhas")
        val currentUid = FirebaseAuth.getInstance().currentUser!!.uid

        ref.orderByChild("code").equalTo(code)
            .addListenerForSingleValueEvent(object: ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                if(!snapshot.exists()){
                    Toast.makeText(requireContext(), "Código inválido!", Toast.LENGTH_SHORT).show()
                    return
                }

                for(child in snapshot.children) {
                    val campaignId = child.key!!

                    val path = ref.child(campaignId)
                        .child("jogadores")
                        .child(currentUid)
                        .child(sheetId)

                    path.setValue(true)
                        .addOnSuccessListener {
                            Toast.makeText(requireContext(), "Campanha encontrada!", Toast.LENGTH_SHORT).show()
                        }

                }
            }

            override fun onCancelled(error: DatabaseError) {}

        })
    }

    private fun loadUserSheets(spinner: Spinner) {
        val uid = FirebaseAuth.getInstance().currentUser!!.uid
        val ref = FirebaseDatabase.getInstance().getReference("fichas").child(uid)

        ref.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {

                val nomes = mutableListOf<String>()        // exibe no Spinner
                val ids = mutableListOf<String>()          // id real da ficha

                for (fichaSnapshot in snapshot.children) {
                    val fichaId = fichaSnapshot.key ?: continue
                    val ficha = fichaSnapshot.getValue(TalesGeneralInfo::class.java)

                    nomes.add(ficha?.name ?: "Sem nome")
                    ids.add(fichaId)
                }

                val adapter = object : ArrayAdapter<String>(
                    requireContext(),
                    android.R.layout.simple_spinner_item,
                    nomes
                ) {
                    override fun getItemId(position: Int): Long {
                        return ids[position].hashCode().toLong()
                    }

                    fun getItemKey(position: Int): String {
                        return ids[position]
                    }
                }

                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinner.adapter = adapter

                // Guardar os IDs como "tag"
                spinner.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        spinner.setTag(ids[position])  // guarda o ID real
                    }

                    override fun onNothingSelected(parent: AdapterView<*>) {}
                })
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }


}