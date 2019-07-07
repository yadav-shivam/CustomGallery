package com.customgallery.imageviews

import com.customgallery.imageviews.ViewImagesViews


internal class ViewImagesPresenter(private val view: ViewImagesViews) : ViewImagesModelListener {

    private var viewImagesModel: ViewImagesModel? = null

    /*
    * method is used to close the activtity
    * */
    fun closeWindow() {
        view.closeWindow()
    }

    /*
    * mehtod is used when the user click on the image
    * */
    fun ImageClicked(position: Int, imageUrl: String, alertVisibility: Int) {
        view.ImageClicked(position, imageUrl, alertVisibility)

    }

    /*
    * method is used to show the alert dialog
    * */
    fun showAlertDialog(fragmentPosition: Int, imagePosition: Int) {
    }

    fun initView() {
        view.initializeViews()
    }
}