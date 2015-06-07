package dk.trustworks.timemanager.client.dto;


public class BudgetContainer {

	private String name;
	
	private Budget[] budgets;
	
	public BudgetContainer() {
		
	}
	
	public BudgetContainer(String name, Budget[] budgets) {
		this.setName(name);
		this.setBudgets(budgets);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Budget[] getBudgets() {
		return budgets;
	}

	public void setBudgets(Budget[] budgets) {
		this.budgets = budgets;
	}
}
