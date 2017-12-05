package com.nm.cig.data;
import javax.xml.bind.annotation.*;

@XmlRootElement(name = "to")
public class ToUser {

	private String meetingId;
	private String name;
	private String email;
	private String login;
	//private String uuid;
	

	public String getMeetingId() {
		return meetingId;
	}
	public void setMeetingId(String meetingId) {
		this.meetingId = meetingId;
	}
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
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ToUser other = (ToUser) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
	@XmlElement(name = "login")
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
/*	@XmlElement(name = "uuid")
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
*/	
}
