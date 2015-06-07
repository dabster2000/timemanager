package dk.trustworks.timemanager.time.dto;

public class TaskWeekView {

	private String taskUUID;

	private String taskName;

	private double monday;

	private double tuesday;

	private double wednesday;

	private double thursday;

	private double friday;

	private double saturday;

	private double sunday;

	private double budgetLeft;

	public TaskWeekView() {
	}

	public TaskWeekView(String taskUUID, String taskName, double monday, double tuesday, double wednesday, double thursday, double friday, double saturday, double sunday, double budgetLeft) {
		this.taskUUID = taskUUID;
		this.taskName = taskName;
		this.monday = monday;
		this.tuesday = tuesday;
		this.wednesday = wednesday;
		this.thursday = thursday;
		this.friday = friday;
		this.saturday = saturday;
		this.sunday = sunday;
		this.budgetLeft = budgetLeft;
	}

	public String getTaskUUID() {
		return taskUUID;
	}

	public void setTaskUUID(String taskUUID) {
		this.taskUUID = taskUUID;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public double getMonday() {
		return monday;
	}

	public void setMonday(double monday) {
		this.monday = monday;
	}

	public double getTuesday() {
		return tuesday;
	}

	public void setTuesday(double tuesday) {
		this.tuesday = tuesday;
	}

	public double getWednesday() {
		return wednesday;
	}

	public void setWednesday(double wednesday) {
		this.wednesday = wednesday;
	}

	public double getThursday() {
		return thursday;
	}

	public void setThursday(double thursday) {
		this.thursday = thursday;
	}

	public double getFriday() {
		return friday;
	}

	public void setFriday(double friday) {
		this.friday = friday;
	}

	public double getSaturday() {
		return saturday;
	}

	public void setSaturday(double saturday) {
		this.saturday = saturday;
	}

	public double getSunday() {
		return sunday;
	}

	public void setSunday(double sunday) {
		this.sunday = sunday;
	}

	public double getBudgetLeft() {
		return budgetLeft;
	}

	public void setBudgetLeft(double budgetLeft) {
		this.budgetLeft = budgetLeft;
	}

	public void addWorkToDay(int day, double hours) {
		switch (day) {
		case 0:
			setMonday(getMonday() + hours);
			break;
		case 1:
			setTuesday(getTuesday() + hours);
			break;
		case 2:
			setWednesday(getWednesday() + hours);
			break;
		case 3:
			setThursday(getThursday() + hours);
			break;
		case 4:
			setFriday(getFriday() + hours);
			break;
		case 5:
			setSaturday(getSaturday() + hours);
			break;
		case 6:
			setSunday(getSunday() + hours);
			break;
		}

	}

}
