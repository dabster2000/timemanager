package dk.trustworks.timemanager.bi.comparators;

import dk.trustworks.timemanager.client.model.MonthBudgetHistory;

import java.util.Comparator;

public class MonthBudgetComparable implements Comparator<MonthBudgetHistory> {

	@Override
	public int compare(MonthBudgetHistory o1, MonthBudgetHistory o2) {
		return (o1.getMonth()>o2.getMonth() ? 1 : (o1.getMonth()==o2.getMonth() ? 0 : -1));
	}

}
