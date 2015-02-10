package com.fantasy.file.ws.client;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMNamespace;
import org.apache.axiom.soap.SOAP11Constants;
import org.apache.axiom.soap.SOAPBody;
import org.apache.axiom.soap.SOAPEnvelope;
import org.apache.axiom.soap.SOAPFactory;
import org.apache.axis2.Constants;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.OperationClient;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.context.ConfigurationContextFactory;
import org.apache.axis2.context.MessageContext;
import org.apache.axis2.rpc.client.RPCServiceClient;
import org.apache.axis2.wsdl.WSDLConstants;
import org.junit.Test;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.xml.namespace.QName;
import java.io.File;

public class AttachmentClient {
    private static EndpointReference targetEPR = new EndpointReference(
            "http://127.0.0.1:8080/services/FileUploadService");


    @Test
    public void transferFile() throws Exception {
        String filePath =  AttachmentClient.class.getResource("/files/mm.jpeg").getPath();// AttachmentClient.class.getResource("/files/mm.mp3").getPath();
//        String destFile = AttachmentClient.class.getResource("/files/mm.mp4").getPath();
        Options options = new Options();
        options.setTo(targetEPR);
        options.setProperty(Constants.Configuration.ENABLE_SWA,
                Constants.VALUE_TRUE);
        options.setSoapVersionURI(SOAP11Constants.SOAP_ENVELOPE_NAMESPACE_URI);
// Increase the time out when sending large attachments
        options.setTimeOutInMilliSeconds(10000);
        options.setTo(targetEPR);
        options.setAction("urn:uploadFile");

// assume the use runs this sample at
// <axis2home>/samples/soapwithattachments/ dir
        StringBuilder repositoryPath = new StringBuilder();
        repositoryPath.append(this.getClass().getResource("/").getPath());
        repositoryPath.append("files");
        System.out.println(repositoryPath.toString());
        ConfigurationContext configContext = ConfigurationContextFactory.createConfigurationContextFromFileSystem(repositoryPath.toString(), null);

        RPCServiceClient sender = new RPCServiceClient(configContext, null);
        sender.setOptions(options);
        OperationClient mepClient = sender.createClient(ServiceClient.ANON_OUT_IN_OP);


        MessageContext mc = new MessageContext();
        FileDataSource fileDataSource = new FileDataSource(new File(filePath));

// Create a dataHandler using the fileDataSource. Any implementation of
// javax.activation.DataSource interface can fit here.
        DataHandler dataHandler = new DataHandler(fileDataSource);
        String attachmentID = mc.addAttachment(dataHandler);

//        mepClient.addMessageContext(mc);

//        sender.invokeBlocking(new QName("http://ws.file.fantasy.com", "uploadFile"),new Object[]{"","","",attachmentID});

        SOAPFactory fac = OMAbstractFactory.getSOAP11Factory();
        SOAPEnvelope env = fac.getDefaultEnvelope();
        OMNamespace omNs = fac.createOMNamespace("http://ws.file.fantasy.com", "");
        OMElement uploadFile = fac.createOMElement("uploadFile", omNs);

        OMElement fileName = fac.createOMElement("fileName", omNs);
        fileName.setText("name");

        OMElement contentType = fac.createOMElement("contentType", omNs);
        contentType.setText("html");

        OMElement dir = fac.createOMElement("dir", omNs);
        dir.setText("dir");

        OMElement attchmentID = fac.createOMElement("attchmentID", omNs);
        attchmentID.setText(attachmentID);

        uploadFile.addChild(fileName);
        uploadFile.addChild(contentType);
        uploadFile.addChild(dir);
        uploadFile.addChild(attchmentID);

        env.getBody().addChild(uploadFile);
        System.out.println("message====" + env);
        mc.setEnvelope(env);

        mepClient.addMessageContext(mc);
        mepClient.execute(true);
        MessageContext response = mepClient.getMessageContext(WSDLConstants.MESSAGE_LABEL_IN_VALUE);
        SOAPBody body = response.getEnvelope().getBody();
        OMElement element = body.getFirstElement().getFirstChildWithName(new QName("http://ws.file.fantasy.com", "return"));
        System.out.println(element.getText());
    }
}