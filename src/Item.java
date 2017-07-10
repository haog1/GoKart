import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public abstract class Item extends GameObject{
		
	public Item(double x, double y, String img_url) 
			throws SlickException{

		this.img = (new Image(Game.ASSETS_PATH + img_url));
		this.x = x;
		this.y = y;
	
	}
	
	public void render(Graphics g, int cam_x, int cam_y) {
        int screen_x = (int) (this.x - cam_x);
        int screen_y = (int) (this.y - cam_y);
		img.drawCentered(screen_x, screen_y);
		
	}

	// every item has an effect when it is applyed
	public abstract void effect(World world) 
			throws SlickException;
	
}
