/*
 File: CSVParser.java**
 Description: Class for an object that can parse a CSV file and convert it to JSON format.
 Created: 6/5/20
 Author: Tim Chester
 email: chestert@student.vvc.edu
*/

import java.util.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class CSVParser 
{
	Map<String, Map<String, ArrayList<String>>> code_map; //The map all data from a CSV file is put into
	
	ArrayList<String> headers; //Stores the headers for a CSV file
	
	public CSVParser() //Default constructor, initializes code_map
	{
		code_map = new LinkedHashMap<>();
	}
	
	/**
	*Initializes the maps
	*@param line used to set the 2 dimensional LinkedHashMap and ArrayList
	*/
	public void set_map(String line)
	{
		String code, date;
		
		/*
		 * These lines split the line of data and put it into an ArrayList
		 */
		String[] temp;
		temp = line.split(",");
		List<String> h = new ArrayList<String>(Arrays.asList(temp));
		ArrayList<String> alist = (ArrayList<String>) h; //alist is used to hold the data from the line
		
		code = alist.get(0); //gets the iso_code
		date = alist.get(3); //gets the date
		
		if (!code_map.containsKey(code)) //if the code does not exist in the map already...
		{
			Map<String, ArrayList<String>> temp_map = new LinkedHashMap<>();
			temp_map.put(date, alist);
			
			code_map.put(code, temp_map); //a new iso_code entry is put into the map
		}
		else
		{
			code_map.get(code).put(date, alist); //If the code exists in the map already, a new date is added in the second map
		}
	}
	
	/**
	*Reads a CSV file
	*@param f is read from
	*/
	public void read_records(File f)
	{
		try
		{
			FileReader fread = new FileReader(f);
			BufferedReader bread = new BufferedReader(fread); 
			
			String line;
			String[] temp;
			line = bread.readLine();
			
			temp = line.split(","); //Splits the line and puts it into a String array
			List<String> h = new ArrayList<String>(Arrays.asList(temp)); //puts the String array into a list of Strings
			headers = (ArrayList<String>) h; //puts the list of Strings into the headers member ArrayList
			
			while((line = bread.readLine()) != null) //While the reader has not reached the end of the file
			{
				if (!line.isEmpty())
				{
					set_map(line); //Sets the map with the data
				}
			}			
			bread.close();
		}
		catch (IOException ioe)
		{
			System.out.println("Could not read file.");
			ioe.printStackTrace();
		}
		
	}
	
	/**
	*Prints the code_map in JSON format
	*/
	public void print_records()
	{
		for (String key : code_map.keySet()) //Iterates through first map
		{
			System.out.println("\"" + key + "\"" + " :  {");
			for (Map.Entry<String, ArrayList<String>> entry : code_map.get(key).entrySet()) //iterates through second map
			{
				System.out.println("    \"" + entry.getKey() + "\" :  {"); 
				for (int i = 0; i < entry.getValue().size(); i++) //iterates through the ArrayList of data in the second map
				{
					System.out.println("        \"" + headers.get(i) + "\" : \"" + entry.getValue().get(i) + "\",");
				}
				System.out.println("    },");
			}
			System.out.println("},");
		}
	}
	
	/**
	 * For extra credit. Allows the user to search for a country, using the iso_code, and specific date.
	 */
	public void search_interface()
	{
		char ans;
		String iso_code, date;
		do
		{
			System.out.println("Would you like to search for a specific country and date? (type y to proceed, and anything else to quit) ");
			Scanner in = new Scanner(System.in);
			ans = in.next().charAt(0);
			in.nextLine(); //clears the line
			if (ans == 'y')
			{
				System.out.println("Enter the country code/iso code. (must be in all capital letters)");
				iso_code = in.nextLine();

				if (code_map.containsKey(iso_code))
				{
					System.out.println("Enter the date. (must be in YYYY-MM-DD format)");
					date = in.nextLine();

					if (code_map.get(iso_code).containsKey(date))
					{
						print_search(iso_code, date);
					}
					else
					{
						System.out.println("Date not found.");
					}
				}
				else
				{
					
					System.out.println("Code not found");
				}
			}
		}
		while (ans == 'y');
	}
	
	/**
	* For extra credit. Prints the data tied to the country and date.
	* @param iso_code the iso_code, which is the country, the user searched for
	* @param date the date the user searched for
	*/
	public void print_search(String iso_code, String date)
	{
		System.out.println("\"" + iso_code + "\" :  {");
		System.out.println("    \"" + date + "\" :  {");
		
		ArrayList<String> temp = code_map.get(iso_code).get(date); //Temporary ArrayList to print data
		for (int i = 0; i < temp.size(); i++)
		{
			System.out.println("        \"" + headers.get(i) + "\" : \"" + temp.get(i) + "\",");
		}
		System.out.println("    },");
		System.out.println("},");
	}
}
