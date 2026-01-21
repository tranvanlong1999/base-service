/*******************************************************************************
 * Copyright (c) 2017 ANHTCN.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package com.example.baseservice.common.exception.model;

import lombok.Data;

@Data
public class MessageField {
	private String fieldName;
	private String message;

	public MessageField(String fieldName, String message) {
		super();
		this.fieldName = fieldName;
		this.message = message;
	}

}
