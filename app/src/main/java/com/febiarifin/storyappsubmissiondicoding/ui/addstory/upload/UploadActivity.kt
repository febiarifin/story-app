package com.febiarifin.storyappsubmissiondicoding.ui.addstory.upload

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.ActionBar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.febiarifin.storyappsubmissiondicoding.databinding.ActivityUploadBinding
import com.febiarifin.storyappsubmissiondicoding.ui.addstory.camera.CameraActivity
import com.febiarifin.storyappsubmissiondicoding.ui.main.MainActivity
import com.febiarifin.storyappsubmissiondicoding.utils.rotateFile
import com.febiarifin.storyappsubmissiondicoding.utils.uriToFile
import java.io.File

class UploadActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUploadBinding
    private var getFile: File? = null
    private lateinit var viewModel: UploadViewModel

    companion object {
        const val CAMERA_X_RESULT = 200
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                Toast.makeText(
                    this,
                    "Tidak mendapatkan permission.",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(UploadViewModel::class.java)

        val actionBar: ActionBar? = supportActionBar
        actionBar?.hide()

        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }

        binding.btnClose.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }


        binding.btnAddCamera.setOnClickListener {
            startCameraX()
        }

        binding.btnAddGallery.setOnClickListener {
            startGallery()
        }

        binding.btnSendStory.setOnClickListener {
            uploadImage()
        }

        viewModel.isLoading.observe(this,{
            showLoading(it)
        })

        viewModel.message.observe(this, {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        })

        viewModel.isError.observe(this,{
            if (it){
                binding.edAddDescription.setError("Please input description your story!")
            }else if(!it){
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        })

        viewModel.imageFromCamera.observe(this,{
            it?.let { file ->
                getFile = file
                binding.ivStoryPreview.setImageBitmap(BitmapFactory.decodeFile(file.path))
            }
        })

        viewModel.imageFromGallery.observe(this,{
            if(it != null){
                binding.ivStoryPreview.setImageURI(it)
            }
        })
    }

    private fun uploadImage() {
        val description = binding.edAddDescription.text.toString()
        viewModel.uploadImage(this, getFile, description)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private fun startCameraX() {
        val intent = Intent(this, CameraActivity::class.java)
        launcherIntentCameraX.launch(intent)
    }

    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == CAMERA_X_RESULT) {
            val myFile = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                it.data?.getSerializableExtra("picture", File::class.java)
            } else {
                @Suppress("DEPRECATION")
                it.data?.getSerializableExtra("picture")
            } as? File
            val isBackCamera = it.data?.getBooleanExtra("isBackCamera", true) as Boolean
            myFile?.let { file ->
                rotateFile(file, isBackCamera)
                getFile = file
                binding.ivStoryPreview.setImageBitmap(BitmapFactory.decodeFile(file.path))
                viewModel.setImageFormCamera(getFile as File)
            }
        }
    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg = result.data?.data as Uri
            selectedImg.let { uri ->
                val myFile = uriToFile(uri, this@UploadActivity)
                getFile = myFile
                binding.ivStoryPreview.setImageURI(uri)
                viewModel.setImageFormGallery(uri)
            }
        }
    }

    private fun showLoading(isLoading: Boolean) = if (isLoading) binding.progressbar.visibility =
        View.VISIBLE else binding.progressbar.visibility = View.GONE

}