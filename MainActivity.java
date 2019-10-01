package com.example.ashud.cookieclicker;

import android.animation.ValueAnimator;
import android.graphics.Color;
import android.media.Image;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.concurrent.atomic.AtomicInteger;

public class MainActivity extends AppCompatActivity {

    ImageView imageView;
    ImageView wizardTower;
    ImageView grandma;
    ImageView backgroundOne;
    ImageView backgroundTwo;
    TextView score;
    AtomicInteger counter;
    ConstraintLayout constraintLayout,ConstraintLayoutWizard;
    int grandmaCounter = 0, wizardTowerCounter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = (ImageView) findViewById(R.id.imageView);
        counter = new AtomicInteger(0);
        score = (TextView) findViewById(R.id.score);

        constraintLayout = (ConstraintLayout) findViewById(R.id.layout);
        ConstraintLayoutWizard = (ConstraintLayout) findViewById(R.id.ConstraintLayoutWizard);
        wizardTower = (ImageView) findViewById(R.id.imageView2);
        grandma = (ImageView) findViewById(R.id.imageView3);

        constraintLayout.bringChildToFront(imageView);
        constraintLayout.bringChildToFront(wizardTower);
        constraintLayout.bringChildToFront(score);
        final ImageView backgroundOne = (ImageView) findViewById(R.id.background_one);
        final ImageView backgroundTwo = (ImageView) findViewById(R.id.background_two);

        final ValueAnimator back = ValueAnimator.ofFloat(0f, 1f);
        back.setRepeatCount(ValueAnimator.INFINITE);
        back.setInterpolator(new LinearInterpolator());
        back.setDuration(10000L);
        back.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                final float rightSide = (float) back.getAnimatedValue();
                final float width = backgroundOne.getWidth();
                final float xMove = width * rightSide;
                backgroundOne.setTranslationX(xMove);
                backgroundTwo.setTranslationX(xMove - width);
            }
        });

        back.start();


        final ScaleAnimation scaleAnimation = new ScaleAnimation(0.5f, 1.0f, .5f, 1.0f, Animation.RELATIVE_TO_SELF, .5f, Animation.RELATIVE_TO_SELF, .5f);
        scaleAnimation.setDuration(500);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(scaleAnimation);
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        IncreaseScore(1 + 4 * grandmaCounter + 8 * wizardTowerCounter);
                    }
                });

                thread.start();

                PlusOne();
            }
        });

        wizardTower.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (counter.get() >= 30) {
                    IncreaseScore(-30);
                    wizardTowerCounter++;
                    AddUpgrades(R.drawable.wizardtower2);
                }
            }
        });

        grandma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (counter.get() >= 15) {
                    IncreaseScore(-15);
                    grandmaCounter++;
                    AddUpgrades(R.drawable.grandma2);
                }
            }
        });
    }

    public void IncreaseScore(int amount) {
        counter.addAndGet(amount);

        score.post(new Runnable() {
            @Override
            public void run() {
                score.setText(counter.get() + "");
            }
        });
    }

      public void PlusOne(){
          final TextView plusOne = new TextView(this);
          TranslateAnimation translateAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, -4f);
          translateAnimation.setDuration(2000);
          plusOne.setId(View.generateViewId());

        ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT,ConstraintLayout.LayoutParams.WRAP_CONTENT);
        plusOne.setLayoutParams(params);
        constraintLayout.addView(plusOne);
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(constraintLayout);

        constraintSet.connect(plusOne.getId(),ConstraintSet.TOP,score.getId(),ConstraintSet.BOTTOM);
        constraintSet.connect(plusOne.getId(),ConstraintSet.BOTTOM,score.getId(),ConstraintSet.BOTTOM);
        constraintSet.connect(plusOne.getId(),ConstraintSet.LEFT,constraintLayout.getId(),ConstraintSet.LEFT);
        constraintSet.connect(plusOne.getId(),ConstraintSet.RIGHT,constraintLayout.getId(),ConstraintSet.RIGHT);
        float x = (float) Math.random()*1f;
        float y = (float)Math.random()*1f;

        constraintSet.setVerticalBias(plusOne.getId(),y);
        constraintSet.setHorizontalBias(plusOne.getId(),x);

        constraintSet.applyTo(constraintLayout);

        int totalAmount = 1 + grandmaCounter * 10 + wizardTowerCounter * 10;
        plusOne.setText("+" + totalAmount);
        plusOne.setTextColor(Color.WHITE);
        plusOne.startAnimation(translateAnimation);

        plusOne.postDelayed(new Runnable() {
            @Override
            public void run() {
                AlphaAnimation alphaAnimation = new AlphaAnimation(1f, 0f);
                alphaAnimation.setDuration(470);
                alphaAnimation.setInterpolator(new AccelerateInterpolator());
                alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        plusOne.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                plusOne.startAnimation(alphaAnimation);
            }
        }, 1500);

    }

    public void AddUpgrades(int id) {
          ImageView imageView = new ImageView(this);
          imageView.setId(View.generateViewId());

          ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);

          params.width = 100;
          params.height = 100;

          imageView.setLayoutParams(params);

          ConstraintLayoutWizard.addView(imageView);
          imageView.bringToFront();
          ConstraintLayoutWizard.bringChildToFront(imageView);

        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(ConstraintLayoutWizard);
        constraintSet.connect(imageView.getId(),ConstraintSet.TOP,ConstraintLayoutWizard.getId(),ConstraintSet.TOP);
        constraintSet.connect(imageView.getId(),ConstraintSet.BOTTOM,ConstraintLayoutWizard.getId(),ConstraintSet.BOTTOM);
        constraintSet.connect(imageView.getId(),ConstraintSet.LEFT,ConstraintLayoutWizard.getId(),ConstraintSet.LEFT);
        constraintSet.connect(imageView.getId(),ConstraintSet.RIGHT,ConstraintLayoutWizard.getId(),ConstraintSet.RIGHT);

        final AlphaAnimation alphaAnimation = new AlphaAnimation(0f, 1f);
        alphaAnimation.setDuration(450);

        switch (id) {
            case R.drawable.wizardtower2:
                imageView.setImageResource(R.drawable.wizardtower2);
                constraintSet.setVerticalBias(imageView.getId(), (float) (Math.random()));
                constraintSet.setHorizontalBias(imageView.getId(), (float) (Math.random() * 0.24f + 0.72f));
                imageView.startAnimation(alphaAnimation);
                break;
            case R.drawable.grandma2:
                imageView.setImageResource(R.drawable.grandma2);
                constraintSet.setVerticalBias(imageView.getId(), (float) (Math.random()));
                constraintSet.setHorizontalBias(imageView.getId(), (float) (Math.random() * 0.24f));
                imageView.startAnimation(alphaAnimation);
                break;
        }


        constraintSet.applyTo(ConstraintLayoutWizard);
    }
}
