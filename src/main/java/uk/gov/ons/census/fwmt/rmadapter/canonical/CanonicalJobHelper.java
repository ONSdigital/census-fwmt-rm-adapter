package uk.gov.ons.census.fwmt.rmadapter.canonical;

import uk.gov.ons.census.fwmt.canonical.v1.Address;
import uk.gov.ons.census.fwmt.canonical.v1.CancelFieldWorkerJobRequest;
import uk.gov.ons.census.fwmt.canonical.v1.Contact;
import uk.gov.ons.census.fwmt.canonical.v1.CreateFieldWorkerJobRequest;
import uk.gov.ons.census.fwmt.canonical.v1.Pause;
import uk.gov.ons.census.fwmt.canonical.v1.UpdateFieldWorkerJobRequest;
import uk.gov.ons.census.fwmt.common.error.GatewayException;
import uk.gov.ons.ctp.response.action.message.instruction.ActionAddress;
import uk.gov.ons.ctp.response.action.message.instruction.ActionContact;
import uk.gov.ons.ctp.response.action.message.instruction.ActionInstruction;
import uk.gov.ons.ctp.response.action.message.instruction.ActionPause;
import uk.gov.ons.ctp.response.action.message.instruction.ActionRequest;

import javax.xml.datatype.XMLGregorianCalendar;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.UUID;

import static uk.gov.ons.census.fwmt.common.data.modelcase.CaseRequest.TypeEnum.HH;

public final class CanonicalJobHelper {

  public static CreateFieldWorkerJobRequest newCreateJob(ActionInstruction actionInstruction) throws GatewayException {
    CreateFieldWorkerJobRequest createJobRequest = new CreateFieldWorkerJobRequest();
    ActionRequest actionRequest = actionInstruction.getActionRequest();
    ActionAddress actionAddress = actionRequest.getAddress();
    ActionContact actionContact = actionRequest.getContact();
    ActionPause actionPause = actionRequest.getPause();

    createJobRequest.setCaseId(UUID.fromString(actionRequest.getCaseId()));
    createJobRequest.setCaseReference(actionRequest.getCaseRef());
    createJobRequest.setCaseType(setCaseType(actionRequest));
    createJobRequest.setSurveyType("Treatment ID");
    createJobRequest.setEstablishmentType(actionAddress.getEstabType());
    createJobRequest.setMandatoryResource(actionRequest.getFieldOfficerID());
    createJobRequest.setCoordinatorId(actionRequest.getCoordinatorId());
    createJobRequest.setActionType(actionRequest.getActionType());

    Contact contact = getContact(actionContact, actionAddress);
    createJobRequest.setContact(contact);

    Address address = getAddress(actionAddress);
    createJobRequest.setAddress(address);

    createJobRequest.setUua(actionRequest.isUndeliveredAsAddress());

    checkIfSai(createJobRequest, actionAddress);

    Pause pause = getPause(actionPause);
    createJobRequest.setPause(pause);

    // TODO better login for handling this - only used in CE OR CCS jobs
    if (actionRequest.getAddressType().equals("CSS")) {
      createJobRequest.setCcsQuestionnaireURL(actionRequest.getCcsQuestionaireUrl());
    }
    if (actionRequest.getAddressType().equals("CE")) {
      createJobRequest.setCeDeliveryRequired(actionRequest.isCeDeliveryReqd());
    }
    if (actionRequest.getAddressType().equals("CE")) {
      createJobRequest.setCeCE1Complete(actionRequest.isCeCE1Complete());
    }

    if (actionRequest.getAddressType().equals("CE")) {
      createJobRequest.setCeExpectedResponses(actionRequest.getCeExpectedResponses().intValue());
    }
    if (actionRequest.getAddressType().equals("CE")) {
      createJobRequest.setCeActualResponses(actionRequest.getCeActualdResponses().intValue());
    }

    return createJobRequest;
  }

  private static Pause getPause(ActionPause actionPause) {
    Pause pause = new Pause();
    //    pause.setEffectiveDate(convertXmlGregorianCalendarToDate(actionPause.getEffectiveDate()));
    pause.setCode(actionPause.getCode());
    pause.setReason(actionPause.getReason());
    pause.setHoldUntil(convertXmlGregorianToOffsetDateTime(actionPause.getHoldUntil()));

    return pause;
  }

  private static Contact getContact(ActionContact actionContact, ActionAddress actionAddress) {
    Contact contact = new Contact();
    contact.setForename(actionContact.getForename());
    contact.setSurname(actionContact.getSurname());
    contact.setOrganisationName(actionAddress.getOrganisationName());
    contact.setPhoneNumber(actionContact.getPhoneNumber());

    return contact;
  }

  private static Address getAddress(ActionAddress actionAddress) {
    Address address = new Address();
    address.setArid(actionAddress.getArid());
    address.setUprn(actionAddress.getUprn());
    address.setLine1(actionAddress.getLine1());
    address.setLine2(actionAddress.getLine2());
    address.setLine3(actionAddress.getLine3());
    address.setTownName(actionAddress.getTownName());
    address.setPostCode(actionAddress.getPostcode());
    address.setLatitude(actionAddress.getLatitude());
    address.setLongitude(actionAddress.getLongitude());

    return address;
  }

  private static String setCaseType(ActionRequest actionRequest) throws GatewayException {
    String addressType = actionRequest.getAddressType();
    String addressLevel = actionRequest.getAddressLevel();

    if (addressType.equals("HH")) {
      return "Household";
    } else if (addressType.equals("CE") && addressLevel.equals("E")) {
      return "CE";
    } else if (addressType.equals("CE") && addressLevel.equals("U")) {
      return "CE Unit Level";
    } else if (addressType.equals("SPG")) {
      return "CE SPG";
    } else if (addressType.equals("CSS Int")) {
      return "CSS Interview";
    } else {
      // TODO return a default string or throw an exception?
      throw new GatewayException(GatewayException.Fault.SYSTEM_ERROR, "Unable to set case type using "
              + addressType + " and " + addressLevel);
    }
  }

  private static void checkIfSai(CreateFieldWorkerJobRequest createJobRequest, ActionAddress actionAddress) {
    if (String.valueOf(actionAddress.getType()).equals(String.valueOf(HH)) && actionAddress.getEstabType()
        .equals("Sheltered Accommodation")) {
      createJobRequest.setSai(true);
    } else {
      createJobRequest.setSai(false);
    }
  }

  private static Date convertXmlGregorianCalendarToDate(XMLGregorianCalendar xmlGregorianCalendar) {
    GregorianCalendar cal = xmlGregorianCalendar.toGregorianCalendar();

    return cal.getTime();
  }

  private static OffsetDateTime convertXmlGregorianToOffsetDateTime(XMLGregorianCalendar xmlGregorianCalendar) {
    GregorianCalendar cal = xmlGregorianCalendar.toGregorianCalendar();
    Date date = cal.getTime();

    return OffsetDateTime.ofInstant(date.toInstant(), ZoneId.of("+01:00"));
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
