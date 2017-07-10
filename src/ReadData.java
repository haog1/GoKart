import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.StringTokenizer;
import org.newdawn.slick.SlickException;

public class ReadData{
	private BufferedReader reader = null;
	private static final int FirNum = 48;
	private static final int SecNum = 2;

	// read item.txt
	public ArrayList<Item> readInput(World world) 
			throws SlickException, Exception{

		String itemType, lineRead;

		File file = new File(world.LIST);
		ArrayList<Item> itemList = new ArrayList<Item>();

		try{
			// buffer read to give memories
			reader = new BufferedReader(new FileReader(file));

			lineRead = reader.readLine();

			while(lineRead != null){
				// skip empy lines
				if (lineRead.isEmpty()){
					lineRead = reader.readLine();
					continue;
				}
				// using " " and "," as tokenizer
				StringTokenizer line = new StringTokenizer(lineRead, " ,");
				itemType = line.nextToken();
				if(itemType.contains("Oil")){
					// skip the word "can"
					line.nextToken(); 
					
				}

				// record each coordinates and add
				// them to its corresponding arraylist
				if(itemType.equals("Boost")){
					
					double x = Double.parseDouble(line.nextToken());
					double y = Double.parseDouble(line.nextToken());
					itemList.add(new Boost(x,y, World.BOOST));
				}else if(itemType.equals("Oil")){
					
					double x = Double.parseDouble(line.nextToken());
					double y = Double.parseDouble(line.nextToken());
					itemList.add(new OilCan(x,y, World.OILCAN));

				}else if(itemType.equals("Tomato")){
					
					double x = Double.parseDouble(line.nextToken());
					double y = Double.parseDouble(line.nextToken());
					itemList.add(new Tomato(x,y, World.TOMATO));

				}
				lineRead = reader.readLine();
			}
			reader.close();

		} 
		// error handing
		catch (FileNotFoundException exception)
		{
			System.out.println (world.LIST + " was not found");
		}
		return itemList;
	}
	
	/**
	 * read way points
 	 * basically, it's similar with previous function,
	 * but instead using double array to store all way points,
	 * since there no need to store strings
	 */
	public  int[][] readWayPoints(World world) 
			throws Exception{

		int waypts[][] = new int[FirNum][SecNum];
		String itemType, lineRead;
		File file = new File(world.LIST2);
		
		try{
			reader = new BufferedReader(new FileReader(file));
			lineRead = reader.readLine();
			int i = 0;
			while(lineRead != null){
				if(lineRead.isEmpty()){
					lineRead = reader.readLine();
					continue;
				}
				
				StringTokenizer line = new StringTokenizer(lineRead, ", ");
				itemType = line.nextToken();
				
				if(isInteger(itemType)){
					waypts[i][0] = Integer.parseInt(itemType);
					waypts[i][1] = Integer.parseInt(line.nextToken());
					i++;
				}
				lineRead = reader.readLine();
			}
			reader.close();
		}
		catch(FileNotFoundException exception)
		{
			System.out.println (world.LIST2 + " was not found");

		}
		
		return waypts;
	}
	
	/** 
	 * convert integer in string form to Integer
	 */
	public boolean isInteger(String str){
		try{
			Integer.parseInt(str);
			return true;
		}
		catch(Exception e){
			return false;
		}
	}
	
	
}