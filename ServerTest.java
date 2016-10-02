import org.junit.Test;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

import static org.junit.Assert.*;
import java.io.*;
import java.net.*;

public class ServerTest {
	@BeforeClass
	public static void beforeClass() {
		try {
			Server.Start();
		} catch( Exception e ) {

		}
	}

	@AfterClass
	public static void afterClass() {
		Server.Stop();
	}

	@Test
	public void testCase1() {
		try {
			StringBuilder result = new StringBuilder();
		    URL url = new URL("http://localhost:8081");
      		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
      		conn.setRequestMethod("GET");
      		BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
      		String line;
      		while ((line = rd.readLine()) != null) {
        		result.append(line);
      		}
      		rd.close();
      		String s = result.toString();

     		assertTrue(s.contains("<!DOCTYPE html>"));
     	} catch( Exception e ) {
     		fail(e.toString());
     	}
   }
}
