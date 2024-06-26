package com.example.vialidolid

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import androidx.fragment.app.Fragment
import android.location.LocationRequest.Builder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

import androidx.appcompat.app.AlertDialog
import android.provider.Settings
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.core.app.ActivityCompat.finishAffinity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.Marker
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.json.JSONArray
import org.json.JSONException

class MapsFragment : Fragment() {
    val coordenadas = mutableMapOf<String, LatLng>()
    //Trackear ubicacion del usuario
    var trackUser = true
    var zoom = 15f
    private lateinit var imageButtonUbicacion: ImageButton

    private lateinit var map : GoogleMap
    private var LOCATION_PERMISSION_REQUEST = 1

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    public lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback
    private lateinit var dataMap: HashMap<String, Marcador>

    //Para el filtro
    private lateinit var cbAlumbrado: CheckBox
    private lateinit var cbBache: CheckBox
    private lateinit var cbMaltratoAnimal: CheckBox
    private lateinit var cbOoapas: CheckBox
    private lateinit var cbVial: CheckBox

    private var alumbradoChecked = true
    private var bacheChecked = true
    private var maltratoAnimalChecked = true
    private var ooapasChecked = true
    private var vialChecked = true

    //Boton para esconder y mostrar lista de filtro
    private lateinit var linearLayout: LinearLayout
    private lateinit var imageButton: ImageButton
    private var isLinearLayoutVisible = false

    //Boton para nuevo reporte en mapa
    private lateinit var fabButton: FloatingActionButton



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

    }

    private val callback = OnMapReadyCallback { googleMap ->
        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */

        map = googleMap
        var inicial = CameraUpdateFactory.newLatLngZoom( LatLng(19.702896, -101.190384), zoom)
        map.moveCamera(inicial)
        map.animateCamera(inicial)

        googleMap.setOnMapLoadedCallback {
            // El mapa se ha cargado completamente, ahora puedes rastrear la ubicación del usuario
            // y realizar otras operaciones necesarias
        }
        /*val sydney = LatLng(-34.0, 151.0)
        googleMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))

        val sydney2 = LatLng(-34.1, 151.0)
        googleMap.addMarker(MarkerOptions().position(sydney2).title("Marker in Sydney"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney2))

        */

        getLocationAccess()

        //Obtener las coordenadas para los marcadores de los reportes y añadirlos
        obtenerMarcadores()





        //Cambiar el comportamiento por defecto de los marcadores de google maps
        map.setOnMarkerClickListener(object : GoogleMap.OnMarkerClickListener {
            override fun onMarkerClick(marker: Marker): Boolean {
                var title = marker.title
                var tipo = dataMap[title]?.tipoReporte
                //Crear Bundle con el id y el tipo para posteriormente obtener los datos dentro del fragmento
                var bundle = Bundle()
                bundle.putInt("idReporte", title?.toIntOrNull() ?: 0)
                bundle.putInt("tipoReporte",tipo?: 0)

                //Crear fragmento y adjuntar bundleS
                var reportFragment = reportFragment()
                reportFragment.arguments = bundle

                //Mostrar fragmento
                reportFragment.show(parentFragmentManager, "TAG")

                //True remueve el comportamiento por defecto de los marcadores
                return true
            }
        })

        //En caso de que el usario se ponga a recorrer el mapa dejar de centrar su ubicacion
        map.setOnCameraMoveStartedListener { reasonCode ->
            if (reasonCode == GoogleMap.OnCameraMoveStartedListener.REASON_GESTURE) {
                // I will no longer keep updating the camera location because
                // the user interacted with it. This is my field I check before
                // snapping the camera location to the latest value.
                trackUser = false
            }
        }

        //
        map.setOnMyLocationButtonClickListener {
            // Aquí puedes definir la acción que deseas realizar al presionar el botón de ubicación

            trackUser = true
            zoom = map.cameraPosition.zoom

            //false mantiene el comportamiento por defecto de los marcadores
            false
        }




    }



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_maps, container, false)

        //Inicializar checkbox del filtro
        cbAlumbrado = view.findViewById(R.id.cbAlumbrado)
        cbBache = view.findViewById(R.id.cbBache)
        cbMaltratoAnimal = view.findViewById(R.id.cbMaltratoAnimal)
        cbOoapas = view.findViewById(R.id.cbOoapas)
        cbVial= view.findViewById(R.id.cbVial)

        //Colocar estado inicial en activado
        cbAlumbrado.isChecked = true
        cbBache.isChecked = true
        cbMaltratoAnimal.isChecked = true
        cbOoapas.isChecked = true
        cbVial.isChecked = true

        //Crear listeners
        cbAlumbrado.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) alumbradoChecked=true else alumbradoChecked=false
            actualizarMarcadores()
        }
        cbBache.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) bacheChecked=true else bacheChecked=false
            actualizarMarcadores()
        }
        cbMaltratoAnimal.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) maltratoAnimalChecked=true else maltratoAnimalChecked=false
            actualizarMarcadores()
        }
        cbOoapas.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) ooapasChecked=true else ooapasChecked=false
            actualizarMarcadores()
        }
        cbVial.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) vialChecked=true else vialChecked=false
            actualizarMarcadores()
        }

        //listener de boton esconder/mostrar lista filtro
        linearLayout = view.findViewById(R.id.linearLayout)
        imageButton = view.findViewById(R.id.imageButton)

        linearLayout.visibility = View.GONE
        imageButton.setOnClickListener {
            toggleLinearLayoutVisibility()
        }

        //Listener boton trackear ubicación
        imageButtonUbicacion = view.findViewById(R.id.imageButtonUbicacion)
        imageButtonUbicacion.setOnClickListener {
            trackUser = true
            zoom = map.cameraPosition.zoom
        }

        fabButton = view.findViewById(R.id.fabButton)
        fabButton.setOnClickListener {
            //Toast.makeText(requireContext(), "Hola", Toast.LENGTH_SHORT).show()
            val Intent = Intent(requireActivity(),nuevo_reporte::class.java)
            startActivity(Intent)
        }


        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)

    }

    private fun getLocationAccess() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            //map.isMyLocationEnabled = true
            checkLocationEnabled()
            getLocationUpdates()
            startLocationUpdates()
        } else {
            requestPermissions(
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST
            )
            //map.isMyLocationEnabled = true
        }
    }

    private fun checkLocationEnabled() {
        val locationManager = requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val isLocationEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        if (!isLocationEnabled) {
            showEnableLocationDialog()
        }
    }

    private fun showEnableLocationDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("GPS Desactivado")
            .setMessage("Para utilizar la función de ubicación, necesitas activar el GPS.")
            .setPositiveButton("Ir a Configuración") { _, _ ->
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
            .setNegativeButton("Cancelar") { dialog, _ ->
                dialog.dismiss()
            }
        val dialog = builder.create()
        dialog.show()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>,  grantResults:
    IntArray) {
        if(requestCode == LOCATION_PERMISSION_REQUEST){
            if(grantResults.contains(PackageManager.PERMISSION_GRANTED)){
                checkLocationEnabled()
                getLocationAccess()
                println("Entre aqui")
                //TODO wachear que no haga bucle
            }
            else{
                Toast.makeText(requireContext(),"No se proporcionó permisos", Toast.LENGTH_LONG).show()
                requireActivity().finishAffinity()
            }

        }
    }

    private fun getLocationUpdates(){
        //Parameteres for location Request
        locationRequest= LocationRequest.Builder(1000)
            .apply {
                setPriority(Priority.PRIORITY_HIGH_ACCURACY)
                setMinUpdateIntervalMillis(1000)
            }.build()

        locationCallback = object : LocationCallback(){
            override fun onLocationResult(locationResult: LocationResult) {
                if(locationResult.locations.isNotEmpty()){
                    val location = locationResult.lastLocation
                    if(location != null){
                        val latLng = LatLng(location.latitude,location.longitude)
                        val markerOptions = MarkerOptions().position(latLng)
                        if(trackUser)
                            map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,zoom))
                    }
                }
            }
        }
    }

    private fun startLocationUpdates(){
        fusedLocationClient.requestLocationUpdates(locationRequest,locationCallback,null)
    }

    fun obtenerMarcadores() {
        val url = "http://${resources.getString(R.string.server_ip)}/rest/obtenerMarcadores.php" // URL de la API que proporciona los datos

        val request = JsonArrayRequest(Request.Method.GET, url, null,
            Response.Listener<JSONArray> { response ->
                dataMap = HashMap<String, Marcador>()

                try {
                    // Recorre el arreglo de respuesta JSON y guarda los datos en el mapa
                    for (i in 0 until response.length()) {
                        val jsonObject = response.getJSONObject(i)
                        val idReporte = jsonObject.getString("id_reporte")
                        val latitud = if (jsonObject.isNull("latitud")) 0.0 else jsonObject.getDouble("latitud")
                        val longitud = if (jsonObject.isNull("longitud")) 0.0 else jsonObject.getDouble("longitud")
                        val tipoReporte = jsonObject.optInt("tipo_reporte", 0)

                        val reporte = Marcador(idReporte, latitud, longitud, tipoReporte)
                        dataMap[idReporte] = reporte
                    }

                    // El mapa "dataMap" ahora contiene los datos obtenidos de la API
                    // Puedes utilizar el mapa según tus necesidades

                    // Ejemplo de uso: Recorrer el mapa con un ciclo
                    for ((idReporte, reporte) in dataMap) {
                        println("ID del Reporte: $idReporte")
                        println("Latitud: ${reporte.latitud}")
                        println("Longitud: ${reporte.longitud}")
                        println("Tipo de Reporte: ${reporte.tipoReporte}")
                        println()
                    }
                    crearMarcadores()

                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            Response.ErrorListener { error ->
                // Manejo de errores en la solicitud
                error.printStackTrace()
            })

        // Agrega la solicitud a la cola de solicitudes de Volley
        Volley.newRequestQueue(context).add(request)
    }

    fun crearMarcadores(){
        for ((idReporte, reporte) in dataMap) {

            map.addMarker(MarkerOptions().position(LatLng(reporte.latitud,reporte.longitud)).title(reporte.idReporte).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)))

            // println("ID del Reporte: $idReporte")
            // println("Latitud: ${reporte.latitud}")
            // println("Longitud: ${reporte.longitud}")
            // println("Tipo de Reporte: ${reporte.tipoReporte}")
            // println()
        }

    }

    private fun actualizarMarcadores() {
        // Limpiar los marcadores existentes en el mapa
        map.clear()

        // Crear marcadores solo para los tipos de reporte seleccionados
        if(alumbradoChecked){
            for ((idReporte, reporte) in dataMap) {
                if (reporte.tipoReporte==1) {
                    map.addMarker(MarkerOptions().position(LatLng(reporte.latitud, reporte.longitud)).title(reporte.idReporte).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)))
                }
            }
        }

        if(bacheChecked){
            for ((idReporte, reporte) in dataMap) {
                if (reporte.tipoReporte==2) {
                    map.addMarker(MarkerOptions().position(LatLng(reporte.latitud, reporte.longitud)).title(reporte.idReporte).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)))
                }
            }
        }

        if(maltratoAnimalChecked){
            for ((idReporte, reporte) in dataMap) {
                if (reporte.tipoReporte==3) {
                    map.addMarker(MarkerOptions().position(LatLng(reporte.latitud, reporte.longitud)).title(reporte.idReporte).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)))
                }
            }
        }

        if(ooapasChecked){
            for ((idReporte, reporte) in dataMap) {
                if (reporte.tipoReporte==4) {
                    map.addMarker(MarkerOptions().position(LatLng(reporte.latitud, reporte.longitud)).title(reporte.idReporte).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)))
                }
            }
        }
        if(vialChecked){
            for ((idReporte, reporte) in dataMap) {
                if (reporte.tipoReporte==5) {
                    map.addMarker(MarkerOptions().position(LatLng(reporte.latitud, reporte.longitud)).title(reporte.idReporte).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)))
                }
            }
        }

    }
    private fun toggleLinearLayoutVisibility() {
        if (isLinearLayoutVisible) {
            linearLayout.visibility = View.GONE
            isLinearLayoutVisible = false
        } else {
            linearLayout.visibility = View.VISIBLE
            isLinearLayoutVisible = true
        }
    }

}