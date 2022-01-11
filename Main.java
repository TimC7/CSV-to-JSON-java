/*
 File: Main.java
 Description: Reads a CSV file and converts it to JSON format.
 			For extra credit, the program also allows the user to search for a specific country and date, and then prints data pertaining to country and date.
 Created: 6/5/20
 Author: Tim Chester
 email: chestert@student.vvc.edu
*/

import java.io.File;

public class Main
{
   public static void main(String[] args)
   {
	   CSVParser data = new CSVParser();
	   
	   File dataFile = new File("owid_covid_data.csv");
	   
	   data.read_records(dataFile);
	   data.print_records();
	   data.search_interface();
   }
}

