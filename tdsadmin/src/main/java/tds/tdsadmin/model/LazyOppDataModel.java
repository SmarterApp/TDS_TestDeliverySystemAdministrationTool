/*******************************************************************************
 * Educational Online Test Delivery System
 * Copyright (c) 2016 American Institutes for Research
 * 
 * Distributed under the AIR Open Source License, Version 1.0
 * See accompanying file AIR-License-1_0.txt or at 
 * http://www.smarterapp.org/documents/American_Institutes_for_Research_Open_Source_Software_License.pdf
 ******************************************************************************/
package tds.tdsadmin.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LazyOppDataModel extends LazyDataModel<TestOpportunity> {

	private static final Logger _logger = LoggerFactory.getLogger(LazyOppDataModel.class);

	private static final long serialVersionUID = 1L;

	private List<TestOpportunity> datasource;

	public LazyOppDataModel(List<TestOpportunity> datasource) {
		this.datasource = datasource;
	}

	@Override
	public TestOpportunity getRowData(String rowKey) {
		for (TestOpportunity testOpportunity : datasource) {
			if (testOpportunity.getAltSsid().equals(rowKey))
				return testOpportunity;
		}

		return null;
	}

	@Override
	public Object getRowKey(TestOpportunity testOpportunity) {
		return testOpportunity.getAltSsid();
	}

	@Override
	public List<TestOpportunity> load(int first, int pageSize, String sortField, SortOrder sortOrder,
			Map<String, String> filters) {
		List<TestOpportunity> data = new ArrayList<TestOpportunity>();

		// filter
		for (TestOpportunity testOpportunity : datasource) {
			boolean match = true;

			if (filters != null) {
				for (Iterator<String> it = filters.keySet().iterator(); it.hasNext();) {
					try {
						String filterProperty = it.next();
						Object filterValue = filters.get(filterProperty);
						String fieldValue = String
								.valueOf(testOpportunity.getClass().getField(filterProperty).get(testOpportunity));

						if (filterValue == null || fieldValue.startsWith(filterValue.toString())) {
							match = true;
						} else {
							match = false;
							break;
						}
					} catch (Exception e) {
						_logger.error(e.getMessage(), e);
					}
				}
			}

			if (match) {
				data.add(testOpportunity);
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
				_logger.error(e.getMessage());
				return data.subList(first, first + (dataSize % pageSize));
			}
		} else {
			return data;
		}
	}
}
