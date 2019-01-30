package uk.gov.ons.fwmt.census.rmadapter.canonical;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import uk.gov.ons.ctp.response.action.message.instruction.ActionAddress;
import uk.gov.ons.ctp.response.action.message.instruction.ActionInstruction;
import uk.gov.ons.ctp.response.action.message.instruction.ActionRequest;
import uk.gov.ons.fwmt.fwmtgatewaycommon.data.Address;
import uk.gov.ons.fwmt.fwmtgatewaycommon.data.Contact;
import uk.gov.ons.fwmt.fwmtgatewaycommon.data.FWMTCancelJobRequest;
import uk.gov.ons.fwmt.fwmtgatewaycommon.data.FWMTCreateJobRequest;
import uk.gov.ons.fwmt.fwmtgatewaycommon.data.FWMTUpdateJobRequest;
import uk.gov.ons.fwmt.fwmtgatewaycommon.error.CTPException;

public final class CanonicalJobBuilder {

  public static FWMTCreateJobRequest newCreateJob(ActionInstruction actionInstruction) throws CTPException {
    FWMTCreateJobRequest fwmtCreateJobRequest = new FWMTCreateJobRequest();
    ActionRequest actionRequest = actionInstruction.getActionRequest();
    ActionAddress actionAddress = actionRequest.getAddress();

    Address address = new Address();
    address.setLine1(actionAddress.getLine1());
    address.setLine2(actionAddress.getLine2());
    address.setTownName(actionAddress.getTownName());
    address.setPostCode(actionAddress.getPostcode());
    address.setLatitude(actionAddress.getLatitude());
    address.setLongitude(actionAddress.getLongitude());
    address.setOrganisationName(actionAddress.getOrganisationName());
    
    // TODO not yet implemented in Canonical
    //address.setCategory(actionAddress.getCategory());

    fwmtCreateJobRequest.setJobIdentity(actionRequest.getCaseRef()); 
    fwmtCreateJobRequest.setSurveyType(actionRequest.getSurveyRef()); 
    //TODO set as per data mapping
    //fwmtCreateJobRequest.setMandatoryResourceAuthNo(actionRequest();
    //fwmtCreateJobRequest.setPreallocatedJob();

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    try {
      fwmtCreateJobRequest.setDueDate(LocalDate.parse(actionRequest.getReturnByDate(), formatter)); 
    } catch (RuntimeException e) {
      throw new CTPException(CTPException.Fault.SYSTEM_ERROR, e,
          "Failed to convert return by date, expected format dd/MM/yyyy: ",
          actionRequest.getReturnByDate());
    }

    fwmtCreateJobRequest.setAddress(address);
    fwmtCreateJobRequest.setActionType("Create");
    
    Contact contact = new Contact();
    contact.setEmail(actionRequest.getContact().getEmailAddress());
    contact.setForename(actionRequest.getContact().getForename());
    contact.setPhoneNumber(actionRequest.getContact().getPhoneNumber());
    contact.setSurname(actionRequest.getContact().getSurname());
    
    fwmtCreateJobRequest.setContact(contact);
    
    Map<String, String> additionalPropertiesMap = new HashMap<>();
    additionalPropertiesMap.put("caseId", actionRequest.getCaseId());
    additionalPropertiesMap.put("establishmentType", actionRequest.getAddress().getEstabType());
    additionalPropertiesMap.put("region", actionRequest.getRegion());
    fwmtCreateJobRequest.setAdditionalProperties(additionalPropertiesMap);

    return fwmtCreateJobRequest;
  }

  public static FWMTCancelJobRequest newCancelJob(ActionInstruction actionInstruction) {
    FWMTCancelJobRequest fwmtCancelJobRequest = new FWMTCancelJobRequest();
    fwmtCancelJobRequest.setActionType("Cancel");
    fwmtCancelJobRequest.setJobIdentity(actionInstruction.getActionCancel().getCaseRef());
    fwmtCancelJobRequest.setReason(actionInstruction.getActionCancel().getReason());

    return fwmtCancelJobRequest;
  }

  public static FWMTUpdateJobRequest newUpdateJob(ActionInstruction actionInstruction) {
    FWMTUpdateJobRequest fwmtUpdateJobRequest = new FWMTUpdateJobRequest();
    fwmtUpdateJobRequest.setActionType("update");

    return fwmtUpdateJobRequest;
  }
}
