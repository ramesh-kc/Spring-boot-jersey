package com.tutorial.jerseydemo;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "users")
@Path("/users")
public class UserResource {

	private static Map<Integer, User> dB = new HashMap<>();

	@GET
	@Produces("application/json")
	public Users getUsers() {
		Users users = new Users();
		users.setUsers(new ArrayList<>(dB.values()));
		return users;
	}

	@GET
	@Path("/{id}")
	@Produces("application/json")
	public Response getUserById(@PathParam("id") int id) throws URISyntaxException {
		User user = dB.get(id);

		if (null == user) {
			return Response.status(400).build();
		}
		return Response.status(200).entity(user).contentLocation(new URI("/user-management/" + id)).build();
	}

	@POST
	@Consumes("application/json")
	@Produces("application/json")
	public Response createUser(User user) throws URISyntaxException {
		if (user.getFirstName() == null || user.getLastName() == null) {
			return Response.status(400).entity("PLEASE PROVIDE ALL MANDATORY INPUTS").build();
		}
		user.setId(dB.values().size() + 1);
		user.setUri("/user-management/"+user.getId());
		dB.put(user.getId(), user);
		return Response.status(201).entity(user).contentLocation(new URI(user.getUri())).build();
	}

	@DELETE
	@Path("/{id}")
	public Response deleteUser(@PathParam("id") int id) throws URISyntaxException {
		User user = dB.get(id);

		if (null != user) {
			dB.remove(user.getId());
			return Response.status(200).build();
		}
		return Response.status(400).build();
	}

	@PUT
	@Path("/{id}")
	@Consumes("application/json")
	@Produces("application/json")
	public Response updateUser(@PathParam("id") int id, User user) throws  URISyntaxException {
		User tempUser = dB.get(id);

		if (null == user) {
			return Response.status(400).build();
		}
		tempUser.setFirstName(user.getFirstName());
		tempUser.setLastName(user.getLastName());
		dB.put(tempUser.getId(), tempUser);
		return Response.status(200).entity(tempUser).build();
	}
	static {
		User user = new User();
		user.setId(1);
		user.setFirstName("Ramesh");
		user.setLastName("KC");
		user.setUri("user-management/1");

		User user2 = new User();
		user2.setId(2);
		user2.setFirstName("Binod");
		user2.setLastName("Bastakoti");
		user2.setUri("user-management/2");

		dB.put(user.getId(), user);
		dB.put(user2.getId(), user2);
	}
}
