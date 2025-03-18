package sd.lab1;

import java.net.* ;
import java.util.*;

/**
 * Basic TCP client... 
 *
 */
public class TcpClient {
    
	private static final String QUIT = "!quit";

	public static void main(String[] args) throws Exception {

		Discovery d = new Discovery(Discovery.DISCOVERY_ADDR);
		d.start();
		Scanner scan = new Scanner(System.in);
		URI[] uris = d.knownUrisOf("Teste", 2);

		URI uri = uris[1];
		String host = uri.getHost();
		int port = uri.getPort();

    	try( Socket sock = new Socket( host , port)) {
    		String input;
    		do {
    			input = scan.nextLine();
    			sock.getOutputStream().write( (input + System.lineSeparator()).getBytes() );
    		} while( ! input.equals(QUIT));
    	}
    	
    	scan.close();
		
    }  
}
