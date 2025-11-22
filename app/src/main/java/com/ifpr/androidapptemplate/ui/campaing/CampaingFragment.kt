package com.ifpr.androidapptemplate.ui.campaing

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.StorageReference
import com.ifpr.androidapptemplate.baseclasses.TalesCampaign
import com.ifpr.androidapptemplate.databinding.FragmentCampaignBinding
import com.ifpr.androidapptemplate.ui.dashboard.DashboardFragment

class CampaingFragment : Fragment() {
    private var _binding: FragmentCampaignBinding? = null

    private lateinit var campaingImageView: ImageView
    private lateinit var campaignSelectImageBtn: Button
    private lateinit var campaingNameEditText: EditText
    private lateinit var campaingTypeSpinner: Spinner
    private lateinit var campaingDescriptionEditText: EditText
    private lateinit var campaingCreateBtn: Button

    private lateinit var databaseReference: DatabaseReference
    private lateinit var storageReference: StorageReference
    private lateinit var auth: FirebaseAuth

    companion object {
        private const val PICK_IMAGE_REQUEST = 1
    }

    private var imageUri : Uri? = null

    private val binding get() = _binding

    private lateinit var campaignViewModel: CampaignViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        campaignViewModel = ViewModelProvider(this).get(CampaignViewModel::class.java)

        _binding = FragmentCampaignBinding.inflate(inflater, container, false)
        val binding = _binding!!
        val root: View = binding.root


        campaignViewModel.text.observe(viewLifecycleOwner) {
            binding.textCampaign.text = it
        }

        campaingImageView = binding.imageCampaign
        campaignSelectImageBtn = binding.buttonSelectImageCampaign
        campaingNameEditText = binding.campaignName
        campaingTypeSpinner = binding.campaignType
        campaingDescriptionEditText = binding.campaignDescription
        campaingCreateBtn = binding.saveCampaignBtn

        auth = FirebaseAuth.getInstance()

        campaignSelectImageBtn.setOnClickListener {
            openFileChoose()
        }

        campaingCreateBtn.setOnClickListener {
            saveCampaign()
        }

        return root

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun openFileChoose(){
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, CampaingFragment.Companion.PICK_IMAGE_REQUEST)
    }

    private fun saveCampaign(){
        val campaignName = campaingNameEditText.text.toString().trim()
        val campaingType = campaingTypeSpinner.selectedItem.toString()
        val campaingDescription = campaingDescriptionEditText.text.toString().trim()

        if(campaignName.isEmpty() || campaingDescription.isEmpty()) {
            Toast.makeText(context, "Por favor, preencha todos os campos obrigatórios", Toast.LENGTH_SHORT).show()
            return
        }

        val talesCampaing = TalesCampaign(campaignName, campaingType, campaingDescription)
        uploadDataToFirestone(talesCampaing)
    }

    private fun uploadDataToFirestone(talesCampaign: TalesCampaign){

        if(imageUri != null){
            val inputStream = context?.contentResolver?.openInputStream(imageUri!!)
            val bytes = inputStream?.readBytes()
            inputStream?.close()

            if(bytes != null){
                val base64Image = Base64.encodeToString(bytes, Base64.DEFAULT)

                talesCampaign.base64Image = base64Image

                saveCampaingIntoDatabase(talesCampaign)
            }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CampaingFragment.Companion.PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK
            && data != null && data.data != null
        ) {
            imageUri = data.data
            Glide.with(this).load(imageUri).into(campaingImageView)
        }
    }

    private fun saveCampaingIntoDatabase(talesCampaign: TalesCampaign) {
        databaseReference = FirebaseDatabase.getInstance().getReference("campanhas")

        val itemId = databaseReference.push().key
        if(itemId != null){
            talesCampaign.ownerUid = auth.uid
            databaseReference.child(itemId).setValue(talesCampaign)
                .addOnSuccessListener {
                    Toast.makeText(context, "Campanha cadastrada com sucesso!", Toast.LENGTH_SHORT)
                        .show()
                    requireActivity().supportFragmentManager.popBackStack()
                }.addOnFailureListener {
                    Toast.makeText(context, "Falha ao cadastrar Campanha", Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(context, "Erro ao gerar ID da Campanha", Toast.LENGTH_SHORT).show()
        }
    }
}