package org.openmrs.module.dependencyupdates.mail;

import org.jasypt.util.password.StrongPasswordEncryptor;
import org.junit.Assert;
import org.junit.Test;

public class SenderTest {
	
	@Test
	public void checkForRightPassword_shouldVerifyEnteredPassord() {
		
		Sender sender = new Sender(new StrongPasswordEncryptor(), "name", "test_email@example.org", "testPassword 23242//*-");
		System.out.println(sender.getEncryptedPassword());
		Assert.assertFalse(sender.checkForRightPassword("testPassword23242//*-", sender.getEncryptedPassword()));
		Assert.assertTrue(sender.checkForRightPassword("testPassword 23242//*-", sender.getEncryptedPassword()));
	}
}
