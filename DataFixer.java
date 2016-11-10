import java.io.*;
import java.util.*;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;


public class DataFixer {

	static File dir;
	public static ArrayList<String> tradableDates = new ArrayList<String>();     // ArrayList containing all possible tradable dates


	public DataFixer(File dir) throws IOException {
		DataFixer.dir = dir;
		DataFixer.initTradableDates();
	}


	/*
	 * Constructs ArrayList of all possible tradable dates from 1985 - 2070
	 */
	public static void initTradableDates () throws IOException {

		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Enter the path to TradableDates.txt");
		String text = in.readLine();
		File datesFile = new File(text);   // file containing list of tradable dates
		Scanner s = new Scanner(datesFile);

		while (s.hasNext()) {
			tradableDates.add(s.next());
		}
	}


	/*
	 * Loops through each file or directory in the initial directory and passes the path and name to fixData
	 */
	public void fixFiles() throws IOException {

		if (dir.isDirectory()) {

			File[] items = dir.listFiles();

			for (File item : items) {

				fixData(item.getPath(), item.getName());

			}
		}
	}


	/*
	 * Returns true if a given string is an integer and false otherwise
	 */
	public static boolean isInteger(String str) {
		return str.matches("^-?[0-9]+(\\.[0-9]+)?$");
	}


	/*
	 * Generates a new fixed .csv file that removes false data points (recorded stock information on non-tradable days) and fills in 
	 * missing data (from days that were tradable and yet no data was recorded) with the previous day's stock information
	 */
	public static void fixData (String path, String name) throws IOException {

		CSVReader iterator = new CSVReader(new FileReader(path));           // iterates through the file


		ArrayList<String[]> lines = new ArrayList<String[]>();
		String [] nextLine;                      // nextLine is an array of string values from the line
		int pos;                                 // position of recorded stock date on tradableDates


		lines.add(iterator.readNext());          // copies the headlines of data onto the new .csv file

		/*
		 * Determines the position on tradableDates of the starting date of the stock
		 */

		if ((nextLine = iterator.readNext()) != null) {

			pos = tradableDates.indexOf(nextLine[0]) + 1;
			lines.add(nextLine);

		} else pos = 0;


		/*
		 * Iterates through the file line by line and writes the corrected line into the new .csv file
		 */

		while ((nextLine = iterator.readNext()) != null) {

			if ((isInteger(nextLine[0].substring(0,4))) && (Integer.parseInt(nextLine[0].substring(0,4)) >= 1985 )) { 
				// checks that data is stock information after 1984

				if (tradableDates.get(pos).equals((nextLine[0]))) { // checks if any tradable dates were missed

					lines.add(nextLine);
					pos++;

				} else  if (tradableDates.contains(nextLine[0])) { // checks if the day-specific data was reported on a tradable day


					lines.add(nextLine);
					pos++;
					String missingDate = (String) tradableDates.get(pos);
					System.out.println(name + " " + missingDate + " is missing");

					/*
					String copy = nextLine[0];
					nextLine[0] = missingDate;

					lines.add(nextLine);

					String[] useThis = new String[7];
					useThis[0] = copy;
					for (int i = 1; i < 7; i++) {
						useThis[i] = nextLine[i];
					}

					lines.add(useThis);
					
					pos+=2;
					*/

				} else {
					System.out.println("This data is made up:" + " " + name + " " + nextLine[0]);
				}
			}
		}

		CSVWriter writer = new CSVWriter(new FileWriter(path, false), ',', CSVWriter.NO_QUOTE_CHARACTER);              // writes the fixed data onto a new .csv file
		writer.writeAll(lines);

		iterator.close();
		writer.close();

	}
}


