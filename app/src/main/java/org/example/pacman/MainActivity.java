package org.example.pacman;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends Activity {

    //reference to the main view
    GameView gameView;
    //reference to the game class.
    Game game;
    private Timer myTimer;
    private Timer gameTimer;
    private boolean isRunning = true;
    private int gameLength = 1 * 60;
    private int timeLeft;
    private Direction currentDirection = Direction.STOP;

    private Menu myMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //saying we want the game to run in one mode only
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);

        timeLeft = gameLength;

        gameView =  findViewById(R.id.gameView);
        TextView textView = findViewById(R.id.points);

        game = new Game(this,textView);
        game.setGameView(gameView);
        gameView.setGame(game);

        game.newGame();

        findViewById(R.id.moveLeft).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                currentDirection = Direction.LEFT;
            }
        });

        findViewById(R.id.moveRight).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                currentDirection = Direction.RIGHT;
            }
        });

        findViewById(R.id.moveUp).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                currentDirection = Direction.UP;
            }
        });

        findViewById(R.id.moveDown).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                currentDirection = Direction.DOWN;
            }
        });

        myTimer = new Timer();
        //We will call the timer 5 times each second
        myTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                TimerMethod();
            }

        }, 0, 50); //0 indicates we start now, 100 is the number of miliseconds between each call

        gameTimer = new Timer();
        gameTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                gameRunning();
            }

        }, 0, 1000);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        myMenu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        } else if (id == R.id.action_newGame) {
            currentDirection = Direction.STOP;
            timeLeft = gameLength;
            isRunning = true;
            game.newGame();
            return true;
        } else if (id == R.id.action_continue) {
            isRunning = true;
            myMenu.findItem(R.id.action_pause).setEnabled(true);
            item.setEnabled(false);
            return true;
        } else if (id == R.id.action_pause) {
            isRunning = false;
            myMenu.findItem(R.id.action_continue).setEnabled(true);
            item.setEnabled(false);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void gameRunning() {
        this.runOnUiThread(GameTimer_Tick);
    }

    private Runnable GameTimer_Tick =new Runnable() {
        public void run() {
            if (!game.isGameOver() && isRunning)
            {
                timeLeft--;
                TextView gameTimerView = findViewById(R.id.gameTime);
                gameTimerView.setText("Time remaining: " + timeLeft);
                if (timeLeft == 0) {
                    game.gameOver = true;
                    isRunning = false;
                    gameView.invalidate();
                }
            }
        }
    };

    private void TimerMethod()
    {
        //This method is called directly by the timer
        //and runs in the same thread as the timer.

        //We call the method that will work with the UI
        //through the runOnUiThread method.
        this.runOnUiThread(Timer_Tick);
    }

    private Runnable Timer_Tick = new Runnable() {
        public void run() {
            if (!game.isGameOver() && isRunning)
            {
                game.performMovement(currentDirection);
            }
        }
    };

}
