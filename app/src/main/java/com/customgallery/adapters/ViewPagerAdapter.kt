package com.customgallery.adapters

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import java.util.*

class ViewPagerAdapter(manager: FragmentManager) : FragmentPagerAdapter(manager) {
    private val mFragmentList = ArrayList<Fragment>()
    private val mFragmentTitleList = ArrayList<String>()
    private val mFragmentListId = ArrayList<String>()

    override fun getItem(position: Int): Fragment {
        return mFragmentList[position]
    }

    override fun getCount(): Int {
        return mFragmentList.size
    }


    fun addFragment(fragment: Fragment, title: String, i: Int) {
        mFragmentList.add(fragment)
        mFragmentTitleList.add(title)
        mFragmentListId.add(i.toString())
    }

    fun addFragment(fragment: Fragment) {
        mFragmentList.add(fragment)
    }


    override fun getPageTitle(position: Int): CharSequence? {
        return mFragmentTitleList[position]
    }
}
