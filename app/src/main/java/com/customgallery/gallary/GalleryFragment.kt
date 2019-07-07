package com.customgallery.gallary
import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.customgallery.AppConstants
import com.customgallery.Gallery
import com.customgallery.PhotosListHolder
import com.customgallery.R
import com.customgallery.adapters.ViewPagerAdapter
import com.customgallery.asynctask.AsyncTaskForGallary
import com.customgallery.imageviews.ViewImagesFragment
import com.customgallery.listeners.AsyncListener
import com.customgallery.listeners.GallaryActivityListener
import com.customgallery.pojos.ImagesDetailsBean
import com.customgallery.pojos.SelectedImagesBean
import kotlinx.android.synthetic.main.activity_gallary.*


/*
* this class is used to load and select the decor images
* */
class GalleryFragment() : DialogFragment(), GallaryViews, View.OnClickListener, AsyncListener, GallaryActivityListener {


    private var mFolderList: ArrayList<AsyncTaskForGallary.FolderModel>? = null
    private var mCounter: Int = 0
    private var mSelectedImagesList: ArrayList<SelectedImagesBean>? = null
    private var mDeleteImagesPermanently: ArrayList<SelectedImagesBean>? = null
    private var mPagerAdapter: ViewPagerAdapter? = null
    private var mAnimationBounce: Animation? = null
    private var mBottomUp: Animation? = null
    private var mPresenter: GallaryPresenter? = null
    private var imagesFetchListener: Gallery.fetchImagesListener?=null;

    fun setImagesFetchListener(imagesFetchListener : Gallery.fetchImagesListener){
        this.imagesFetchListener=imagesFetchListener;
    }

    /*
    * override when no internet connection
    * */
    /*
    * method is used to get the images acc. to the folder name
    * */
    fun getImagesByFolderName(string: String): ArrayList<ImagesDetailsBean>? {
        if (mFolderList != null && mFolderList!!.size > 0)
            for (i in mFolderList!!.indices) {
                if (mFolderList!![i].mFolderName.equals(string, true)) {
                    return mFolderList!![i].mImagesDetails
                }
            }
        return null
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog?.getWindow()?.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view =inflater.inflate(R.layout.activity_gallary, container, false)
        return view
    }

    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AsyncTaskForGallary(this).execute()

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mPresenter = GallaryPresenter(this)
        mPresenter!!.initView()

    }


    override fun onResume() {
        super.onResume()
    }

    /*
    * init the view in the Playvideo screen
    * */
    @SuppressLint("SetTextI18n")
    override fun initializeViews() {
        mSelectedImagesList = ArrayList()
        initClickListener()
        setAlertText(0)
        initAnimations()
        setThePagerListener()

    }

    /*
    * method is used to set the pager listener
    * */
    private fun setThePagerListener() {
        vPImageSlider.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                val fragment: ViewImagesFragment = mPagerAdapter?.getItem(position) as ViewImagesFragment
                fragment.UpdateAllCounters(mSelectedImagesList!!)
            }

            override fun onPageScrollStateChanged(state: Int) {

            }
        })

    }

    /*
    * method is used toinit aniations
    * */
    private fun initAnimations() {
        mAnimationBounce = AnimationUtils.loadAnimation(activity!!, R.anim.bounce)
        mBottomUp = AnimationUtils.loadAnimation(activity!!, R.anim.bottom_up)

    }

    /*
    * method is used to set teh bottom alert text
    * */
    @SuppressLint("SetTextI18n")
    fun setAlertText(count: Int) {
        val minSize=1
        if (count == 0) {
            tvBringWallToLife.visibility = View.GONE
            tvMessageCountAlert.visibility = View.VISIBLE
            frame_bring_wall.visibility = View.GONE
            tvMessageCountAlert.text = "Please select minimum" + " " + minSize + " " + "photos"
        } else if (count < minSize) {
            tvBringWallToLife.visibility = View.GONE
            tvMessageCountAlert.visibility = View.VISIBLE
            frame_bring_wall.visibility = View.GONE
            if (count == minSize) {
                tvMessageCountAlert.text = "Select" + " " + (minSize - count).toString() + " " + "more photos"
            } else {
                tvMessageCountAlert.text = "Select"+ " " + (minSize - count).toString() + " " + "more photos"
            }
        } else {
            tvMessageCountAlert.visibility = View.GONE
            frame_bring_wall.visibility = View.VISIBLE
            if (tvBringWallToLife.visibility == View.GONE) {
                tvBringWallToLife.visibility = View.VISIBLE
                tvBringWallToLife.startAnimation(mBottomUp)
            }
        }
    }


    /*
    *method is used to setup the view pager
    * */
    private fun setupTheViewPager() {
        if (mFolderList!!.size > 0) {
            mPagerAdapter = ViewPagerAdapter(childFragmentManager)
            for (i in mFolderList!!.indices) {
                val bundleImages = Bundle()
                bundleImages.putString(AppConstants.FOLDER_NAME, mFolderList!![i].mFolderName)
                val imagesListFragment = ViewImagesFragment(this, i)
                imagesListFragment.arguments = bundleImages
                mPagerAdapter!!.addFragment(imagesListFragment, mFolderList!![i].mFolderName, i)
            }
            vPImageSlider.adapter = mPagerAdapter
        }
        tbLayout.setupWithViewPager(vPImageSlider)

    }


    /*
       * initilize the click listener
       * */
    private fun initClickListener() {
        tvBringWallToLife?.setOnClickListener(this)

    }

    /*
    * handle all the onclick on the home screen views
    * */
    override fun onClick(view: View?) {
        when (view?.id) {
            tvBringWallToLife.id -> sendPhotos(mSelectedImagesList)
//            ivBackButton.id -> mPresenter?.closeWindow()
        }
    }

    /*
    * when user press the cross button
    * */
    override fun closeWindow() {
    }


    /*
   * mehtod is used when all the images is fetched
   * */
    override fun allImagesFetched(folderNameWithListFolder: ArrayList<AsyncTaskForGallary.FolderModel>) {
        mPresenter?.itemFetched(folderNameWithListFolder)
    }

    /*
    * item fetched from the async
    * */
    override fun itemFetched(list: ArrayList<AsyncTaskForGallary.FolderModel>) {
        if (list.size > 0) {
            mFolderList = list
            setupTheViewPager()
        }
    }

    /*
    * get the updated counter
    * */
    override fun getCounter(isSelected: Boolean, mImageUrl: String, fragmentPosition: Int, position1: Int, mDeletedPosition: String): Int {
        if (isSelected) {
            ++mCounter
            val bean = SelectedImagesBean()
            bean.count = (mCounter).toString()
            bean.imagePath = mImageUrl
            bean.mFragmentPosition = fragmentPosition
            bean.mImagePositionPosition = position1
            mSelectedImagesList!!.add(bean)
        } else {
            --mCounter
            for (j in mDeletedPosition.toInt() until mSelectedImagesList!!.size) {
                mSelectedImagesList!![j].count = ((mSelectedImagesList!![j].count).toInt() - 1).toString()
            }
            if (mSelectedImagesList!!.size > 0) {
                mSelectedImagesList!!.removeAt(mDeletedPosition.toInt() - 1)
            }
        }
        setAlertText(mSelectedImagesList!!.size)
        return mCounter
    }

    /*
    * method is used to ge th the selected list by the user
    * */
    override fun getSelectedList(): ArrayList<SelectedImagesBean>? {
        return mSelectedImagesList
    }


    /*
    * method is used to send the selected photos for the editing
    * */
    override fun sendPhotos(mSelectedImagesList: ArrayList<SelectedImagesBean>?) {
        tvBringWallToLife.startAnimation(mAnimationBounce)
        mAnimationBounce!!.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {
            }

            override fun onAnimationEnd(animation: Animation) {
                mDeleteImagesPermanently?.clear()
                imagesFetchListener?.imagesFetched(mSelectedImagesList!!)
                dismiss()
            }

            override fun onAnimationRepeat(animation: Animation) {

            }
        })
    }



    /*
    * method is used to notify the adapter
    * */
    private fun notifyChangesInImages() {
        mSelectedImagesList = PhotosListHolder.getSelectedList()
        for (i in 0 until mSelectedImagesList!!.size) {
            val fragment: ViewImagesFragment = mPagerAdapter?.getItem(mSelectedImagesList!![i].mFragmentPosition) as ViewImagesFragment
            fragment.updateNewCounters(mSelectedImagesList!![i])
            // mSelectedImagesList!![i].mAppliedFilter=AppConstants.ORIGINAL_FILTER
        }
        val mDeletedList: ArrayList<SelectedImagesBean>? = PhotosListHolder.getSelectedList()
        for (i in 0 until mDeletedList!!.size) {
            mCounter--
            val fragment: ViewImagesFragment = mPagerAdapter?.getItem(mDeletedList[i].mFragmentPosition) as ViewImagesFragment
            fragment.removeDeletedCount(mDeletedList[i].mFragmentPosition, mDeletedList[i].mImagePositionPosition)
        }
        setAlertText(mSelectedImagesList!!.size)
    }

    /*
    * method is used to open the drawer layout
    * */
    override fun openDrawerLayout() {
    }

    /*
    * method is used to notify the data
    * */
    private fun notifyDeletedData() {
        for (i in 0 until mDeleteImagesPermanently!!.size) {
            mCounter--
            val fragmentForDeletion: ViewImagesFragment = mPagerAdapter?.getItem(mDeleteImagesPermanently!![i].mFragmentPosition) as ViewImagesFragment
            fragmentForDeletion.removeDeletedCount(mDeleteImagesPermanently!![i].mFragmentPosition, mDeleteImagesPermanently!![i].mImagePositionPosition)
            for (j in ((mDeleteImagesPermanently!![i].count).toInt() - 1) until mSelectedImagesList!!.size) {
                mSelectedImagesList!![j].count = ((mSelectedImagesList!![j].count).toInt() - 1).toString()
            }
            if (mSelectedImagesList!!.size > 0) {
                mSelectedImagesList!!.removeAt(((mFolderList!![mDeleteImagesPermanently!![i].mFragmentPosition].mImagesDetails[mDeleteImagesPermanently!![i].mImagePositionPosition].mCounter).toInt() - 1))
            }
            fragmentForDeletion.UpdateAllCounters(mSelectedImagesList)
            fragmentForDeletion.deleteImagePermanently(mDeleteImagesPermanently!![i].mFragmentPosition, mDeleteImagesPermanently!![i].mImagePositionPosition)
            fragmentForDeletion.notifyAllD()
        }
        setAlertText(mSelectedImagesList!!.size)
    }

    /*
    * syn all imges one by one in images
    * */
    override fun galleryImagesInProgress(mAlImages: ArrayList<AsyncTaskForGallary.FolderModel>) {
    }



    override fun onDestroy() {
        super.onDestroy()
    }
}
