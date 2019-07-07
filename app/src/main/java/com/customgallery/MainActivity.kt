package com.customgallery

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.customgallery.adapters.DisplayImagesAdapter
import com.customgallery.pojos.SelectedImagesBean
import kotlinx.android.synthetic.main.activity_main.*

private val PERMISSION_CODE_FOR_GALLARY: Int = 123

class MainActivity : AppCompatActivity(), Gallery.fetchImagesListener {
    private var selectedImagesList:ArrayList<SelectedImagesBean> ?=null

    override fun imagesFetched(selectedImagesList: ArrayList<SelectedImagesBean>) {
        this.selectedImagesList=selectedImagesList;
        rv_images.layoutManager=LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
        rv_images.adapter=DisplayImagesAdapter(selectedImagesList)

    }

    private val activity= this;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        tvBtnGetStartedInFragment.setOnClickListener {
            startAsyncTaskforImags()
        }
    }



    /*
  * method is used to get the images using the aasync task
  * */
    private fun startAsyncTaskforImags() {
        isGalleryPermissionGranted()
    }

    /*
   * method is used to check that wheather device gives the permision or not
   * */
    private fun isGalleryPermissionGranted() {
        if (PermissionUtility.isPermissionGranted(activity, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE), PERMISSION_CODE_FOR_GALLARY)) {
            if (hasPermission(Manifest.permission.READ_EXTERNAL_STORAGE, PERMISSION_CODE_FOR_GALLARY)) {
                val mAnimationBounce = AnimationUtils.loadAnimation(activity, R.anim.bounce)
                tvBtnGetStartedInFragment.startAnimation(mAnimationBounce)
                mAnimationBounce!!.setAnimationListener(object : Animation.AnimationListener {
                    override fun onAnimationStart(animation: Animation) {
                    }

                    override fun onAnimationEnd(animation: Animation) {
                        if (selectedImagesList==null)
                        Gallery.openGallery(activity, this@MainActivity)
                        else
                            Gallery.openGallery(activity, selectedImagesList!!, this@MainActivity)
                    }

                    override fun onAnimationRepeat(animation: Animation) {

                    }
                })
            }
        }
    }

    /*
   * check if has permission or not
   * */
    fun hasPermission(permission: String, reqId: Int): Boolean {
        val result = ContextCompat.checkSelfPermission(activity, permission)
        if (result == PackageManager.PERMISSION_GRANTED)
            return true
        else {
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(permission), reqId)
            return false
        }
    }

}
