package com.customgallery.imageviews

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.customgallery.App
import com.customgallery.GridSpacingItemDecoration
import com.customgallery.R
import com.customgallery.adapters.CustomImagesAdapter
import com.customgallery.gallary.GalleryFragment
import com.customgallery.listeners.GallaryActivityListener
import com.customgallery.listeners.GalleryImageClickListener
import com.customgallery.pojos.ImagesDetailsBean
import com.customgallery.pojos.SelectedImagesBean
import kotlinx.android.synthetic.main.fragment_images_list.*


@SuppressLint("ValidFragment")
/*
 * images list fragment for showing the gallary images
 * */
class ViewImagesFragment(val getListener: GallaryActivityListener, val fragmentPosition: Int) : Fragment(),
    ViewImagesViews, View.OnClickListener,
    GalleryImageClickListener {

    private var mImagesList: ArrayList<ImagesDetailsBean>? = null
    private var mImagesAdapter: CustomImagesAdapter? = null
    private var mPresenter: ViewImagesPresenter? = null
    private var isLoadedAlready: Boolean = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isLoadedAlready = false
        mPresenter!!.initView()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mPresenter = ViewImagesPresenter(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view= inflater.inflate(R.layout.fragment_images_list, container, false)
        return view
    }

    /*
    * initialize the views
    * */
    override fun initializeViews() {
        getImages()
        setRecyclerView()
    }

    /*
    * method is used to set the recycler view in andnorid
    * */
    private fun setRecyclerView() {
        if (mImagesList != null && mImagesList!!.size > 0) {
            rvCustomImages.layoutManager = GridLayoutManager(App.instance, 3)
            mImagesAdapter = CustomImagesAdapter(mImagesList!!, this)
            if (!isLoadedAlready) {
                val imagesSpacing = resources.getDimensionPixelSize(R.dimen.gallary_images_spacing)
                rvCustomImages.addItemDecoration(GridSpacingItemDecoration(3, imagesSpacing, false))
            }
            rvCustomImages.adapter = mImagesAdapter
            (rvCustomImages.itemAnimator as DefaultItemAnimator).supportsChangeAnimations = false
            isLoadedAlready = true
            rvCustomImages.setHasFixedSize(true)
            rvCustomImages.setItemViewCacheSize(20)
            rvCustomImages.isDrawingCacheEnabled = true
            rvCustomImages.drawingCacheQuality = View.DRAWING_CACHE_QUALITY_HIGH
            rvCustomImages.layoutManager!!.isAutoMeasureEnabled = false
        }
    }

    /*
    * method is used to getthe images from bundle passed from the activity
    * */
    private fun getImages() {
        val bundleImage: Bundle = arguments!!
        if (bundleImage.getString("folder-name") != null) {
            mImagesList = ArrayList()
            mImagesList = (getListener as GalleryFragment).getImagesByFolderName(bundleImage.getString("folder-name"))
        }
    }

    /*
    * method is used when the user tab on the gallery image
    * */
    override fun galleryImageClickListener(position: Int, imageUrl: String, alertVisibility: Int) {
        mPresenter?.ImageClicked(position, imageUrl, alertVisibility)
    }

    /*
    * image clicked by user
    * */
    override fun ImageClicked(position: Int, imageUrl: String, alertVisibility: Int) {
        try {
            if (mImagesList!![position].mIsSelected) {
                mImagesList!![position].mIsSelected = false
                UpdateAllCounters(getListener.getSelectedList())
                getListener.getCounter(false, mImagesList!![position].mImageUrl, fragmentPosition, position, mImagesList!![position].mCounter).toString()
                mImagesAdapter!!.notifyDataSetChanged()
                UpdateAllCounters(getListener.getSelectedList())
                var posit=0;
            } else {
                if (alertVisibility == View.VISIBLE) {
                    mPresenter?.showAlertDialog(fragmentPosition, position)
                } else {
                    showCountBubble(position)
                }
            }
        } catch (e: Exception) {
            Toast.makeText(activity,e.message,Toast.LENGTH_LONG).show()
        }

    }

    fun showCountBubble(imagePosition: Int) {
        if (mImagesList != null && mImagesList?.size!! > 0) {
            mImagesList!![imagePosition].mIsSelected = true
            mImagesList!![imagePosition].mCounter = getListener.getCounter(true,
                mImagesList!![imagePosition].mImageUrl, fragmentPosition, imagePosition, mImagesList!![imagePosition].mCounter).toString()
            mImagesAdapter!!.notifyItemChanged(imagePosition)
        }
    }

    /*
    * method is used to update all the counters when user remove the image from the selected list
    * */
    fun UpdateAllCounters(selectedList: ArrayList<SelectedImagesBean>?) {
        /*for (i in 0 until selectedList!!.size) {
            if(selectedList[i].mFragmentPosition==fragmentPosition){
                mImagesList!![selectedList[i].mImagePositionPosition].mCounter = selectedList[i].count
                mImagesAdapter!!.notifyItemChanged(selectedList[i].mImagePositionPosition)
            }
            if(selectedList[i].mIsSelected){
                mImagesAdapter!!.notifyItemChanged(selectedList[i].mImagePositionPosition)
            }
        }*/
        for (i in 0 until selectedList!!.size) {
            for (j in 0 until mImagesList!!.size) {
                if (mImagesList!![j].mImageUrl.equals(selectedList[i].imagePath, true)) {
                    mImagesList!![j].mCounter = selectedList[i].count
                    mImagesAdapter!!.notifyItemChanged(j)
                }
            }
        }

        mImagesAdapter!!.notifyDataSetChanged()
    }

    /*
    * notify deleted items
    * */
    fun removeDeletedCount(mFragmentPosition: Int, mImagePositionPosition: Int) {
        mImagesList!![mImagePositionPosition].mIsSelected = false
        mImagesList!![mImagePositionPosition].mCounter = "0"
        mImagesAdapter!!.notifyItemChanged(mImagePositionPosition)
    }

    override fun onClick(v: View?) {

    }

    override fun closeWindow() {

    }
    /*
    * method is used to chnage the count when swiping to the field
    * */
    fun notifyData(imagePosition: Int) {
        val selectedList: ArrayList<SelectedImagesBean> = getListener.getSelectedList()!!
        if (mImagesList != null) {
            for (i in 0 until mImagesList!!.size) {
                if (mImagesList!![i].mIsSelected) {
                    for (j in 0 until selectedList.size) {
                        if (selectedList[j].imagePath.equals(mImagesList!![i].mImageUrl)) {
                            mImagesList!![i].mCounter = selectedList[j].count
                        }
                    }
                }
            }
            mImagesAdapter!!.notifyItemChanged(imagePosition)
        }
    }

    /*
    * delete image permamanently
    * */
    fun deleteImagePermanently(mFragmentPosition: Int, mImagePositionPosition: Int) {
        mImagesList!!.removeAt(mImagePositionPosition)
        mImagesAdapter!!.notifyItemRemoved(mImagePositionPosition)
    }

    fun notifyAllD() {
        mImagesAdapter?.notifyDataSetChanged()
    }

    /*
    * update new counters after deletion
    * */
    fun updateNewCounters(selectedImagesBean: SelectedImagesBean) {
        mImagesList!![selectedImagesBean.mImagePositionPosition].mCounter = selectedImagesBean.count
        mImagesAdapter!!.notifyItemChanged(selectedImagesBean.mImagePositionPosition)
    }


}