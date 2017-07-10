import org.newdawn.slick.SlickException;

public class Boost extends Item{
	
	private static final int TIME = 3000;

	public Boost(double x, double y, String img_url) throws SlickException {
		super(x, y, img_url);

	}

	@Override
	public void effect(World world) {
		world.getPlayer().setBoostTime(TIME);
		
	}
}