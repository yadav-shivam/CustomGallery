package com.customgallery.asynctask


import android.annotation.SuppressLint
import android.database.Cursor
import android.net.Uri
import android.os.AsyncTask
import android.provider.MediaStore
import com.customgallery.App
import com.customgallery.listeners.AsyncListener
import com.customgallery.pojos.ImagesDetailsBean
import java.io.Serializable
import java.util.*


/*
* class is used to get the device images(foldername+its images) in the background
* */
class AsyncTaskForGallary(val listener: AsyncListener) : AsyncTask<Void, ArrayList<AsyncTaskForGallary.FolderModel>, Void>() {

    private var mAlImages = ArrayList<AsyncTaskForGallary.FolderModel>()
    private var mAllImagesList = ArrayList<ImagesDetailsBean>()
    private var mIsFolder: Boolean = false
    private var mAllPicturesBean: AsyncTaskForGallary.FolderModel? = null

    override fun onPreExecute() {
        mAllImagesList = ArrayList()
        mAllPicturesBean = AsyncTaskForGallary.FolderModel()
    }

    /*
    * method is used to ge the folder and the all the images path folderName in the back ground
    * */
    override fun doInBackground(vararg params: Void?): Void? {
        fetchImagesFromGallery()
        return null
    }

    /*
    * when all the images fetched succesfully
    * */
    override fun onPostExecute(result: Void?) {
        val sortedList = ArrayList<FolderModel>()
        sortedList.add(0,mAlImages.get(0));
        mAlImages.removeAt(0)
        for (i in 0 until mAlImages!!.size) {
            if(mAlImages[i].mFolderName.contentEquals("Camera"))
            {
                sortedList.add(1,mAlImages[i])
                mAlImages.removeAt(i)
                break;
            }
        }
        Collections.sort(mAlImages, comparator);
        Collections.reverse(mAlImages);
        sortedList.addAll(mAlImages)
        listener.allImagesFetched(sortedList)
    }



    @SuppressLint("Recycle")
    /*
    * get the all images from the gallary with folder name
    * */
    private fun fetchImagesFromGallery() {
        mAlImages.clear()
        var intPosition = 0
        val uri: Uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val cursorData: Cursor?
        val columnIndexData: Int
        val columnIndexFolderName: Int
        mAllPicturesBean!!.mFolderName = "All"
        var absolutePathOfImage: String? = null
        val projection = arrayOf(MediaStore.MediaColumns.DATA, MediaStore.Images.Media.BUCKET_DISPLAY_NAME)
        val orderBy = MediaStore.Images.Media.DATE_TAKEN
        cursorData = App.instance!!.contentResolver.query(uri, projection, null, null, "$orderBy DESC")
        columnIndexData = cursorData!!.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA)
        columnIndexFolderName = cursorData.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME)
        while (cursorData.moveToNext()) {
            absolutePathOfImage = cursorData.getString(columnIndexData)
            for (i in mAlImages.indices) {
                if (mAlImages.get(i).mFolderName.equals(cursorData.getString(columnIndexFolderName))) {
                    mIsFolder = true
                    intPosition = i
                    break
                } else {
                    mIsFolder = false
                }
            }

            if (mIsFolder) {
                if(!absolutePathOfImage.contains(".gif")) {
                    val alPath = ArrayList<ImagesDetailsBean>()
                    alPath.addAll(mAlImages.get(intPosition).mImagesDetails)
                    val bean = ImagesDetailsBean()
                    bean.mImageUrl = absolutePathOfImage
                    alPath.add(bean)

                    mAllImagesList.add(bean)
                    mAlImages.get(intPosition).mImagesDetails = alPath
                }

            } else {
                if(!absolutePathOfImage.contains(".gif")) {
                    val alPath = java.util.ArrayList<ImagesDetailsBean>()
                    val bean = ImagesDetailsBean()
                    bean.mImageUrl = absolutePathOfImage
                    mAllImagesList.add(bean)
                    alPath.add(bean)
                    val objModel = AsyncTaskForGallary.FolderModel()
                    objModel.mFolderName = cursorData.getString(columnIndexFolderName)
                    objModel.mImagesDetails = alPath
                    mAlImages.add(objModel)
                }
            }
        }
        mAllPicturesBean!!.mImagesDetails = mAllImagesList
        mAlImages.add(0, mAllPicturesBean!!)
    }


    /*
    * custom model class
    * */
    class FolderModel : Serializable {
        lateinit var mFolderName: String
        lateinit var mImagesDetails: ArrayList<ImagesDetailsBean>
    }

    var comparator:Comparator<AsyncTaskForGallary.FolderModel> = object:Comparator<AsyncTaskForGallary.FolderModel>
    {
        override fun compare(p0: AsyncTaskForGallary.FolderModel?, p1: AsyncTaskForGallary.FolderModel?): Int {
            //return Integer.compare(p0!!.mImagesDetails.size, p1!!.mImagesDetails.size)
            return Integer.compare(p0!!.mImagesDetails.size, p1!!.mImagesDetails.size)
            //return p0!!.mFolderName.compareTo(p1!!.mFolderName,true)
        }
    }
}