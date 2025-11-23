package com.ifpr.androidapptemplate.ui.dashboard

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
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.StorageReference
import com.ifpr.androidapptemplate.R
import com.ifpr.androidapptemplate.baseclasses.TalesAttributes
import com.ifpr.androidapptemplate.baseclasses.TalesGeneralInfo
import com.ifpr.androidapptemplate.baseclasses.TalesItems
import com.ifpr.androidapptemplate.baseclasses.TalesSkills
import com.ifpr.androidapptemplate.databinding.FragmentDashboardBinding


class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null

//    private lateinit var tarefaEditText: EditText
    private lateinit var itemImageView: ImageView
    private var imageUri: Uri? = null
//    private lateinit var descricaoEditText: EditText
//    private lateinit var dataInicioEditText: EditText
//    private lateinit var dataFimEditText: EditText

    // Campos para as informações gerais
    private lateinit var sheetCharNameEditText: EditText
    private lateinit var sheetCharTypeEditText: EditText
    private lateinit var sheetCharAgeEditText: EditText
    private lateinit var sheetCharLuckPointsEditText: EditText
    private lateinit var sheetCharDriveEditText: EditText
    private lateinit var sheetCharAnchorEditText: EditText
    private lateinit var sheetCharProblemEditText: EditText
    private lateinit var sheetCharPrideEditText: EditText
    private lateinit var sheetCharDescriptionEditText: EditText
    private lateinit var sheetCharSongEditText: EditText

    // Campos para os atributos
    private lateinit var sheetCharBodyEditText: EditText
    private lateinit var sheetCharTechEditText: EditText
    private lateinit var sheetCharHeartEditText: EditText
    private lateinit var sheetCharMindEditText: EditText

    // Campos para as habilidades
    private lateinit var sheetCharSneakEditText: EditText
    private lateinit var sheetCharForceEditText: EditText
    private lateinit var sheetCharMoveEditText: EditText
    private lateinit var sheetCharTinkerEditText: EditText
    private lateinit var sheetCharProgramEditText: EditText
    private lateinit var sheetCharCalculateEditText: EditText
    private lateinit var sheetCharContactEditText: EditText
    private lateinit var sheetCharCharmEditText: EditText
    private lateinit var sheetCharLeadEditText: EditText
    private lateinit var sheetCharInvestigateEditText: EditText
    private lateinit var sheetCharComprehendEditText: EditText
    private lateinit var sheetCharEmpathizeEditText: EditText

    // Campos para os relacionamentos
    private lateinit var sheetCharRelationshipsEditText: EditText

    // Campos para os itens
    private lateinit var sheetCharIconicItemEditText: EditText
    private lateinit var sheetCharItem1EditText: EditText
    private lateinit var sheetCharItem1BonusEditText: EditText
    private lateinit var sheetCharItem2EditText: EditText
    private lateinit var sheetCharItem2BonusEditText: EditText
    private lateinit var sheetCharItem3EditText: EditText
    private lateinit var sheetCharItem3BonusEditText: EditText
    private lateinit var sheetCharItem4EditText: EditText
    private lateinit var sheetCharItem4BonusEditText: EditText
    private lateinit var sheetCharItem5EditText: EditText
    private lateinit var sheetCharItem5BonusEditText: EditText

    // Campo Esconderijo
    private lateinit var sheetCharHideoutEditText: EditText

    //TODO("Declare aqui as outras variaveis do tipo EditText que foram inseridas no layout")

    private lateinit var salvarButton: Button
    private lateinit var selectImageButton: Button
    private lateinit var databaseReference: DatabaseReference
    private lateinit var storageReference: StorageReference
    private lateinit var auth: FirebaseAuth

    companion object {
        private const val PICK_IMAGE_REQUEST = 1
    }

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val dashboardViewModel = ViewModelProvider(this).get(DashboardViewModel::class.java)

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textDashboard
        dashboardViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }

        val view = inflater.inflate(R.layout.fragment_dashboard, container, false)
        itemImageView = view.findViewById(R.id.image_item)
        salvarButton = view.findViewById(R.id.salvarItemButton)
        selectImageButton = view.findViewById(R.id.button_select_image)
//        tarefaEditText = view.findViewById(R.id.tarefaItemEditText)
        //TODO("Capture aqui os outro campos que foram inseridos no layout. Por exemplo, ate
        // o momento so foi capturado o endereco (EditText)")
//        descricaoEditText = view.findViewById(R.id.descricaoItemEditText)
//        dataInicioEditText = view.findViewById(R.id.dataInicioItemEditText)
//        dataFimEditText = view.findViewById(R.id.dataFimItemEditText)

        // Inicializando os campos de EditText com view.findViewById
        sheetCharNameEditText = view.findViewById(R.id.sheetCharName)
        sheetCharTypeEditText = view.findViewById(R.id.sheetCharType)
        sheetCharAgeEditText = view.findViewById(R.id.sheetCharAge)
        sheetCharLuckPointsEditText = view.findViewById(R.id.sheetCharLuckPoints)
        sheetCharDriveEditText = view.findViewById(R.id.sheetCharDrive)
        sheetCharAnchorEditText = view.findViewById(R.id.sheetCharAnchor)
        sheetCharProblemEditText = view.findViewById(R.id.sheetCharProblem)
        sheetCharPrideEditText = view.findViewById(R.id.sheetCharPride)
        sheetCharDescriptionEditText = view.findViewById(R.id.sheetCharDescription)
        sheetCharSongEditText = view.findViewById(R.id.sheetCharSong)

        // Inicializando os campos de atributos
        sheetCharBodyEditText = view.findViewById(R.id.sheetCharBody)
        sheetCharTechEditText = view.findViewById(R.id.sheetCharTech)
        sheetCharHeartEditText = view.findViewById(R.id.sheetCharHeart)
        sheetCharMindEditText = view.findViewById(R.id.sheetCharMind)

        // Inicializando os campos de habilidades
        sheetCharSneakEditText = view.findViewById(R.id.sheetCharSneak)
        sheetCharForceEditText = view.findViewById(R.id.sheetCharForce)
        sheetCharMoveEditText = view.findViewById(R.id.sheetCharMove)
        sheetCharTinkerEditText = view.findViewById(R.id.sheetCharTinker)
        sheetCharProgramEditText = view.findViewById(R.id.sheetCharProgram)
        sheetCharCalculateEditText = view.findViewById(R.id.sheetCharCalculate)
        sheetCharContactEditText = view.findViewById(R.id.sheetCharContact)
        sheetCharCharmEditText = view.findViewById(R.id.sheetCharCharm)
        sheetCharLeadEditText = view.findViewById(R.id.sheetCharLead)
        sheetCharInvestigateEditText = view.findViewById(R.id.sheetCharInvestigate)
        sheetCharComprehendEditText = view.findViewById(R.id.sheetCharComprehend)
        sheetCharEmpathizeEditText = view.findViewById(R.id.sheetCharEmpathize)

        // Inicializando o campo de relacionamentos
        sheetCharRelationshipsEditText = view.findViewById(R.id.sheetCharRelationships)

        // Inicializando os campos de itens
        sheetCharIconicItemEditText = view.findViewById(R.id.sheetCharIconicItem)
        sheetCharItem1EditText = view.findViewById(R.id.sheetCharItem1)
        sheetCharItem1BonusEditText = view.findViewById(R.id.sheetCharItem1Bonus)
        sheetCharItem2EditText = view.findViewById(R.id.sheetCharItem2)
        sheetCharItem2BonusEditText = view.findViewById(R.id.sheetCharItem2Bonus)
        sheetCharItem3EditText = view.findViewById(R.id.sheetCharItem3)
        sheetCharItem3BonusEditText = view.findViewById(R.id.sheetCharItem3Bonus)
        sheetCharItem4EditText = view.findViewById(R.id.sheetCharItem4)
        sheetCharItem4BonusEditText = view.findViewById(R.id.sheetCharItem4Bonus)
        sheetCharItem5EditText = view.findViewById(R.id.sheetCharItem5)
        sheetCharItem5BonusEditText = view.findViewById(R.id.sheetCharItem5Bonus)

        // Inicializando o campo do esconderijo
        sheetCharHideoutEditText = view.findViewById(R.id.sheetCharHideout)

        auth = FirebaseAuth.getInstance()

        selectImageButton.setOnClickListener {
            openFileChooser()
        }

        salvarButton.setOnClickListener {
            salvarItem()
        }

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun openFileChooser() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    private fun salvarItem() {
        //TODO("Capture aqui o conteudo que esta nos outros editTexts que foram criados")
        val name = sheetCharNameEditText.text.toString().trim()
        val type = sheetCharTypeEditText.text.toString().trim()
        val age = try {
            sheetCharAgeEditText.text.toString().toInt()
        } catch (e: NumberFormatException) {
            0
        }
        val luckPoints = try {
            sheetCharLuckPointsEditText.text.toString().toInt()
        } catch (e: NumberFormatException) {
            0
        }
        val drive = sheetCharDriveEditText.text.toString().trim()
        val anchor = sheetCharAnchorEditText.text.toString().trim()
        val problem = sheetCharProblemEditText.text.toString().trim()
        val pride = sheetCharPrideEditText.text.toString().trim()
        val description = sheetCharDescriptionEditText.text.toString().trim()
        val song = sheetCharSongEditText.text.toString().trim()

// Atributos
        val body = try {
            sheetCharBodyEditText.text.toString().toInt()
        } catch (e: NumberFormatException) {
            0
        }
        val tech = try {
            sheetCharTechEditText.text.toString().toInt()
        } catch (e: NumberFormatException) {
            0
        }
        val heart = try {
            sheetCharHeartEditText.text.toString().toInt()
        } catch (e: NumberFormatException) {
            0
        }
        val mind = try {
            sheetCharMindEditText.text.toString().toInt()
        } catch (e: NumberFormatException) {
            0
        }

// Habilidades
        val sneak = try {
            sheetCharSneakEditText.text.toString().toInt()
        } catch (e: NumberFormatException) {
            0
        }
        val force = try {
            sheetCharForceEditText.text.toString().toInt()
        } catch (e: NumberFormatException) {
            0
        }
        val move = try {
            sheetCharMoveEditText.text.toString().toInt()
        } catch (e: NumberFormatException) {
            0
        }
        val tinker = try {
            sheetCharTinkerEditText.text.toString().toInt()
        } catch (e: NumberFormatException) {
            0
        }
        val program = try {
            sheetCharProgramEditText.text.toString().toInt()
        } catch (e: NumberFormatException) {
            0
        }
        val calculate = try {
            sheetCharCalculateEditText.text.toString().toInt()
        } catch (e: NumberFormatException) {
            0
        }
        val contact = try {
            sheetCharContactEditText.text.toString().toInt()
        } catch (e: NumberFormatException) {
            0
        }
        val charm = try {
            sheetCharCharmEditText.text.toString().toInt()
        } catch (e: NumberFormatException) {
            0
        }
        val lead = try {
            sheetCharLeadEditText.text.toString().toInt()
        } catch (e: NumberFormatException) {
            0
        }
        val investigate = try {
            sheetCharInvestigateEditText.text.toString().toInt()
        } catch (e: NumberFormatException) {
            0
        }
        val comprehend = try {
            sheetCharComprehendEditText.text.toString().toInt()
        } catch (e: NumberFormatException) {
            0
        }
        val empathize = try {
            sheetCharEmpathizeEditText.text.toString().toInt()
        } catch (e: NumberFormatException) {
            0
        }

// Relacionamentos
        val relationships = sheetCharRelationshipsEditText.text.toString().trim()

// Itens
        val iconicItem = sheetCharIconicItemEditText.text.toString().trim()

        val item1 = sheetCharItem1EditText.text.toString().trim()
        val item2 = sheetCharItem2EditText.text.toString().trim()
        val item3 = sheetCharItem3EditText.text.toString().trim()
        val item4 = sheetCharItem4EditText.text.toString().trim()
        val item5 = sheetCharItem5EditText.text.toString().trim()

        val item1Bonus = try {
            sheetCharItem1BonusEditText.text.toString().toInt()
        } catch (e: NumberFormatException) {
            0
        }
        val item2Bonus = try {
            sheetCharItem2BonusEditText.text.toString().toInt()
        } catch (e: NumberFormatException) {
            0
        }
        val item3Bonus = try {
            sheetCharItem3BonusEditText.text.toString().toInt()
        } catch (e: NumberFormatException) {
            0
        }
        val item4Bonus = try {
            sheetCharItem4BonusEditText.text.toString().toInt()
        } catch (e: NumberFormatException) {
            0
        }
        val item5Bonus = try {
            sheetCharItem5BonusEditText.text.toString().toInt()
        } catch (e: NumberFormatException) {
            0
        }

        val itensNome = listOf(item1, item2, item3, item4, item5)
        val itensBonus = listOf(item1Bonus, item2Bonus, item3Bonus, item4Bonus, item5Bonus)

        for (i in itensNome.indices) {
            // Verifica se o nome do item não está vazio e o bônus é 0
            if (itensNome[i].isNotEmpty() && itensBonus[i] == 0) {
                Toast.makeText(context, "Por favor, insira o bônus do seu item '${itensNome[i]}'", Toast.LENGTH_SHORT).show()
                return
            }
        }

        val attributes = TalesAttributes(body, tech, heart, mind)
        val skills = TalesSkills(sneak, force, move, tinker, program, calculate, contact, charm, lead, investigate, comprehend, empathize)
        val items = TalesItems(iconicItem, 2, item1, item1Bonus, item2, item2Bonus, item3, item3Bonus, item4, item4Bonus, item5, item5Bonus )


// Esconderijo
        val hideout = sheetCharHideoutEditText.text.toString().trim()

//        val tarefa = tarefaEditText.text.toString().trim()
//        val descricaoTarefa = descricaoEditText.text.toString().trim()
//        val dataInicio = dataInicioEditText.text.toString().trim()
//        val dataFim = dataFimEditText.text.toString().trim()



        // Verifica se campos obrigatórios estão vazios ou com valores inválidos
        if (name.isEmpty() || type.isEmpty() || drive.isEmpty() || anchor.isEmpty() ||
            problem.isEmpty() || pride.isEmpty() || description.isEmpty() || song.isEmpty() ||
            body == null || tech == null || heart == null || mind == null ||
            iconicItem.isNullOrEmpty() || age == 0) {

            Toast.makeText(context, "Por favor, preencha todos os campos obrigatórios", Toast.LENGTH_SHORT)
                .show()
            return
        }

// Verificar se os objetos opcionais são nulos
        if (attributes == null || skills == null || items == null) {
            Toast.makeText(context, "Certifique-se de preencher os campos adicionais opcionais", Toast.LENGTH_SHORT)
                .show()
            return
        }
        uploadImageToFirestore()
    }


    private fun uploadImageToFirestore() {
        if (imageUri != null) {
            val inputStream = context?.contentResolver?.openInputStream(imageUri!!)
            val bytes = inputStream?.readBytes()
            inputStream?.close()

            if (bytes != null) {
                val base64Image = Base64.encodeToString(bytes, Base64.DEFAULT)
//                val tarefa = tarefaEditText.text.toString().trim()
//                val descricaoTarefa = descricaoEditText.text.toString().trim()
//                val dataInicio = dataInicioEditText.text.toString().trim()
//                val dataFim = dataFimEditText.text.toString().trim()

                val name = sheetCharNameEditText.text.toString().trim()
                val type = sheetCharTypeEditText.text.toString().trim()
                val age = try {
                    sheetCharAgeEditText.text.toString().toInt()
                } catch (e: NumberFormatException) {
                    0
                }
                val luckPoints = try {
                    sheetCharLuckPointsEditText.text.toString().toInt()
                } catch (e: NumberFormatException) {
                    0
                }
                val drive = sheetCharDriveEditText.text.toString().trim()
                val anchor = sheetCharAnchorEditText.text.toString().trim()
                val problem = sheetCharProblemEditText.text.toString().trim()
                val pride = sheetCharPrideEditText.text.toString().trim()
                val description = sheetCharDescriptionEditText.text.toString().trim()
                val song = sheetCharSongEditText.text.toString().trim()

                // Coletando os valores dos atributos
                val body = try {
                    sheetCharBodyEditText.text.toString().toInt()
                } catch (e: NumberFormatException) {
                    0
                }
                val tech = try {
                    sheetCharTechEditText.text.toString().toInt()
                } catch (e: NumberFormatException) {
                    0
                }
                val heart = try {
                    sheetCharHeartEditText.text.toString().toInt()
                } catch (e: NumberFormatException) {
                    0
                }
                val mind = try {
                    sheetCharMindEditText.text.toString().toInt()
                } catch (e: NumberFormatException) {
                    0
                }

                // Coletando as habilidades (skills)
                val sneak = try {
                    sheetCharSneakEditText.text.toString().toInt()
                } catch (e: NumberFormatException) {
                    0
                }
                val force = try {
                    sheetCharForceEditText.text.toString().toInt()
                } catch (e: NumberFormatException) {
                    0
                }
                val move = try {
                    sheetCharMoveEditText.text.toString().toInt()
                } catch (e: NumberFormatException) {
                    0
                }
                val tinker = try {
                    sheetCharTinkerEditText.text.toString().toInt()
                } catch (e: NumberFormatException) {
                    0
                }
                val program = try {
                    sheetCharProgramEditText.text.toString().toInt()
                } catch (e: NumberFormatException) {
                    0
                }
                val calculate = try {
                    sheetCharCalculateEditText.text.toString().toInt()
                } catch (e: NumberFormatException) {
                    0
                }
                val contact = try {
                    sheetCharContactEditText.text.toString().toInt()
                } catch (e: NumberFormatException) {
                    0
                }
                val charm = try {
                    sheetCharCharmEditText.text.toString().toInt()
                } catch (e: NumberFormatException) {
                    0
                }
                val lead = try {
                    sheetCharLeadEditText.text.toString().toInt()
                } catch (e: NumberFormatException) {
                    0
                }
                val investigate = try {
                    sheetCharInvestigateEditText.text.toString().toInt()
                } catch (e: NumberFormatException) {
                    0
                }
                val comprehend = try {
                    sheetCharComprehendEditText.text.toString().toInt()
                } catch (e: NumberFormatException) {
                    0
                }
                val empathize = try {
                    sheetCharEmpathizeEditText.text.toString().toInt()
                } catch (e: NumberFormatException) {
                    0
                }

                // Coletando os relacionamentos
                val relationships = sheetCharRelationshipsEditText.text.toString().trim()

                // Coletando os itens
                val iconicItem = sheetCharIconicItemEditText.text.toString().trim()

                val item1 = sheetCharItem1EditText.text.toString().trim()
                val item2 = sheetCharItem2EditText.text.toString().trim()
                val item3 = sheetCharItem3EditText.text.toString().trim()
                val item4 = sheetCharItem4EditText.text.toString().trim()
                val item5 = sheetCharItem5EditText.text.toString().trim()

                val item1Bonus = try {
                    sheetCharItem1BonusEditText.text.toString().toInt()
                } catch (e: NumberFormatException) {
                    0
                }
                val item2Bonus = try {
                    sheetCharItem2BonusEditText.text.toString().toInt()
                } catch (e: NumberFormatException) {
                    0
                }
                val item3Bonus = try {
                    sheetCharItem3BonusEditText.text.toString().toInt()
                } catch (e: NumberFormatException) {
                    0
                }
                val item4Bonus = try {
                    sheetCharItem4BonusEditText.text.toString().toInt()
                } catch (e: NumberFormatException) {
                    0
                }
                val item5Bonus = try {
                    sheetCharItem5BonusEditText.text.toString().toInt()
                } catch (e: NumberFormatException) {
                    0
                }

                // Criando o objeto para os atributos, habilidades e itens
                val attributes = TalesAttributes(body, tech, heart, mind)
                val skills = TalesSkills(sneak, force, move, tinker, program, calculate, contact, charm, lead, investigate, comprehend, empathize)
                val items = TalesItems(iconicItem, 2, item1, item1Bonus, item2, item2Bonus, item3, item3Bonus, item4, item4Bonus, item5, item5Bonus)

                // Coletando o esconderijo
                val hideout = sheetCharHideoutEditText.text.toString().trim()
                //TODO("Capture aqui o conteudo que esta nos outros editTexts que foram criados")

//                val item = Item(tarefa, descricaoTarefa, dataInicio, dataFim, base64Image)
                val talesSheet = TalesGeneralInfo(name, type, age, luckPoints, drive, anchor, problem, pride, description, song, relationships, hideout, attributes, skills, items, base64Image)

                saveItemIntoDatabase(talesSheet)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK
            && data != null && data.data != null
        ) {
            imageUri = data.data
            Glide.with(this).load(imageUri).into(itemImageView)
        }
    }

    private fun saveItemIntoDatabase(talesSheet: TalesGeneralInfo) {
        //TODO("Altere a raiz que sera criada no seu banco de dados do realtime database.
        // Renomeie a raiz itens")
        databaseReference = FirebaseDatabase.getInstance().getReference("fichas")

        // Cria uma chave unica para o novo item
        val itemId = databaseReference.push().key
        if (itemId != null) {
            databaseReference.child(auth.uid.toString()).child(itemId).setValue(talesSheet)
                .addOnSuccessListener {
                    Toast.makeText(context, "Ficha cadastrado com sucesso!", Toast.LENGTH_SHORT)
                        .show()
                    requireActivity().supportFragmentManager.popBackStack()
                }.addOnFailureListener {
                    Toast.makeText(context, "Falha ao cadastrar o Ficha", Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(context, "Erro ao gerar o ID do item", Toast.LENGTH_SHORT).show()
        }
    }
}