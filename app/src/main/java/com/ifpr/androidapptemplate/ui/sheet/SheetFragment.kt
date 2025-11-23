package com.ifpr.androidapptemplate.ui.sheet

import android.app.Dialog
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
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.ifpr.androidapptemplate.baseclasses.TalesAttributes
import com.ifpr.androidapptemplate.baseclasses.TalesConditions
import com.ifpr.androidapptemplate.baseclasses.TalesGeneralInfo
import com.ifpr.androidapptemplate.baseclasses.TalesItems
import com.ifpr.androidapptemplate.baseclasses.TalesSkills
import com.ifpr.androidapptemplate.databinding.DialogRollBinding
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
    private lateinit var sheetCharSneakBtn: ImageButton
    private lateinit var sheetCharForce: EditText
    private lateinit var sheetCharForceBtn: ImageButton
    private lateinit var sheetCharMove: EditText
    private lateinit var sheetCharMoveBtn: ImageButton
    //Tecnologia
    private lateinit var sheetCharTinker: EditText
    private lateinit var sheetCharTinkerBtn: ImageButton
    private lateinit var sheetCharProgram: EditText
    private lateinit var sheetCharProgramBtn: ImageButton
    private lateinit var sheetCharCalculate: EditText
    private lateinit var sheetCharCalculateBtn: ImageButton
    //Coração
    private lateinit var sheetCharContact: EditText
    private lateinit var sheetCharContactBtn: ImageButton
    private lateinit var sheetCharCharm: EditText
    private lateinit var sheetCharCharmBtn: ImageButton
    private lateinit var sheetCharLead: EditText
    private lateinit var sheetCharLeadBtn: ImageButton
    //Tecnologia
    private lateinit var sheetCharInvestigate: EditText
    private lateinit var sheetCharInvestigateBtn: ImageButton
    private lateinit var sheetCharComprehend: EditText
    private lateinit var sheetCharComprehendBtn: ImageButton
    private lateinit var sheetCharEmpathize: EditText
    private lateinit var sheetCharEmpathizeBtn: ImageButton

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

    //Esconderijo
    private lateinit var sheetCharHideout: EditText

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
    private lateinit var sheetUpdateBtn: Button
    lateinit var fichaId: String
    private lateinit var xpChecks : List<CheckBox>


    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        data = arguments?.getSerializable("charData") as TalesGeneralInfo
        fichaId = arguments?.getString("fichaId") ?: ""
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        _binding = FragmentSheetBinding.inflate(inflater, container, false)
        val binding = _binding!!
        val root: View = binding.root

        val imageBase64 = data.base64Image

        xpChecks = listOf(
            binding.checkboxXp1,
            binding.checkboxXp2,
            binding.checkboxXp3,
            binding.checkboxXp4,
            binding.checkboxXp5,
            binding.checkboxXp6,
            binding.checkboxXp7,
            binding.checkboxXp8,
            binding.checkboxXp9,
            binding.checkboxXp10
        )

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
        sheetCharSneakBtn = binding.btnRollSneak

        sheetCharForce = binding.sheetViewEditForce
        sheetCharForce.setText(data.skills?.force.toString())
        sheetCharForceBtn = binding.btnRollForce

        sheetCharMove = binding.sheetViewEditMove
        sheetCharMove.setText(data.skills?.move.toString())
        sheetCharMoveBtn = binding.btnRollMove

        sheetCharTinker = binding.sheetViewEditTinker
        sheetCharTinker.setText(data.skills?.tinker.toString())
        sheetCharTinkerBtn = binding.btnRollTinker

        sheetCharProgram = binding.sheetViewEditProgram
        sheetCharProgram.setText(data.skills?.program.toString())
        sheetCharProgramBtn = binding.btnRollProgram

        sheetCharCalculate = binding.sheetViewEditCalculate
        sheetCharCalculate.setText(data.skills?.calculate.toString())
        sheetCharCalculateBtn = binding.btnRollCalculate

        sheetCharContact = binding.sheetViewEditContact
        sheetCharContact.setText(data.skills?.contact.toString())
        sheetCharContactBtn = binding.btnRollContact

        sheetCharCharm = binding.sheetViewEditCharm
        sheetCharCharm.setText(data.skills?.charm.toString())
        sheetCharCharmBtn = binding.btnRollCharm

        sheetCharLead = binding.sheetViewEditLead
        sheetCharLead.setText(data.skills?.lead.toString())
        sheetCharLeadBtn = binding.btnRollLead

        sheetCharInvestigate = binding.sheetViewEditInvestigate
        sheetCharInvestigate.setText(data.skills?.investigate.toString())
        sheetCharInvestigateBtn = binding.btnRollInvestigate

        sheetCharComprehend = binding.sheetViewEditComprehend
        sheetCharComprehend.setText(data.skills?.comprehend.toString())
        sheetCharComprehendBtn = binding.btnRollComprehend

        sheetCharEmpathize = binding.sheetViewEditEmpathize
        sheetCharEmpathize.setText(data.skills?.empathize.toString())
        sheetCharEmpathizeBtn = binding.btnRollEmpathize

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

            // Esconderijo
        sheetCharHideout = binding.sheetViewEditHideout
        sheetCharHideout.setText(data.hideout)

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

        sheetUpdateBtn = binding.sheetViewSaveUpdateBtn

        //Botões de Rolagem
        setupRollButton(sheetCharSneakBtn, sheetCharSneak, sheetCharBody, "Furtividade")
        setupRollButton(sheetCharForceBtn, sheetCharForce, sheetCharBody, "Força")
        setupRollButton(sheetCharMoveBtn, sheetCharMove, sheetCharBody, "Movimentação")

        setupRollButton(sheetCharTinkerBtn, sheetCharTinker, sheetCharTech, "Mecânica")
        setupRollButton(sheetCharProgramBtn, sheetCharProgram, sheetCharTech, "Programação")
        setupRollButton(sheetCharCalculateBtn, sheetCharCalculate, sheetCharTech, "Cálculo")

        setupRollButton(sheetCharContactBtn, sheetCharContact, sheetCharHeart, "Contatos")
        setupRollButton(sheetCharCharmBtn, sheetCharCharm, sheetCharHeart, "Charme")
        setupRollButton(sheetCharLeadBtn, sheetCharLead, sheetCharHeart, "Liderança")

        setupRollButton(sheetCharInvestigateBtn, sheetCharInvestigate, sheetCharMind, "Investigação")
        setupRollButton(sheetCharComprehendBtn, sheetCharComprehend, sheetCharMind, "Conhecimento")
        setupRollButton(sheetCharEmpathizeBtn, sheetCharEmpathize, sheetCharMind, "Empatia")

        sheetUpdateBtn.setOnClickListener {
            updateSheet(fichaId)
        }

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

    private fun setupRollButton(button: ImageButton, skill: EditText, attribute: EditText, title: String) {

        button.setOnClickListener {
            val skillValue = skill.text.toString().trim().toIntOrNull() ?: 0
            val attributeValue = attribute.text.toString().trim().toIntOrNull() ?: 0

            rollAttributes(title, skillValue + attributeValue)
        }

    }

    private fun rollDice(quantity: Int): List<Int> {
        return List(quantity) {(1..6).random()}
    }

    private fun countSuccesses(results: List<Int>): Int {
        return results.count { it >= 6 }
    }

    private fun showRollDialog(title: String, results: List<Int>, successes: Int){
        val dialog = Dialog(requireContext())

        val dialogBinding = DialogRollBinding.inflate(layoutInflater)

        dialog.setContentView(dialogBinding.root)

        dialogBinding.dialogTitle.text = title
        dialogBinding.dialogResults.text = "" +
                "Rolagem: ${results.joinToString(", ")}\nSucessos: $successes"

        dialogBinding.dialogOk.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun rollAttributes(title:String, value: Int) {
        val results = rollDice(value)
        val success = countSuccesses(results)

        showRollDialog(title, results, success)
    }


    private fun updateSheet(fichaId: String){

        val updatedAttributes = TalesAttributes(
            sheetCharBody.text.toString().trim().toInt(),
            sheetCharTech.text.toString().trim().toInt(),
            sheetCharHeart.text.toString().trim().toInt(),
            sheetCharMind.text.toString().trim().toInt()
        )

        val updatedSkills = TalesSkills(
            sheetCharSneak.text.toString().trim().toInt(),
            sheetCharForce.text.toString().trim().toInt(),
            sheetCharMove.text.toString().trim().toInt(),
            sheetCharTinker.text.toString().trim().toInt(),
            sheetCharProgram.text.toString().trim().toInt(),
            sheetCharCalculate.text.toString().trim().toInt(),
            sheetCharContact.text.toString().trim().toInt(),
            sheetCharCharm.text.toString().trim().toInt(),
            sheetCharLead.text.toString().trim().toInt(),
            sheetCharInvestigate.text.toString().trim().toInt(),
            sheetCharComprehend.text.toString().trim().toInt(),
            sheetCharEmpathize.text.toString().trim().toInt()
        )

        val updatedItems = TalesItems(
            sheetCharIconicItem.text.toString().trim(),
            2,
            sheetCharItem1.text.toString().trim(),
            sheetCharItem1Bonus.text.toString().trim().toInt(),
            sheetCharItem2.text.toString().trim(),
            sheetCharItem2Bonus.text.toString().trim().toInt(),
            sheetCharItem3.text.toString().trim(),
            sheetCharItem3Bonus.text.toString().trim().toInt(),
            sheetCharItem4.text.toString().trim(),
            sheetCharItem4Bonus.text.toString().trim().toInt(),
            sheetCharItem5.text.toString().trim(),
            sheetCharItem5Bonus.text.toString().trim().toInt()
        )

        val updatedConditions = TalesConditions(
            sheetCharUpset.isChecked,
            sheetCharScared.isChecked,
            sheetCharExhausted.isChecked,
            sheetCharInjured.isChecked,
            sheetCharBroken.isChecked
        )

        val xpCount = xpChecks.count { it.isChecked }

        val updatedSheet = TalesGeneralInfo(
            sheetCharName.text.toString().trim(),
            sheetCharType.text.toString().trim(),
            sheetCharAge.text.toString().trim().toInt(),
            sheetCharLP.text.toString().trim().toInt(),
            sheetCharDrive.text.toString().trim(),
            sheetCharAnchor.text.toString().trim(),
            sheetCharProblem.text.toString().trim(),
            sheetCharPride.text.toString().trim(),
            sheetCharDescription.text.toString().trim(),
            sheetCharSong.text.toString().trim(),
            sheetCharRelationships.text.toString().trim(),
            sheetCharHideout.text.toString().trim(),
            updatedAttributes,
            updatedSkills,
            updatedItems,
            data.base64Image,
            data.imageUrl,
            updatedConditions,
            xpCount,
            sheetCharPrideUser.isChecked
        )

        val database = FirebaseDatabase.getInstance().reference
        val uid = FirebaseAuth.getInstance().uid!!

        database.child("fichas").child(uid).child(fichaId)
            .setValue(updatedSheet)
            .addOnSuccessListener {
                Toast.makeText(context, "Ficha Atualizada!", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(context, "Erro ao atualizar a ficha!", Toast.LENGTH_SHORT).show()
            }

    }

}