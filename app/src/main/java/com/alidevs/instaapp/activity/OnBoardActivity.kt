package com.alidevs.instaapp.activity

import android.os.Bundle
import com.github.paolorotolo.appintro.AppIntro
import android.content.Intent
import android.support.v4.app.Fragment
import com.alidevs.instaapp.fragment.intro_fragments.FirstFragment
import com.alidevs.instaapp.fragment.intro_fragments.SecondFragment
import com.alidevs.instaapp.fragment.intro_fragments.ThirdFragment


class OnBoardActivity : AppIntro(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        addSlide(FirstFragment())
        addSlide(SecondFragment())
        addSlide(ThirdFragment())

    }

    override fun onSkipPressed(currentFragment: Fragment?) {
        super.onSkipPressed(currentFragment)
        val intent = Intent(this@OnBoardActivity,CreateNewLoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onDonePressed(currentFragment: Fragment?) {
        super.onDonePressed(currentFragment)
        val intent = Intent(this@OnBoardActivity,CreateNewLoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}
