package org.openmrs.module.facilitydata.web.controller;

import org.openmrs.Concept;
import org.openmrs.Location;
import org.openmrs.api.context.Context;
import org.openmrs.api.db.hibernate.DbSessionFactory;
import org.openmrs.module.facilitydata.model.CodedFacilityDataQuestionType;
import org.openmrs.module.facilitydata.model.DocumentTypeFacilityDataQuestionType;
import org.openmrs.module.facilitydata.model.FacilityDataCodedOption;
import org.openmrs.module.facilitydata.model.FacilityDataFormQuestion;
import org.openmrs.module.facilitydata.model.FacilityDataQuestion;
import org.openmrs.module.facilitydata.model.FacilityDataQuestionType;
import org.openmrs.module.facilitydata.model.FacilityDataValue;
import org.openmrs.module.facilitydata.model.FreeTextFacilityDataQuestionType;
import org.openmrs.module.facilitydata.model.NumericFacilityDataQuestionType;
import org.openmrs.module.facilitydata.model.enums.DocumentType;
import org.openmrs.module.facilitydata.service.FacilityDataService;
import org.openmrs.module.facilitydata.validator.FacilityDataQuestionTypeValidator;
import org.openmrs.module.webservices.rest.web.RestConstants;
import org.openmrs.util.OpenmrsConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


import java.text.ParseException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.InvalidParameterException;
import java.util.Map.Entry;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.SQLQuery;
import org.hibernate.procedure.ProcedureCall;
import org.hibernate.transform.AliasedTupleSubsetResultTransformer;

import javax.persistence.ParameterMode;
import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping(value = "/rest/" + RestConstants.VERSION_1 + "/facilitydata/data")
public class FacilityDataValueController {

	
	@RequestMapping(value = "/{uuid}", method = RequestMethod.GET)
	@ResponseBody
	public facilitydata getEncounterDumps(HttpServletRequest request,
			@PathVariable(value="uuid") String uuid) {

	    FacilityDataService facilityDataService= Context.getService(FacilityDataService.class);
	    FacilityDataValue fdv = facilityDataService.getFacilityDataValueByUuid(uuid);
	    
	    facilitydata fd = new facilitydata();
	    fd.setUuid(fdv.getUuid());
	    fd.setFacility(fdv.getFacility().getName());
	    fd.setFromDate(fdv.getFromDate());
	    fd.setToDate(fdv.getToDate());
	    fd.setComments(fdv.getComments());
	    fd.setQuestion(fdv.getQuestion().getName());
	    if(fdv.getValueCoded() != null)
	    	fd.setValueCoded(fdv.getValueCoded().getName());
	    fd.setValueNumeric(fdv.getValueNumeric());
	    fd.setValueText(fdv.getValueText());
	    
		return fd;

	}
	
	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public String getReportByName(HttpServletRequest request,
			@RequestBody facilitydata[] fds) {
		
	    FacilityDataService facilityDataService= Context.getService(FacilityDataService.class);
	    for(int i = 0; i < fds.length; i++){
	    	
	    	facilitydata fd = (facilitydata) fds[i];	
	    	
		    FacilityDataValue fdv = new FacilityDataValue();
		    Location loc = Context.getLocationService().getLocationByUuid(fd.getFacility());
		    //Location loc = Context.getLocationService().getLocationByUuid("454cab34-0fba-4744-897e-be913d67cf80");
		    fdv.setFacility(loc);
		    fdv.setFromDate(fd.getFromDate());
		    fdv.setToDate(fd.getToDate());
		    FacilityDataFormQuestion fdq = facilityDataService.getFacilityDataFormQuestion(fd.getQuestion());
		    //FacilityDataFormQuestion fdq = facilityDataService.getFacilityDataFormQuestion("77cf5d94-c681-4969-9a21-2c9025e1df67");
		    fdv.setQuestion(fdq);
		    if(fd.getValueCoded() != null){
		    	FacilityDataCodedOption fdco = facilityDataService.getCodedoptionByUuid(fd.getValueCoded());
		    	fdv.setValueCoded(fdco);
		    }
		    if(fd.getValueNumeric() != null)
		    	fdv.setValueNumeric(fd.getValueNumeric());
		    if(fd.getValueText() != null)
		    	fdv.setValueText(fd.getValueText());
		    
		    facilityDataService.saveFacilityDataValue(fdv);
	    }
		
	    return "Success.";
		
	}

	
	@RequestMapping(value = "/{uuid}", method = RequestMethod.POST)
    public String saveQuestionType(HttpServletRequest request, 
    		@RequestBody facilitydata fd) throws Exception {
    	
    	/*FacilityDataQuestionType questionType = getQuestionType(id, dataType);
    	questionType.setName(name);
    	questionType.setDescription(description);
		questionType.setFieldStyle(fieldStyle);
    	
    	if (questionType instanceof NumericFacilityDataQuestionType) {
    		NumericFacilityDataQuestionType numericType = (NumericFacilityDataQuestionType) questionType;
    		numericType.setMinValue(minValue);
    		numericType.setMaxValue(maxValue);
    		numericType.setAllowDecimals(allowDecimals != null && allowDecimals == Boolean.TRUE);
    	}
    	else if(questionType instanceof FreeTextFacilityDataQuestionType) {
			FreeTextFacilityDataQuestionType freeTextType=(FreeTextFacilityDataQuestionType)questionType;
			freeTextType.setQuestionText(questionText);
		}
		else if(questionType instanceof DocumentTypeFacilityDataQuestionType) {
			DocumentTypeFacilityDataQuestionType documentTypeQuestion=(DocumentTypeFacilityDataQuestionType)questionType;
			documentTypeQuestion.setDocumentType(DocumentType.valueOf(documentType));
		}
    	else if (questionType instanceof CodedFacilityDataQuestionType) {
    		CodedFacilityDataQuestionType codedType = (CodedFacilityDataQuestionType) questionType;

        	Map<Integer, Integer> codedOptionBreakdown = getService().getCodedOptionBreakdown();
        	Date now = new Date();
			List<Integer> passedOptionIds = Arrays.asList(optionId);

			// Get existing options from the question and clear the question out
			Map<Integer, FacilityDataCodedOption> existingOptions = new HashMap<Integer, FacilityDataCodedOption>();
			for (FacilityDataCodedOption option : codedType.getOptions()) {
				existingOptions.put(option.getId(), option);
			}
			codedType.getOptions().clear();

			// Go through and retire or delete options that have been removed
			List<Integer> needRetiring = new ArrayList<Integer>();
			for (Iterator<Integer> existingOptionIdIterator = existingOptions.keySet().iterator(); existingOptionIdIterator.hasNext();) {
				Integer existingOptionId = existingOptionIdIterator.next();
				if (!passedOptionIds.contains(existingOptionId)) { // In this case we need to delete or retire
					Integer numValues = codedOptionBreakdown.get(existingOptionId);
					if (numValues == null || numValues.intValue() == 0) { // If no values are saved for it, we can delete
						existingOptionIdIterator.remove();
					}
					else { // If values exist for it, we have to retire
						needRetiring.add(existingOptionId);
					}
				}
			}

        	// Now add the passed in options using the new order

			for (int i=0; i<passedOptionIds.size(); i++) {
				if (optionCode[i].length() > 0 && optionName[i].length() > 0) {
					Integer optId = passedOptionIds.get(i);
					FacilityDataCodedOption codedOption = existingOptions.get(optId);
					if (codedOption == null) {
						codedOption = new FacilityDataCodedOption();
					}
					codedOption.setName(optionName[i]);
					codedOption.setCode(optionCode[i]);
					codedOption.setDescription(optionDescription[i]);
					codedOption.setRetired(false);
					codedOption.setRetiredBy(null);
					codedOption.setDateRetired(null);
					codedOption.setQuestionType(codedType);
					//codedType.addFacilityDataCodedOption(codedOption);
					codedType.getOptions().add(codedOption);
				}
        	}

			for (Integer retiredId : needRetiring) {
				FacilityDataCodedOption option = existingOptions.get(retiredId);
				option.setRetired(true);
				option.setRetiredBy(Context.getAuthenticatedUser());
				option.setDateRetired(now);
				//codedType.addFacilityDataCodedOption(option);
				codedType.getOptions().add(option);
			}
    		
    	}
    	
    	BindingResult result = new BeanPropertyBindingResult(questionType, "questionType");
        new FacilityDataQuestionTypeValidator().validate(questionType, result);
        if (result.hasErrors()) {
            return "/module/facilitydata/questionTypeForm";
        }
        Context.getService(FacilityDataService.class).saveQuestionType(questionType);
        return "redirect:questionType.list";*/
		return fd.getValueText();
    }


}
