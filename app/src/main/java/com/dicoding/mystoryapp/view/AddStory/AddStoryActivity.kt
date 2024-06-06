package com.dicoding.mystoryapp.view.AddStory

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.dicoding.mystoryapp.R
import com.dicoding.mystoryapp.databinding.ActivityAddStoryBinding
import com.dicoding.mystoryapp.getImageUri
import com.dicoding.mystoryapp.reduceFileImage
import com.dicoding.mystoryapp.uriToFile
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody

class AddStoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddStoryBinding
    private var imageUri: Uri? = null
    private lateinit var fusedLocation: FusedLocationProviderClient
    private var  lat: Double? = null
    private var lon: Double? = null
    private val viewModel by viewModels<AddStoryViewModel>()

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {isGranted: Boolean ->
        if (isGranted) {
            Toast.makeText(this, getString(R.string.request_granted), Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(this, getString(R.string.request_denied), Toast.LENGTH_LONG).show()
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        fusedLocation = LocationServices.getFusedLocationProviderClient(this)
        val token = intent.getStringExtra("TOKENS").toString()

        viewModel.errorNotif.observe(this) {
            if (it?.error == true) {
                Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
            } else {
                showToast(getString(R.string.upload_success))
                @Suppress("DEPRECATION")
                onBackPressed()
            }
        }

        if (!allPermissionsGranted()) {
            requestPermissionLauncher.launch(REQUIRED_PERMISSION)
        }

        viewModel.isLoading.observe(this) {
            showLoading(it)
        }

        binding.apply {
            addLocation.setOnClickListener {
                if (!addLocation.isChecked) {
                    lat = null
                    lon = null
                    addLocation.isChecked = false
                } else {
                    addLocation.isChecked = true
                    requestLocatedPermission()
                }

            }
            buttonGaleri.setOnClickListener { startGallery() }
            buttonCamera.setOnClickListener { startCamera() }
            ActionPost.setOnMenuItemClickListener {menu ->
                when(menu.itemId) {
                    R.id.upload -> {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                            uploadPhoto(token)
                        }
                        true
                    }
                    else -> {
                        false
                    }
                }
            }
            ActionPost.setNavigationOnClickListener {
                @Suppress("DEPRECATION")
                onBackPressed()
            }

        }
    }

    private fun showLoading(isloading: Boolean) {
        binding.progressBar.visibility = if (isloading) View.VISIBLE else View.GONE
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun uploadPhoto(token: String) {
        val desc = binding.edAddDescription.text.toString()
        if (imageUri != null && desc.isNotEmpty() ) {
            val imageFile = uriToFile(imageUri, this).reduceFileImage()
            val requestBody = desc.toRequestBody("text/plain".toMediaType())
            val reqImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
            val multipartBody = MultipartBody.Part.createFormData(
                "photo",
                imageFile.name,
                reqImageFile
            )
            Log.e("AddStoryActivity", "$lat + $lon")
            viewModel.addStory(token,  requestBody, multipartBody, lat, lon)
        } else {
            showToast(getString(R.string.empty_error))
        }
    }


    private fun getMyLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED) {
            binding.addLocation.isActivated = false
            return
        }

        fusedLocation.lastLocation.addOnSuccessListener { mylocation ->
            lat = mylocation.latitude
            lon = mylocation.longitude
        }

    }

    private fun allPermissionsGranted() =
        ContextCompat.checkSelfPermission(this, REQUIRED_PERMISSION) == PackageManager.PERMISSION_GRANTED

    private fun requestLocatedPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_CODE
            )
            showToast("Location permission is needed for accessing location.")
            binding.addLocation.isChecked = false
        } else {
            getMyLocation()
        }
    }

    private fun startCamera() {
        imageUri = getImageUri(this)
        launcherCamera.launch(imageUri!!)
    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            showImage()
        }
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            imageUri = uri
            showImage()
        } else {
            Log.d("Photo Picker", "No media selected")
        }
    }

    private fun showImage() {
        imageUri?.let {
            Log.d("Image URI", "showImage: $it")
            binding.PreviewImage.setImageURI(it)
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        private const val REQUIRED_PERMISSION = Manifest.permission.CAMERA
        private const val REQUEST_CODE = 101
    }
}