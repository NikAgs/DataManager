import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;


public class DateGetter {


	static String[] today = new String[3];
	
	public DateGetter() throws IOException {

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

			System.out.println("S&P" + " works");
			String[] nextLine;
			File path = new File("/Users/TheNik/Documents/LaVeda/s&p.csv");
			CSVWriter writer = new CSVWriter(new FileWriter(path), ',', CSVWriter.NO_QUOTE_CHARACTER); 

			String[] useThis = new String[1];
			
			while ((nextLine = webReader.readNext()) != null) {
				
				useThis[0] = nextLine[0];
				writer.writeNext(useThis);
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


	public static void main(String[] args) throws IOException {
		DateGetter dr = new DateGetter();
		dr.retrieveStockData("^GSPC");
	}
}
