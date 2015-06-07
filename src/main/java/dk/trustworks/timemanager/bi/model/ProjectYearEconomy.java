package dk.trustworks.timemanager.bi.model;

import lombok.NonNull;

public class ProjectYearEconomy {

	private @NonNull String projectUUID;
	private @NonNull String projectName;
	private double[] amount = new double[12];

	public ProjectYearEconomy() {
	}

	public ProjectYearEconomy(String projectUUID, String projectName) {
		this.projectUUID = projectUUID;
		this.projectName = projectName;
	}

	public ProjectYearEconomy(String projectUUID, String projectName, double[] amount) {
		this.projectUUID = projectUUID;
		this.projectName = projectName;
		this.amount = amount;
	}

	public String getProjectUUID() {
		return projectUUID;
	}

	public void setProjectUUID(String projectUUID) {
		this.projectUUID = projectUUID;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public double[] getAmount() {
		return amount;
	}

	public void setAmount(double[] amount) {
		this.amount = amount;
	}
}
