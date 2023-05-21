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
import androidx.core.app.ActivityCompat.finishAffinity
import com.google.android.gms.location.*

class MapsFragment : Fragment() {
    val coordenadas = mutableMapOf<String, LatLng>()

    private lateinit var map : GoogleMap
    private var LOCATION_PERMISSION_REQUEST = 1

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    public lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val coordenada1 = LatLng(-33.9, 151.0)
        val coordenada2 = LatLng(-33.8, 151.0)
        coordenadas["Coordenada 1"] = coordenada1
        coordenadas["Coordenada 2"] = coordenada2

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
        val sydney = LatLng(-34.0, 151.0)
        googleMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))

        val sydney2 = LatLng(-34.1, 151.0)
        googleMap.addMarker(MarkerOptions().position(sydney2).title("Marker in Sydney"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney2))


        googleMap.addMarker(MarkerOptions().position(coordenadas["Coordenada 1"] as LatLng).title("Marker in Sydney"))
        googleMap.addMarker(MarkerOptions().position(coordenadas["Coordenada 2"] as LatLng).title("Marker in Sydney"))

        getLocationAccess()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
        ActivityCompat.requestPermissions(requireActivity(), arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST)

    }

    private fun getLocationAccess() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            map.isMyLocationEnabled = true
            checkLocationEnabled()
            getLocationUpdates()
            startLocationUpdates()
        } else {
            ActivityCompat.requestPermissions(
                requireActivity(),
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
        locationRequest= LocationRequest.Builder(3000)
            .apply {
                setPriority(Priority.PRIORITY_HIGH_ACCURACY)
                setMinUpdateIntervalMillis(2000)
            }.build()

        locationCallback = object : LocationCallback(){
            override fun onLocationResult(locationResult: LocationResult) {
                if(locationResult.locations.isNotEmpty()){
                    val location = locationResult.lastLocation
                    if(location != null){
                        val latLng = LatLng(location.latitude,location.longitude)
                        val markerOptions = MarkerOptions().position(latLng)
                        map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,16f))
                    }
                }
            }
        }
    }

    private fun startLocationUpdates(){
        fusedLocationClient.requestLocationUpdates(locationRequest,locationCallback,null)
    }


}