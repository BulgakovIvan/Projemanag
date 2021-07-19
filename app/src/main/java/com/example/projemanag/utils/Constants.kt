package com.example.projemanag.utils

import android.app.Activity
import android.net.Uri
import android.webkit.MimeTypeMap

object Constants {

    const val TAG = "ups"
    const val USERS: String = "users"
    const val BOARDS: String = "boards"
    const val IMAGE: String = "image"
    const val NAME: String = "name"
    const val MOBILE: String = "mobile"
    const val ASSIGNED_TO: String = "assignedTo"
    const val DOCUMENT_ID: String = "documentId"
    const val TASK_LIST: String = "taskList"
    const val READ_STORAGE_PERMISSION_CODE = 1
    const val BOARD_DETAIL: String = "board_detail"
    const val ID: String = "id"

    fun getFileExtension(activity: Activity, uri: Uri?): String? {
        return MimeTypeMap.getSingleton().getExtensionFromMimeType(activity.contentResolver.getType(uri!!))
    }

}