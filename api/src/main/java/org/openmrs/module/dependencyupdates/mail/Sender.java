package org.openmrs.module.dependencyupdates.mail;

import java.io.Serializable;

import org.jasypt.util.password.StrongPasswordEncryptor;
import org.openmrs.BaseOpenmrsObject;

public class Sender extends BaseOpenmrsObject implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private Integer id;
	
	private String uuid;
	
	private String name;
	
	private String emailAddress;
	
	private String encryptedPassword;
	
	/**
	 * Not to be stored in the database but rather the result of {@link #getEncryptedPassword()}
	 */
	private String password;
	
	private StrongPasswordEncryptor passwordEncryptor;
	
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
	
	public Sender(StrongPasswordEncryptor passwordEncryptor, String name, String emailAddress, String password) {
		setPasswordEncryptor(passwordEncryptor);
		setName(name);
		setEmailAddress(emailAddress);
		setPassword(password);
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getEmailAddress() {
		return emailAddress;
	}
	
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public StrongPasswordEncryptor getPasswordEncryptor() {
		return passwordEncryptor;
	}
	
	public void setPasswordEncryptor(StrongPasswordEncryptor passwordEncryptor) {
		this.passwordEncryptor = passwordEncryptor;
	}
	
	/**
	 * @should verify entered password
	 * @param inputPassword
	 * @return
	 */
	public boolean checkForRightPassword(String inputPassword, String encryptedPassword) {
		if (getPasswordEncryptor().checkPassword(inputPassword, encryptedPassword)) {
			return true;
		} else {
			return false;
		}
	}
	
	public String getEncryptedPassword() {
		encryptedPassword = getPasswordEncryptor().encryptPassword(getPassword());
		return encryptedPassword;
	}
	
}
