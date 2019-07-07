package com.customgallery.listeners

import com.customgallery.asynctask.AsyncTaskForGallary

interface AsyncListener {
    fun allImagesFetched(folderNameWithListFolder: ArrayList<AsyncTaskForGallary.FolderModel>)
    fun galleryImagesInProgress(mAlImages: ArrayList<AsyncTaskForGallary.FolderModel>)
}
