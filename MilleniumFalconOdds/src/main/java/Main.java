

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		C3PO c3PO = new C3PO(new File("src/main/java/examples/example1/millennium-falcon.json"));
		//C3PO c3PO = new C3PO(new File("millenium-falcon.json"));
		
		
		try {
			double odds1;
			odds1 = c3PO.giveMeTheOdds(new File("src/main/java/examples/example1/empire.json"));
			System.out.println("Example1: " + odds1);

			double odds2;
			odds2 = c3PO.giveMeTheOdds(new File("src/main/java/examples/example2/empire.json"));
			System.out.println("Example2: " + odds2);
			
			double odds3;
			odds3 = c3PO.giveMeTheOdds(new File("src/main/java/examples/example3/empire.json"));
			System.out.println("Example3: " + odds3);
			
			double odds4;
			odds4 = c3PO.giveMeTheOdds(new File("src/main/java/examples/example4/empire.json"));
			System.out.println("Example4: " + odds4);
			
			
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
