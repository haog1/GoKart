import org.newdawn.slick.SlickException;

public class Octopus extends Kart{
	
	private int stackTimer;
	private static final int END = 330;
	
	public Octopus(double x, double y, Angle angle, String img_url) 
			throws SlickException {
		super(x, y, angle, img_url);
		stackTimer = STACK_TIME;
	}

	@Override
	public void update(double rotate_dir, double move_dir,
			World world, Boolean use_item) throws SlickException {

		if(index  < 48){
			if(this.y < END){
				velocity = 0;
				rotate_dir = 0;
			}
			
			// check and apply tomato or oil can effect
			checkOilSlick(world);
			checkTomatoProj(world);

			int num = 0;
			
			// check if the kart is crahsed or not
			// sometimes it is blocke by wall if it is loosed by player 
			// due too a fast acceleration
			if(stackTimer == 0){
				stackTimer = STACK_TIME;
				if(this.x - getAvoidStackX() < STACK_LIMIT && this.y - getAvoidStackY() < STACK_LIMIT){
					num = recover(this.x ,this.y, world);
					
					// if find a nearest point
					// then reset it
					if(num != -1){
						this.x = world.getWaypoints()[num][0];
						this.y = world.getWaypoints()[num][1];

						setAvoidStackX(this.x);
						setAvoidStackY(this.y);
					}
				}else{
					setAvoidStackX(this.x);
					setAvoidStackY(this.y);
				}
			}else{
				stackTimer--;
			}

			// get distance between octoupus and player
			double distToPlayer = getDist(this.x, world.getPlayer().getX(),
					this.y, world.getPlayer().getY());


			// if player is near octopus
			if(SMALLER_RANGE <= distToPlayer && distToPlayer <=  LARGER_RANGE){
				move(rotate_dir, rotate_speed, world,
						world.getPlayer().getX(), world.getPlayer().getY());
			}
			else 
				// if near octopus waypoint
			{
				if(getDist(this.x, world.getWaypoints()[index][0], 
						this.y, world.getWaypoints()[index][1]) <= LARGER_RANGE){
					index += 1;
				}else{
					move(rotate_dir, rotate_speed, world, 
							world.getWaypoints()[index][0],
							world.getWaypoints()[index][1]);
				}
			}
		}
	}

}

