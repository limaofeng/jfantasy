package com.fantasy.framework.util.asm;

import com.fantasy.attr.bean.AttributeType;
import com.fantasy.attr.bean.AttributeValue;
import com.fantasy.attr.bean.AttributeVersion;
import com.fantasy.framework.util.FantasyClassLoader;
import com.fantasy.framework.util.common.StringUtil;
import com.fantasy.framework.util.regexp.RegexpUtil;
import com.fantasy.security.bean.User;
import org.junit.Test;
import org.objectweb.asm.*;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Arrays;

public class AsmUtilTest implements Opcodes {

    @Test
    public void test() throws IOException, InterruptedException, ClassNotFoundException {

        Article article = new Article();

        AttributeVersion version = new AttributeVersion();
        version.setNumber("temp");
        version.setClassName(Article.class.getName());

        AttributeType attributeType = new AttributeType();
        attributeType.setName("测试数据类型");
        attributeType.setDataType(String.class.getName());

        com.fantasy.attr.bean.Attribute attribute = new com.fantasy.attr.bean.Attribute();
        attribute.setCode("test");
        attribute.setName("测试字段");
        attribute.setAttributeType(attributeType);

        version.setAttributes(Arrays.asList(attribute));


        AttributeValue attributeValue = new AttributeValue();
        attributeValue.setAttribute(attribute);
        attributeValue.setVersion(version);


        String className = Article.class.getName() + "$" + version.getNumber();
        String superClass = Article.class.getName();

        Property[] properties = new Property[]{new Property(attribute.getCode(),String.class)};

        String relativePath = RegexpUtil.replace(className, "\\.", "/");
        ClassWriter cw = new ClassWriter(F_FULL);

        Label l0 = new Label();
        Label l1 = new Label();
        Label l2 = new Label();

        /**
         * 注：第一个参数为版本号
         */
        cw.visit(V1_7, ACC_PUBLIC, relativePath, null, RegexpUtil.replace(superClass, "\\.", "/"), new String[0]);

        cw.visitSource(RegexpUtil.parseGroup(className, "\\.([A-Za-z0-9_]+)$", 1) + ".java", null);

        // 构造方法
        MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, "<init>", Type.getMethodDescriptor(Type.getReturnType("V"), new Type[0]), null, null);
        mv.visitLabel(l0);
        mv.visitVarInsn(ALOAD, 0);
        mv.visitMethodInsn(INVOKESPECIAL, RegexpUtil.replace(superClass, "\\.", "/"), "<init>", Type.getMethodDescriptor(Type.getReturnType("V"), new Type[0]));
        mv.visitInsn(RETURN);
        mv.visitLabel(l1);
        mv.visitLocalVariable("this", AsmUtil.getTypeDescriptor(className), null, l0, l1, 0);
        mv.visitMaxs(1, 1);
        mv.visitEnd();

        // visitLabel 方法标记行号
        for (Property property : properties) {
            String fieldName = property.getName();
            String descriptor = Type.getDescriptor(property.getType());
            String signature = AsmUtil.getSignature(property.getType(), property.getGenericTypes());

            // 属性
            cw.visitField(ACC_PRIVATE, fieldName, descriptor, signature, null).visitEnd();
            String methodName, methodDescriptor;
            int[] loadAndReturnOf = AsmUtil.loadAndReturnOf(descriptor);

            // set方法
            if (property.isWrite()) {
                methodName = "set" + StringUtil.upperCaseFirst(fieldName);
                methodDescriptor = Type.getMethodDescriptor(Type.getReturnType("V"), new Type[] { Type.getType(property.getType()) });
                signature = property.getGenericTypes().length != 0 ? ("(" + AsmUtil.getSignature(property.getType(), property.getGenericTypes()) + ")V") : null;

                mv = cw.visitMethod(ACC_PUBLIC, methodName, methodDescriptor, signature, new String[] {});
                mv.visitCode();
                mv.visitLabel(l0);
                mv.visitVarInsn(ALOAD, F_FULL);
                mv.visitVarInsn(ALOAD, F_APPEND);
                mv.visitFieldInsn(PUTFIELD, relativePath, fieldName, descriptor);
                mv.visitLabel(l1);
                mv.visitInsn(RETURN);
                mv.visitLabel(l2);
                mv.visitLocalVariable("this", AsmUtil.getTypeDescriptor(className), null, l0, l2, 0);
                mv.visitLocalVariable(fieldName, descriptor, AsmUtil.getSignature(property.getType(), property.getGenericTypes()), l0, l2, 1);
                // mv.visitVarInsn(loadAndReturnOf[0], F_APPEND);// ALOAD
                mv.visitMaxs(2, 2);
                mv.visitEnd();
            }

            // get方法
            if (property.isRead()) {
                methodName = "get" + StringUtil.upperCaseFirst(fieldName);
                methodDescriptor = Type.getMethodDescriptor(Type.getType(property.getType()), new Type[0]);
                signature = property.getGenericTypes().length != 0 ? ("()" + AsmUtil.getSignature(property.getType(), property.getGenericTypes())) : null;
                mv = cw.visitMethod(ACC_PUBLIC, methodName, methodDescriptor, signature, null);
                mv.visitCode();
                mv.visitLabel(l0);
                mv.visitVarInsn(ALOAD, 0);
                mv.visitFieldInsn(GETFIELD, relativePath, fieldName, descriptor);
                mv.visitInsn(loadAndReturnOf[1]);// ARETURN
                mv.visitLabel(l1);
                mv.visitLocalVariable("this", AsmUtil.getTypeDescriptor(className), null, l0, l1, 0);
                mv.visitMaxs(1, 1);
                mv.visitEnd();
            }
        }

        // toString方法
        mv = cw.visitMethod(ACC_PUBLIC, "toString", Type.getMethodDescriptor(Type.getType(String.class), new Type[0]), null, null);
        mv.visitCode();
        mv.visitLabel(l0);
        mv.visitLdcInsn(" AsmUtil makeClass !");
        mv.visitInsn(ARETURN);
        mv.visitLabel(l1);
        mv.visitLocalVariable("this", AsmUtil.getTypeDescriptor(className), null, l0, l1, 0);
        mv.visitMaxs(1, 1);
        mv.visitEnd();

        cw.visitEnd();

        Class clzz = FantasyClassLoader.getClassLoader().loadClass(cw.toByteArray(), className);

        System.out.println(User.class.isAssignableFrom(clzz));

        for(Method method : clzz.getDeclaredMethods()){
            System.out.println(method.toString());
        }

        System.out.println(clzz);

    }

}
