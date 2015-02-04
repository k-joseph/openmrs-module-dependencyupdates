package org.openmrs.module.dependencyupdates.mail;

import java.io.Serializable;

import org.openmrs.BaseOpenmrsObject;

public class Email extends BaseOpenmrsObject implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private Integer id;
	
	private String uuid;
	
	private String to;
	
	private String from;
	
	private String subject;
	
	private String email;
	
	private String host;
	
	public String getUuid() {
		return uuid;
	}
	
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	
	@Override
	public Integer getId() {
		return id;
	}
	
	@Override
	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getTo() {
		return to;
	}
	
	public void setTo(String to) {
		this.to = to;
	}
	
	public String getFrom() {
		return from;
	}
	
	public void setFrom(String from) {
		this.from = from;
	}
	
	public String getSubject() {
		return subject;
	}
	
	public void setSubject(String subject) {
		this.subject = subject;
	}
	
	public String getComposeEmail() {
		return email;
	}
	
	public void setComposeEmail(String composeEmail) {
		this.email = composeEmail;
	}

	public String getHost() {
	    return host;
    }

	public void setHost(String host) {
	    this.host = host;
    }
	
}
