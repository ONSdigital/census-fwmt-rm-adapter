package uk.gov.ons.census.fwmt.rmadapter.canonical;

import uk.gov.ons.census.fwmt.canonical.v1.Address;
import uk.gov.ons.census.fwmt.canonical.v1.CancelFieldWorkerJobRequest;
import uk.gov.ons.census.fwmt.canonical.v1.Contact;
import uk.gov.ons.census.fwmt.canonical.v1.CreateFieldWorkerJobRequest;
import uk.gov.ons.census.fwmt.canonical.v1.Pause;
import uk.gov.ons.census.fwmt.canonical.v1.UpdateFieldWorkerJobRequest;
import uk.gov.ons.census.fwmt.common.error.GatewayException;
import uk.gov.ons.ctp.response.action.message.instruction.ActionAddress;
import uk.gov.ons.ctp.response.action.message.instruction.ActionInstruction;
import uk.gov.ons.ctp.response.action.message.instruction.ActionRequest;

import java.util.UUID;

import static uk.gov.ons.census.fwmt.common.data.modelcase.CaseRequest.TypeEnum.HH;

public final class CanonicalJobHelper {

  public static CreateFieldWorkerJobRequest newCreateJob(ActionInstruction actionInstruction) throws GatewayException {
    CreateFieldWorkerJobRequest createJobRequest = new CreateFieldWorkerJobRequest();
    ActionRequest actionRequest = actionInstruction.getActionRequest();
    ActionAddress actionAddress = actionRequest.getAddress();

    createJobRequest.setCaseId(UUID.fromString(actionRequest.getCaseId()));
    createJobRequest.setCaseReference(actionRequest.getCaseRef());
    createJobRequest.setCaseReference("caseTypeCreate");
    createJobRequest.setSurveyType("surveyTypeCreate");
    createJobRequest.setEstablishmentType(actionAddress.getEstabType());

    // does not apply to HouseHold
    createJobRequest.setMandatoryResource(null);

    createJobRequest.setCoordinatorId("add after xsd update");

    Contact contact = new Contact();
    contact.setForename(actionRequest.getContact().getForename());
    contact.setSurname(actionRequest.getContact().getSurname());
    contact.setOrganisationName(actionAddress.getOrganisationName());
    contact.setPhoneNumber(actionRequest.getContact().getPhoneNumber());
    createJobRequest.setContact(contact);

    Address address = new Address();
    address.setArid("add once xsd update");
    address.setUprn("add once xsd update");
    address.setLine1(actionAddress.getLine1());
    address.setLine2(actionAddress.getLine2());
    address.setLine3("add once xsd update");
    address.setTownName(actionAddress.getTownName());
    address.setPostCode(actionAddress.getPostcode());
    address.setLatitude(actionAddress.getLatitude());
    address.setLongitude(actionAddress.getLongitude());
    createJobRequest.setAddress(address);

    createJobRequest.setUua("undeliveredAsAddress from xsd");

    if (String.valueOf(actionAddress.getType()).equals(String.valueOf(HH)) && actionAddress.getEstabType()
        .equals("Sheltered Accommodation")) {
      createJobRequest.setSai(true);
    } else {
      createJobRequest.setSai(false);
    }

    Pause pause = new Pause();
    // Need to be updated with new xsd changes
    //    pause.setEffectiveDate();
    pause.setCode("xsd");
    pause.setReason("xsd");
    //    pause.setHoldUntil("xsd");

    // Need to be corrected with new xsd
    createJobRequest.setCcsQuestionnaireURL("xsd");
    createJobRequest.setCeDeliveryRequired(false);
    createJobRequest.setCeCE1Complete(false);
    createJobRequest.setCeExpectedResponses(50);
    createJobRequest.setCeActualResponses(40);

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
