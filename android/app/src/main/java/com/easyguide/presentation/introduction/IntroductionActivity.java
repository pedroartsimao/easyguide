package com.easyguide.presentation.introduction;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.easyguide.BaseActivity;
import com.easyguide.R;
import com.easyguide.presentation.login.LoginActivity;
import com.easyguide.ui.adapter.IntroductionAdapter;

import butterknife.BindArray;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class IntroductionActivity extends BaseActivity implements ViewPager.OnPageChangeListener {

    @BindView(R.id.viewpager_intro)
    ViewPager viewPagerIntro;

    @BindView(R.id.linearlayout_bottom_page)
    LinearLayout linearLayoutBottomPage;

    @BindView(R.id.imageview_icon_1)
    ImageView imageViewIcon1;
    @BindView(R.id.imageview_icon_2)
    ImageView imageViewIcon2;

    private int[] icons = new int[]{
            R.drawable.ic_introduction_1,
            R.drawable.ic_introduction_2,
            R.drawable.ic_introduction_3
    };

    @BindArray(R.array.introduction_titles)
    String[] titles;
    @BindArray(R.array.introduction_messages)
    String[] messages;

    private int lastPage = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introduction);
        ButterKnife.bind(this);
        setupViewPager();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

    @Override
    public void onPageSelected(int position) {}

    @Override
    public void onPageScrollStateChanged(int state) {
        if (state == ViewPager.SCROLL_STATE_IDLE || state == ViewPager.SCROLL_STATE_SETTLING) {
            if (lastPage != viewPagerIntro.getCurrentItem()) {
                lastPage = viewPagerIntro.getCurrentItem();

                ImageView fadeoutImage;
                ImageView fadeinImage;
                if (imageViewIcon1.getVisibility() == View.VISIBLE) {
                    fadeoutImage = imageViewIcon1;
                    fadeinImage = imageViewIcon2;
                } else {
                    fadeoutImage = imageViewIcon2;
                    fadeinImage = imageViewIcon1;
                }

                fadeinImage.bringToFront();
                fadeinImage.setImageResource(icons[lastPage]);
                fadeinImage.clearAnimation();
                fadeoutImage.clearAnimation();

                fadeoutImage.startAnimation(imageViewAnimation(fadeoutImage, R.anim.fade_out));
                fadeinImage.startAnimation(imageViewAnimation(fadeinImage, R.anim.fade_in));
            }
        }
    }

    @OnClick(R.id.button_start_exploring)
    public void buttonStartExploringOnClick() {
        finish();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    private void setupViewPager() {
        IntroductionAdapter adapter = new IntroductionAdapter(titles, messages, linearLayoutBottomPage);
        viewPagerIntro.setAdapter(adapter);
        viewPagerIntro.setPageMargin(0);
        viewPagerIntro.setOffscreenPageLimit(1);
        viewPagerIntro.addOnPageChangeListener(this);
    }

    private Animation imageViewAnimation(final ImageView imageView, final int anim) {
        Animation animation = AnimationUtils.loadAnimation(this, anim);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                if(anim == R.anim.fade_in) {
                    imageView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if(anim == R.anim.fade_out) {
                    imageView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        return animation;
    }
}
