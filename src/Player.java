/* SWEN20003 Object Oriented Software Development
 * Kart Racing Game
 * Sample Solution
 * Author: Matt Giuca <mgiuca>
 */

import org.newdawn.slick.SlickException;

/** The player's kart (Donkey).
 */
public class Player extends Character
{

	/** acceleration while the player is driving, in px/ms^2. */

	private static final int RANGE = 40;

	public int timer = 0;

	private Item item;

	private int boostTime;


	public Player(double x, double y, Angle angle, String img_url) 
			throws SlickException {
		super(x, y, angle, img_url);

		boostTime = 0; 
		acceleration = NORMAL_ACC;
		rotate_speed = NORMAL_ROT;

	}

	/** Update the player for a frame.
	 * Adjusts the player's angle and velocity based on input, and updates the
	 * player's position. Prevents the player from entering a blocking tile.
	 * @param rotate_dir The player's direction of rotation
	 *      (-1 for anti-clockwise, 1 for clockwise, or 0).
	 * @param move_dir The player's movement in the car's axis (-1, 0 or 1).
	 * @param delta Time passed since last frame (milliseconds).
	 * @param world The world the player is on (to get friction and blocking).
	 * @throws SlickException 
	 */
	public void update(double rotate_dir, double move_dir,
			World world, Boolean use_item, Boolean is_drift) throws SlickException
	{	

		if(this.getY() > 1026){
			// game timer
			timer ++;

		}else{

			if(this.getY() < 200){
				velocity = 0;
			}
			// player lose control when game is finished
			world.setGameEnd(1);
			move_dir = 0;
			rotate_dir = 0;
		}

		if(use_item && item != null){
			// apply item effect if the key is pressed
			item.effect(world);
			item = null;
		}

		// boost effect on player
		if(boostTime > 0){
			Boost();
			move_dir =  1;
		}else{
			acceleration = NORMAL_ACC;
		}

		// oil slick effect on player
		if(getSpinningTime() > 0){
			rotate_dir = 1;
			move_dir = 0;
			rotate_speed = NORMAL_ROT * 2;
			setSpinningTime(getSpinningTime()-1);
		} else{
			rotate_speed = NORMAL_ROT;
		}


		// calculate speed
		this.velocity += acceleration * move_dir;

		// the last two params are just for the function to be called correctlly
		// do not have any meaning there
		move(rotate_dir, rotate_speed, world, is_drift);

		// checking distance between player and items
		for(Item drops : world.getItemList()){
			if(getDist(drops.x, this.getX(), drops.y, this.getY()) < RANGE){
				item = drops;
				world.getItemList().remove(drops);
				break;
			}
		}

		// call oil functions, written in Character.java
		checkOilSlick(world);
	}

	public void move(double rotate_dir, double rotate_speed, World world, Boolean is_drift ){

		double temp = rotate_dir;
		int changeSpeed = 0;

		if(is_drift){
			rotate_dir = 0;			
			if(changeSpeed == 0){
				changeSpeed = 1;
			}	

		}else{
			rotate_dir = temp;
		}

		// Modify the player's angle
		Angle rotateamount = new Angle(rotate_speed * temp);
		this.setAngle(this.getAngle().add(rotateamount));

		Angle rotateAmount = new Angle(rotate_speed * rotate_dir);
		this.setRealAngle(this.getRealAngle().add(rotateAmount));

		// Determine the friction of the current location
		double friction = world.frictionAt((int) this.getX(), (int) this.getY());
		// Then, reduce due to friction (this has the effect of creating a
		// maximum velocity for a given coefficient of friction and
		// acceleration)

		this.velocity *= 1 - friction;

		// Modify the position, based on velocity
		// Calculate the amount to move in each direction
		double amount = this.velocity;
		// Compute the next position, but don't move there yet
		double next_x = this.getX() + this.getRealAngle().getXComponent(amount);
		double next_y = this.getY() + this.getRealAngle().getYComponent(amount);
		
		
		if(!is_drift){
			setRealAngle(getAngle());
			if(changeSpeed == 1){
				velocity = 0;
				changeSpeed = 0;
				
			}
		}

		// If the intended destination is a blocking tile, do not move there
		// (crash) -- instead, set velocity to 0

		if (world.blockingAt((int) next_x, (int) next_y) 
				|| checkCollision(world, next_x, next_y))
		{

			this.velocity = 0;
		}
		else
		{

			this.x = (next_x);
			this.y = (next_y);
		}
	}


	private void Boost(){
		acceleration = 0.0008;
		// boost time decrease per ms
		boostTime--;

	}

	@Override
	public boolean checkCollision(World world, double next_x, double next_y) {
		for(Kart car: world.getKarts()){
			if(getDist(car.x, next_x, car.y, next_y) < RANGE){
				return true;
			}
		}

		return false;
	}

	// read player's current ranking
	public int readRanking(World world){
		int rank = 1;
		// using arraylist to check the y position
		for(Kart car : world.getKarts()){
			if(car.y < this.getY()){
				rank += 1;
			}
		}
		if(this.getX() <= 1026){
			world.setFinalRank(rank);
		}
		return rank;
	}


	/**
	 * @return the item
	 */
	public Item getItem() {
		return item;
	}
	
	/** The X coordinate of the player (pixels). */
	public double getX()
	{
		return this.x;
	}

	/** The Y coordinate of the player (pixels). */
	public double getY()
	{
		return this.y;
	}

	/** The player's current velocity, in the direction the player is facing
	 * (px/ms).
	 */
	public double getVelocity()
	{
		return this.velocity;
	}


	/**
	 * @param boostTime the boostTime to set
	 */
	public void setBoostTime(int boostTime) {
		this.boostTime = boostTime;
	}

}
