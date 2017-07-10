import org.newdawn.slick.SlickException;

public class Tomato extends Item{	
	
	public Tomato(double x, double y, String img_url) throws SlickException {
		super(x, y, img_url);
		
	}
	

	@Override
	public void effect(World world) 
			throws SlickException {
		
		// get angle that needs rotate
		Angle rotateAngle = world.getPlayer().getAngle();
		// get starting position
		double x = world.getPlayer().getX();
		double y = world.getPlayer().getY();
		// shoot tomatoproj from TomatoProj.java
		world.getTomatoProj().add(new TomatoProj(x, y, TOMATOPROJ, rotateAngle));
		
	}
	
}