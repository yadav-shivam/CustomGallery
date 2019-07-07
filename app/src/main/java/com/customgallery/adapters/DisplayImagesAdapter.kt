package com.customgallery.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.customgallery.R
import com.customgallery.pojos.SelectedImagesBean
import kotlinx.android.synthetic.main.gallary_image_row.view.*

class DisplayImagesAdapter(private var selectedImagesList: List<SelectedImagesBean>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view= LayoutInflater.from(viewGroup.context).inflate(R.layout.gallary_image_row,viewGroup,false)

        return ImagesViewHolder(view)
    }

    override fun getItemCount(): Int {
        return selectedImagesList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ImagesViewHolder).bind(selectedImagesList[position])
    }

    internal class ImagesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        init {
            itemView.setPadding(10,0,0,0)
            itemView.ivOverlay.visibility=View.GONE
            itemView.ivOverlaySelected.visibility=View.GONE
        }

        fun bind(selectedImagesBean: SelectedImagesBean){
            Glide.with(itemView.context).load(selectedImagesBean.imagePath).into(itemView.ivCustomImage)
        }
    }
}