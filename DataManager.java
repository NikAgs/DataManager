import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;


public class DataManager {

	public static void main(String[] args) throws IOException {

		final long startTime = System.currentTimeMillis();
		
		try {
			
			File n = new File("/Users/TheNik/Documents/LaVeda/NYSEstocks copy"); //TODO:change directory to your directory containing the files
			
			DataFixer fixer = new DataFixer(n);
			DataFlipper Flipper = new DataFlipper(n);
			DataUpdater Updater = new DataUpdater(n);
			
			
			
			//fixer.fixFiles();           //Uncommennt this if you want to fix the files
			
			Flipper.flipFiles();          //These update the files already in the directory
			Updater.updateFiles();
			Flipper.flipFiles();
			

		} catch (FileNotFoundException e) {

			System.out.println("Enter the directory containing the stocks: ");
			BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
			String text = in.readLine();
			File f = new File(text);
			DataFlipper flipper = new DataFlipper(f);
			DataUpdater updater = new DataUpdater(f);
			flipper.flipFiles();
			updater.updateFiles();
			flipper.flipFiles();
		}
		
		final long endTime = System.currentTimeMillis();

		System.out.println("Total execution time: " + ((endTime - startTime) / 1000) + " s" );
	}
	


}
