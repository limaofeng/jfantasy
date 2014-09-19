package com.fantasy.framework.util.asm;

import com.fantasy.attr.bean.AttributeType;
import com.fantasy.attr.bean.AttributeValue;
import com.fantasy.attr.bean.AttributeVersion;
import com.fantasy.framework.util.common.ClassUtil;
import com.fantasy.framework.util.ognl.OgnlUtil;
import org.junit.Test;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class AsmUtilTest implements Opcodes {

    @Test
    public void asmTest(){
        Article article = new Article();
        System.out.println(ClassUtil.forName("int"));
        System.out.println(Type.getDescriptor(Long.class));
    }

    @Test
    public void test() throws IOException, InterruptedException, ClassNotFoundException, IllegalAccessException, InstantiationException {

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

        List<AttributeValue> attributeValues = Arrays.asList(attributeValue);


//        AttributeValueUtil.makeClass(version);

        String className = Article.class.getName() + "$" + version.getNumber();
        String superClass = Article.class.getName();

        Property property = new Property(attribute.getCode(),String.class);

        Property[] properties = new Property[]{property};


        property.setGetMethodCreator(new MethodCreator() {

            @Override
            public void execute(MethodVisitor mv) {

                String className = AsmContext.getContext().get("className", String.class);
                String superClassName = AsmContext.getContext().get("superClassName", String.class);
                Property property = AsmContext.getContext().get("property", Property.class);

                String superClassInternalName = Type.getInternalName(ClassUtil.forName(superClassName));
                String newClassInternalName = className.replace('.', '/');

                Label l0 = new Label();
                Label l1 = new Label();
                Label l2 = new Label();

                mv.visitLabel(l0);
                mv.visitVarInsn(ALOAD, F_FULL);
                mv.visitFieldInsn(GETFIELD, newClassInternalName, property.getName(), Type.getDescriptor(property.getType()));
                mv.visitJumpInsn(IFNULL,l1);

                mv.visitVarInsn(ALOAD, F_FULL);
                mv.visitFieldInsn(GETFIELD, newClassInternalName, property.getName(), Type.getDescriptor(property.getType()));
                mv.visitInsn(ARETURN);

                mv.visitLabel(l1);

                mv.visitFrame(F_SAME,0,new Object[0],0,new Object[0]);

                mv.visitVarInsn(ALOAD, F_FULL);
                mv.visitFieldInsn(GETFIELD, superClassInternalName, "attributeValues", "Ljava/util/List;");
                mv.visitLdcInsn(property.getName());
                mv.visitMethodInsn(INVOKESTATIC, "com/fantasy/framework/util/asm/AttributeValueUtil", "getValue", "(Ljava/util/List;Ljava/lang/String;)Ljava/lang/Object;");
                mv.visitTypeInsn(CHECKCAST,Type.getInternalName(property.getType()));
                mv.visitInsn(ARETURN);
                mv.visitLabel(l2);
                mv.visitLocalVariable("this", AsmUtil.getTypeDescriptor(className), null, l0, l2, 0);
                mv.visitMaxs(2, 1);
            }

        });

        if(String.class.isAssignableFrom(property.getType())) {

            property.setSetMethodCreator(new MethodCreator() {

                @Override
                public void execute(MethodVisitor mv) {

                    String className = AsmContext.getContext().get("className", String.class);
                    String superClassName = AsmContext.getContext().get("superClassName", String.class);
                    Property property = AsmContext.getContext().get("property", Property.class);

                    String superClassInternalName = Type.getInternalName(ClassUtil.forName(superClassName));
                    String newClassInternalName = className.replace('.', '/');

                    String fieldName = property.getName();
                    String descriptor = Type.getDescriptor(property.getType());

                    Label l0 = new Label();
                    Label l1 = new Label();
                    Label l2 = new Label();

//                    mv.visitLabel(l0);
//                    mv.visitVarInsn(Opcodes.ALOAD, Opcodes.F_FULL);
//                    mv.visitVarInsn(Opcodes.ALOAD, Opcodes.F_APPEND);
//                    mv.visitFieldInsn(Opcodes.PUTFIELD, newClassInternalName, fieldName, descriptor);
//                    mv.visitLabel(l1);
//                    mv.visitInsn(Opcodes.RETURN);
//                    mv.visitLabel(l2);
//                    mv.visitLocalVariable("this", AsmUtil.getTypeDescriptor(className), null, l0, l2, 0);
//                    mv.visitLocalVariable(fieldName, descriptor, AsmUtil.getSignature(property.getType(), property.getGenericTypes()), l0, l2, 1);
//                    mv.visitMaxs(2, 2);



                    mv.visitLabel(l0);
                    mv.visitVarInsn(Opcodes.ALOAD, Opcodes.F_FULL);
                    mv.visitVarInsn(Opcodes.ALOAD, Opcodes.F_FULL);
                    mv.visitFieldInsn(Opcodes.GETFIELD, superClassInternalName, "attributeValues", "Ljava/util/List;");
                    mv.visitLdcInsn(property.getName());
                    mv.visitVarInsn(Opcodes.ALOAD,Opcodes.F_APPEND);
                    mv.visitMethodInsn(INVOKESTATIC, "com/fantasy/framework/util/asm/AttributeValueUtil", "saveValue", "(Ljava/util/List;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/Object;");
                    mv.visitTypeInsn(CHECKCAST,Type.getInternalName(property.getType()));
                    mv.visitFieldInsn(Opcodes.PUTFIELD, newClassInternalName, property.getName(), Type.getDescriptor(property.getType()));
                    mv.visitLabel(l1);
                    mv.visitInsn(Opcodes.RETURN);
                    mv.visitLabel(l2);
                    mv.visitLocalVariable("this", AsmUtil.getTypeDescriptor(className), null, l0, l2, 0);
                    mv.visitLocalVariable(fieldName, descriptor, AsmUtil.getSignature(property.getType(), property.getGenericTypes()), l0, l2, 1);
                    mv.visitMaxs(4, 2);

                }

            });

        }


        Class clzz = AsmUtil.makeClass(className,superClass,properties);

//        System.out.println(AsmUtil.trace(Article.class));

//        System.out.println(AsmUtil.trace(clzz));


        for(java.lang.reflect.Method method : clzz.getDeclaredMethods()){
            System.out.println(method.toString());
        }

        Object o = clzz.newInstance();

        OgnlUtil.getInstance().setValue("attributeValues",o,attributeValues);

        OgnlUtil.getInstance().setValue("test",o,"123");

        System.out.println(o);

        System.out.println(OgnlUtil.getInstance().getValue("test",o));

//       System.out.println(AsmUtil.trace(Article.class));

    }

}
