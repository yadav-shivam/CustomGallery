package com.customgallery.gallary

import com.customgallery.asynctask.AsyncTaskForGallary
import com.customgallery.pojos.SelectedImagesBean


internal class GallaryPresenter(var view: GallaryViews) : GallaryModelListener {

    private var gallaryModel: GallaryModel? = null

    fun setModel() {
        gallaryModel = GallaryModel(this)
    }

    fun destroy() {
//        gallaryModel?.detachListener()
        gallaryModel = null
    }

    fun initView() {
        view.initializeViews()
    }

    /*
    * method is used to close the activtity
    * */
    fun closeWindow() {
        view.closeWindow()
    }

    /*
    * item fetched from the async task
    * */
    fun itemFetched(list: ArrayList<AsyncTaskForGallary.FolderModel>) {
        view.itemFetched(list)
    }

    /*
    * method is used to send the photos for the editing
    * */
    fun sendPhotosForEditing(mSelectedImagesList: ArrayList<SelectedImagesBean>?) {
        view.sendPhotos(mSelectedImagesList)
    }


}