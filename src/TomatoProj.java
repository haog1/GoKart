import org.newdawn.slick.SlickException;

public class TomatoProj extends Item{
	private Angle angle;
	private static final double speed = 1.7;
	private boolean hit;

	public TomatoProj(double x, double y, String img_url, Angle angle) 
			throws SlickException {
		super(x, y, img_url);
		this.angle = angle;
		hit = false;
	}

	public void effect(Kart kart)
			throws SlickException {
		kart.setSpinningTime(700);
		
	}

	@Override
	public void update(World world) {

		double amount = speed;

		double next_x = this.x + angle.getXComponent(amount);
		double next_y = this.y + angle.getYComponent(amount);

		if (world.blockingAt((int) next_x, (int) next_y))
		{

			hit = true;
		}else {
			this.x = next_x;
			this.y = next_y;
		}
		
	}

	/**
	 * @return the hit
	 */
	public boolean isHit() {
		return hit;
	}

	@Override
	public void effect(World world) 
			throws SlickException {		
	}
	
}