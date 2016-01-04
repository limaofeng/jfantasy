package org.jfantasy.framework.util.asm;

public class MethodInfo {

    private String methodName;
    private String methodDescriptor;
    private String signature;
    MethodCreator methodCreator;

    public MethodInfo(String methodName, String methodDescriptor, String signature, MethodCreator methodCreator) {
        this.methodName = methodName;
        this.methodDescriptor = methodDescriptor;
        this.signature = signature;
        this.methodCreator = methodCreator;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getMethodDescriptor() {
        return methodDescriptor;
    }

    public void setMethodDescriptor(String methodDescriptor) {
        this.methodDescriptor = methodDescriptor;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public MethodCreator getMethodCreator() {
        return methodCreator;
    }

    public void setMethodCreator(MethodCreator methodCreator) {
        this.methodCreator = methodCreator;
    }
}
