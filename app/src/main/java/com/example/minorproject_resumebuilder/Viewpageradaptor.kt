package com.example.minorproject_resumebuilder

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager2.adapter.FragmentStateAdapter

class Viewpageradaptor(fa:FragmentActivity):FragmentStateAdapter(fa){
    override fun getItemCount(): Int {
        return  3
    }

    override fun createFragment(position: Int): Fragment {
        return  when(position){
            0->Page1()
            1->Page2()
            else -> {
                Page3()
            }
        }
    }

}
