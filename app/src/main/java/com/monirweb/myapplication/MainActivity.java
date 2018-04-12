package com.monirweb.myapplication;

import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViews();

        initializeView();

        countTime();

        generateNumberToNextLevel();
    }
    //End of onCreate



    private void findViews(){
        leftButton=(Button) findViewById(R.id.left_button);
        rightButton=(Button) findViewById(R.id.right_button);
        equal=(Button) findViewById(R.id.equal);
        coin=(ImageView) findViewById(R.id.coin);
        clock=(ImageView) findViewById(R.id.clock);
        coinCounter=(TextView) findViewById(R.id.coin_counter);
        levelCounter=(TextView) findViewById(R.id.level_counter);

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
        CountDownTimer countDownTimer=new CountDownTimer(20000,1000) {
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
                if (gameInProgress==true) {
                    setCoinAnimation();
                    score++;
                }
            }
        }else if (whichIsPressed==RIGHT_BUTTON) {
            if (leftButtonValue<rightButtonValue){
                if (gameInProgress==true) {
                    setCoinAnimation();
                    score++;
                }
            }
        }else if (whichIsPressed==EQUAL_BUTTON) {
            if (leftButtonValue==rightButtonValue){
                if (gameInProgress==true) {
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
        scaleAnimation = AnimationUtils.loadAnimation(MainActivity.this,R.anim.scale);
        leftButton.startAnimation(scaleAnimation);
        rightButton.startAnimation(scaleAnimation);
    }

    private void setCoinAnimation(){
        scaleCoinAnimation = AnimationUtils.loadAnimation(MainActivity.this,R.anim.coin_scale);
        coin.startAnimation(scaleCoinAnimation);
    }

    private void setLevelAnimation(){
        scaleLevelAnimation = AnimationUtils.loadAnimation(MainActivity.this,R.anim.clock_scale);
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
        if(gameInProgress==false){ //instead of level number -> if (level==ALL_LEVEL){
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
