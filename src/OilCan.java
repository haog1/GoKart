import org.newdawn.slick.SlickException;

public class OilCan extends Item{


	private static final int CLOSE = 40;

	public OilCan(double x, double y, String img_url)
			throws SlickException {
		super(x, y, img_url);	

	}


	@Override
	public void effect(World world) 
			throws SlickException {
		// get the angle
		Angle rotateAngle = world.getPlayer().getAngle();
		// get the place
		double xPos = world.getPlayer().getX() - rotateAngle.getXComponent(CLOSE);
		double yPos = world.getPlayer().getY() - rotateAngle.getYComponent(CLOSE);

		// check if the position can place a oil slick or not
		if (!world.blockingAt((int) xPos, (int) yPos)){
			world.getOilSlick().add(new OilSlick(xPos, yPos, OILSLICK));
		}
	}



}