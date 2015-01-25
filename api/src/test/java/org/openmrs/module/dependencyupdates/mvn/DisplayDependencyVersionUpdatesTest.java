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

import net.sf.json.JSONObject;

import org.junit.Assert;
import org.junit.Test;
import org.openmrs.module.dependencyupdates.DependencyUpdatesProperties;
import org.openmrs.test.Verifies;

public class DisplayDependencyVersionUpdatesTest {
	
	//TODO Instead of hard-coding this out rather use the @DependencyUpdatesProperties object
	private String openmrsRoot = "/media/KJoseph/Projects/openMRS/openmrs-core-master/openmrs-core/";
	
	private String mavenShRun = "sh /home/k-joseph/MyProgs/apache-maven-3.0.4/bin/mvn";
	
	DisplayDependencyVersionUpdates updates = new DisplayDependencyVersionUpdates(new DependencyUpdatesProperties(
	        "/media/KJoseph/Projects/openMRS/openmrs-core-master/openmrs-core/",
	        "/media/KJoseph/Projects/openMRS/Modules/mine/dependencyUpdates.log",
	        "/home/k-joseph/MyProgs/apache-maven-3.0.4/"));
	
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
		        "/media/KJoseph/Projects/openMRS/Modules/mine/dependencyUpdates.log")));
		
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
		//updates.readVersionsFromLogFile("/media/KJoseph/Projects/openMRS/Modules/mine/dependencyUpdates.log");
	}
	
	/**
	 * Test for {@link DisplayDependencyVersionUpdates#runMvnVersionCmdAndSaveLogsToFileSystem()}
	 */
	@Test
	@Verifies(value = "should run maven display versions update command save logs to file system", method = "runMvnVersionCmdAndSaveLogsToFileSystem()")
	public void displayDependencyVersionUpdates_runMvnVersionCmdAndSaveLogsToFileSystem() {
		File file = new File("/media/KJoseph/Projects/openMRS/Modules/mine/dependencyUpdates.log");
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
		        .splitLogVersionLinesToDisplayVersions("/media/KJoseph/Projects/openMRS/Modules/mine/dependencyUpdates.log");
		
		Assert.assertNotNull(returnedVersionUpdates);
		System.out.println(returnedVersionUpdates);
	}
}
