import org.newdawn.slick.SlickException;

public class OilSlick extends Item{
	
	public OilSlick(double x, double y, String img_url) 
			throws SlickException {
		super(x, y, img_url);

	}

	public void slick(Character c) throws SlickException {
		// spinning time is 700ms == 0.7 seconds
		c.setSpinningTime(700);
				
	}

	@Override
	public void effect(World world) throws SlickException {
		// TODO Auto-generated method stub
		
	}
	
}