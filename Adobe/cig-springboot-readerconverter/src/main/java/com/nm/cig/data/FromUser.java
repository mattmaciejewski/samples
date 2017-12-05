package com.nm.cig.data;

import javax.xml.bind.annotation.*;

@XmlRootElement(name = "from")
public class FromUser {

	private String name;
	private String email;
	private String login;
	
	@XmlElement(name = "name")
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@XmlElement(name = "email")
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	@XmlElement(name = "login")
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
/*	public FromUser(String name, String email, String login) {
		super();
		this.name = name;
		this.email = email;
		this.login = login;
	}
	public FromUser() {
		super();
	}
*/	
	
}
