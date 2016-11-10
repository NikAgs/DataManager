import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;


public class DataFlipper {

	static File dir;
	
	public DataFlipper (File dir) {
		DataFlipper.dir = dir;
	}
	
	/*
	 * Loops through each file or directory in the initial directory and passes the path and name to fixData
	 */
	public void flipFiles() throws IOException {

		if (dir.isDirectory()) {

			File[] items = dir.listFiles();

			for (File item : items) {

				flipData(item.getPath(), item.getName());

			}
		}
	}

	public static void flipData (String path, String name) throws IOException {

		CSVReader iterator = new CSVReader(new FileReader(path));           // iterates through the file
		CSVReader reader = new CSVReader(new FileReader(path));             // reads the file all at once
		

		
		ArrayList<String[]> lines = new ArrayList<String[]>();
		String [] nextLine;                      // nextLine is an array of string values from the line
		int top = -1;                             

		lines.add(iterator.readNext());    // copies the headlines of data onto the new .csv file

		int maxSize = reader.readAll().size();
		reader.close();
		String [][] stack = new String[maxSize][13];

		/*
		 * Iterates through the file line by line and pushes each nextLine array onto the stack
		 */
		while ((nextLine = iterator.readNext()) != null) {
			stack[++top] = nextLine;
		}


		/*
		 * Writes each nextLine on the stack to the new file
		 */
		while (top != -1) {
			lines.add(stack[top--]);
		}
		CSVWriter writer = new CSVWriter(new FileWriter(path, false), ',', CSVWriter.NO_QUOTE_CHARACTER);             // writes the fixed data onto a new .csv file
		writer.writeAll(lines);
		iterator.close();
		writer.close();

	}
}
