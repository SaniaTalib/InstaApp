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

        // Add your slide fragments here.
        // AppIntro will automatically generate the dots indicator and buttons.
        addSlide(FirstFragment())
        addSlide(SecondFragment())
        addSlide(ThirdFragment())
       /* // Instead of fragments, you can also use our default slide.
        // Just create a `SliderPage` and provide title, description, background and image.
        // AppIntro will do the rest.
        val sliderPage = SliderPage()
        sliderPage.title = title
        sliderPage.setDescription(description)
        sliderPage.imageDrawable = image
        sliderPage.bgColor = backgroundColor
        addSlide(AppIntroFragment.newInstance(sliderPage))*/

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


/*

import android.os.Bundle
import android.support.v4.app.Fragment
import com.alidevs.instaapp.R
import com.matthewtamlin.sliding_intro_screen_library.buttons.IntroButton
import com.matthewtamlin.sliding_intro_screen_library.core.IntroActivity
import android.content.Intent
import com.matthewtamlin.sliding_intro_screen_library.pages.ParallaxPage
import com.matthewtamlin.android_utilities_library.helpers.BitmapEfficiencyHelper
import com.matthewtamlin.android_utilities_library.helpers.ScreenSizeHelper
import android.content.Context
import android.support.v4.content.ContextCompat.startActivity
import com.matthewtamlin.sliding_intro_screen_library.background.ColorBlender
import com.matthewtamlin.sliding_intro_screen_library.transformers.MultiViewParallaxTransformer


class OnBoardActivity : IntroActivity() {


    private val BACKGROUND_COLORS = intArrayOf(-0xcfb002, -0x33ff9a, -0x66ff01)
    val DISPLAY_ONCE_PREFS = "display_only_once_spfile"
    val DISPLAY_ONCE_KEY = "display_only_once_spkey"


    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.NoActionBar);
        super.onCreate(savedInstanceState)

        if (introductionCompletedPreviously()) {
            val nextActivity = Intent(this, CreateNewLoginActivity::class.java)
            startActivity(nextActivity)
            finish()
        }

        hideStatusBar()
        configureTransformer()
        configureBackground()

    }

    override fun generateFinalButtonBehaviour(): IntroButton.Behaviour {
        val sp = getSharedPreferences(DISPLAY_ONCE_PREFS, Context.MODE_PRIVATE)
        val pendingEdits = sp.edit().putBoolean(DISPLAY_ONCE_KEY, true)

        // Define the next activity intent and create the Behaviour to use for the final button
        val nextActivity = Intent(this, CreateNewLoginActivity::class.java)
        return IntroButton.ProgressToNextActivity(nextActivity, pendingEdits)
    }

    override fun generatePages(savedInstanceState: Bundle?): MutableCollection<out Fragment> {
// This variable holds the pages while they are being created
        var pages : MutableCollection<Fragment> =  ArrayList()

        // Get the screen dimensions so that Bitmaps can be loaded efficiently
        val screenWidth = ScreenSizeHelper.getScreenWidthPx(this)
        val screenHeight = ScreenSizeHelper.getScreenHeightPx(this)

        // Load the Bitmap resources into memory
        val frontDots = BitmapEfficiencyHelper.decodeResource(
            this, R.drawable.splash_screen_image,
            screenWidth, screenHeight
        )
        val backDots = BitmapEfficiencyHelper.decodeResource(
            this, R.drawable.demo,
            screenWidth, screenHeight
        )

        // Create as many pages as there are background colors
        for (i in 0 until BACKGROUND_COLORS.size) {
            val newPage = ParallaxPage.newInstance()
            newPage.frontImage = frontDots
            newPage.backImage = backDots
            pages.add(newPage)
        }

        return pages
    }


    private fun introductionCompletedPreviously(): Boolean {
        val sp = getSharedPreferences(DISPLAY_ONCE_PREFS, Context.MODE_PRIVATE)
        return sp.getBoolean(DISPLAY_ONCE_KEY, false)
    }

    */
/**
     * Sets this IntroActivity to use a MultiViewParallaxTransformer page transformer.
     *//*

    private fun configureTransformer() {
        val transformer = MultiViewParallaxTransformer()
        transformer.withParallaxView(com.alidevs.instaapp.R.id.page_fragment_imageHolderFront, 1.2f)
        setPageTransformer(false, transformer)
    }

    */
/**
     * Sets this IntroActivity to use a ColorBlender background manager.
     *//*

    private fun configureBackground() {
        val backgroundManager = ColorBlender(BACKGROUND_COLORS)
        setBackgroundManager(backgroundManager)
    }


}
*/
