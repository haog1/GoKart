import org.newdawn.slick.SlickException;

public class Dog extends Kart{
		
	private static final double LARGER_ACC = 1.1 * NORMAL_ACC;
	private static final double SMALLER_ACC = 0.9 * NORMAL_ACC;
	
	private static int stackTimer;
	
	public Dog(double x, double y, Angle angle, String img_url) 
			throws SlickException {		
		super(x, y, angle, img_url);
		acceleration = NORMAL_ACC;
		stackTimer = STACK_TIME;

	}

	
	@Override
	public void update(double rotate_dir, double move_dir, World world, Boolean use_item) throws SlickException {

		if(index  < 48){
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
				//System.out.println("decrement");
				stackTimer--;
			}
			
			// check the distance between current position and next waypoint
			if(getDist(this.x, world.getWaypoints()[index][0], 
					this.y, world.getWaypoints()[index][1]) <= LARGER_RANGE){
				index += 1;
			}else{
				
				// check the relative position between itself and player
				// and therefore change the acceleration
				if(isBehindPlayer(world.getPlayer())){
					acceleration = LARGER_ACC;
				}else{
					acceleration = SMALLER_ACC;
				}
				
				move(rotate_dir, rotate_speed, world, 
						world.getWaypoints()[index][0], world.getWaypoints()[index][1]);
			}

		}
	}
	
	public boolean isBehindPlayer(Player player){
		if(player.getY() < this.y){
			return true;
		}
		return false;
	}	
}

