package com.customgallery

import android.app.Activity
import com.customgallery.pojos.SelectedImagesBean

class PhotosListHolder {

    companion object {
        var mSelectedImageList: ArrayList<SelectedImagesBean>? = ArrayList()
        fun setSelectedList(list: ArrayList<SelectedImagesBean>) {
            mSelectedImageList = list
        }
        fun getSelectedList():ArrayList<SelectedImagesBean> ? {
            return mSelectedImageList;
        }

        fun clearImages(activity: Activity) {
            mSelectedImageList?.clear()
        }
    }
}