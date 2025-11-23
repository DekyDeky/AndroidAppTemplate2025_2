package com.ifpr.androidapptemplate.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import android.util.Base64
import android.widget.*
import android.graphics.BitmapFactory
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.ifpr.androidapptemplate.R
import com.ifpr.androidapptemplate.baseclasses.TalesGeneralInfo
import com.ifpr.androidapptemplate.databinding.FragmentHomeBinding
import com.ifpr.androidapptemplate.ui.ai.AiLogicActivity

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    private lateinit var currentAddressTextView: TextView

    private lateinit var IFPRDistanceTextView: TextView
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    private lateinit var locationRequest: LocationRequest

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

//        inicializaGerenciamentoLocalizacao(view)

        val container = view.findViewById<LinearLayout>(R.id.itemContainer)
        carregarFichas(container)

        val fab = view.findViewById<FloatingActionButton>(R.id.fab_ai)

        fab.setOnClickListener {
            val context = view.context
            val intent = Intent(context, AiLogicActivity::class.java)
            context.startActivity(intent)
        }

        return view
    }

//    private fun inicializaGerenciamentoLocalizacao(view: View) {
//        currentAddressTextView = view.findViewById(R.id.currentAddressTextView)
//        IFPRDistanceTextView = view.findViewById(R.id.IFPRDistanceTextView)
//
//        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
//
//        if (ActivityCompat.checkSelfPermission(
//                requireContext(),
//                Manifest.permission.ACCESS_FINE_LOCATION
//            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
//                requireContext(),
//                Manifest.permission.ACCESS_COARSE_LOCATION
//            ) != PackageManager.PERMISSION_GRANTED
//        ) {
//            requestLocationPermission()
//        } else {
//            getCurrentLocation()
//        }
//    }
//
//    private fun requestLocationPermission() {
//        requestPermissions(
//            arrayOf(
//                Manifest.permission.ACCESS_FINE_LOCATION,
//                Manifest.permission.ACCESS_COARSE_LOCATION
//            ),
//            LOCATION_PERMISSION_REQUEST_CODE
//        )
//    }
//
//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<out String>,
//        grantResults: IntArray
//    ) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
//            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                getCurrentLocation()
//            } else {
//                Snackbar.make(
//                    requireView(),
//                    "Permission denied. Cannot access location.",
//                    Snackbar.LENGTH_LONG
//                ).show()
//            }
//        }
//    }
//
//    private fun getCurrentLocation() {
//        if (ActivityCompat.checkSelfPermission(
//                requireContext(),
//                Manifest.permission.ACCESS_FINE_LOCATION
//            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
//                requireContext(),
//                Manifest.permission.ACCESS_COARSE_LOCATION
//            ) != PackageManager.PERMISSION_GRANTED
//        ) {
//            return
//        }
//
//
//        locationCallback = object : LocationCallback() {
//            override fun onLocationResult(locationResult: LocationResult) {
//                locationResult.lastLocation?.let { location ->
//                    displayAddress(location)
//                }
//            }
//        }
//
//        locationRequest = LocationRequest.create().apply {
//            interval = 30000 // Intervalo em milissegundos para atualizacoes de localizacao
//            fastestInterval =
//                30000 // O menor intervalo de tempo para receber atualizacoes de localizacao
//            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
//        }
//
//        fusedLocationClient.requestLocationUpdates(
//            locationRequest,
//            locationCallback,
//            Looper.getMainLooper()
//        )
//
//    }
//
//
//    private fun displayAddress(location: Location) {
//        val geocoder = Geocoder(requireContext(), Locale.getDefault())
//        val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)
//
//        CoroutineScope(Dispatchers.IO).launch {
//            try {
//                val address = addresses?.firstOrNull()?.getAddressLine(0) ?: "Address not found"
//                withContext(Dispatchers.Main) {
//                    currentAddressTextView.text = address
//                }
//            } catch (e: Exception) {
//                withContext(Dispatchers.Main) {
//                    currentAddressTextView.text = "Error: ${e.message}"
//                }
//            }
//        }
//
//        val targetLocation = Location("").apply {
//            latitude = -24.33522997524075
//            longitude = -50.65224388708329
//        }
//
//        val distanceInMeters = location.distanceTo(targetLocation)
//        val distanceInKm = distanceInMeters / 1000
//        IFPRDistanceTextView.text = "Distância até o IFPR: %.2f km".format(distanceInKm)
//    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun carregarFichas(container: LinearLayout) {
        val uid = FirebaseAuth.getInstance().currentUser!!.uid
        val databaseRef = FirebaseDatabase.getInstance().getReference("fichas").child(uid)

        databaseRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                container.removeAllViews()


                for (itemSnapshot in snapshot.children) {
                    val item = itemSnapshot.getValue(TalesGeneralInfo::class.java) ?: continue

                    val itemView = LayoutInflater.from(container.context)
                        .inflate(R.layout.item_template, container, false)

                    val imageView = itemView.findViewById<ImageView>(R.id.item_image)
//                        val tarefaView = itemView.findViewById<TextView>(R.id.item_tarefa)
//                        val descricaoView = itemView.findViewById<TextView>(R.id.item_descricao)
//                        val dataInicio = itemView.findViewById<TextView>(R.id.item_dataInicio)
//                        val dataFim = itemView.findViewById<TextView>(R.id.item_dataFim)
                    val charNameView = itemView.findViewById<TextView>(R.id.sheet_charName)
                    val charAgeView = itemView.findViewById<TextView>(R.id.sheet_charAge)
                    val charTypeView = itemView.findViewById<TextView>(R.id.sheet_charType)
                    val charDescriptionView = itemView.findViewById<TextView>(R.id.sheet_charDescription)
                    val charBodyView = itemView.findViewById<TextView>(R.id.sheet_charBody)
                    val charTechView = itemView.findViewById<TextView>(R.id.sheet_charTech)
                    val charHeartView = itemView.findViewById<TextView>(R.id.sheet_charHeart)
                    val charMindView = itemView.findViewById<TextView>(R.id.sheet_charMind)

                    val sheetOpenBtn = itemView.findViewById<Button>(R.id.sheet_openBtn)

//                        tarefaView.text = "${item.tarefa ?: "Não informado"}"
//                        descricaoView.text = "${item.descricao ?: "Não informado"}"
//                        dataInicio.text = "Ínicio em: ${item.dataInicio ?: "Não informado"}"
//                        dataFim.text = "Finalização em: ${item.dataFim ?: "Não informado"}"

                    charNameView.text = item.name ?: " Não informado"
                    charAgeView.text = "Idade: ${item.age.toString() ?: " Não informado"}"
                    charTypeView.text = "Arquétipo: ${item.type ?: " Não informado"}"
                    charDescriptionView.text = "Descrição: ${item.description ?: " Não Informado"}"
                    charBodyView.text = item.attributes?.body?.toString() ?: "Não Informado"
                    charTechView.text = item.attributes?.tech?.toString() ?: "Não Informado"
                    charHeartView.text = item.attributes?.heart?.toString() ?: "Não Informado"
                    charMindView.text  = item.attributes?.mind?.toString() ?: "Não Informado"

                    if (!item.imageUrl.isNullOrEmpty()) {
                        Glide.with(container.context).load(item.imageUrl).into(imageView)
                    } else if (!item.base64Image.isNullOrEmpty()) {
                        try {
                            val bytes = Base64.decode(item.base64Image, Base64.DEFAULT)
                            val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                            imageView.setImageBitmap(bitmap)
                        } catch (_: Exception) {}
                    }

                    sheetOpenBtn.setOnClickListener {
                        openCharSheet(item)
                    }

                    container.addView(itemView)
                }

            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(container.context, "Erro ao carregar dados", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun openCharSheet(talesSheet: TalesGeneralInfo){
        val bundle = Bundle()
        bundle.putSerializable("charData", talesSheet)

        val navController = findNavController(requireActivity(), R.id.nav_host_fragment_activity_main)
        navController.navigate(R.id.navigation_char_sheet, bundle)
    }
}