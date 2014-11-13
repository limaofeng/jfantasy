package com.fantasy.framework.ws.axis2;

import org.apache.axiom.soap.SOAPEnvelope;
import org.apache.axis2.AxisFault;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.async.AxisCallback;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.context.ConfigurationContextFactory;
import org.apache.axis2.context.MessageContext;
import org.apache.axis2.databinding.utils.BeanUtil;
import org.apache.axis2.engine.DefaultObjectSupplier;
import org.apache.axis2.rpc.client.RPCServiceClient;
import org.apache.axis2.transport.http.HTTPConstants;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.xml.namespace.QName;
import javax.xml.ws.WebServiceException;
import java.net.URL;
import java.util.regex.Pattern;

public abstract class WebServiceClient {

    private final Log logger = LogFactory.getLog(getClass());

    private static String WEBCLASSES_PATH = null;

    static {
        URL url = Thread.currentThread().getContextClassLoader().getResource("/");
        if (url == null) {
            url = WebServiceClient.class.getResource("/");
        }
        WEBCLASSES_PATH = replace(url.getPath(), "[\\/]$", "");
    }

    private EndpointReference targetEPR;
    private String targetNamespace;
    private String endPointReference;
    private String serviceName;
    private String axis2xml;
    private boolean manageSession = false;
    private ConfigurationContext configurationContext;

    public WebServiceClient(String serviceName) {
        this.serviceName = serviceName;
    }

    public void afterPropertiesSet() throws Exception {
        assert this.endPointReference != null:"endPointReference不能为空";
        assert this.targetNamespace != null:"targetNamespace不能为空";
        assert this.serviceName != null:"serviceName不能为空";
        if (this.axis2xml != null) {
            if (this.axis2xml.startsWith("classpath:")) {
                this.axis2xml = replace(this.axis2xml, "^classpath:", classes() + "/");
            }
            String path = replaceFirst(this.axis2xml, "[^/:]+[.]xml$", "");
            this.configurationContext = ConfigurationContextFactory.createConfigurationContextFromFileSystem(path, this.axis2xml);
        }
        this.targetEPR = new EndpointReference(this.endPointReference.concat("/").concat(this.serviceName));
    }

    private RPCServiceClient createServiceClient() throws AxisFault {
        RPCServiceClient serviceClient;
        if (this.configurationContext != null) {
            serviceClient = new RPCServiceClient(this.configurationContext, null);
        } else {
            serviceClient = new RPCServiceClient();
        }
        Options options = serviceClient.getOptions();
        options.setManageSession(manageSession);
        //初始化超时时间
        options.setTimeOutInMilliSeconds(30000);
        //设置Http客户端连接可以复用
        options.setProperty(HTTPConstants.REUSE_HTTP_CLIENT, Boolean.TRUE);
        this.logger.debug(this.endPointReference.concat("/").concat(this.serviceName));
        options.setTo(this.targetEPR);
        return serviceClient;
    }

    public <T> T invokeOption(String opName, Object[] opArgs, Class<T> opReturnType) {
        RPCServiceClient serviceClient = null;
        try {
            QName opQName = new QName(targetNamespace, opName);
            Object[] response = (serviceClient = this.createServiceClient()).invokeBlocking(opQName, opArgs, new Class[]{opReturnType});
            return opReturnType.cast(response.length > 0 ? response[0] : null);
        } catch (AxisFault e) {
            logger.error(e.getMessage(), e);
            throw new WebServiceException(e);
        } finally {
            if (serviceClient != null) {
                try {
                    serviceClient.cleanupTransport();
                    serviceClient.cleanup();
                } catch (AxisFault axisFault) {
                    axisFault.printStackTrace();
                }
            }
        }
    }

    public Object invokeOption(String opName, Object[] opArgs, Class<?>[] opReturnType) {
        RPCServiceClient serviceClient = null;
        try {
            QName opQName = new QName(this.targetNamespace, opName);
            Object[] response = (serviceClient = this.createServiceClient()).invokeBlocking(opQName, opArgs, opReturnType);
            return response.length > 0 ? response[0] : null;
        } catch (AxisFault e) {
            logger.error(e.getMessage(), e);
            throw new WebServiceException(e);
        } finally {
            if (serviceClient != null) {
                try {
                    serviceClient.cleanupTransport();
                    serviceClient.cleanup();
                } catch (AxisFault axisFault) {
                    axisFault.printStackTrace();
                }
            }
        }
    }

    public <T> void invokeNonBlocking(String opName, Object[] opArgs, final Class<T> opReturnType) {
        try {
            QName opQName = new QName(this.targetNamespace, opName);
            this.createServiceClient().invokeNonBlocking(opQName, opArgs, new AxisCallback() {
                @Override
                public void onMessage(MessageContext messageContext) {
                    SOAPEnvelope envelope = messageContext.getEnvelope();
                    try {
                        Object t = BeanUtil.deserialize(opReturnType, messageContext.getEnvelope().cloneOMElement(), new DefaultObjectSupplier(), null);
                    } catch (AxisFault axisFault) {
                        axisFault.printStackTrace();
                    }
                }

                @Override
                public void onFault(MessageContext messageContext) {

                }

                @Override
                public void onError(Exception e) {

                }

                @Override
                public void onComplete() {

                }
            });
        } catch (AxisFault e) {
            logger.error(e.getMessage(), e);
            throw new WebServiceException(e);
        }
    }

    public void setManageSession(boolean manageSession) {
        this.manageSession = manageSession;
    }

    public void setAxis2xml(String axis2xml) {
        this.axis2xml = axis2xml;
    }

    public void setTargetNamespace(String targetNamespace) {
        this.targetNamespace = targetNamespace;
    }

    public void setEndPointReference(String endPointReference) {
        this.endPointReference = endPointReference;
    }

    private static String classes() {
        return WEBCLASSES_PATH;
    }

    private static String replace(String input, String regEx, String replacement) {
        Pattern pattern = Pattern.compile(regEx, Pattern.CASE_INSENSITIVE);
        return pattern.matcher(input).replaceAll(replacement);
    }

    private static String replaceFirst(String input, String regEx, String replacement) {
        Pattern pattern = Pattern.compile(regEx, Pattern.CASE_INSENSITIVE);
        return pattern.matcher(input).replaceFirst(replacement);
    }

}