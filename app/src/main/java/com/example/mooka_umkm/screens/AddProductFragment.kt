package com.example.mooka_umkm.screens


import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.example.mooka_customer.extension.finishLoading
import com.example.mooka_customer.extension.showmessage
import com.example.mooka_customer.extension.toLoading
import com.example.mooka_umkm.R
import com.example.mooka_umkm.network.Repository
import com.example.mooka_umkm.network.lib.Resource
import kotlinx.android.synthetic.main.fragment_add_product.view.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

/**
 * A simple [Fragment] subclass.
 */
class AddProductFragment : Fragment() {

    private lateinit var photoUri:String
    private lateinit var viewRoot:View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewRoot = inflater.inflate(R.layout.fragment_add_product, container, false)

        val addProductFragmentArgs by navArgs<AddProductFragmentArgs>()

        viewRoot.btn_upload_foto.setOnClickListener {
            setImage()
        }

        viewRoot.btn_tambahkan_produk.setOnClickListener {
            postNewProduct(addProductFragmentArgs)
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
            val requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file)
            MultipartBody.Part.createFormData("file", file.name, requestBody)
        } else null
    }

    private fun getRealPath (imageUri: Uri) : String? {
        val cursor = activity!!.contentResolver
            .query(imageUri, null, null, null, null)

        return if (cursor != null) {
            cursor.moveToFirst()

            val imageIndex = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
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

    private fun postNewProduct(addProductFragmentArgs: AddProductFragmentArgs) {
        val image = getPhotoMultipart()!!
        val requestUmkmId =
            MultipartBody.Part.createFormData("umkm_id", addProductFragmentArgs.umkmid.toString())
        val requestUmkmName =
            MultipartBody.Part.createFormData("title", viewRoot.et_nama_produk.text.toString())
        val requestHarga =
            MultipartBody.Part.createFormData("harga", viewRoot.et_harga_produk.text.toString())
        val requestStock =
            MultipartBody.Part.createFormData("stock", viewRoot.et_jumlah_stok.text.toString())
        val requestDescription = MultipartBody.Part.createFormData(
            "description",
            viewRoot.et_deskripsi_produk.text.toString()
        )

        Repository.postNewProduct(
            requestUmkmId,
            requestUmkmName,
            requestHarga,
            requestStock,
            requestDescription,
            image
        ).observe(this, Observer {
            when (it?.status) {
                Resource.LOADING -> {
                    Log.d("Loading", it.status.toString())
                    viewRoot.btn_tambahkan_produk.toLoading()
                }
                Resource.SUCCESS -> {
                    viewRoot.btn_tambahkan_produk.finishLoading()
                    Log.d("Success", it.data.toString())
                    activity!!.supportFragmentManager.popBackStack()
                }
                Resource.ERROR -> {
                    Log.d("Error", it.message!!)
                    context?.showmessage("Something is wrong")
                }
            }
        })
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
