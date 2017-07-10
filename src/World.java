/* SWEN20003 Object Oriented Software Development
 * Kart Racing Game
 * Sample Solution
 * Author: Matt Giuca <mgiuca>
 */

import java.util.ArrayList;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.TiledMap;

/** Represents the entire game world.
 * (Designed to be instantiated just once for the whole game).
 */
public class World
{
	/** The world map (two dimensional grid of tiles).
	 * The concept of tiles is a private implementation detail to World. All
	 * public methods deal pixels, not tiles.
	 */
	private TiledMap map;	
	
	/** The panel bar */
	private Panel panel;	
	private Player player;
	private ReadData dataReader;
	
	//private OilCan oilCan;
	private ArrayList<Kart> karts;
	private ArrayList<Item> itemList = new ArrayList<Item>();	
	private ArrayList<OilSlick> oilSlick = new ArrayList<OilSlick>();
	private ArrayList<TomatoProj> tomatoProj = new ArrayList<TomatoProj>();
	
	private int gameStart = 0;
	private int gameEnd = 0;
	private int finalRank = 1;

	/** waypoints variable */
	private int waypoints[][];	
	
	/**item pos, waypoints */	
	public final String LIST = "data/items.txt";
	public final String LIST2 = "data/waypoints.txt";
	/** karts */
	private static final String DOG = "/karts/dog.png";
	private static final String ELEPHANT = "/karts/elephant.png";
	private static final String OCTOPUS = "/karts/octopus.png";
	private static final String DONKEY = "/karts/donkey.png";

	/** items */
	public static final String OILCAN = "/items/oilcan.png";
	public static final String BOOST = "/items/boost.png";
	public static final String TOMATO = "/items/tomato.png";


	/**
	 * Create a new World object. 
	 * @throws Exception
	 */
	public World()
			throws Exception
	{
		this.setMap(new TiledMap(Game.ASSETS_PATH + "/map.tmx",
				Game.ASSETS_PATH));
		this.panel = new Panel();

		this.dataReader = new ReadData();

		this.player = new Player(1332, 13086, Angle.fromDegrees(0), DONKEY);
		
		// Initialize AI 
		karts = new ArrayList<Kart>();

		this.karts.add(new Elephant(1260, 13086, Angle.fromDegrees(0), ELEPHANT));
		this.karts.add(new Dog(1404, 13086, Angle.fromDegrees(0), DOG));
		this.karts.add(new Octopus(1476, 13086, Angle.fromDegrees(0), OCTOPUS));

		// Initialize items
		itemList = dataReader.readInput(this);

		// Initialize and read way points
		waypoints = dataReader.readWayPoints(this);
	}

	/**
	 *  Update the game state for a frame.
	 * @param rotate_dir The player's direction of rotation
	 *      (-1 for anti-clockwise, 1 for clockwise, or 0).
	 * @param move_dir The player's movement in the car's axis (-1, 0 or 1).
	 * @param use_item check if the item is used by pressing control key
	 * @param is_drift check if user wants to drif the car by pressing shift key
	 * @param command check if the game can start or not by pressing Q
	 * @throws SlickException
	 */
	public void update(double rotate_dir, double move_dir, Boolean use_item, Boolean is_drift, char command)
			throws SlickException
	{
		if(getGameStart() == 1){
			//update karts
			for(Kart car: karts){
				car.update(rotate_dir, move_dir, this, use_item);

			}
			
			// check if the tomato is used 
			// then remove it from arraylist
			if(tomatoProj != null){
				for(TomatoProj tp: tomatoProj){
					tp.update(this);
					if(tp.isHit()){
						tomatoProj.remove(tp);
						// for loop need to be break
						// since the syntax of arraylist
						// does not allow to keep iterate
						break;
					}
				}
			}
						
			// update the player last
			this.player.update(rotate_dir, move_dir, this, use_item, is_drift);

		} else if( command == 'q') { //set the command so that game can start
			setGameStart(1);
		}
	}

	/** Render the entire screen, so it reflects the current game state.
	 * @param g The Slick graphics object, used for drawing.
	 */
	public void render(Graphics g)
			throws SlickException
	{	
		// Calculate the top-left corner of the screen, in pixels, relative to
		// the top-left corner of the map -- center the camera around the
		// player
		int cam_x = (int) this.player.getX() - (Game.SCREENWIDTH / 2);
		int cam_y = (int) this.player.getY() - (Game.SCREENHEIGHT / 2);

		// Calculate the camera location (in tiles) and offset (in pixels)
		int cam_tile_x = cam_x / this.getMap().getTileWidth();
		int cam_tile_y = cam_y / this.getMap().getTileHeight();
		int cam_offset_x = cam_x % this.getMap().getTileWidth();
		int cam_offset_y = cam_y % this.getMap().getTileHeight();

		// Render 24x18 tiles of the map to the screen, starting from the
		// camera location in tiles (rounded down). Begin drawing at a
		// negative offset relative to the screen, to ensure smooth scrolling.
		int screen_tilew = Game.SCREENWIDTH / this.getMap().getTileWidth() + 2;
		int screen_tileh = Game.SCREENHEIGHT / this.getMap().getTileHeight() + 2;

		// render map
		this.getMap().render(-cam_offset_x, -cam_offset_y, cam_tile_x, cam_tile_y,
				screen_tilew, screen_tileh);

		//oilCan.render(g, (int)this.getPlayer().getX(), (int)this.getPlayer().getY());
		
		//render items
		for(Item drops: itemList){
			drops.render(g, cam_x, cam_y);
		}
		
		
		// render oil slick
		if(oilSlick != null){
			for(OilSlick os: oilSlick){
				os.render(g, cam_x, cam_y);
			}
		}
		
		// render tomato projectle
		if(tomatoProj != null){
			for(TomatoProj tp: tomatoProj){
				tp.render(g, cam_x, cam_y);
			}
		}
		
		// render AI
		for(Kart car : karts){
			car.render(g, cam_x, cam_y);
		}
		
		
		// Render the player
		this.player.render(g, cam_x, cam_y);

		// render panel
		this.panel.render(g, player.readRanking(this), player.getItem(),player.timer, this);
	}

	/** Get the friction coefficient of a map location.
	 * @param x Map tile x coordinate (in pixels).
	 * @param y Map tile y coordinate (in pixels).
	 * @return Friction coefficient at that location.
	 */
	public double frictionAt(int x, int y)
	{
		int tile_x = x / this.getMap().getTileWidth();
		int tile_y = y / this.getMap().getTileHeight();
		int tileid = this.getMap().getTileId(tile_x, tile_y, 0);
		String friction = this.getMap().getTileProperty(tileid, "friction", null);
		return Double.parseDouble(friction);
	}

	/** Determines whether a particular map location blocks movement.
	 * @param x Map tile x coordinate (in pixels).
	 * @param y Map tile y coordinate (in pixels).
	 * @return true if the tile at that location blocks movement.
	 */
	public boolean blockingAt(int x, int y)
	{
		return frictionAt(x, y) >= 1;
	}


	/**
	 * @return the karts
	 */
	public ArrayList<Kart> getKarts() {
		return karts;
	}

	
	/**
	 * @return the player
	 */
	public Player getPlayer() {
		return player;
	}

	/** Get the width of the game world in pixels. */
	public int getWidth()
	{
		return this.getMap().getWidth() * this.getMap().getTileWidth();
	}

	/** Get the height of the game world in pixels. */
	public int getHeight()
	{
		return this.getMap().getHeight() * this.getMap().getTileHeight();
	}

	/**
	 * @return the way points
	 */
	public int[][] getWaypoints() {
		return waypoints;
	}


	/**
	 * @return the itemList
	 */
	public ArrayList<Item> getItemList() {
		return itemList;
	}


	/**
	 * @param itemList the itemList to set
	 */
	public void setItemList(ArrayList<Item> itemList) {
		this.itemList = itemList;
	}


	/**
	 * @return the oilSlick
	 */
	public ArrayList<OilSlick> getOilSlick() {
		return oilSlick;
	}


	/**
	 * @param oilSlick the oilSlick to set
	 */
	public void setOilSlick(ArrayList<OilSlick> oilSlick) {
		this.oilSlick = oilSlick;
	}


	/**
	 * @return the tomatoProj
	 */
	public ArrayList<TomatoProj> getTomatoProj() {
		return tomatoProj;
	}


	/**
	 * @param tomatoProj the tomatoProj to set
	 */
	public void setTomatoProj(ArrayList<TomatoProj> tomatoProj) {
		this.tomatoProj = tomatoProj;
	}


	/**
	 * @return the map
	 */
	public TiledMap getMap() {
		return map;
	}


	/**
	 * @param map the map to set
	 */
	public void setMap(TiledMap map) {
		this.map = map;
	}


	/**
	 * @return the gameStart
	 */
	public int getGameStart() {
		return gameStart;
	}


	/**
	 * @param gameStart the gameStart to set
	 */
	public void setGameStart(int gameStart) {
		this.gameStart = gameStart;
	}


	/**
	 * @return the gameEnd
	 */
	public int getGameEnd() {
		return gameEnd;
	}


	/**
	 * @param gameEnd the gameEnd to set
	 */
	public void setGameEnd(int gameEnd) {
		this.gameEnd = gameEnd;
	}


	/**
	 * @return the finalRank
	 */
	public int getFinalRank() {
		return finalRank;
	}


	/**
	 * @param finalRank the finalRank to set
	 */
	public void setFinalRank(int finalRank) {
		this.finalRank = finalRank;
	}

}
