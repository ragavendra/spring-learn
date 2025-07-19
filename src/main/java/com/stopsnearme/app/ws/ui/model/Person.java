package com.stopsnearme.app.ws.ui.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
public class Person {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@NotNull(message="First name cannot be null")
	@Size(min=2, message = "First name must not be less than 2 characters")
	private String firstName;

	@NotNull(message="Last name cannot be null")
	@Size(min=2, message = "Last name must not be less than 2 characters")
	private String lastName;

	@NotNull(message="User name cannot be null")
	@Size(min=2, message = "User name must not be less than 2 characters")
	private String userName;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		userName = userName.toLowerCase();
		this.userName = userName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
}
