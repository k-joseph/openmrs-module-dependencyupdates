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

/**
 */
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;

import net.sf.json.JSONObject;

import org.openmrs.module.dependencyupdates.DependencyUpdatesProperties;

/**
 * Checks for availability of new dependency versions and stores the log onto the file system and
 * also uses the stored log to extracts out of it the real needed updates. <br/>
 * <br/>
 * The logic in this class is based on the structure of the logs obtained after running the '$mvn
 * versions:display-dependency-updates' command
 */
public class DisplayDependencyVersionUpdates {
	
	private DependencyUpdatesProperties dependencyUpdateProperties;
	
	public String dependencyUpdatesLogFile;
	
	public String pomXMLFile;
	
	private String mvnCommand;
	
	//private ArrayList<String> logVersionLineList;
	
	private String versionLinePartA;
	
	private String versionLinePartB;
	
	public String getDependencyUpdatesLogFile() {
		return getDependencyUpdateProperties().getDependencyUpdatesLogFile();
	}
	
	public String getPomXMLFile() {
		return getDependencyUpdateProperties().getPomXMLFile();
	}
	
	public DisplayDependencyVersionUpdates(DependencyUpdatesProperties props) {
		setDependencyUpdateProperties(props);
	}
	
	public DependencyUpdatesProperties getDependencyUpdateProperties() {
		return dependencyUpdateProperties;
	}
	
	public void setDependencyUpdateProperties(DependencyUpdatesProperties props) {
		this.dependencyUpdateProperties = props;
	}
	
	/**
	 * @should run maven display versions update command save logs to file system
	 * @return
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public FileInputStream runMvnVersionCmdAndSaveLogsToFileSystem() throws IOException, InterruptedException {
		
		Process p = null;
		try {
			setMvnCommand(getDependencyUpdateProperties().getMvnVersionDisplayDependencyUpdatesFinalCmd());
			p = Runtime.getRuntime().exec(getMvnCommand());
			System.out.println("Successfully sav(ing/ed) log at: " + getDependencyUpdatesLogFile());
		}
		catch (IOException e) {
			System.err.println("Error in exec() method");
			e.printStackTrace();
		}
		
		System.setOut(new PrintStream(new FileOutputStream(getDependencyUpdatesLogFile())));
		
		copy(p.getInputStream(), System.out);
		p.waitFor();
		
		return new FileInputStream(getDependencyUpdatesLogFile());
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
	 * @should read the log file on the file system and extract out of it the details of the project
	 *         including the new and previous versions
	 * @param logFilePath
	 */
	public ArrayList<String> readLogFileLineByLine(String logFilePath) {
		ArrayList<String> versionLines = new ArrayList<String>();// Open the file
		FileInputStream fstream = null;
		
		try {
			fstream = new FileInputStream(logFilePath);
		}
		catch (FileNotFoundException e) {
			System.out.println("File not found at: " + logFilePath);
		}
		BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
		
		String strLine;
		
		//Read File Line By Line
		try {
			while ((strLine = br.readLine()) != null) {
				// Print the content on the console
				System.out.println(strLine);
				
				// TODO split the file contents to use the versions details
				
				boolean matchAnyWhere = strLine.matches("(?i).* -> .*");
				boolean endWithDots = strLine.endsWith("...");
				boolean containScanning = strLine.matches("(?i).*Scanning.*");
				//check a match any where for ' -> ' or ends with ' ...'
				if (!containScanning) {
					if (endWithDots) {
						versionLinePartA = strLine;
						continue;
					}
					if (versionLinePartA != null && !strLine.equals(versionLinePartA)) {
						String partBSplit[] = splitLineUsingSpace(strLine);
						for (int i = 0; i < partBSplit.length; i++) {
							if (i == 0) {
								versionLinePartB = "";
							}
							if (!partBSplit[i].equals("[INFO]")) {
								versionLinePartB += partBSplit[i] + " ";
							}
						}
						//
					}
					if (versionLinePartA != null && versionLinePartB != null) {
						strLine = versionLinePartA + " " + versionLinePartB;
						versionLinePartA = null;
						versionLinePartB = null;
					}
					if (matchAnyWhere) {
						versionLines.add(strLine);
					}
				}
			}
			//Close the input stream
			br.close();
			System.out.println();
			System.out.println();
			System.out.println("===============================================================================");
			System.out.println();
			System.out.println();
		}
		catch (IOException e) {
			System.out.println("Error generated" + e);
		}
		return versionLines;
	}
	
	public String[] splitLineUsingSpace(String line) {
		String result[] = line.split("\\s+");
		return result;
	}
	
	/**
	 * Auto generated method comment
	 * 
	 * @should slit out real dependency version details out the logs
	 * @param logVersionLines
	 * @return versionDetails as GSONObject
	 */
	public JSONObject splitLogVersionLinesToDisplayVersions(String logFilePath) {
		JSONObject allVersionDetails = new JSONObject();
		JSONObject versionDetails = new JSONObject();
		ArrayList<String> logVersionLines = readLogFileLineByLine(logFilePath);
		
		for (int i = 0; i < logVersionLines.size(); i++) {
			String linesubStrings[] = splitLineUsingSpace(logVersionLines.get(i));
			
			for (int j = 0; j < linesubStrings.length; j++) {
				if (j == 0) {}//Do nothing since 0 contains [INFO]
				else if (j == 1) {
					String[] groupAndArtifactIds = linesubStrings[j].split(":");
					if (groupAndArtifactIds.length == 2) {
						versionDetails.put("group_id", groupAndArtifactIds[0]);
						versionDetails.put("artifact_id", groupAndArtifactIds[1]);
					}
				} else if (j == 3) { //skips "....................................." and contains something like: 2.2.3
					versionDetails.put("current_version", linesubStrings[j]);
				} else if (j == 5) { //contains something like: 4.1.4.RELEASE
					versionDetails.put("new_version", linesubStrings[j]);
					
				}
				int num = i + 1;
				if (!versionDetails.isEmpty()) {
					allVersionDetails.put("dep_update_" + num, versionDetails);
				}
			}
		}
		
		if (allVersionDetails.isEmpty()) {
			allVersionDetails = null;
		}
		
		return allVersionDetails;
	}
	
	public String getMvnCommand() {
		return mvnCommand;
	}
	
	public void setMvnCommand(String mvnCommand) {
		this.mvnCommand = mvnCommand;
	}
	
}
