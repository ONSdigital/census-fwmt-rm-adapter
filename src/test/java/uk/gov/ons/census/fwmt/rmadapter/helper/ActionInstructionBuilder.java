package uk.gov.ons.census.fwmt.rmadapter.helper;

import org.checkerframework.checker.units.qual.A;
import uk.gov.ons.ctp.response.action.message.instruction.ActionAddress;
import uk.gov.ons.ctp.response.action.message.instruction.ActionCancel;
import uk.gov.ons.ctp.response.action.message.instruction.ActionContact;
import uk.gov.ons.ctp.response.action.message.instruction.ActionInstruction;
import uk.gov.ons.ctp.response.action.message.instruction.ActionRequest;
import uk.gov.ons.ctp.response.action.message.instruction.ActionUpdate;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.XMLGregorianCalendar;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.OffsetDateTime;

import static uk.gov.ons.census.fwmt.rmadapter.utils.UtilityMethods.getXMLGregorianCalendarNow;

public class ActionInstructionBuilder {

  public ActionInstruction createActionInstructionBuilder() {
    ActionInstruction actionInstruction = new ActionInstruction();
    ActionRequest actionRequest = new ActionRequest();
    ActionAddress actionAddress = new ActionAddress();

    actionRequest.setCaseId("8ed3fc08-e95f-44db-a6d7-cde4e76a6182");
    actionRequest.setSurveyRef("testSurveyRef");
    actionRequest.setReturnByDate("11/11/2000");
    actionRequest.setUndeliveredAsAddress(false);
    actionRequest.setBlankQreReturned(false);

    ActionContact contact = new ActionContact();

    actionAddress.setLatitude(BigDecimal.valueOf(1000.00));
    actionAddress.setLongitude(BigDecimal.valueOf(1000.00));
    actionAddress.setLine1("addressLine1");
    actionAddress.setLine2("addressLine2");
    actionAddress.setPostcode("testPostcode");
    actionAddress.setTownName("testTownName");
    actionAddress.setCountry("E");

    actionRequest.setAddress(actionAddress);
    actionInstruction.setActionRequest(actionRequest);
    actionRequest.setContact(contact);
    actionRequest.setCoordinatorId("coordID");

    actionRequest.setAddressType("HH");

    return actionInstruction;
  }

  public ActionInstruction createActionInstructionBuilderCEE() {
    ActionInstruction actionInstruction = new ActionInstruction();
    ActionRequest actionRequest = new ActionRequest();
    ActionAddress actionAddress = new ActionAddress();

    actionRequest.setCaseId("8ed3fc08-e95f-44db-a6d7-cde4e76a6182");
    actionRequest.setSurveyRef("testSurveyRef");
    actionRequest.setReturnByDate("11/11/2000");
    actionRequest.setUndeliveredAsAddress(false);
    actionRequest.setBlankQreReturned(false);

    actionAddress.setLatitude(BigDecimal.valueOf(1000.00));
    actionAddress.setLongitude(BigDecimal.valueOf(1000.00));
    actionAddress.setLine1("addressLine1");
    actionAddress.setLine2("addressLine2");
    actionAddress.setPostcode("testPostcode");
    actionAddress.setTownName("testTownName");
    actionAddress.setCountry("E");

    actionRequest.setAddress(actionAddress);
    actionInstruction.setActionRequest(actionRequest);
    actionRequest.setCoordinatorId("coordID");
    actionRequest.setAddressType("CE");
    actionRequest.setAddressLevel("E");
    actionRequest.setCeDeliveryReqd(true);
    actionRequest.setCeCE1Complete(false);
    actionRequest.setCeExpectedResponses(BigInteger.valueOf(20));
    actionRequest.setCeActualResponses(BigInteger.valueOf(15));

    return actionInstruction;
  }

  public ActionInstruction createActionInstructionBuilderCEU() {
    ActionInstruction actionInstruction = new ActionInstruction();
    ActionRequest actionRequest = new ActionRequest();
    ActionAddress actionAddress = new ActionAddress();

    actionRequest.setCaseId("8ed3fc08-e95f-44db-a6d7-cde4e76a6182");
    actionRequest.setSurveyRef("testSurveyRef");
    actionRequest.setReturnByDate("11/11/2000");
    actionRequest.setUndeliveredAsAddress(false);
    actionRequest.setBlankQreReturned(false);

    actionAddress.setLatitude(BigDecimal.valueOf(1000.00));
    actionAddress.setLongitude(BigDecimal.valueOf(1000.00));
    actionAddress.setLine1("addressLine1");
    actionAddress.setLine2("addressLine2");
    actionAddress.setPostcode("testPostcode");
    actionAddress.setTownName("testTownName");
    actionAddress.setCountry("E");

    actionRequest.setAddress(actionAddress);
    actionInstruction.setActionRequest(actionRequest);

    actionRequest.setAddressType("CE");
    actionRequest.setAddressLevel("U");
    actionRequest.setCoordinatorId("coordID");
    actionRequest.setCeDeliveryReqd(true);
    actionRequest.setCeCE1Complete(false);
    actionRequest.setCeExpectedResponses(BigInteger.valueOf(20));
    actionRequest.setCeActualResponses(BigInteger.valueOf(15));

    return actionInstruction;
  }

  public ActionInstruction cancelActionInstructionBuilder() {
    ActionInstruction actionInstruction = new ActionInstruction();
    ActionCancel actionCancel = new ActionCancel();

    actionCancel.setReason("reason");
    actionCancel.setActionId("testActionID");
    actionCancel.setCaseRef("testCaseRef");
    actionCancel.setCaseId("8ed3fc08-e95f-44db-a6d7-cde4e76a6182");
    actionCancel.setAddressType("HH");

    actionInstruction.setActionCancel(actionCancel);

    return actionInstruction;
  }

  public ActionInstruction updateActionInstructionBuilder() throws DatatypeConfigurationException {
    ActionInstruction actionInstruction = new ActionInstruction();
    ActionUpdate actionUpdate = new ActionUpdate();

    actionUpdate.setCaseId("8ed3fc08-e95f-44db-a6d7-cde4e76a6182");
    actionUpdate.setAddressType("HH");
    actionUpdate.setBlankQreReturned(false);
    actionUpdate.setActionableFrom(getXMLGregorianCalendarNow());
    actionUpdate.setCeActualResponses(BigInteger.valueOf(0));
    actionUpdate.setCeExpectedResponses(BigInteger.valueOf(0));

    actionInstruction.setActionUpdate(actionUpdate);

    return actionInstruction;
  }

  public ActionInstruction updateCCSActionInstructionBuilder() throws DatatypeConfigurationException {
    ActionInstruction actionInstruction = new ActionInstruction();
    ActionUpdate actionUpdate = new ActionUpdate();

    actionUpdate.setCaseId("8ed3fc08-e95f-44db-a6d7-cde4e76a6182");
    actionUpdate.setAddressType("CCSPL");
    actionUpdate.setBlankQreReturned(false);
    actionUpdate.setActionableFrom(getXMLGregorianCalendarNow());
    actionUpdate.setCeActualResponses(BigInteger.valueOf(0));
    actionUpdate.setCeExpectedResponses(BigInteger.valueOf(0));

    actionInstruction.setActionUpdate(actionUpdate);

    return actionInstruction;
  }

  public ActionInstruction createNisraActionInstructionBuilder() throws DatatypeConfigurationException {
    ActionInstruction actionInstruction = new ActionInstruction();
    ActionRequest actionRequest = new ActionRequest();
    ActionAddress actionAddress = new ActionAddress();

    actionRequest.setCaseId("8ed3fc08-e95f-44db-a6d7-cde4e76a6182");
    actionRequest.setSurveyRef("testSurveyRef");
    actionRequest.setReturnByDate("11/11/2000");
    actionRequest.setUndeliveredAsAddress(false);
    actionRequest.setBlankQreReturned(false);

    actionAddress.setLatitude(BigDecimal.valueOf(1000.00));
    actionAddress.setLongitude(BigDecimal.valueOf(1000.00));
    actionAddress.setLine1("addressLine1");
    actionAddress.setLine2("addressLine2");
    actionAddress.setPostcode("testPostcode");
    actionAddress.setTownName("testTownName");
    actionAddress.setCountry("N");

    actionRequest.setAddress(actionAddress);
    actionInstruction.setActionRequest(actionRequest);
    actionRequest.setFieldOfficerId("testFieldOfficer");
    actionRequest.setCoordinatorId("testCoord");

    actionRequest.setAddressType("HH");

    return actionInstruction;
  }

  public ActionInstruction createIncorrectNisraActionInstructionBuilder() throws DatatypeConfigurationException {
    ActionInstruction actionInstruction = new ActionInstruction();
    ActionRequest actionRequest = new ActionRequest();
    ActionAddress actionAddress = new ActionAddress();

    actionRequest.setCaseId("8ed3fc08-e95f-44db-a6d7-cde4e76a6182");
    actionRequest.setSurveyRef("testSurveyRef");
    actionRequest.setReturnByDate("11/11/2000");
    actionRequest.setUndeliveredAsAddress(false);
    actionRequest.setBlankQreReturned(false);

    actionAddress.setLatitude(BigDecimal.valueOf(1000.00));
    actionAddress.setLongitude(BigDecimal.valueOf(1000.00));
    actionAddress.setLine1("addressLine1");
    actionAddress.setLine2("addressLine2");
    actionAddress.setPostcode("testPostcode");
    actionAddress.setTownName("testTownName");
    actionAddress.setCountry("N");

    actionRequest.setAddress(actionAddress);
    actionInstruction.setActionRequest(actionRequest);
    actionRequest.setFieldOfficerId("");

    actionRequest.setAddressType("HH");

    return actionInstruction;
  }

  public ActionInstruction cancelActionInstructionBuilderForPause() {
    ActionInstruction actionInstruction = new ActionInstruction();
    ActionCancel actionCancel = new ActionCancel();

    actionCancel.setReason("reason");
    actionCancel.setActionId("testActionID");
    actionCancel.setCaseRef("testCaseRef");
    actionCancel.setCaseId("8ed3fc08-e95f-44db-a6d7-cde4e76a6182");
    actionCancel.setAddressType("HH");

    actionInstruction.setActionCancel(actionCancel);

    return actionInstruction;
  }

  public ActionInstruction cancelActionInstructionBuilderForNonHouseHold() {
    ActionInstruction actionInstruction = new ActionInstruction();
    ActionCancel actionCancel = new ActionCancel();

    actionCancel.setReason("reason");
    actionCancel.setActionId("testActionID");
    actionCancel.setCaseRef("testCaseRef");
    actionCancel.setCaseId("8ed3fc08-e95f-44db-a6d7-cde4e76a6182");
    actionCancel.setAddressType("CC");

    actionInstruction.setActionCancel(actionCancel);

    return actionInstruction;
  }

  public ActionInstruction createActionInstructionBuilderWithoutCoordId() {
    ActionInstruction actionInstruction = new ActionInstruction();
    ActionRequest actionRequest = new ActionRequest();
    ActionAddress actionAddress = new ActionAddress();

    actionRequest.setCaseId("8ed3fc08-e95f-44db-a6d7-cde4e76a6182");
    actionRequest.setSurveyRef("testSurveyRef");
    actionRequest.setReturnByDate("11/11/2000");
    actionRequest.setUndeliveredAsAddress(false);
    actionRequest.setBlankQreReturned(false);

    actionAddress.setLatitude(BigDecimal.valueOf(1000.00));
    actionAddress.setLongitude(BigDecimal.valueOf(1000.00));
    actionAddress.setLine1("addressLine1");
    actionAddress.setLine2("addressLine2");
    actionAddress.setPostcode("testPostcode");
    actionAddress.setTownName("testTownName");
    actionAddress.setCountry("E");

    actionRequest.setAddress(actionAddress);
    actionInstruction.setActionRequest(actionRequest);

    actionRequest.setAddressType("HH");

    return actionInstruction;
  }
}
