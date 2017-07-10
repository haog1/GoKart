import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public abstract class Character extends GameObject{
	
	public static final double NORMAL_ACC = 0.0005;	
	public static final double NORMAL_ROT = 0.004;

	public double acceleration;
	public double rotate_speed;

	private Angle angle;
	private Angle realAngle;
	public double velocity;
	
	private int spinningTime; // for tomato and oil effects
	
	
	/**
	 *  Constructor
	 * @param x coordinate
	 * @param y coordinate
	 * @param angle rotate_degree
	 * @param img_url image file
	 * @throws SlickException
	 */
	public Character(double x, double y, Angle angle, String img_url)
			throws SlickException
	{
		this.img = new Image(Game.ASSETS_PATH + img_url);
		this.x = (x);
		this.y = (y);
		this.angle = angle;
		this.realAngle = angle;
		this.velocity = 0;
		this.rotate_speed = NORMAL_ROT;
		this.acceleration = NORMAL_ACC;
	}
	
	/** Draw the player to the screen at the correct place.
     * @param g The current Graphics context.
     * @param cam_x Left edge of the screen in pixels, relative to map.
     * @param cam_y Top edge of the screen in pixels, relative to map.
     */
    public void render(Graphics g, int cam_x, int cam_y)
    {
        int screen_x = (int) (this.x - cam_x);
        int screen_y = (int) (this.y - cam_y);
        img.setRotation((float) this.angle.getDegrees());
        img.drawCentered(screen_x, screen_y);
    }
    
	/**
	 *  This funciton checks if karts or player will hill others
	 * @param world
	 * @param next_x
	 * @param next_y
	 * @return and then finally set velocity to zero
	 */
    public abstract boolean checkCollision(World world, double next_x, double next_y);
    
    
    /**
     * This function gets distance between any two points
     * @param x1 initial point
     * @param x2
     * @param y1 second point
     * @param y2
     * @return
     */
	public double getDist(double x1, double x2, double y1, double y2){
		return Math.sqrt((x1-x2) * (x1-x2) + (y1-y2) * (y1-y2));
	}
    
	
	/**
	 *  Check if oil can is used if player already had have one oil can
	 *  and it can effect every car, including player.
	 * @param world
	 * @throws SlickException
	 */
	public void checkOilSlick(World world) throws SlickException{
		if(world.getOilSlick() != null){
			for(OilSlick os: world.getOilSlick()){
				if(getDist(this.x, os.x, this.y, os.y) < 40){
					os.slick(this);
					world.getOilSlick().remove(os);
					break;
				}
			}
		}
	}
	/**
	 * @return the angle
	 */
	public Angle getAngle() {
		return angle;
	}
	
	/**
	 * @param angle the angle to set
	 */
	public void setAngle(Angle angle) {
		this.angle = angle;
	}

	/**
	 * @param velocity the velocity to set
	 */
	public void setVelocity(double velocity) {
		this.velocity = velocity;
	}

	/**
	 * @return the spinningTime
	 */
	public int getSpinningTime() {
		return spinningTime;
	}

	/**
	 * @param spinningTime the spinningTime to set
	 */
	public void setSpinningTime(int spinningTime) {
		this.spinningTime = spinningTime;
	}

	/**
	 * @return the realAngle
	 */
	public Angle getRealAngle() {
		return realAngle;
	}

	/**
	 * @param realAngle the realAngle to set
	 */
	public void setRealAngle(Angle realAngle) {
		this.realAngle = realAngle;
	}
	
	
	
	
}