package org.example.pacman;

import android.content.Context;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

public class GameView extends View {

	Game game;
    int h,w; //used for storing our height and width of the view

	public void setGame(Game game)
	{
		this.game = game;
	}


	/* The next 3 constructors are needed for the Android view system,
	when we have a custom view.
	 */
	public GameView(Context context) {
		super(context);

	}

	public GameView(Context context, AttributeSet attrs) {
		super(context,attrs);
	}


	public GameView(Context context, AttributeSet attrs, int defStyleAttr)
	{
		super(context, attrs, defStyleAttr);
	}

	//In the onDraw we put all our code that should be
	//drawn whenever we update the screen.
	@Override
	protected void onDraw(Canvas canvas) {
		if (game.isGameOver()) {
			Toast.makeText(this.getContext(),"GAME OVER!",Toast.LENGTH_LONG).show();
		} else {
			//Here we get the height and weight
			h = canvas.getHeight();
			w = canvas.getWidth();
			//update the size for the canvas to the game.
			game.setSize(h,w);
//		Log.d("GAMEVIEW","h = "+h+", w = "+w);

			//Making a new paint object
			canvas.drawColor(Color.WHITE); //clear entire canvas to white color

			if (!game.areCoinsInitialized()) {
				game.initializeCoins();
			}

			Paint paint = new Paint();
			paint.setColor(Color.YELLOW);
			//TODO loop through the list of goldcoins and draw them.
//			Log.d("Number of coins: ", "" + game.getCoins().size());
			ArrayList<GoldCoin> coinsLeft = game.getCoins();
			if (coinsLeft.size() > 0) {
				for (GoldCoin gc : coinsLeft) {
					int drawX = gc.getLocation().x * game.getGridRatio();
					int drawY = gc.getLocation().y * game.getGridRatio();
					if (!gc.isTaken()) {
						canvas.drawCircle(drawX + 50, drawY + 50, 20, paint);
					}
				}
			}

			ArrayList<Ghost> enemies = game.getEnemies();
			for (Ghost ghost : enemies) {
				canvas.drawBitmap(ghost.getGhostBitmap(), ghost.location.x, ghost.location.y, paint);
			}

			//draw the pacman_right
			Location pacmanLocation = game.getPacmanLocation();
			canvas.drawBitmap(game.getPacBitmap(), pacmanLocation.x, pacmanLocation.y, paint);
//		canvas.drawBitmap(game.getPacBitmap(), null, new RectF(game.getPacx(), game.getPacy(), game.getPacx() + 80, game.getPacy() + 80), paint);
		}
		super.onDraw(canvas);
	}

}
