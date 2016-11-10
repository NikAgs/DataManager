import java.net.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.io.*;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;

public class DataUpdater  {

	static String[] lastLine = new String[7];
	static File dir;
    static boolean isCsvFile = false;
	
	public DataUpdater (File dir) {
		DataUpdater.dir = dir;
	}


	/*
	 * Loops through each file or directory in the initial directory
	 */
	public void updateFiles() throws IOException {

		if (dir.isDirectory()) {

			File[] items = dir.listFiles();

			for (File item : items) {

				String name = getName(item.getName());

				if (isCsvFile) {
					
					String [][] missingDates = missingDateRange(item.getPath());
					URL source = new URL(getURL(missingDates, name));
					updateFile(source, item.getPath(), name, lastLine[0]);
					
				}
			}
		}
	}


	/*
	 * Returns an array where the first element is an array of strings containing the initial date of the missing range
	 * and the second element is an array of strings containing today's date 
	 */
	private static String[][] missingDateRange(String path) throws IOException {

		String[] initial = new String [3];
		String[] today = new String[3];

		Calendar currentDate = Calendar.getInstance();                    // gets the current date
		SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd"); 
		String dateNow = formatter.format(currentDate.getTime());
		today[0] = (dateNow.substring(5,7));
		today[1] = (dateNow.substring(8,10));
		today[2] = (dateNow.substring(0,4));

		String [][] missingDates = new String [][] {initial,today};

		CSVReader reader = new CSVReader(new FileReader(path));           // iterates through the file
		String [] currentLine;                                            // currentLine is an array of string values from the line

		while ((currentLine = reader.readNext()) != null) 
		{
			lastLine = currentLine;
		}                                            

		initial[0] = (lastLine[0].substring(5,7));
		initial[1] = (lastLine[0].substring(8,10));
		initial[2] = (lastLine[0].substring(0,4));

		reader.close();
		return missingDates;
	}


	/*
	 * Returns the index of the company that is referenced by a given file name
	 */
	private static String getName(String name) {

		if (name.contains(".csv")) {
			isCsvFile = true;
			int period = name.indexOf(".csv");
			String returnThis = name.substring(0,period);
			return returnThis;
		} else {
			isCsvFile= false;
			return name;
		}
	}



	/*
	 * Uses the Yahoo! Finance API to generate a URL containing historical stock data from a given stock
	 * within a given range of dates
	 */
	private static String getURL(String[][] missingDates, String name) {
		String beginning = "http://ichart.yahoo.com/table.csv?s=";
		String initialMonth = Integer.toString(Integer.parseInt(missingDates[0][0]) - 1);     // accounts for Yahoo! Finance's indexed month format
		String todayMonth = Integer.toString(Integer.parseInt(missingDates[1][0]) - 1);

		String url = beginning.concat(name + "&a=" + initialMonth + "&b=" + missingDates[0][1] + "&c=" + missingDates[0][2]
				+ "&d=" + todayMonth + "&e=" + missingDates[1][1] + "&f=" + missingDates[1][2] + "&g=d&ignore=.csv");
		return url;

	}



	/*
	 * Re-writes a given file to include data up to the current data given the URL of a .csv formatted Yahoo! Finance webpage
	 * which contains the missing data
	 * 
	 * NOTE: If the URL is not found or there is no missing data, then the file will not be re-written
	 */
	private static void updateFile(URL source, String path, String name, String date) {

		try {

			CSVReader webReader = new CSVReader(new InputStreamReader(source.openStream()));
			CSVReader fileReader = new CSVReader(new FileReader(path));


			String [] nextLine;
			webReader.readNext();                         // skips the headline of the web data

			int top = -1;
			ArrayList<String []> stack = new ArrayList<String[]>();

			while (((nextLine = webReader.readNext()) != null) && (!nextLine[0].equals(date)))  {

				stack.add(nextLine);
				top++;

			}

			CSVWriter writer = new CSVWriter(new FileWriter(path, true), ',', CSVWriter.NO_QUOTE_CHARACTER); 
			while (top > -1) {
				writer.writeNext(stack.get(top));
				top--;
			}

			webReader.close();
			fileReader.close();
			writer.close();

		} catch (IOException e) {
			System.out.println(name + " is not found on Yahoo! Finance");

		}

	}

}
