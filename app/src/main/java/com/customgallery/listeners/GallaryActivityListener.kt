package com.customgallery.listeners
import com.customgallery.pojos.SelectedImagesBean

interface GallaryActivityListener {
    fun getCounter(isSelected: Boolean, mImageUrl: String, fragmentPosition: Int, position1: Int, mDeletedPosition: String): Int
    fun getSelectedList(): ArrayList<SelectedImagesBean>?
}
