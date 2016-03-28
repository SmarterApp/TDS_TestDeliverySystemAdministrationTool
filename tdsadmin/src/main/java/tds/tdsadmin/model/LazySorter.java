package tds.tdsadmin.model;

import java.util.Comparator;
import java.util.Date;

import org.primefaces.model.SortOrder;
import org.springframework.security.util.FieldUtils;

public class LazySorter implements Comparator<TestOpportunity> {

	private String sortField;

	private SortOrder sortOrder;

	public LazySorter(String sortField, SortOrder sortOrder) {
		this.sortField = sortField;
		this.sortOrder = sortOrder;
	}

	public int compare(TestOpportunity testOpportunity1, TestOpportunity testOpportunity2) {
		try {
			
			Object value1 = FieldUtils.getFieldValue(testOpportunity1, sortField);//TestOpportunity.class.getField(this.sortField).get(TestOpportunity1);
			Object value2 = FieldUtils.getFieldValue(testOpportunity2, sortField);//TestOpportunity.class.getField(this.sortField).get(TestOpportunity2);

			int value;
			if (value1 == null && value2 == null)
				value = 0;
			else if (value1 == null)
				value = -1;
			else if (value2 == null)
				value = 1;
			else
				value = ((Comparable) value1).compareTo(value2);

			return SortOrder.ASCENDING.equals(sortOrder) ? value : -1 * value;
		} catch (Exception e) {
			throw new RuntimeException();
		}
	}
}