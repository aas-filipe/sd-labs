package lab2.clients;

import java.io.IOException;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lab2.api.User;
import lab2.api.service.RestUsers;
import org.glassfish.jersey.client.ClientConfig;

public class UpdateUserClient {

	public static void main(String[] args) throws IOException {
		
		if( args.length != 6) {
			System.err.println( "Use: java " + CreateUserClient.class.getCanonicalName() + " url userId oldpwd fullName email password");
			return;
		}
		
		String serverUrl = args[0];
		String userId = args[1];
		String oldpwd = args[2];
		String fullName = args[3];
		String email = args[4];
		String password = args[5];
		
		User usr = new User( userId, fullName, email, password);
		
		System.out.println("Sending request to server.");

		ClientConfig clientConfig = new ClientConfig();
		Client client = ClientBuilder.newClient(clientConfig);

		WebTarget target = client.target(serverUrl).path(RestUsers.PATH).path(userId).queryParam("password", oldpwd);

		Response r =  target.request()
				.accept(MediaType.APPLICATION_JSON)
				.put(Entity.entity(usr, MediaType.APPLICATION_JSON));

		if(r.getStatus() == Response.Status.OK.getStatusCode() && r.hasEntity()){
			System.out.println("Success, user with userId " + userId + " has been updated.");
			System.out.println(r.readEntity(String.class));
		}
		else{
			System.out.println("Failed to update user.");
		}
	}
	
}
