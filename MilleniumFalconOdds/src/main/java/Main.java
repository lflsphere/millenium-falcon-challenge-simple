

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		C3PO c3PO = new C3PO(new File("src/main/java/examples/example1/millennium-falcon.json"));
		//C3PO c3PO = new C3PO(new File("millenium-falcon.json"));
		
		double odds;
		try {
			odds = c3PO.giveMeTheOdds(new File("src/main/java/examples/example3/empire.json"));
			
			System.out.println(odds);

			
		} catch (StreamReadException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DatabindException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		

  
       

	}

}
