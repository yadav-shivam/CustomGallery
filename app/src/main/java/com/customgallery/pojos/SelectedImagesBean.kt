package com.customgallery.pojos

import android.os.Parcel
import android.os.Parcelable


open class SelectedImagesBean() : Parcelable {
    lateinit var imagePath: String
    var editedImage: String = ""
    var count: String= "0"
    var mSizeType: String = "0"
    var mImagesCount: String = "1"
    var mFragmentPosition: Int = 0
    var mImagePositionPosition: Int = 0
    var mIsSelected: Boolean = false
    var mFilterSelected: Boolean = false
    var hideViewIstTime: Boolean = false

    constructor(parcel: Parcel) : this() {
        imagePath = parcel.readString()
        editedImage = parcel.readString()
        count = parcel.readString()
        mSizeType = parcel.readString()
        mImagesCount = parcel.readString()
        mFragmentPosition = parcel.readInt()
        mImagePositionPosition = parcel.readInt()
        mIsSelected = parcel.readByte() != 0.toByte()
        mFilterSelected = parcel.readByte() != 0.toByte()
        hideViewIstTime = parcel.readByte() != 0.toByte()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(imagePath)
        parcel.writeString(editedImage)
        parcel.writeString(count)
        parcel.writeString(mSizeType)
        parcel.writeString(mImagesCount)
        parcel.writeInt(mFragmentPosition)
        parcel.writeInt(mImagePositionPosition)
        parcel.writeByte(if (mIsSelected) 1 else 0)
        parcel.writeByte(if (mFilterSelected) 1 else 0)
        parcel.writeByte(if (hideViewIstTime) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SelectedImagesBean> {
        override fun createFromParcel(parcel: Parcel): SelectedImagesBean {
            return SelectedImagesBean(parcel)
        }

        override fun newArray(size: Int): Array<SelectedImagesBean?> {
            return arrayOfNulls(size)
        }
    }
}