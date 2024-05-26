package com.example.androidfundamental.ui.detail

import android.content.Context
import android.os.Bundle
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.androidfundamental.R

class SectionAdapter(private val mContext: Context, fm: FragmentManager, data: Bundle) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private val mFragmentBundle: Bundle = data

    @StringRes
    private val mTabTitles = intArrayOf(R.string.followers, R.string.followings)

    override fun getCount(): Int = 2

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> FollowersFragment().apply { arguments = mFragmentBundle }
            1 -> FollowingFragment().apply { arguments = mFragmentBundle }
            else -> throw IllegalArgumentException("Invalid position: $position")
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return mContext.resources.getString(mTabTitles[position])
    }
}
