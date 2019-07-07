package com.customgallery.imageviews

interface ViewImagesViews  {
    fun initializeViews()
    fun closeWindow()
    fun ImageClicked(position: Int, imageUrl: String, alertVisibility: Int)
}
