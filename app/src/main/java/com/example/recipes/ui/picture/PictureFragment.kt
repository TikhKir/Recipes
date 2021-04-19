package com.example.recipes.ui.picture

import android.Manifest
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.recipes.MainActivity
import com.example.recipes.R
import com.example.recipes.databinding.PictureFragmentBinding
import com.example.recipes.utils.ImageSaver
import com.example.recipes.utils.REQUEST_CODE_WRITE_PERMISSION
import com.example.recipes.utils.StorageUtility
import dagger.hilt.android.AndroidEntryPoint
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import javax.inject.Inject

@AndroidEntryPoint
class PictureFragment : Fragment(), EasyPermissions.PermissionCallbacks {

    companion object {
        private const val EXTRA_IMAGE_URL = "EXTRA_IMAGE_URL"
        private const val VISIBILITY_KEY = "VISIBILITY_KEY"
        fun newInstance(imageUrl: String): PictureFragment {
            val args = Bundle().apply { putString(EXTRA_IMAGE_URL, imageUrl) }
            return PictureFragment().apply { arguments = args }
        }
    }

    @Inject
    lateinit var imageSaver: ImageSaver
    private var bitmapToSave: Bitmap? = null

    private var _binding: PictureFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var imageUrl: String
    private var visibility = false


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = PictureFragmentBinding
            .inflate(LayoutInflater.from(context), container, false)
        return binding.root
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        visibility = savedInstanceState?.getBoolean(VISIBILITY_KEY) ?: false
        (activity as MainActivity).fullScreenModeOn()
        initArgs()
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setVisible(visibility)
        setupImage()
        setupSaveButton()
        setupViewVisibilityChanger()
    }

    private fun setVisible(isVisible: Boolean) {
        binding.btSave.isVisible = isVisible
    }

    private fun setupViewVisibilityChanger() = with(binding) {
        flHidden.setOnClickListener {
            if (visibility) {
                btSave.isVisible = false
                visibility = false
            } else {
                btSave.isVisible = true
                visibility = true
            }
        }
    }

    private fun initArgs() {
        imageUrl = arguments?.getString(EXTRA_IMAGE_URL)!!
    }

    private fun setupSaveButton() {
        binding.btSave.setOnClickListener {
            if (bitmapToSave != null)
                doIfStorageAvailableOrRequest { imageSaver.save(bitmapToSave!!) }
            else
                Toast.makeText(context, getString(R.string.image_is_not_loaded), Toast.LENGTH_SHORT).show()
        }
    }


    private fun setupImage() {
        Glide.with(this)
            .asBitmap()
            .load(imageUrl)
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    binding.ivBigPicture.setImageBitmap(resource)
                    bitmapToSave = resource
                }

                override fun onLoadCleared(placeholder: Drawable?) {}
            })
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            AppSettingsDialog.Builder(this).build().show()
        } else requestPermissions()
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {}

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    private fun doIfStorageAvailableOrRequest(execute: () -> Unit) {
        if (StorageUtility.hasWriteStoragePermissions(requireContext())) execute()
        else requestPermissions()
    }

    private fun requestPermissions() {
        if (StorageUtility.hasWriteStoragePermissions(requireContext())) return
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            EasyPermissions.requestPermissions(
                this,
                getString(R.string.request_storage_dialog),
                REQUEST_CODE_WRITE_PERMISSION,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(VISIBILITY_KEY, visibility)
    }

    override fun onDetach() {
        super.onDetach()
        (activity as MainActivity).fullScreenModeOff()
    }


}