import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public abstract class GameObject{
	
	/** objects */
	public static final String OILSLICK = "/items/oilslick.png"; 
	public static final String TOMATOPROJ = "/items/tomato-projectile.png";
	
	public Image img; // image variable
	public double x; // car's x position
	public double y; // car's y position
	
	/**
	 * This function only provide a frame and need to be finished
	 * according to different needs 
	 * @param g
	 * @param cam_x
	 * @param cam_y
	 */
	public abstract void render(Graphics g, int cam_x, int cam_y);

	/**
	 * this function provides update function for its base classes
	 * since i cannot make it abstract, so i just make it empty
	 * @param world
	 * @throws SlickException
	 */
	public void update(World world) throws SlickException {
		// TODO Auto-generated method stub
		
	}
	
}