package com.customgallery.pojos

import java.io.Serializable

class ImagesDetailsBean : Serializable {
    lateinit var mImageUrl: String
    var mCounter: String = "0"
    var mIsSelected: Boolean = false
}