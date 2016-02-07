package tds.tdsadmin.model;

import java.util.Comparator;
import org.primefaces.model.SortOrder;

public class LazySorter implements Comparator<TestOpportunity> {

	private String sortField;

	private SortOrder sortOrder;

	public LazySorter(String sortField, SortOrder sortOrder) {
		this.sortField = sortField;
		this.sortOrder = sortOrder;
	}

	public int compare(TestOpportunity TestOpportunity1, TestOpportunity TestOpportunity2) {
		try {
			Object value1 = TestOpportunity.class.getField(this.sortField).get(TestOpportunity1);
			Object value2 = TestOpportunity.class.getField(this.sortField).get(TestOpportunity2);

			int value = ((Comparable) value1).compareTo(value2);

			return SortOrder.ASCENDING.equals(sortOrder) ? value : -1 * value;
		} catch (Exception e) {
			throw new RuntimeException();
		}
	}
}