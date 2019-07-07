package com.customgallery.adapters

import android.graphics.Bitmap
import android.support.v4.content.ContextCompat
import android.support.v7.widget.AppCompatImageView
import android.support.v7.widget.AppCompatTextView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.customgallery.App
import com.customgallery.R
import com.customgallery.listeners.GalleryImageClickListener
import com.customgallery.pojos.ImagesDetailsBean
import kotlinx.android.synthetic.main.gallary_image_row.view.*


/*
* This Adapter is used to show show the customer reviews
*/
class CustomImagesAdapter(private var mImagesList: ArrayList<ImagesDetailsBean>?, val listener: GalleryImageClickListener) : RecyclerView.Adapter<CustomImagesAdapter.CustomerReviewViewHolder>() {
    private var updatePosition: Int? = 0
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomerReviewViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.gallary_image_row, parent, false)
        setObserver(view)
        return CustomerReviewViewHolder(view)
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)

    }

    override fun onBindViewHolder(holder: CustomerReviewViewHolder, position: Int, payloads: MutableList<Any>) {
        super.onBindViewHolder(holder, position, payloads)
    }

    override fun onViewAttachedToWindow(holder: CustomerReviewViewHolder) {
        super.onViewAttachedToWindow(holder)

    }


    override fun onBindViewHolder(holder: CustomerReviewViewHolder, position: Int) {
        setTheCounterVisibility(holder, position)
        holder.bind(mImagesList!![position])
    }

    /*
   * method  is used to set the observer
   * */
    private fun setObserver(binding: View) {
        val viewTreeObserver = binding.ivCustomImage.viewTreeObserver
        viewTreeObserver.addOnGlobalLayoutListener {
            binding.ivCustomImage.layoutParams = RelativeLayout.LayoutParams(binding.ivCustomImage.width, binding.ivCustomImage.width)
        }
    }

    /*
    * method is used to show the counter
    * */
    private fun setTheCounterVisibility(holder: CustomerReviewViewHolder, position: Int) {
        if (mImagesList!![position].mIsSelected) {
            holder.binding.tvImageCounter.text = mImagesList!![position].mCounter
            if (position != updatePosition) {
                startVisibleWithoutAnimation(holder.binding.tvImageCounter, holder.binding.ivOverlaySelected)
            } else {
                startVisibleAnimation(holder.binding.tvImageCounter, holder.binding.ivOverlaySelected)
                updatePosition = -1
            }
        } else {
            startGoneAnimation(holder.binding.tvImageCounter, holder.binding.ivOverlaySelected)
        }
    }

    private fun startGoneAnimation(view: AppCompatTextView, ivOverlay: AppCompatImageView) {
        view.animate()
                .scaleX(0.0f)
                .scaleY(0.0f)
                .setDuration(500)
                .start()
        view.visibility = View.GONE
        ivOverlay.visibility = View.GONE

    }

    private fun startVisibleAnimation(view: AppCompatTextView, ivOverlay: AppCompatImageView) {
        view.animate()
                .scaleX(1.0f)
                .scaleY(1.0f)
                .setDuration(500)
                .start()
        view.visibility = View.VISIBLE
        ivOverlay.visibility = View.VISIBLE
        setPaddingToCountView(view)
        ivOverlay.setBackgroundColor(ContextCompat.getColor(App.instance!!, R.color.btn_get_started_start_color))
    }

    private fun startVisibleWithoutAnimation(view: AppCompatTextView, ivOverlay: AppCompatImageView) {
        view.animate()
                .scaleX(1.0f)
                .scaleY(1.0f)
                .setDuration(0)
                .start()
        view.visibility = View.VISIBLE
        ivOverlay.visibility = View.VISIBLE
        setPaddingToCountView(view)
        ivOverlay.setBackgroundColor(ContextCompat.getColor(App.instance!!, R.color.btn_get_started_start_color))
    }

    /*
    * method is used to set the padding according to the count in the textv iew
    * */
    private fun setPaddingToCountView(view: AppCompatTextView) {
        val count: Int = view.text.toString().toInt()
        when (count) {
            in 1..9 -> view.setPadding(9, 5, 9, 5)
            in 10..99 -> view.setPadding(9, 7, 9, 7)
            in 100..999 -> view.setPadding(9, 10, 9, 10)
        }
    }

    override fun getItemCount(): Int {
        return mImagesList!!.size
    }

    inner class CustomerReviewViewHolder(val binding: View) : RecyclerView.ViewHolder(binding), View.OnClickListener {

        init {
            binding.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            try {
                listener.galleryImageClickListener(adapterPosition, mImagesList!![adapterPosition].mImageUrl, binding.ivAlertImage.visibility)
            } catch (e: Exception) {

            }
        }


        fun bind(bean: ImagesDetailsBean) {
//            getImageResolution(Uri.parse(bean.mImageUrl), binding.ivAlertImage, binding.ivOverlay)
            val requestOptions = RequestOptions()
                    .encodeFormat(Bitmap.CompressFormat.JPEG)
                    .encodeQuality(50).diskCacheStrategy(DiskCacheStrategy.ALL)
            Glide.with(App.instance!!)
                    .load(bean.mImageUrl)
                    .apply(requestOptions)
                    .into( binding.ivCustomImage)

        }
    }

    /*
    * get the resolution of the image if the image is matched or not
    * */
    /*private fun getImageResolution(uri: Uri, alertView: AppCompatImageView, ivOverlay: AppCompatImageView) {
        if (uri.path != null) {
            val options = BitmapFactory.Options()
            options.inJustDecodeBounds = true
            BitmapFactory.decodeFile(File(uri.path).absolutePath, options)
            val imageHeight = options.outHeight
            val imageWidth = options.outWidth
            val width = AppSharedPreference.instance.getInt(AppSharedPreference.LOW_RESOLUTION_WIDTH)
            val heigth = AppSharedPreference.instance.getInt(AppSharedPreference.LOW_RESOLUTION_HEIGHT)
            if (imageWidth >= width && imageHeight >= heigth) {
                alertView.visibility = View.GONE
                ivOverlay.visibility = View.GONE
            } else {
                alertView.visibility = View.VISIBLE
                ivOverlay.visibility = View.VISIBLE
            }
        }
    }*/

}



