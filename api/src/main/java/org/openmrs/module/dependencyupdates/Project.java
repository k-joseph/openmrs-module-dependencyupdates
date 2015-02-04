package org.openmrs.module.dependencyupdates;

import java.io.Serializable;

import org.openmrs.BaseOpenmrsObject;

/**
 * A POJO for a project whose dependency updates we want to check for
 */
public class Project extends BaseOpenmrsObject implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private Integer id;
	
	private String uuid;
	
	/**
	 * Name of the project whose dependencies we want to check updates for such as openmrs-core,
	 * openmrs-module-chartsearch, openmrs-module-formentry
	 */
	private String projectName;
	
	/**
	 * Name of the log file where the logs will be saved
	 */
	private String dependencyUpdatesLogFileName;
	
	/**
	 * The directory where to find the mvn binary or program or sh file
	 */
	private String mvnHome;
	
	/**
	 * Directory where the log file is to be placed into
	 */
	private String dataDirectory;
	
	/**
	 * The datetime on which the last online update was checked
	 */
	private String lastCheckedOn;
	
	private String description;
	
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
	
	public String getProjectName() {
		return projectName;
	}
	
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	
	public String getDependencyUpdatesLogFileName() {
		return dependencyUpdatesLogFileName;
	}
	
	public void setDependencyUpdatesLogFileName(String dependencyUpdatesLogFileName) {
		this.dependencyUpdatesLogFileName = dependencyUpdatesLogFileName;
	}
	
	public String getMvnHome() {
		return mvnHome;
	}
	
	public void setMvnHome(String mvnHome) {
		this.mvnHome = mvnHome;
	}
	
	public String getDataDirectory() {
		return dataDirectory;
	}
	
	public void setDataDirectory(String dataDirectory) {
		this.dataDirectory = dataDirectory;
	}
	
	public String getLastCheckedOn() {
		return lastCheckedOn;
	}
	
	public void setLastCheckedOn(String lastCheckedOn) {
		this.lastCheckedOn = lastCheckedOn;
	}

	public String getDescription() {
	    return description;
    }

	public void setDescription(String description) {
	    this.description = description;
    }
	
}
