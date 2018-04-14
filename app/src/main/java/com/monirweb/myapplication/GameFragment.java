package com.monirweb.myapplication;

import android.app.Fragment;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;

/**
 * Created by monir on 4/14/2018.
 */

public class GameFragment extends Fragment {


    private final int ALL_LEVEL=20;
    private final int LEFT_BUTTON=0;
    private final int RIGHT_BUTTON=1;
    private final int EQUAL_BUTTON=2;

    private Animation scaleAnimation;
    private Animation scaleCoinAnimation;
    private Animation scaleLevelAnimation;
    private Button leftButton;
    private Button rightButton;
    private Button equal;
    private TextView coinCounter;
    private TextView levelCounter;
    private ImageView coin;
    private ImageView clock;

    private int level=0;
    private int score=0;
    private int leftButtonValue;
    private int rightButtonValue;
    private boolean gameInProgress;

    private CountDownTimer countDownTimer;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.game_fragment,container,false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        findViews(view);

        initializeView();

        countTime();

        generateNumberToNextLevel();
    }
    //End of onViewCreated


    @Override
    public void onPause() {
        super.onPause();
        countDownTimer.cancel();
    }

    private void findViews(View view){
        leftButton=(Button) view.findViewById(R.id.left_button);
        rightButton=(Button) view.findViewById(R.id.right_button);
        equal=(Button) view.findViewById(R.id.equal);
        coin=(ImageView) view.findViewById(R.id.coin);
        clock=(ImageView) view.findViewById(R.id.clock);
        coinCounter=(TextView) view.findViewById(R.id.coin_counter);
        levelCounter=(TextView) view.findViewById(R.id.level_counter);

    }

    private void initializeView(){

        coinCounter.setText("0");
        setButtonAnimation();

        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                evaluateAndContinueGame(LEFT_BUTTON);
            }
        });

        rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                evaluateAndContinueGame(RIGHT_BUTTON);
            }
        });

        equal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                evaluateAndContinueGame(EQUAL_BUTTON);
            }
        });

    }

    private void countTime(){
        countDownTimer=new CountDownTimer(20000,1000) {
            @Override
            public void onTick(long remainingTime) {
                levelCounter.setText(getString(R.string.timer_counter_text,(int)(remainingTime/1000)));
            }

            @Override
            public void onFinish() {
                levelCounter.setText(getString(R.string.end_of_game_message));
                gameInProgress =false;
            }
        };
        countDownTimer.start();
        gameInProgress=true;
    }

    private void evaluate(int whichIsPressed){

        if (whichIsPressed==LEFT_BUTTON){
            if (leftButtonValue>rightButtonValue) {
                if (gameInProgress) {
                    setCoinAnimation();
                    score++;
                }
            }
        }else if (whichIsPressed==RIGHT_BUTTON) {
            if (leftButtonValue<rightButtonValue){
                if (gameInProgress) {
                    setCoinAnimation();
                    score++;
                }
            }
        }else if (whichIsPressed==EQUAL_BUTTON) {
            if (leftButtonValue==rightButtonValue){
                if (gameInProgress) {
                    setCoinAnimation();
                    score++;
                }
            }
        }
    }


    private void evaluateAndContinueGame(int whichIsPressed){
        evaluate(whichIsPressed);
        coinCounter.setText(String.valueOf(score));
        generateNumberToNextLevel();
    }

    private void setButtonAnimation(){
        scaleAnimation = AnimationUtils.loadAnimation(getActivity(),R.anim.scale);
        leftButton.startAnimation(scaleAnimation);
        rightButton.startAnimation(scaleAnimation);
    }

    private void setCoinAnimation(){
        scaleCoinAnimation = AnimationUtils.loadAnimation(getActivity(),R.anim.coin_scale);
        coin.startAnimation(scaleCoinAnimation);
    }

    private void setLevelAnimation(){
        scaleLevelAnimation = AnimationUtils.loadAnimation(getActivity(),R.anim.clock_scale);
        clock.startAnimation(scaleLevelAnimation);
    }


    private int generateInt(){
        Random random=new Random();
        int number=random.nextInt();
        if (number<0){
            number=number*-1;
        }
        number=number%31;
        return number;
    }

    private void generateNumberToNextLevel(){
        if(!gameInProgress){ //instead of level number -> if (level==ALL_LEVEL){
            setLevelAnimation();
            //show dialog with coins
            return;
        }
        level++;
        //levelCounter.setText(getString(R.string.level_counter_text,level)); //no need because we change it to timer
        leftButtonValue=generateInt();
        rightButtonValue=generateInt();
        leftButton.setText(String.valueOf(leftButtonValue));
        rightButton.setText(String.valueOf(rightButtonValue));
    }
}
