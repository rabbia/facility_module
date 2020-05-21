/**
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */
package org.openmrs.module.facilitydata.validator;

import org.openmrs.annotation.Handler;
import org.openmrs.module.facilitydata.model.FacilityDataQuestion;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * Validates a Facility Data Question
 */
@Handler(supports={FacilityDataQuestion.class})
public class FacilityDataQuestionValidator extends BaseFacilityMetadataValidator {

	/**
     * @see Validator#supports(Class)
     */
    @SuppressWarnings("unchecked")
    public boolean supports(Class c) {
	return FacilityDataQuestion.class.isAssignableFrom(c);
    }

    /**
	 * @see Validator#validate(Object, Errors)
	 */
    public void validate(Object o, Errors errors) {
    	super.validate(o, errors);
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "error.null");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "questionType", "error.null");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "periodApplicability", "error.null");
    }
}
