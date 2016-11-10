import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;


public class SingleFiler {
	static boolean isCsvFile = false;
	
	public static void putIntoFile (File dir) throws IOException  {


		if (dir.isDirectory()) {

			File[] items = dir.listFiles();
			CSVWriter writer = new CSVWriter(new FileWriter("/Users/TheNik/Documents/LaVeda/fixedAMEX.csv"), ',', CSVWriter.NO_QUOTE_CHARACTER); 
			
			String[] header = new String[8];
			
			
			
			header[0] = "stockID";
			header[1] = "date";
			header[2] = "open";
			header[3] = "high";
			header[4] = "low";
			header[5] = "close";
			header[6] = "volume";
			header[7] = "adjustedClose";
			
			
			writer.writeNext(header);
			

			for (File item : items) {

				String name = getName(item.getName());

				if (isCsvFile) {

					CSVReader reader = new CSVReader(new FileReader(item));
					reader.readNext();
					
					String[] line = new String[8];
					line[0] = name;
					String[] currentLine;
					
					while ((currentLine = reader.readNext()) != null) {
						
						for (int i = 1; i < 8; i ++) {
							line[i] = currentLine[i - 1];
							
						}
						writer.writeNext(line);
					}
					
					
					reader.close();

				}
			}
			writer.close();
		}
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
	
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub

		putIntoFile(new File("/Users/TheNik/Documents/LaVeda/AMEXstocks copy"));
	}

}
