package dk.trustworks.timemanager.client.dto;

public class Budget {

    private String name;

    private double remainingBudget;
    
    private double usedBudget;

    private double budget;

    public Budget() {
    }

    public Budget(String name, double remainingBudget, double usedBudget, double budget) {
        this.name = name;
        this.remainingBudget = remainingBudget;
        this.usedBudget = usedBudget;
        this.budget = budget;
    }

    public Budget(Double remainingBudget) {
        this.setRemainingBudget(remainingBudget);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getRemainingBudget() {
        return remainingBudget;
    }

    public void setRemainingBudget(double remainingBudget) {
        this.remainingBudget = remainingBudget;
    }

    public double getUsedBudget() {
        return usedBudget;
    }

    public void setUsedBudget(double usedBudget) {
        this.usedBudget = usedBudget;
    }

    public double getBudget() {
        return budget;
    }

    public void setBudget(double budget) {
        this.budget = budget;
    }
}
