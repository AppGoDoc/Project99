package br.com.appgo.appgo.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.rd.PageIndicatorView;
import com.rd.animation.type.AnimationType;

import br.com.appgo.appgo.R;
import br.com.appgo.appgo.adapter.TutorialAdapter;
import br.com.appgo.appgo.model.TutorialText;

public class TutorialActivity extends AppCompatActivity {
    TutorialText tutorialText;
    PageIndicatorView indicatorView;
    TutorialAdapter tutorialAdapter;
    ViewPager pager;
    ImageView logoGO;
    Button mExit, mInitAppgo;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);

        tutorialText = new TutorialText();
        tutorialAdapter = new TutorialAdapter(tutorialText.CreateTutorialText(), this);
        logoGO = findViewById(R.id.image_go);
        logoGO.setVisibility(View.GONE);
        mExit = findViewById(R.id.btn_exit_tutorial);
        mExit.setVisibility(View.VISIBLE);
        mInitAppgo = findViewById(R.id.init_appgo);
        mInitAppgo.setVisibility(View.GONE);
        mInitAppgo.setEnabled(false);
        mExit.setEnabled(true);
        pager = findViewById(R.id.pager_tutorial);
        pager.setAdapter(tutorialAdapter);
        indicatorView = findViewById(R.id.indicator_page_tutorial);
        indicatorView.setCount(tutorialText.CreateTutorialText().size());
        indicatorView.setAnimationType(AnimationType.DROP);
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                indicatorView.setSelection(position);
                if (position < 3){
                    mExit.setEnabled(true);
                    mExit.setVisibility(View.VISIBLE);
                    mInitAppgo.setVisibility(View.GONE);
                    mInitAppgo.setEnabled(false);
                    logoGO.setVisibility(View.GONE);

                }
                else {
                    mExit.setVisibility(View.GONE);
                    mExit.setEnabled(false);
                    mInitAppgo.setVisibility(View.VISIBLE);
                    mInitAppgo.setEnabled(true);
                    logoGO.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mInitAppgo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
