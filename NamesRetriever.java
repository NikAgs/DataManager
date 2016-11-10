import java.io.File;
import java.io.IOException;


public class NamesRetriever {

	static File dir;
	
	public NamesRetriever (File dir) {
		NamesRetriever.dir = dir;
	}
	
	public static void getNames() throws IOException {

		if (dir.isDirectory()) {

			File[] items = dir.listFiles();

			for (File item : items) {

				String name = item.getName();

				if (name.length() < 6){
					System.out.println(name.substring(0,1) );
				} else 

					if (name.length() < 7){
						System.out.println(name.substring(0,2));
					} else 

						if (name.length() < 8){
							System.out.println(name.substring(0,3));
						} else 

							System.out.println(name.substring(0,4));
				
			}
		}
	}

}
