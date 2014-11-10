package com.fantasy.attr.util;

import com.fantasy.attr.DynaBean;
import com.fantasy.attr.bean.Attribute;
import com.fantasy.attr.bean.AttributeType;
import com.fantasy.attr.bean.AttributeValue;
import com.fantasy.attr.bean.AttributeVersion;
import com.fantasy.attr.service.AttributeVersionService;
import com.fantasy.framework.spring.SpringContextUtil;
import com.fantasy.framework.util.asm.*;
import com.fantasy.framework.util.common.BeanUtil;
import com.fantasy.framework.util.common.ClassUtil;
import com.fantasy.framework.util.common.ObjectUtil;
import com.fantasy.framework.util.ognl.OgnlUtil;
import ognl.TypeConverter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicReference;

public class VersionUtil {

    private static final Log logger = LogFactory.getLog(VersionUtil.class);

    private static final ConcurrentMap<String, Class> dynaBeanClassCache = new ConcurrentHashMap<String, Class>();
    private static final ConcurrentMap<String, AttributeVersion> versionCache = new ConcurrentHashMap<String, AttributeVersion>();
    private static AttributeVersionService attributeVersionService;

    public static DynaBean makeDynaBean(DynaBean bean) {
        if (bean.getVersion() == null) {
            return bean;
        }
        return createDynaBean(ClassUtil.getRealClass(bean), bean.getVersion().getNumber(), bean);
    }

    public static DynaBean makeDynaBean(Class<DynaBean> clazz, String number) {
        return createDynaBean(clazz, number);
    }

    public static <T> T createDynaBean(Class<T> clazz, String number) {
        AttributeVersion version = getVersion(clazz, number);
        DynaBean dynaBean = (DynaBean) ClassUtil.newInstance(makeClass(version));
        List<AttributeValue> attributeValues = new ArrayList<AttributeValue>(version.getAttributes().size());
        for (Attribute attribute : version.getAttributes()) {
            AttributeValue attributeValue = new AttributeValue();
            attributeValue.setAttribute(attribute);
            attributeValue.setVersion(version);
            attributeValues.add(attributeValue);
        }
        dynaBean.setAttributeValues(attributeValues);
        return clazz.cast(dynaBean);
    }

    private static DynaBean createDynaBean(Class<?> clazz, String number, DynaBean bean) {
        AttributeVersion version = getVersion(clazz, number);
        DynaBean dynaBean = (DynaBean) ClassUtil.newInstance(makeClass(version));
        List<AttributeValue> attributeValues = new ArrayList<AttributeValue>(version.getAttributes().size());
        for (Attribute attribute : version.getAttributes()) {
            AttributeValue attributeValue = new AttributeValue();
            AttributeValue sourceValue = ObjectUtil.find(bean.getAttributeValues(), "attribute.code", attribute.getCode());
            if (sourceValue != null) {
                attributeValue.setId(sourceValue.getId());
                attributeValue.setValue(sourceValue.getValue());
                attributeValue.setTargetId(sourceValue.getTargetId());
            }
            attributeValue.setAttribute(attribute);
            attributeValue.setVersion(version);
            attributeValues.add(attributeValue);
        }
        BeanUtil.copyProperties(dynaBean, bean);
        dynaBean.setAttributeValues(attributeValues);
        return dynaBean;
    }

    public static AttributeVersion getVersion(Class<?> clazz, String number) {
        if (!versionCache.containsKey(clazz.getName() + "$v" + number)) {
            versionCache.putIfAbsent(clazz.getName() + "$v" + number, getAttributeVersionService().getVersion(clazz, number));
        }
        return versionCache.get(clazz.getName() + "$v" + number);
    }

    private synchronized static AttributeVersionService getAttributeVersionService() {
        if (attributeVersionService == null) {
            attributeVersionService = SpringContextUtil.getBeanByType(AttributeVersionService.class);
        }
        return attributeVersionService;
    }

    public static Class makeClass(Class<?> clazz, String number) {
        return makeClass(getVersion(clazz, number));
    }

    public static Class makeClass(AttributeVersion version) {
        String className = version.getClassName() + "$v" + version.getNumber();
        if (!dynaBeanClassCache.containsKey(className)) {
            String superClass = version.getClassName();
            List<Property> properties = new ArrayList<Property>();
            List<MethodInfo> methodInfos = new ArrayList<MethodInfo>();
            for (Attribute attribute : version.getAttributes()) {
                final Property property = new Property(attribute.getCode(), ClassUtil.forName(attribute.getAttributeType().getDataType()));
//                property.setGetMethodCreator(getMethodCreator);
//                if (String.class.isAssignableFrom(property.getType())) {
//                    property.setSetMethodCreator(setMethodCreator);
//                } else {
//                    methodInfos.add(new MethodInfo("set" + StringUtil.upperCaseFirst(property.getName()), Type.getMethodDescriptor(Type.getReturnType("V"), new Type[]{Type.getType(String.class)}), null, setMethodCreator));
//                }
                properties.add(property);
            }
            dynaBeanClassCache.putIfAbsent(className, AsmUtil.makeClass(className, superClass, properties.toArray(new Property[properties.size()]), methodInfos.toArray(new MethodInfo[methodInfos.size()])));
        }
        return dynaBeanClassCache.get(className);
    }

    private static MethodCreator getMethodCreator = new MethodCreator() {

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
            Label l3 = new Label();

            mv.visitLabel(l0);
            mv.visitVarInsn(Opcodes.ALOAD, Opcodes.F_FULL);
            mv.visitFieldInsn(Opcodes.GETFIELD, newClassInternalName, property.getName(), Type.getDescriptor(property.getType()));
            mv.visitJumpInsn(Opcodes.IFNULL, l1);

            mv.visitLabel(l2);
            mv.visitVarInsn(Opcodes.ALOAD, Opcodes.F_FULL);
            mv.visitFieldInsn(Opcodes.GETFIELD, newClassInternalName, property.getName(), Type.getDescriptor(property.getType()));
            mv.visitInsn(Opcodes.ARETURN);

            mv.visitLabel(l1);
            mv.visitFrame(Opcodes.F_SAME, 0, new Object[0], 0, new Object[0]);

            mv.visitVarInsn(Opcodes.ALOAD, Opcodes.F_FULL);
            mv.visitVarInsn(Opcodes.ALOAD, Opcodes.F_FULL);

            mv.visitFieldInsn(Opcodes.GETFIELD, superClassInternalName, "attributeValues", "Ljava/util/List;");
            mv.visitLdcInsn(property.getName());
            mv.visitMethodInsn(Opcodes.INVOKESTATIC, "com/fantasy/attr/util/VersionUtil", "getValue", "(Ljava/util/List;Ljava/lang/String;)Ljava/lang/Object;");
            mv.visitTypeInsn(Opcodes.CHECKCAST, Type.getInternalName(property.getType()));
            mv.visitInsn(Opcodes.DUP_X1);
            mv.visitFieldInsn(Opcodes.PUTFIELD, newClassInternalName, property.getName(), Type.getDescriptor(property.getType()));
            mv.visitInsn(Opcodes.ARETURN);
            mv.visitLabel(l3);
            mv.visitLocalVariable("this", AsmUtil.getTypeDescriptor(className), null, l0, l3, 0);
            mv.visitMaxs(3, 1);
        }

    };

    private static MethodCreator setMethodCreator = new MethodCreator() {

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

            mv.visitLabel(l0);
            mv.visitVarInsn(Opcodes.ALOAD, Opcodes.F_FULL);
            mv.visitVarInsn(Opcodes.ALOAD, Opcodes.F_FULL);
            mv.visitFieldInsn(Opcodes.GETFIELD, superClassInternalName, "attributeValues", "Ljava/util/List;");
            mv.visitLdcInsn(property.getName());
            mv.visitVarInsn(Opcodes.ALOAD, Opcodes.F_APPEND);
            mv.visitMethodInsn(Opcodes.INVOKESTATIC, "com/fantasy/attr/util/VersionUtil", "saveValue", "(Ljava/util/List;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/Object;");
            mv.visitTypeInsn(Opcodes.CHECKCAST, Type.getInternalName(property.getType()));
            mv.visitFieldInsn(Opcodes.PUTFIELD, newClassInternalName, property.getName(), Type.getDescriptor(property.getType()));
            mv.visitLabel(l1);
            mv.visitInsn(Opcodes.RETURN);
            mv.visitLabel(l2);
            mv.visitLocalVariable("this", AsmUtil.getTypeDescriptor(className), null, l0, l2, 0);
            mv.visitLocalVariable(fieldName, descriptor, AsmUtil.getSignature(property.getType(), property.getGenericTypes()), l0, l2, 1);
            mv.visitMaxs(4, 2);

        }

    };

    public static Object saveValue(Object root, List<AttributeValue> attributeValues, String attributeCode, String value) {
        logger.debug("saveValue : " + attributeCode + "==>" + value);
        AttributeValue attributeValue = ObjectUtil.find(attributeValues, "attribute.code", attributeCode);
        AttributeType attributeType = attributeValue.getAttribute().getAttributeType();
        AtomicReference<String> saveValue = new AtomicReference<String>(value);//TODO 保存转换之后的值
        attributeValue.setValue(saveValue.get());
        getOgnlUtil(attributeType).setValue(attributeCode, root, value);
        return value;
    }

    public static Object getValue(Object root, List<AttributeValue> attributeValues, String attributeCode) {
        AttributeValue attributeValue = ObjectUtil.find(attributeValues, "attribute.code", attributeCode);
        logger.debug("getValue : " + attributeCode + "==>" + attributeValue.getValue());
        AttributeType attributeType = attributeValue.getAttribute().getAttributeType();
        Class<?> clazz = ClassUtil.forName(attributeValue.getAttribute().getAttributeType().getDataType());
        if (ClassUtil.isPrimitiveOrWrapper(clazz)) {
            return ClassUtil.newInstance(clazz, attributeValue.getValue());
        } else if (String.class.isAssignableFrom(clazz)) {
            return attributeValue.getValue();
        } else {
            return getOgnlUtil(attributeType).getValue(attributeCode, root);
        }
    }

    public static OgnlUtil getOgnlUtil(AttributeType attributeType) {
        if (!OgnlUtil.containsKey("attr-" + attributeType.getId())) {
            OgnlUtil.getInstance("attr-" + attributeType.getId()).addTypeConverter(ClassUtil.forName(attributeType.getDataType()), (TypeConverter) SpringContextUtil.createBean(ClassUtil.forName(attributeType.getConverter().getTypeConverter()), SpringContextUtil.AUTOWIRE_BY_TYPE));
        }
        return OgnlUtil.getInstance("attr-" + attributeType.getId());
    }

}
