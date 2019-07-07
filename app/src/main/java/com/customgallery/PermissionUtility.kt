package com.customgallery

import android.app.Activity
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import java.util.*

/**
 */
object PermissionUtility {

    fun isPermissionGranted(context: Activity, permission: Array<String>, permissionReqCode: Int): Boolean {

        val permissionsNeeded = ArrayList<String>()
        for (i in permission.indices) {
            val perm = permission[i]
            if (permission[i].startsWith("android.permission")) {
                if (ContextCompat.checkSelfPermission(context,
                                permission[i]) != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(context,
                                    permission[i])) {

                        permissionsNeeded.add(perm)

                    } else {
                        permissionsNeeded.add(perm)

                    }
                }
            }
        }
        if (permissionsNeeded.size > 0) {
            ActivityCompat.requestPermissions(context, permissionsNeeded.toTypedArray(),
                    permissionReqCode)

            return false
        } else {
            return true
        }
    }
}