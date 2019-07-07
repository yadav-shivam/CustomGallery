package com.customgallery

import android.os.Bundle
import android.support.v4.app.FragmentActivity
import com.customgallery.gallary.GalleryFragment
import com.customgallery.pojos.SelectedImagesBean

object Gallery{

    interface fetchImagesListener{
        fun imagesFetched(selectedImagesList: ArrayList<SelectedImagesBean>)
    }

    fun openGallery(context: FragmentActivity, imagesFetchedListener: fetchImagesListener){
        val galleryFragment = GalleryFragment()
        galleryFragment.setImagesFetchListener(imagesFetchedListener)
        val bundle = Bundle()
        galleryFragment.arguments = bundle
        galleryFragment.show(context.supportFragmentManager,"galleryDialog")
        PhotosListHolder.clearImages(context)
    }

 fun openGallery(context: FragmentActivity, selectedImagesList:ArrayList<SelectedImagesBean>,imagesFetchedListener: fetchImagesListener){
        val galleryFragment = GalleryFragment()
        galleryFragment.setImagesFetchListener(imagesFetchedListener)
        val bundle = Bundle()
        galleryFragment.arguments = bundle
        galleryFragment.show(context.supportFragmentManager,"galleryDialog")
        PhotosListHolder.setSelectedList(selectedImagesList)
    }

}
