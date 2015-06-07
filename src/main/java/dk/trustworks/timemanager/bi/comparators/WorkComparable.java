package dk.trustworks.timemanager.bi.comparators;

import dk.trustworks.timemanager.time.model.Work;

import java.util.Comparator;

public class WorkComparable implements Comparator<Work> {

	@Override
	public int compare(Work o1, Work o2) {
		return (o1.getMonth()>o2.getMonth() ? 1 : (o1.getMonth()==o2.getMonth() ? 0 : -1));
	}

}
