package com.fantasy.framework.struts2.rest;

import com.fantasy.framework.struts2.ActionSupport;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

public class RestActionSupport extends ActionSupport {

    private static final long serialVersionUID = -889518620073576882L;

    private static final String DELETE = "DELETE";
    private static final String PUT = "PUT";
    private static final String POST = "POST";
    private static final String GET = "GET";
    private static final String OPTIONS = "OPTIONS";
    private static final String DIVIDER = ", ";

    /**
     * Default execution.
     *
     * @return object because it can return string, result or httpHeader.
     * @throws Exception
     */
    public Object index() throws Exception {
        return execute();
    }

    /**
     * Inspect the implemented methods to know the allowed http methods.
     *
     * @return Include the header "Allow" with the allowed http methods.
     */
    public HttpHeaders options() {

        String methods = OPTIONS;

        Method[] meths = this.getClass().getDeclaredMethods();
        for (Method m : meths) {
            String methodName = m.getName();
            if (!methods.contains(GET) &&
                    (methodName.equals("index")
                            || methodName.equals("show")
                            || methodName.equals("edit")
                            || methodName.equals("editNew"))) {
                methods += DIVIDER + GET;
            } else if (methodName.equals("create")) {
                methods += DIVIDER + POST;
            } else if (methodName.equals("update")) {
                methods += DIVIDER + PUT;
            } else if (methodName.equals("destroy")) {
                methods += DIVIDER + DELETE;
            }
        }

        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        response.addHeader("Allow", methods);

        DefaultHttpHeaders httpHeaders = new DefaultHttpHeaders();
        httpHeaders.apply(request, response, this);
        httpHeaders.disableCaching().withStatus(HttpServletResponse.SC_OK);

        return httpHeaders;
    }

    /**
     * By default, return continue.
     * Is possible override the method to return expectation failed.
     *
     * @return continue
     */
    public HttpHeaders createContinue() {
        return new DefaultHttpHeaders()
                .disableCaching()
                .withStatus(HttpServletResponse.SC_CONTINUE);
    }

    /**
     * By default, return continue.
     * Is possible override the method to return expectation failed.
     *
     * @return continue
     */
    public HttpHeaders updateContinue() {
        return new DefaultHttpHeaders()
                .disableCaching()
                .withStatus(HttpServletResponse.SC_CONTINUE);
    }
}
