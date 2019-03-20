package uk.gov.ons.census.fwmt.rmadapter.utils;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.util.GregorianCalendar;

public final class UtilityMethods {

  public static XMLGregorianCalendar getXMLGregorianCalendarNow() throws DatatypeConfigurationException {
    GregorianCalendar gregorianCalendar = new GregorianCalendar();
    DatatypeFactory datatypeFactory = DatatypeFactory.newInstance();
    return datatypeFactory.newXMLGregorianCalendar(gregorianCalendar);
  }

}
