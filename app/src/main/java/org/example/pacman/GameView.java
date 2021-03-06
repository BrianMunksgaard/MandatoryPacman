package org.example.pacman;

import android.content.Context;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

public class GameView extends View {

	/*
	 * Reference to the game controller.
	 */
	Game game;

	/*
	 * The height and width of the canvas/the game view.
	 */
    int h,w;

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

	/**
	 * Associate the game view vith the game controller.
	 * @param game
	 */
	public void setGame(Game game)
	{
		this.game = game;
	}

	//In the onDraw we put all our code that should be
	//drawn whenever we update the screen.
	@Override
	protected void onDraw(Canvas canvas) {
		if (!game.isGameInitialized()) {
			//Here we get the height and weight
			h = canvas.getHeight();
			w = canvas.getWidth();
			//update the size for the canvas to the game.
			game.setSize(h,w);

			game.initializeGame();
		}

		//Making a new paint object
		canvas.drawColor(Color.WHITE); //clear entire canvas to white color

		Paint paint = new Paint();

		Node[][] gameGrid = game.getGameGrid();
		for (int height = 0; height < gameGrid.length; height++) {
			for (int width = 0; width < gameGrid[height].length; width++) {
				Node node = gameGrid[height][width];
				if (node.isObstructed()) {
					Wall wall = node.getWall();
					int drawX = wall.getLocation().pixelX;
					int drawY = wall.getLocation().pixelY;
					paint.setColor(Color.BLUE);
					paint.setStrokeWidth(100);
					canvas.drawLine(drawX + 50, drawY, drawX + 50, drawY + 100, paint);
				} else if (node.hasCoin()) {
					GoldCoin gc = node.getCoin();
					int drawX = gc.getLocation().pixelX;
					int drawY = gc.getLocation().pixelY;
					paint.setColor(Color.YELLOW);
					canvas.drawCircle(drawX + 50, drawY + 50, 20, paint);
				}
			}
		}

		// INFO The reason Pacman and Enemies are not drawn in the for-loop is due to layering
		// when drawing. The last item draw is going to be on top of the others.
		// draw the Pacman
		Location pacmanLocation = game.getPacmanLocation();
		canvas.drawBitmap(game.getPacBitmap(), pacmanLocation.pixelX, pacmanLocation.pixelY, paint);

		// Draw enemies
		ArrayList<Ghost> enemies = game.getEnemies();
		for (Ghost ghost : enemies) {
			canvas.drawBitmap(ghost.getCharacterBitmap(), ghost.getLocation().pixelX, ghost.getLocation().pixelY, paint);
		}

		if (game.isGameOver()) {
			Toast.makeText(this.getContext(), "GAME OVER!", Toast.LENGTH_LONG).show();
		}

		super.onDraw(canvas);
	}

}
