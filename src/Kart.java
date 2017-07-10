import org.newdawn.slick.SlickException;

public abstract class Kart extends Character {

	// for octopus to trace the player in a certain range
	public final int SMALLER_RANGE = 100;
	public final int LARGER_RANGE = 250;

	public final int STACK_TIME = 4000;
	public static final double STACK_LIMIT = Math.sqrt(40);

	private static final int MAXLEN = 100000;
	private static final int LIMIT = 48;
	private static final int END = 100;
	private final int KARTRANGE = 40;

	// this is tracing for waypoints
	public int index;

	// this two is for reset the pos, if the kart is blocked
	private double prevX;
	private double prevY;

	//this pair of coords are for avoiding karts stack at the corner
	private double avoidStackX;
	private double avoidStackY;

	private int stackTimer;



	public double playerYPos;

	
	/**
	 * Constructor
	 * @param x
	 * @param y
	 * @param angle
	 * @param img_url
	 * @throws SlickException
	 */
	public Kart(double x, double y, Angle angle, String img_url) 
			throws SlickException {

		super(x, y, angle, img_url);
		index = 0;
		setSpinningTime(0);


	}
	public abstract void update(double rotate_dir, double move_dir,
			World world, Boolean use_item) 
					throws SlickException;


    /**
     * This function tells player or karts how to move and where to move
     * @param rotate_dir
     * @param rotate_speed
     * @param world
     * @param targetX: target position
     * @param targetY
     */
	public void move(double rotate_dir, double rotate_speed, World world,
			double targetX, double targetY){
		Angle rotateamount;
		double angle;
		
		if(this.y <= END){
			velocity = 0;
		}
		
		if(getSpinningTime() > 0){
			rotate_dir = 1;
			rotate_speed = NORMAL_ROT * 2;
			setSpinningTime(getSpinningTime()-1);
			rotateamount = new Angle(rotate_speed * rotate_dir);

		} else{
			rotate_speed = NORMAL_ROT;
			velocity += acceleration;
			angle = changeRotation(index, world, this.x, this.y, rotate_dir,
					targetX, targetY);
			rotateamount = new Angle(angle);

		}

		this.setAngle(this.getAngle().add(rotateamount));

		// Determine the friction of the current location
		double friction = world.frictionAt((int) this.x, (int) this.y);

		// Then, reduce due to friction (this has the effect of creating a
		// maximum velocity for a given coefficient of friction and
		// acceleration)

		this.velocity *= 1 - friction;

		// Modify the position, based on velocity
		// Calculate the amount to move in each direction
		double amount = this.velocity;

		// Compute the next position, but don't move there yet
		double next_x = this.x + this.getAngle().getXComponent(amount);
		double next_y = this.y + this.getAngle().getYComponent(amount);

		// If the intended destination is a blocking tile, do not move there
		// (crash) -- instead, set velocity to 0
		if (world.blockingAt((int) next_x, (int) next_y) 
				|| checkCollision(world, next_x, next_y))
		{

			this.velocity = 0;
			this.x = prevX;
			this.y = prevY;
		}
		else
		{
			prevX = this.x;
			prevY = this.y;
			this.x = (next_x);
			this.y = (next_y);
		}	
	}

	/**
	 * 
	 * @param index: indicates the no. of way points
	 * @param world 
	 * @param x: current x pos
	 * @param y: current y pos
	 * @param rotate_dir
	 * @param targetX: indicate the x pos that the kart is going to rotate to
	 * @param targetY: .............y pos .....
	 * @return
	 */
	public double changeRotation(int index, World world, double x, double y,
			double rotate_dir, double targetX, double targetY){

		// the rotate amount
		Angle rotateAngle = Angle.fromCartesian(targetX - x, targetY - y);
		Angle rotateAmount = new Angle(this.getAngle().getRadians()
				- rotateAngle.getRadians());

		// check which direction is going to rotate
		if(rotateAmount.getDegrees() < 0){
			rotate_dir = 1;
		}else if(rotateAmount.getDegrees() > 0){
			rotate_dir = -1;
		}else{
			rotate_dir = 0;
		}
		return rotate_dir * rotate_speed;
	}

	@Override
	public boolean checkCollision(World world, double next_x, double next_y){

		if(getDist(world.getPlayer().getX(), next_x,
				world.getPlayer().getY(), next_y) < KARTRANGE){
			return true;
		}
		for(Kart car: world.getKarts()){
			if(car.equals((Kart)(this))){
				continue;
			}
			if(getDist(car.x, next_x, car.y, next_y) < KARTRANGE){
				return true;
			}
		}


		return false;
	}

	/**
	 * It's for reset the kart, if it is crashed or blocked more than 5 seconds
	 * @param recX: the current position
	 * @param recY
	 * @param world
	 * @return
	 */
	public int recover(double recX, double recY, World world){
		int index = 0, i = 0;
		double prevDist = MAXLEN;

		for(i = 0; i < LIMIT; i++){
			// find next nearest point that is behind the player
			if(recY < world.getWaypoints()[i][1] &&
					getDist(recX, world.getWaypoints()[i][0],
							recY, world.getWaypoints()[i][1]) < prevDist){

				// record the distance length
				prevDist = getDist(recX, world.getWaypoints()[i][0],
						recY, world.getWaypoints()[i][1]);
				// record the index number
				index = i;
			}
		}
		if(index == 0){
			return index;
		}else{
			return -1;
		}

	}

	
	/**
	 * In the arraylist of tomatoproj, apply the effec if someone is chosen
	 * @param world
	 * @throws SlickException
	 */
	public void checkTomatoProj(World world) throws SlickException{
		if(world.getTomatoProj() != null){
			for(TomatoProj tp: world.getTomatoProj()){
				if(getDist(this.x, tp.x, this.y, tp.y) < 40){
					tp.effect(this);
					world.getTomatoProj().remove(tp);
					break;
				}
			}
		}
	}

	/**
	 * @return the prevX
	 */
	public double getPrevX() {
		return prevX;
	}


	/**
	 * @param prevX the prevX to set
	 */
	public void setPrevX(double prevX) {
		this.prevX = prevX;
	}


	/**
	 * @return the prevY
	 */
	public double getPrevY() {
		return prevY;
	}


	/**
	 * @param prevY the prevY to set
	 */
	public void setPrevY(double prevY) {
		this.prevY = prevY;
	}


	/**
	 * @return the avoidStackX
	 */
	public double getAvoidStackX() {
		return avoidStackX;
	}


	/**
	 * @param avoidStackX the avoidStackX to set
	 */
	public void setAvoidStackX(double avoidStackX) {
		this.avoidStackX = avoidStackX;
	}


	/**
	 * @return the avoidStackY
	 */
	public double getAvoidStackY() {
		return avoidStackY;
	}


	/**
	 * @param avoidStackY the avoidStackY to set
	 */
	public void setAvoidStackY(double avoidStackY) {
		this.avoidStackY = avoidStackY;
	}


	/**
	 * @return the stackTimer
	 */
	public int getStackTimer() {
		return stackTimer;
	}


	/**
	 * @param stackTimer the stackTimer to set
	 */
	public void setStackTimer(int stackTimer) {
		this.stackTimer = stackTimer;
	}
	/**
	 * @return the end
	 */
	public static int getEnd() {
		return END;
	}	

}

