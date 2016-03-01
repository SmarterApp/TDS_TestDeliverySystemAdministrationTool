package tds.tdsadmin.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

public class LazyOppDataModel extends LazyDataModel<TestOpportunity> {

	private static final long serialVersionUID = 1L;

	private List<TestOpportunity> datasource;

	public LazyOppDataModel(List<TestOpportunity> datasource) {
		this.datasource = datasource;
	}

	@Override
	public TestOpportunity getRowData(String rowKey) {
		for (TestOpportunity TestOpportunity : datasource) {
			if (TestOpportunity.getAltSsid().equals(rowKey))
				return TestOpportunity;
		}

		return null;
	}

	@Override
	public Object getRowKey(TestOpportunity TestOpportunity) {
		return TestOpportunity.getAltSsid();
	}

	@Override
	public List<TestOpportunity> load(int first, int pageSize, String sortField, SortOrder sortOrder,
			Map<String, String> filters) {
		List<TestOpportunity> data = new ArrayList<TestOpportunity>();

		// filter
		for (TestOpportunity TestOpportunity : datasource) {
			boolean match = true;

			if (filters != null) {
				for (Iterator<String> it = filters.keySet().iterator(); it.hasNext();) {
					try {
						String filterProperty = it.next();
						Object filterValue = filters.get(filterProperty);
						String fieldValue = String
								.valueOf(TestOpportunity.getClass().getField(filterProperty).get(TestOpportunity));

						if (filterValue == null || fieldValue.startsWith(filterValue.toString())) {
							match = true;
						} else {
							match = false;
							break;
						}
					} catch (Exception e) {
						match = false;
					}
				}
			}

			if (match) {
				data.add(TestOpportunity);
			}
		}

		// sort
		if (sortField != null) {
			Collections.sort(data, new LazySorter(sortField, sortOrder));
		}

		// rowCount
		int dataSize = data.size();
		if (this.getRowCount() <= 0) {
			this.setRowCount(dataSize);
		}
		// paginate
		if (dataSize > pageSize) {
			try {
				return data.subList(first, first + pageSize);
			} catch (IndexOutOfBoundsException e) {
				return data.subList(first, first + (dataSize % pageSize));
			}
		} else {
			return data;
		}
	}
}
