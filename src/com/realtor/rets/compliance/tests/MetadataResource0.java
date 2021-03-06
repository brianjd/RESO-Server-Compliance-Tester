/*
 * Test1.java
 *
 */
package com.realtor.rets.compliance.tests;

import java.util.Map;

import org.realtor.rets.retsapi.RETSTransaction;
import org.w3c.dom.Document;

import com.realtor.rets.compliance.TestResult;
import com.realtor.rets.compliance.tests.util.CollectionUtils;

/**
 * Extends the BaseEvaluator Interface to validate against a DTD
 *
 * @author pobrien
 */
public class MetadataResource0 extends BaseEvaluator {

  public MetadataResource0() {
    super();
    specReference = "";
  }

private String notes="";
private String docTypeLocation="";
boolean error=false;
private String errorList="";
  /**
   * Evaluate a set of transactions. This method is called by the TestExecuter
   * Checks the XML response against the RETS DTD
   *
   * @param trans Hash containing all transactions defined in a test script
   * @param testReport Report to which to add individual testResult objects.
   *
   * @return just returns an empty string for now.
   */
  protected TestResult processResults(String transName, RETSTransaction t) {
      if (t == null) {
        return null;
      }

      Map resp = t.getResponseMap();
      String cName = t.getClass().getName();

      Map m = CollectionUtils.copyLowerCaseMap(t.getResponseHeaderMap());
	  boolean isXml = CollectionUtils.hasValue(m,"content-type","text/xml");
      int cnt=CollectionUtils.keyCount(m,"content-type");

      if (cnt > 1)
        {
            String mContent = "Multiple Content-Type's are reported the response for transaction : "+
                               transName;
            String note = "Some clients may fail or be confused if multiple 'Content-Type' values returned ";
            return reportResult("MetadataResource0", mContent,"Info",note,"n/a");

        }


      if (isXml) {
        String body = (String) resp.get("body");
        return validateResource0(transName,body);
      } else {
        String notes = "Transaction " + cName
                       + " does not include a Content-Type of 'text/xml'";
        return reportResult("MetadataResource0",
                                              "Checks to see if the depth and breadth are correct",
                                              "Info", notes);
      }

  }

  /**
   * This is the method that does the actual check of a transaction body for a valid
   *  compact data response.
   *
   * @param transactionName Name of the Transaction checked (from the test script)
   * @param responseBody transaction body which should be valid compact
   *
   * @return results of the test.
   */
  protected TestResult validateResource0(String transactionName,
                                       String responseBody) {
    Document document = null;
    boolean valid=true;
    String status = "Failure";
	notes="";
	errorList="";
	 error=false;
    String jException="";



      if ((responseBody != null) && (responseBody.length() > 0)) {

		notes=notes+responseBody;

		boolean found=(responseBody.indexOf("<METADATA-CLASS")>=0)
		||(responseBody.indexOf("<METADATA-OBJECT")>=0)
		||(responseBody.indexOf("<METADATA-SEARCH_HELP")>=0)
		||(responseBody.indexOf("<METADATA-EDITMASK")>=0)
		||(responseBody.indexOf("<METADATA-LOOKUP")>=0)
		||(responseBody.indexOf("<METADATA-UPDATE_HELP")>=0)
		||(responseBody.indexOf("<METADATA-VALIDATION_LOOKUP")>=0)
		||(responseBody.indexOf("<METADATA-VALIDATION_EXPRESSION")>=0)
		||(responseBody.indexOf("<METADATA-VALIDATION_EXTERNAL")>=0);



		if(found){
		return reportResult("MetadataResource0:  "+transactionName,
		                        "Checks to see if the transaction has valid breadth and depth",
		                        status, "Child metadata beneath METADATA-RESOURCE was present: "+notes,jException,"n/a");
  		}else{
		 return reportResult("MetadataResource0:  "+transactionName,
                        "Checks to see if the transaction has valid breadth and depth",
                        "Success", notes,jException,"n/a");
  		}
	}

	return reportResult("MetadataResource0:  "+transactionName,
			                        "Checks to see if the transaction has valid breadth and depth",
		                        status, notes,jException,"n/a");

}




}
