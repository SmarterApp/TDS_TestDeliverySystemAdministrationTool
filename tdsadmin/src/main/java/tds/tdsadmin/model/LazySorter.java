/*******************************************************************************
 * Educational Online Test Delivery System
 * Copyright (c) 2016 American Institutes for Research
 * 
 * Distributed under the AIR Open Source License, Version 1.0
 * See accompanying file AIR-License-1_0.txt or at 
 * http://www.smarterapp.org/documents/American_Institutes_for_Research_Open_Source_Software_License.pdf
 ******************************************************************************/
package tds.tdsadmin.model;

import java.util.Comparator;
import java.util.Date;

import org.primefaces.model.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.util.FieldUtils;

public class LazySorter implements Comparator<TestOpportunity> {

	private static final Logger _logger = LoggerFactory.getLogger(LazySorter.class);

	private String sortField;

	private SortOrder sortOrder;

	public LazySorter(String sortField, SortOrder sortOrder) {
		this.sortField = sortField;
		this.sortOrder = sortOrder;
	}

	public int compare(TestOpportunity TestOpportunity1, TestOpportunity TestOpportunity2) {
		try {

			Object value1 = FieldUtils.getFieldValue(TestOpportunity1, sortField);// TestOpportunity.class.getField(this.sortField).get(TestOpportunity1);
			Object value2 = FieldUtils.getFieldValue(TestOpportunity2, sortField);// TestOpportunity.class.getField(this.sortField).get(TestOpportunity2);

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
			_logger.error(e.getMessage(), e);
			throw new RuntimeException();
		}
	}
}