import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;


public class DataRetriever  {


	static File dir;
	static String[] today = new String[3];
	

	public DataRetriever(File dir) throws IOException {

		DataRetriever.dir = dir;
		findToday();
	}


	private void findToday() {
		Calendar currentDate = Calendar.getInstance();                    // gets the current date
		SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd"); 
		String dateNow = formatter.format(currentDate.getTime());
		today[0] = (dateNow.substring(5,7));
		today[1] = (dateNow.substring(8,10));
		today[2] = (dateNow.substring(0,4));

	}

	public void retrieveStockData(String stock) throws MalformedURLException {



		URL url = new URL(getURL(stock));

		try {

			CSVReader webReader = new CSVReader(new InputStreamReader(url.openStream()));

			System.out.println(stock + " works");
			String[] nextLine;
			String pathName = dir.toString() + "/" + stock + ".csv";
			File path = new File(pathName);
			//path.createNewFile();
			CSVWriter writer = new CSVWriter(new FileWriter(path), ',', CSVWriter.NO_QUOTE_CHARACTER); 

			while ((nextLine = webReader.readNext()) != null) {
				writer.writeNext(nextLine);
			}

			writer.close();
			webReader.close();

		} catch (IOException e) {
			System.out.println(stock + " doesn't work");

		}

	}


	private String getURL(String name) {
		String beginning = "http://ichart.yahoo.com/table.csv?s=";
		String url = beginning.concat(name + "&a=0&b=1&c=1800&d=" + today[0] + "&e=" + today[1] + "&f=" + today[2] + "&g=d&ignore=.csv");
		return url;

	}

	private void retrieveAllData() throws IOException {

		CSVReader reader = new CSVReader(new FileReader("/Users/TheNik/Documents/LaVeda/AMEXlist.csv")); //TODO:change list to location of stocklist on your computer

		String[] nextLine;

		reader.readNext();

		while ((nextLine = reader.readNext()) != null) {

			String stock = nextLine[0];

			retrieveStockData(stock);
		}
		reader.close();
	}



	public static void main(String[] args) throws IOException {
		DataRetriever dr = new DataRetriever(new File("/Users/TheNik/Documents/LaVeda/AMEXstocks")); //TODO:change directory to location of your stock files
		dr.retrieveAllData();
	}

}
