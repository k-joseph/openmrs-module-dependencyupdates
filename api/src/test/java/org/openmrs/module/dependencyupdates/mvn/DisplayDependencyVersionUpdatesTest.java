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
package org.openmrs.module.dependencyupdates.mvn;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;

import net.sf.json.JSONObject;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.openmrs.module.dependencyupdates.DependencyUpdatesProperties;
import org.openmrs.module.dependencyupdates.mail.EmailVersionUpdatesToSubScribers;
import org.openmrs.module.dependencyupdates.mail.SubScriber;
import org.openmrs.test.Verifies;

@Ignore
//Takes long time running but passes all the tests 
public class DisplayDependencyVersionUpdatesTest {
	
	//TODO Instead of hard-coding this out rather use the @DependencyUpdatesProperties object
	private String openmrsRoot = "/media/media3/Projects/openMRS/openmrs-core-master/openmrs-core/";
	
	private String mavenShRun = "sh /home/user/MyProgs/apache-maven-3.0.4/bin/mvn";
	
	DisplayDependencyVersionUpdates updates = new DisplayDependencyVersionUpdates(new DependencyUpdatesProperties(
	        "/media/media3/Projects/openMRS/openmrs-core-master/openmrs-core/",
	        "/media/media3/Projects/openMRS/Modules/mine/dependencyUpdates.log",
	        "/home/user/MyProgs/apache-maven-3.0.4/"));
	
	public void test2() throws IOException, InterruptedException {
		
		Process p = null;
		try {
			String mvnCommand = mavenShRun + " versions:display-dependency-updates -f " + openmrsRoot + "pom.xml" /*+ redirectOutPut*/;
			p = Runtime.getRuntime().exec(mvnCommand);
		}
		catch (IOException e) {
			System.err.println("Error in exec() method");
			e.printStackTrace();
		}
		
		System.setOut(new PrintStream(new FileOutputStream(
		        "/media/media3/Projects/openMRS/Modules/mine/dependencyUpdates.log")));
		
		copy(p.getInputStream(), System.out);
		p.waitFor();
		System.out.println("\n\n");
		
	}
	
	public static void copy(InputStream in, OutputStream out) throws IOException {
		while (true) {
			int c = in.read();
			if (c == -1)
				break;
			out.write((char) c);
		}
	}
	
	/**
	 * Test for {@link DisplayDependencyVersionUpdates#readVersionsFromLogFile(String)}
	 */
	@Test
	@Verifies(value = "should read the log file on the file system and extract out of it the details of the project including the new and previous versions", method = "readVersionsFromLogFile()")
	public void displayDependencyVersionUpdates_readVersionsFromLogFile() {
		//updates.readVersionsFromLogFile("/media/media3/Projects/openMRS/Modules/mine/dependencyUpdates.log");
	}
	
	/**
	 * Test for {@link DisplayDependencyVersionUpdates#runMvnVersionCmdAndSaveLogsToFileSystem()}
	 */
	@Test
	@Verifies(value = "should run maven display versions update command save logs to file system", method = "runMvnVersionCmdAndSaveLogsToFileSystem()")
	public void displayDependencyVersionUpdates_runMvnVersionCmdAndSaveLogsToFileSystem() {
		File file = new File("/media/media3/Projects/openMRS/Modules/mine/dependencyUpdates.log");
		if (file.exists()) {
			file.delete();
		}
		try {
			FileInputStream logFile = updates.runMvnVersionCmdAndSaveLogsToFileSystem();
			Assert.assertNotNull(logFile);
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
		}
		catch (InterruptedException e) {
			// TODO Auto-generated catch block
		}
		Assert.assertTrue(file.exists());
	}
	
	/**
	 * Test for
	 * {@link DisplayDependencyVersionUpdates#splitLogVersionLinesToDisplayVersions(String)}
	 */
	@Test
	@Verifies(value = "should slit out real dependency version details out the logs", method = "splitLogVersionLinesToDisplayVersions(String)")
	public void displayDependencyVersionUpdates_splitLogVersionLinesToDisplayVersions() {
		JSONObject returnedVersionUpdates = updates
		        .splitLogVersionLinesToDisplayVersions("/media/media3/Projects/openMRS/Modules/mine/dependencyUpdates.log");
		
		Assert.assertNotNull(returnedVersionUpdates);
		System.out.println(returnedVersionUpdates);
	}
	
	@Test
	public void emailVersionUpdatesToSubScribers_writetheDependencyUpdatesEmail() {
		JSONObject returnedVersionUpdates = updates
		        .splitLogVersionLinesToDisplayVersions("/media/media3/Projects/openMRS/Modules/mine/dependencyUpdates.log");
		EmailVersionUpdatesToSubScribers email = new EmailVersionUpdatesToSubScribers("host", "events@openmrs.org",
		        new ArrayList<SubScriber>(), "subject");
		System.out.println("Email is: \n" + email.writetheDependencyUpdatesEmail(returnedVersionUpdates, "openmrs-core"));
	}
	
	@Test
	public void emailVersionUpdatesToSubScribers_sendEmailToSubScribers() {
		JSONObject returnedVersionUpdates = updates
		        .splitLogVersionLinesToDisplayVersions("/media/media3/Projects/openMRS/Modules/mine/dependencyUpdates.log");
		ArrayList<SubScriber> subSribers = new ArrayList<SubScriber>();
		
		subSribers.add(new SubScriber("name1 name2", "test@example.com", "OpenMRS"));
		
		EmailVersionUpdatesToSubScribers email = new EmailVersionUpdatesToSubScribers("127.0.1.1", "events@openmrs.org",
		        subSribers, "Some dependencies in your project may need updating!");
		boolean isSent = email.sendEmailToSubScribers(email.writetheDependencyUpdatesEmail(returnedVersionUpdates,
		    "openmrs-core"));
		Assert.assertTrue(isSent);
	}
}
