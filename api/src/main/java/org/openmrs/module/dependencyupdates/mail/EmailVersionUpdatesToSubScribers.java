/**
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */
package org.openmrs.module.dependencyupdates.mail;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import net.sf.json.JSONObject;

import org.openmrs.module.dependencyupdates.mvn.DisplayDependencyVersionUpdates;

/**
 * This class is responsible to email extracted version updates to all the subscribers in the
 * database
 */
public class EmailVersionUpdatesToSubScribers {
	
	public EmailVersionUpdatesToSubScribers(String host, String from, List<SubScriber> subStribers, String subject) {
		setHost(host);
		setFrom(from);
		setSubject(subject);
		setSubStribers(subStribers);
	}
	
	private String host;
	
	// can include the date of checking the updates by considering to store the date of running the command
	private String subject;
	
	private List<SubScriber> subStribers;
	
	private String from;
	
	public String getFrom() {
		return from;
	}
	
	public void setFrom(String from) {
		this.from = from;
	}
	
	public boolean sendEmailToSubScribers(String dependencyUpdatesEmail) {
		boolean isSent = false;
		
		String host = getHost();
		
		for (int i = 0; i < getSubStribers().size(); i++) {
			String to = getSubStribers().get(i).getEmailAddress();
			Properties properties = System.getProperties();
			
			// Setup mail server
			properties.setProperty("mail.smtp.host", host);
			Session session = Session.getDefaultInstance(properties);
			try {
				MimeMessage message = new MimeMessage(session);
				message.setFrom(new InternetAddress(getFrom()));
				message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
				message.setSubject(getSubject());
				message.setText(dependencyUpdatesEmail);
				
				// Send message
				Transport.send(message);
				System.out.println("Message sent successfully :-) ...");
				isSent = true;
			}
			catch (MessagingException mex) {
				mex.printStackTrace();
			}
		}
		return isSent;
	}
	
	public String writetheDependencyUpdatesEmail(JSONObject versionUpdates, String projectName) {
		String email = "Hi there; " + "Here are the updates available for your dependencies for the " + projectName
		        + " project: \n" + DisplayDependencyVersionUpdates.extractDependecyUpdateEmail(versionUpdates);
		return email;
	}
	
	public List<String> extractEmailDetailsFromJSON(JSONObject json) {
		List<String> emailDetails = new ArrayList<String>();
		
		return emailDetails;
	}
	
	public String getHost() {
		return host;
	}
	
	public void setHost(String host) {
		this.host = host;
	}
	
	public List<SubScriber> getSubStribers() {
		return subStribers;
	}
	
	public void setSubStribers(List<SubScriber> subStribers) {
		this.subStribers = subStribers;
	}
	
	public String getSubject() {
		return subject;
	}
	
	public void setSubject(String subject) {
		this.subject = subject;
	}
}
