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
package org.openmrs.module.dependencyupdates;

import java.util.Date;

public class DependencyUpdatesProperties {
	
	public DependencyUpdatesProperties(String rootDir, String logFile, String mvnHome) {
		setProjectRootDir(rootDir);
		setMvnHome(mvnHome);
		setDependencyUpdatesLogFile(logFile);
	}
	
	private String projectRootDir;
	
	private String dependencyUpdatesLogFile;
	
	private String mvnHome;
	
	private String projectDataDir;
	
	private String projectName;
	
	private Date lastCheckedOn;
	
	public String getProjectRootDir() {
		return projectRootDir;
	}
	
	public void setProjectRootDir(String projectRootDir) {
		this.projectRootDir = projectRootDir;
	}
	
	public String getDependencyUpdatesLogFile() {
		return dependencyUpdatesLogFile;
	}
	
	public void setDependencyUpdatesLogFile(String dependencyUpdatesLogFile) {
		this.dependencyUpdatesLogFile = dependencyUpdatesLogFile;
	}
	
	public String getMvnHome() {
		return mvnHome;
	}
	
	public void setMvnHome(String mvnHome) {
		this.mvnHome = mvnHome;
	}
	
	public String getPomXMLFile() {
		return this.getProjectRootDir() + "/pom.xml";
	}
	
	public String getMvnCommand() {
		return "sh " + this.getMvnHome() + "/bin/mvn";
	}
	
	/**
	 * @return The final maven command that returns the available dependency updates
	 */
	public String getMvnVersionDisplayDependencyUpdatesFinalCmd() {
		return this.getMvnCommand() + " versions:display-dependency-updates -f " + getPomXMLFile();
	}
	
	public String getProjectDataDir() {
		return projectDataDir;
	}
	
	public void setProjectDataDir(String projectDataDir) {
		this.projectDataDir = projectDataDir;
	}
	
	public String getProjectName() {
		return projectName;
	}
	
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	
	public Date getLastCheckedOn() {
		return lastCheckedOn;
	}
	
	public void setLastCheckedOn(Date lastCheckedOn) {
		this.lastCheckedOn = lastCheckedOn;
	}
}
