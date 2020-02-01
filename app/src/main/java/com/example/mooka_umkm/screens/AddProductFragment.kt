package com.example.mooka_umkm.screens


import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.mooka_umkm.R
import kotlinx.android.synthetic.main.fragment_add_product.view.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

/**
 * A simple [Fragment] subclass.
 */
class AddProductFragment : Fragment() {

    lateinit var photoUri:String
    lateinit var viewRoot:View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        viewRoot = inflater.inflate(R.layout.fragment_add_product, container, false)

        viewRoot.btn_upload_foto.setOnClickListener {
            setImage()
        }

        return viewRoot
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (isPickImage(requestCode, resultCode, data)) {
            val imageUri = data!!.data
            photoUri = getRealPath(imageUri!!)!!
            updateUploadFotoText(imageUri)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (isRequestGranted(requestCode, permissions , grantResults))
            openGallery()
    }

    private fun isNeedRequestPermission() :Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
    }

    private fun isPickImage (requestCode: Int, resultCode: Int, data: Intent?) : Boolean {
        return requestCode == 4105 && resultCode == RESULT_OK &&
                data!!.data != null
    }

    private fun  checkPermission () {
        if (isStoragePermissionsGranted()) {
            openGallery()
        } else {
            requestStoragePermissions()
        }
    }

    private fun getImageType (imageUri: Uri) :String {
        val mimeType = MimeTypeMap.getSingleton()
        return mimeType.getExtensionFromMimeType(activity!!.contentResolver.getType(imageUri))!!
    }

    private fun getPhotoMultipart() : MultipartBody.Part? {
        return if(photoUri != null) {
            val file = File(photoUri)
            val requestBody = RequestBody.create(MediaType.parse("image/*"), file)
            MultipartBody.Part.createFormData("gambar", file.name, requestBody)
        } else null
    }

    private fun getRealPath (imageUri: Uri) : String? {
        val cursor = activity!!.contentResolver
            .query(imageUri, null, null, null, null)


        return if (cursor != null) {
            cursor.moveToFirst()

            val imageIndex = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            val realPath = cursor.getString(imageIndex)

            cursor.close()

            realPath
        } else null
    }

    private fun isRequestGranted (requestCode: Int, permissions: Array<out String>, grantResults: IntArray) : Boolean {
        return requestCode == 4106 &&
                permissions.isNotEmpty() &&
                permissions[0] == Manifest.permission.READ_EXTERNAL_STORAGE &&
                grantResults.isNotEmpty() &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED
    }

    private fun isStoragePermissionsGranted () : Boolean {
        return ContextCompat.checkSelfPermission(context!!,
            Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
    }

    private fun openGallery () {
        val photoPickerIntent = Intent(Intent.ACTION_PICK)
        photoPickerIntent.type = "image/*"

        startActivityForResult(photoPickerIntent, 4105)
    }

    private fun requestStoragePermissions () {
        val storagePermissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)

        ActivityCompat.requestPermissions(activity!!, storagePermissions,
            4106)
    }

    private fun setImage () {
        if (isNeedRequestPermission()) {
            checkPermission()
        } else {
            openGallery()
        }
    }

    private fun updateUploadFotoText (imageUri: Uri) {
        val type = getImageType(imageUri)
        val imagePath = imageUri.path

        viewRoot.btn_upload_foto.text = "$imagePath.$type"
    }

}
