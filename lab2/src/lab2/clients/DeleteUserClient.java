package lab2.clients;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lab2.api.service.RestUsers;
import org.glassfish.jersey.client.ClientConfig;

import java.io.IOException;

public class DeleteUserClient {

	public static void main(String[] args) throws IOException {
		
		if( args.length != 3) {
			System.err.println( "Use: java " + CreateUserClient.class.getCanonicalName() + " url userId password");
			return;
		}
		
		String serverUrl = args[0];
		String userId = args[1];
		String password = args[2];
		
		System.out.println("Sending request to server.");

		ClientConfig config = new ClientConfig();
		Client client = ClientBuilder.newClient(config);

		WebTarget target = client.target(serverUrl).path(RestUsers.PATH).path(userId).queryParam("password", password);
		Response r = target.request()
				.accept(MediaType.APPLICATION_JSON)
				.delete();

		if (r.getStatus() == Response.Status.OK.getStatusCode() && r.hasEntity()){
			System.out.println("Success, user with userId" + userId +" was deleted.");
			System.out.println(r.readEntity(String.class));
		}
		else
			System.out.println("Error, HTTP error status: " + r.getStatus());
	}
	
}
