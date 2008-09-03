/**
 * PigLatinTranslatorLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Sep 23, 2006 (12:31:18 GMT+00:00) WSDL2Java emitter.
 */

package com.httprecipes.www._1._11.soap;

public class PigLatinTranslatorLocator extends org.apache.axis.client.Service implements com.httprecipes.www._1._11.soap.PigLatinTranslator {

    public PigLatinTranslatorLocator() {
    }


    public PigLatinTranslatorLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public PigLatinTranslatorLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for PigLatinTranslatorPort
    private java.lang.String PigLatinTranslatorPort_address = "http://www.httprecipes.com/1/11/soap/index.php";

    public java.lang.String getPigLatinTranslatorPortAddress() {
        return PigLatinTranslatorPort_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String PigLatinTranslatorPortWSDDServiceName = "PigLatinTranslatorPort";

    public java.lang.String getPigLatinTranslatorPortWSDDServiceName() {
        return PigLatinTranslatorPortWSDDServiceName;
    }

    public void setPigLatinTranslatorPortWSDDServiceName(java.lang.String name) {
        PigLatinTranslatorPortWSDDServiceName = name;
    }

    public com.httprecipes.www._1._11.soap.PigLatinTranslatorPortType getPigLatinTranslatorPort() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(PigLatinTranslatorPort_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getPigLatinTranslatorPort(endpoint);
    }

    public com.httprecipes.www._1._11.soap.PigLatinTranslatorPortType getPigLatinTranslatorPort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            com.httprecipes.www._1._11.soap.PigLatinTranslatorBindingStub _stub = new com.httprecipes.www._1._11.soap.PigLatinTranslatorBindingStub(portAddress, this);
            _stub.setPortName(getPigLatinTranslatorPortWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setPigLatinTranslatorPortEndpointAddress(java.lang.String address) {
        PigLatinTranslatorPort_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (com.httprecipes.www._1._11.soap.PigLatinTranslatorPortType.class.isAssignableFrom(serviceEndpointInterface)) {
                com.httprecipes.www._1._11.soap.PigLatinTranslatorBindingStub _stub = new com.httprecipes.www._1._11.soap.PigLatinTranslatorBindingStub(new java.net.URL(PigLatinTranslatorPort_address), this);
                _stub.setPortName(getPigLatinTranslatorPortWSDDServiceName());
                return _stub;
            }
        }
        catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        java.lang.String inputPortName = portName.getLocalPart();
        if ("PigLatinTranslatorPort".equals(inputPortName)) {
            return getPigLatinTranslatorPort();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://www.httprecipes.com/1/11/soap/", "PigLatinTranslator");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://www.httprecipes.com/1/11/soap/", "PigLatinTranslatorPort"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("PigLatinTranslatorPort".equals(portName)) {
            setPigLatinTranslatorPortEndpointAddress(address);
        }
        else 
{ // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}
