package com.ifpr.androidapptemplate.ui.sheet

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.R
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import android.util.Base64
import com.bumptech.glide.Glide
import com.ifpr.androidapptemplate.baseclasses.TalesGeneralInfo
import com.ifpr.androidapptemplate.databinding.FragmentDashboardBinding
import com.ifpr.androidapptemplate.databinding.FragmentSheetBinding
import com.ifpr.androidapptemplate.ui.dashboard.DashboardViewModel

class SheetFragment : Fragment() {

    private var _binding: FragmentSheetBinding? = null

    private lateinit var data: TalesGeneralInfo
    private lateinit var sheetCharImg: ImageView

    //General Info
    private lateinit var sheetCharName: EditText
    private lateinit var sheetCharType: EditText
    private lateinit var sheetCharAge: EditText
    private lateinit var sheetCharLP: EditText
    private lateinit var sheetCharDrive: EditText
    private lateinit var sheetCharAnchor: EditText
    private lateinit var sheetCharProblem: EditText
    private lateinit var sheetCharPride: EditText
    private lateinit var sheetCharPrideUser: CheckBox
    private lateinit var sheetCharDescription: EditText
    private lateinit var sheetCharSong: EditText

    //Atributos
    private lateinit var sheetCharBody: EditText
    private lateinit var sheetCharTech: EditText
    private lateinit var sheetCharHeart: EditText
    private lateinit var sheetCharMind: EditText

    //Condições
    private lateinit var sheetCharUpset: CheckBox
    private lateinit var sheetCharScared: CheckBox
    private lateinit var sheetCharExhausted: CheckBox
    private lateinit var sheetCharInjured: CheckBox
    private lateinit var sheetCharBroken: CheckBox

    //Perícias
    //Corpo
    private lateinit var sheetCharSneak: EditText
    private lateinit var sheetCharForce: EditText
    private lateinit var sheetCharMove: EditText
    //Tecnologia
    private lateinit var sheetCharTinker: EditText
    private lateinit var sheetCharProgram: EditText
    private lateinit var sheetCharCalculate: EditText
    //Coração
    private lateinit var sheetCharContact: EditText
    private lateinit var sheetCharCharm: EditText
    private lateinit var sheetCharLead: EditText
    //Tecnologia
    private lateinit var sheetCharInvestigate: EditText
    private lateinit var sheetCharComprehend: EditText
    private lateinit var sheetCharEmpathize: EditText

    //Relacionamentos
    private lateinit var sheetCharRelationships: EditText

    //Itens
    private lateinit var sheetCharIconicItem: EditText
    private lateinit var sheetCharItem1: EditText
    private lateinit var sheetCharItem1Bonus: EditText
    private lateinit var sheetCharItem2: EditText
    private lateinit var sheetCharItem2Bonus: EditText
    private lateinit var sheetCharItem3: EditText
    private lateinit var sheetCharItem3Bonus: EditText
    private lateinit var sheetCharItem4: EditText
    private lateinit var sheetCharItem4Bonus: EditText
    private lateinit var sheetCharItem5: EditText
    private lateinit var sheetCharItem5Bonus: EditText

    //Experiência
    private lateinit var sheetCharXp1: CheckBox
    private lateinit var sheetCharXp2: CheckBox
    private lateinit var sheetCharXp3: CheckBox
    private lateinit var sheetCharXp4: CheckBox
    private lateinit var sheetCharXp5: CheckBox
    private lateinit var sheetCharXp6: CheckBox
    private lateinit var sheetCharXp7: CheckBox
    private lateinit var sheetCharXp8: CheckBox
    private lateinit var sheetCharXp9: CheckBox
    private lateinit var sheetCharXp10: CheckBox



    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        data = arguments?.getSerializable("charData") as TalesGeneralInfo
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val sheetViewModel = ViewModelProvider (this).get(SheetViewModel::class.java)


        _binding = FragmentSheetBinding.inflate(inflater, container, false)
        val binding = _binding!!
        val root: View = binding.root

        val imageBase64 = data.base64Image

        if (!imageBase64.isNullOrEmpty()) {
            val bitmap = base64Bitmap(imageBase64)
            if (bitmap != null) {
                val bytes = Base64.decode(imageBase64, Base64.DEFAULT)

                Glide.with(requireContext())
                    .asBitmap()
                    .load(bytes)
                    .into(binding.sheetViewImage)
            }
        }

        //General Info
        sheetCharName = binding.sheetViewEditName
        sheetCharName.setText(data.name)

        sheetCharType = binding.sheetViewEditType
        sheetCharType.setText(data.type)

        sheetCharAge = binding.sheetViewEditAge
        sheetCharAge.setText(data.age.toString())

        sheetCharLP = binding.sheetViewEditLp
        sheetCharLP.setText(data.luckPoints.toString())

        sheetCharDrive = binding.sheetViewEditDrive
        sheetCharDrive.setText(data.drive)

        sheetCharAnchor = binding.sheetViewEditAnchor
        sheetCharAnchor.setText(data.anchor)

        sheetCharProblem = binding.sheetViewEditProblem
        sheetCharProblem.setText(data.problem)

        sheetCharPride = binding.sheetViewEditPride
        sheetCharPride.setText(data.pride)

        sheetCharPrideUser = binding.checkboxPride

        sheetCharDescription = binding.sheetViewEditDescription
        sheetCharDescription.setText(data.description)

        sheetCharSong = binding.sheetViewEditSong
        sheetCharSong.setText(data.song)

        //Atributos
        sheetCharBody = binding.sheetViewEditBody
        sheetCharBody.setText(data.attributes?.body.toString())

        sheetCharTech = binding.sheetViewEditTech
        sheetCharTech.setText(data.attributes?.body.toString())

        sheetCharHeart = binding.sheetViewEditHeart
        sheetCharHeart.setText(data.attributes?.heart.toString())

        sheetCharMind = binding.sheetViewEditMind
        sheetCharMind.setText(data.attributes?.mind.toString())

        //Condições
        sheetCharUpset = binding.checkboxViewEditUpset
        sheetCharScared = binding.checkboxViewEditScared
        sheetCharExhausted = binding.checkboxViewEditExhausted
        sheetCharInjured = binding.checkboxViewEditInjured
        sheetCharBroken = binding.checkboxViewEditBroken

        //Perícias
            //Corpo
        sheetCharSneak = binding.sheetViewEditSneak
        sheetCharSneak.setText(data.skills?.sneak.toString())

        sheetCharForce = binding.sheetViewEditForce
        sheetCharForce.setText(data.skills?.force.toString())

        sheetCharMove = binding.sheetViewEditMove
        sheetCharMove.setText(data.skills?.move.toString())

            //Tecnologia
        sheetCharTinker = binding.sheetViewEditTinker
        sheetCharTinker.setText(data.skills?.tinker.toString())

        sheetCharProgram = binding.sheetViewEditProgram
        sheetCharProgram.setText(data.skills?.program.toString())

        sheetCharCalculate = binding.sheetViewEditCalculate
        sheetCharCalculate.setText(data.skills?.calculate.toString())

            //Coração
        sheetCharContact = binding.sheetViewEditContact
        sheetCharContact.setText(data.skills?.contact.toString())

        sheetCharCharm = binding.sheetViewEditCharm
        sheetCharCharm.setText(data.skills?.charm.toString())

        sheetCharLead = binding.sheetViewEditLead
        sheetCharLead.setText(data.skills?.lead.toString())

            //Mente
        sheetCharInvestigate = binding.sheetViewEditInvestigate
        sheetCharInvestigate.setText(data.skills?.investigate.toString())

        sheetCharComprehend = binding.sheetViewEditComprehend
        sheetCharComprehend.setText(data.skills?.comprehend.toString())

        sheetCharEmpathize = binding.sheetViewEditEmpathize
        sheetCharEmpathize.setText(data.skills?.empathize.toString())

        //Relacionamentos
        sheetCharRelationships = binding.sheetViewEditRelationships
        sheetCharRelationships.setText(data.relationships)

            //Itens
        sheetCharIconicItem = binding.sheetViewEditIconicItem
        sheetCharIconicItem.setText(data.items?.iconicItem)

        sheetCharItem1 = binding.sheetViewEditItem1
        sheetCharItem1.setText(data.items?.item1)

        sheetCharItem1Bonus = binding.sheetViewEditItem1bonus
        sheetCharItem1Bonus.setText("+" + data.items?.item1Bonus.toString())

        sheetCharItem2 = binding.sheetViewEditItem2
        sheetCharItem2.setText(data.items?.item2)

        sheetCharItem2Bonus = binding.sheetViewEditItem2bonus
        sheetCharItem2Bonus.setText("+" + data.items?.item2Bonus.toString())

        sheetCharItem3 = binding.sheetViewEditItem3
        sheetCharItem3.setText(data.items?.item3)

        sheetCharItem3Bonus = binding.sheetViewEditItem3bonus
        sheetCharItem3Bonus.setText("+" + data.items?.item3Bonus.toString())

        sheetCharItem4 = binding.sheetViewEditItem4
        sheetCharItem4.setText(data.items?.item4)


        sheetCharItem4Bonus = binding.sheetViewEditItem4bonus
        sheetCharItem4Bonus.setText("+" + data.items?.item4Bonus.toString())

        sheetCharItem5 = binding.sheetViewEditItem5
        sheetCharItem5.setText(data.items?.item5)

        sheetCharItem5Bonus = binding.sheetViewEditItem5bonus
        sheetCharItem5Bonus.setText("+" + data.items?.item5Bonus.toString())

            //Experiência
        sheetCharXp1 = binding.checkboxXp1
        sheetCharXp2 = binding.checkboxXp2
        sheetCharXp3 = binding.checkboxXp3
        sheetCharXp4 = binding.checkboxXp4
        sheetCharXp5 = binding.checkboxXp5
        sheetCharXp6 = binding.checkboxXp6
        sheetCharXp7 = binding.checkboxXp7
        sheetCharXp8 = binding.checkboxXp8
        sheetCharXp9 = binding.checkboxXp9
        sheetCharXp10 = binding.checkboxXp10




        return root
    }

    private fun base64Bitmap(base64: String): Bitmap? {
        return try {
            val decodeBytes = Base64.decode(base64, Base64.DEFAULT)
            BitmapFactory.decodeByteArray(decodeBytes, 0, decodeBytes.size)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

}