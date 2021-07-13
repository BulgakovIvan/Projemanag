package com.example.projemanag.activities

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.projemanag.R
import com.example.projemanag.databinding.ActivityCreateBoardBinding
import com.example.projemanag.firebase.FirestoreClass
import com.example.projemanag.models.Board
import com.example.projemanag.utils.Constants
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.IOException

class CreateBoardActivity : BaseActivity() {
    private lateinit var binding: ActivityCreateBoardBinding

    private var mSelectedImageFileUri: Uri? = null
    private lateinit var mUserName: String
    private var mBoardImageURL: String = ""

    private val changeImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        mSelectedImageFileUri = uri

        try {
            Glide
                .with(this)
                .load(mSelectedImageFileUri)
                .fitCenter() //.centerCrop()
                .placeholder(R.drawable.ic_board_place_holder)
                .into(binding.ivBoardImage)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateBoardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbarCreateBoardActivity.title = resources.getString(R.string.create_board_title)
        setupActionBar(binding.toolbarCreateBoardActivity)

        if (intent.hasExtra(Constants.NAME)) {
            mUserName = intent.getStringExtra(Constants.NAME).toString()
        }

        binding.ivBoardImage.setOnClickListener {

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {

                changeImage.launch("image/*")

            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    Constants.READ_STORAGE_PERMISSION_CODE
                )
            }

        }

        binding.btnCreate.setOnClickListener {
            if (mSelectedImageFileUri != null) {
                uploadBoardImvage()
            } else {
                showProgressDialog(resources.getString(R.string.please_wait))
                createBoard()
            }
        }

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>,
        grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == Constants.READ_STORAGE_PERMISSION_CODE) {
            if (grantResults.isNotEmpty()
                && grantResults[0] == PackageManager.PERMISSION_GRANTED
            ) {
                changeImage.launch("image/*")
            }
        } else {
            Toast.makeText(
                this,
                "You just denied the permission for storage. You con also allow it from settings.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun createBoard() {
        val assignedUsersArrayList: ArrayList<String> = ArrayList()
        assignedUsersArrayList.add(getCurrentUserID())

        var board = Board(
            binding.etBoardName.text.toString(),
            mBoardImageURL,
            mUserName,
            assignedUsersArrayList
        )

        FirestoreClass().createBoard(this, board)
    }

    private fun uploadBoardImvage() {
        showProgressDialog(resources.getString(R.string.please_wait))

        val sRef: StorageReference = FirebaseStorage.getInstance().reference.child(
            "BOARD_IMAGE${System.currentTimeMillis()}" +
                    ".${Constants.getFileExtension(this, mSelectedImageFileUri)}"
        )

        sRef.putFile(mSelectedImageFileUri!!)
            .addOnSuccessListener { taskSnapshot ->
                Log.e(Constants.TAG, "Board image url: ${taskSnapshot.metadata!!.reference!!.downloadUrl}")

                taskSnapshot.metadata!!.reference!!.downloadUrl.addOnSuccessListener { uri ->
                    Log.e(Constants.TAG, "$uri")
                    mBoardImageURL = uri.toString()

                    createBoard()
                }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, exception.message, Toast.LENGTH_SHORT).show()
                hideProgressDialog()
            }

    }

    fun boardCreatedSuccessfully() {
        hideProgressDialog()
        finish()
    }
}