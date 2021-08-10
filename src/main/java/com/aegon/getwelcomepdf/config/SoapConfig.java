package com.aegon.getwelcomepdf.config;

import org.apache.cxf.endpoint.Client;
import org.apache.cxf.endpoint.Endpoint;
import org.apache.cxf.ext.logging.LoggingInInterceptor;
import org.apache.cxf.ext.logging.LoggingOutInterceptor;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.message.Message;
import org.apache.cxf.transport.http.HTTPConduit;
import org.apache.cxf.transports.http.configuration.HTTPClientPolicy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.tempuri.Service1;
import org.tempuri.Service1Soap;


@Configuration
public class SoapConfig {
    private final String endpoint = "**********************";
    @Bean
    public Service1Soap getAddress() {

        Service1 getAddressService = new Service1();
        Service1Soap sbmService = getAddressService.getService1Soap();

        Client client = ClientProxy.getClient(sbmService);
        client.getRequestContext().put(Message.ENDPOINT_ADDRESS, endpoint);
        Endpoint endpoint = client.getEndpoint();


        LoggingOutInterceptor outInterceptor = new LoggingOutInterceptor();
        LoggingInInterceptor inInterceptor = new LoggingInInterceptor();
        endpoint.getOutInterceptors().add(outInterceptor);
        endpoint.getInInterceptors().add(inInterceptor);

        HTTPConduit httpConduit = (HTTPConduit) client.getConduit();
        HTTPClientPolicy policy = httpConduit.getClient();
   //     policy.setReceiveTimeout(Long.parseLong(reqTimeout));
   //     policy.setConnectionTimeout(Long.parseLong(conTimetout));
        return sbmService;
    }

}
