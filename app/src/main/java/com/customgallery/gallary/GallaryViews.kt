package com.customgallery.gallary
import com.customgallery.asynctask.AsyncTaskForGallary
import com.customgallery.pojos.SelectedImagesBean

interface GallaryViews {
    fun initializeViews()
    fun closeWindow()
    fun itemFetched(list: ArrayList<AsyncTaskForGallary.FolderModel>)
    fun sendPhotos(mSelectedImagesList: ArrayList<SelectedImagesBean>?)
    fun openDrawerLayout()
}
