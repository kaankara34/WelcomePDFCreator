package org.tempuri;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;

/**
 * This class was generated by Apache CXF 3.4.2
 * 2021-03-26T08:38:52.762+03:00
 * Generated source version: 3.4.2
 *
 */
@WebService(targetNamespace = "http://tempuri.org/", name = "Service1Soap")
@XmlSeeAlso({ObjectFactory.class})
public interface Service1Soap {

    @WebMethod(operationName = "CallSbmService", action = "http://tempuri.org/CallSbmService")
    @RequestWrapper(localName = "CallSbmService", targetNamespace = "http://tempuri.org/", className = "org.tempuri.CallSbmService")
    @ResponseWrapper(localName = "CallSbmServiceResponse", targetNamespace = "http://tempuri.org/", className = "org.tempuri.CallSbmServiceResponse")
    @WebResult(name = "CallSbmServiceResult", targetNamespace = "http://tempuri.org/")
    public java.lang.String callSbmService(

        @WebParam(name = "tcId", targetNamespace = "http://tempuri.org/")
        java.lang.String tcId
    );
}
