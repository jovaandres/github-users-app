package com.sampling.test.githubUser.adapter

import android.content.Context
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.sampling.test.githubUser.ui.FollowFragment
import com.sampling.test.githubUser.R
import org.jetbrains.annotations.Nullable

class SectionsPagerAdapter(private val mContext: Context, fm: FragmentManager, name: String): FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    @StringRes
    private val tabTitle = intArrayOf(
        R.string.tab_text_1,
        R.string.tab_text_2
    )
    private val username = name

    override fun getItem(position: Int): Fragment =
        FollowFragment.newInstance(
            username,
            position
        )


    @Nullable
    override fun getPageTitle(position: Int): CharSequence? = mContext.resources.getString(tabTitle[position])

    override fun getCount(): Int = 2
}