package uk.gov.ons.fwmt.census.rmadapter.canonical;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import uk.gov.ons.ctp.response.action.message.instruction.ActionAddress;
import uk.gov.ons.ctp.response.action.message.instruction.ActionInstruction;
import uk.gov.ons.ctp.response.action.message.instruction.ActionRequest;
import uk.gov.ons.fwmt.census.canonical.v1.Address;
import uk.gov.ons.fwmt.census.canonical.v1.CancelFieldWorkerJobRequest;
import uk.gov.ons.fwmt.census.canonical.v1.Contact;
import uk.gov.ons.fwmt.census.canonical.v1.CreateFieldWorkerJobRequest;
import uk.gov.ons.fwmt.census.canonical.v1.UpdateFieldWorkerJobRequest;
import uk.gov.ons.fwmt.census.common.error.GatewayException;

public final class CanonicalJobHelper {

  public static CreateFieldWorkerJobRequest newCreateJob(ActionInstruction actionInstruction) throws GatewayException {
    CreateFieldWorkerJobRequest createJobRequest = new CreateFieldWorkerJobRequest();
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

    createJobRequest.setJobIdentity(actionRequest.getCaseRef());
    createJobRequest.setCaseId(UUID.fromString(actionRequest.getCaseId()));
    createJobRequest.setSurveyType(actionRequest.getSurveyRef()); 
    //TODO set as per data mapping
    //fwmtCreateJobRequest.setMandatoryResourceAuthNo(actionRequest();
    //fwmtCreateJobRequest.setPreallocatedJob();

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    try {
      createJobRequest.setDueDate(LocalDate.parse(actionRequest.getReturnByDate(), formatter)); 
    } catch (RuntimeException e) {
      throw new GatewayException(GatewayException.Fault.SYSTEM_ERROR, e,
          "Failed to convert return by date, expected format dd/MM/yyyy: ",
          actionRequest.getReturnByDate());
    }

    createJobRequest.setAddress(address);
    createJobRequest.setActionType("Create");
    
    Contact contact = new Contact();
    contact.setEmail(actionRequest.getContact().getEmailAddress());
    contact.setForename(actionRequest.getContact().getForename());
    contact.setPhoneNumber(actionRequest.getContact().getPhoneNumber());
    contact.setSurname(actionRequest.getContact().getSurname());
    
    createJobRequest.setContact(contact);
    
    Map<String, String> additionalPropertiesMap = new HashMap<>();
    additionalPropertiesMap.put("establishmentType", actionRequest.getAddress().getEstabType());
    additionalPropertiesMap.put("region", actionRequest.getRegion());
    createJobRequest.setAdditionalProperties(additionalPropertiesMap);

    return createJobRequest;
  }

  public static CancelFieldWorkerJobRequest newCancelJob(ActionInstruction actionInstruction) {
    CancelFieldWorkerJobRequest cancelJobRequest = new CancelFieldWorkerJobRequest();
    cancelJobRequest.setActionType("Cancel");
    cancelJobRequest.setJobIdentity(actionInstruction.getActionCancel().getCaseRef());
    cancelJobRequest.setReason(actionInstruction.getActionCancel().getReason());

    return cancelJobRequest;
  }

  public static UpdateFieldWorkerJobRequest newUpdateJob(ActionInstruction actionInstruction) {
    UpdateFieldWorkerJobRequest updateJobRequest = new UpdateFieldWorkerJobRequest();
    updateJobRequest.setActionType("update");

    return updateJobRequest;
  }
}
