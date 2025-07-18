package com.stopsnearme.app.ws.ui.controllers;

import java.lang.invoke.MethodHandles;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.stopsnearme.app.ws.ui.model.Person;
import com.stopsnearme.app.ws.ui.model.PersonRepository;
import com.stopsnearme.app.ws.ui.model.request.UpdateUserDetailsRequestModel;
import com.stopsnearme.app.ws.ui.model.request.UserDetailsRequestModel;
import com.stopsnearme.app.ws.ui.model.response.UserRest;
import com.stopsnearme.app.ws.userservice.UserService;

import jakarta.validation.Valid;
import jakarta.persistence.Id;

@RestController
@RequestMapping("/users")
public class UserController {

	Map<String, UserRest> users = new ConcurrentHashMap<String, UserRest>();
	
	@Autowired
	UserService userService;

	PersonRepository personRepo;
	
    public UserController(PersonRepository personRepository) {
		personRepo = personRepository;
    }

    private static final Logger logger = Logger.getLogger(MethodHandles.lookup().lookupClass().getName());

	@GetMapping
	public String getUsers(@RequestParam(value="page", defaultValue="1") int page, 
			@RequestParam(value="limit", defaultValue="50") int limit,
			@RequestParam(value="sort", defaultValue = "desc", required = false) String sort)
	{
        logger.log(Level.WARNING, "In users now");
		return "get users was called with page = " + page + " and limit = " + limit + " and sort = " + sort;
	}
		
	@GetMapping(path="/{userId}", produces =  { MediaType.APPLICATION_JSON_VALUE} )
	// @PostAuthorize("returnObject.body.firstName == authentication.name")
	public ResponseEntity<Person> getPersonName(@PathVariable long userId)
	// public ResponseEntity<UserRest> getPersonName(@PathVariable String userId)
	// public ResponseEntity<Person> getPersonById(@PathVariable Long userId)
	{
		// /*
		Long no = new Long(2);
        return this.personRepo.findById(userId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
		//  */
		// var returnValue = userService.fetchUser(userId);
		// return new ResponseEntity<UserRest>(returnValue, HttpStatus.OK);
	}
	
	/*
	@GetMapping(path="/{userId}", 
			produces =  { 
					MediaType.APPLICATION_XML_VALUE,
					MediaType.APPLICATION_JSON_VALUE
					} )
	public ResponseEntity<UserRest> getUser(@PathVariable String userId)
	{
		if(users.containsKey(userId))
		{
			return new ResponseEntity<>(users.get(userId), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
	}*/
	
	@PostMapping(
			consumes =  { 
			MediaType.APPLICATION_JSON_VALUE
			}, 
			produces =  { 
					MediaType.APPLICATION_JSON_VALUE
					}  )
	public ResponseEntity<UserRest> createUser(@Valid @RequestBody UserDetailsRequestModel userDetails)
	{

		UserRest returnValue = userService.createUser(userDetails);
		users.put(returnValue.getUserId(), returnValue);
		return new ResponseEntity<UserRest>(returnValue, HttpStatus.OK);
	}
	
	@PutMapping(path="/{userId}", consumes =  { 
			MediaType.APPLICATION_JSON_VALUE
			}, 
			produces =  { 
					MediaType.APPLICATION_JSON_VALUE
					}  )
	public UserRest updateUser(@PathVariable String userId, @Valid @RequestBody UpdateUserDetailsRequestModel userDetails)
	{
		 UserRest storedUserDetails = users.get(userId);
		 storedUserDetails.setFirstName(userDetails.getFirstName());
		 storedUserDetails.setLastName(userDetails.getLastName());
		 
		 users.put(userId, storedUserDetails);
		 
		 return storedUserDetails;
	}
	
	@DeleteMapping(path="/{id}")
	public ResponseEntity<Void> deleteUser(@PathVariable String id)
	{
		users.remove(id);
		
		return ResponseEntity.noContent().build();
	}
}
