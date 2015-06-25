package com.fantasy.framework.util.validator;

import com.fantasy.framework.util.common.ClassUtil;
import com.fantasy.framework.util.common.ObjectUtil;
import com.fantasy.framework.util.validator.entities.DefaultValidateable;
import com.fantasy.framework.util.validator.entities.FieldValidator;
import com.fantasy.framework.util.validator.validators.DefaultParamsValidator;
import com.fantasy.framework.util.validator.validators.StackValidator;
import com.fantasy.framework.util.xml.JdomUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom2.Element;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class DefaultValidatorContext implements ValidatorContext {
    private static final String VALIDATION_XML = "-validation.xml";
    private static final Log logger = LogFactory.getLog(DefaultValidatorContext.class);

    private static DefaultValidatorContext context = new DefaultValidatorContext();

    private DefaultValidatorContextParser parser = new DefaultValidatorContextParser(this);

    public static class DefaultValidatorContextParser implements JdomUtil.Parser {
        private DefaultValidatorContext context;
        private Class<?> clazz;

        public DefaultValidatorContextParser(DefaultValidatorContext context) {
            this.context = context;
        }

        @SuppressWarnings("unchecked")
        public void callBack(String tagName, Element element) {
            if ("validator".equalsIgnoreCase(tagName)) {
                context.validateables.get(this.clazz).addValidator(element.getAttributeValue("name"), getValidator(element));
            } else if ("method-validator".equalsIgnoreCase(tagName)) {
                String methodName = element.getAttributeValue("name");
                for (Element ele : element.getChildren()) {
                    String fieldName = ele.getAttributeValue("name");
                    context.validateables.get(this.clazz).addFieldValidators(methodName, fieldName, getFieldValidator(ele.getChildren()));
                }
            }
        }

        @SuppressWarnings("unchecked")
        private Validator getValidator(Element element) {
            String type = element.getAttributeValue("type");
            String classel = element.getAttributeValue("class");
            if ("stack".equals(type)) {
                StackValidator validator = new StackValidator();
                validator.setValidateble(context.validateables.get(this.clazz));
                for (Element ele : element.getChildren()) {
                    String fieldName = ele.getAttributeValue("name");
                    validator.addFieldValidators(fieldName, getFieldValidator(ele.getChildren()));
                }
                return validator;
            }
            Map<String, String> params = new HashMap<String, String>();
            for (Element ele : element.getChildren()) {
                params.put(ele.getName(), ele.getTextTrim());
            }
            if (params.size() == 0) {
                return ClassUtil.newInstance(classel);
            }
            DefaultParamsValidator defaultParamsValidator = ClassUtil.newInstance(classel);
            assert defaultParamsValidator != null;
            defaultParamsValidator.setParams(params);
            return defaultParamsValidator;
        }

        @SuppressWarnings("unchecked")
        private List<FieldValidator> getFieldValidator(List<Element> childNodes) {
            List<FieldValidator> fieldValidators = new ArrayList<FieldValidator>();
            for (Element ele : childNodes) {
                Map<String, String> params = new HashMap<String, String>();
                for (Element ele_ : ele.getChildren()) {
                    params.put(ele_.getName(), ele_.getTextTrim());
                }
                fieldValidators.add(new FieldValidator(ele.getAttributeValue("type"), params));
            }
            return fieldValidators;
        }

        public void setClazz(Class<?> clazz) {
            this.clazz = clazz;
        }

    }

    private ConcurrentMap<Class<?>, Validateable> validateables = new ConcurrentHashMap<Class<?>, Validateable>();

    public Validateable read(DefaultValidateable validateable, Class<?> classes) {
        InputStream stream = classes.getResourceAsStream(classes.getSimpleName() + VALIDATION_XML);
        if (!ObjectUtil.isNull(stream)) {
            this.parser.setClazz(classes);
            JdomUtil.parse(stream, this.parser);
            if (logger.isDebugEnabled()) {
                logger.debug("解析完成:" + classes.getResource(String.valueOf(classes.getSimpleName()) + VALIDATION_XML).getPath());
            }
        }
        if ((!ObjectUtil.isNull(classes.getSuperclass())) && (classes.getSuperclass() != Object.class)) {
            validateable.setSuper(getValidateable(classes.getSuperclass()));
        }
        return validateable;
    }

    public static ValidatorContext getInstance() {
        if (logger.isDebugEnabled()) {
            logger.debug("验证器获取规则如:" + Object.class.getName() + VALIDATION_XML);
        }
        return context;
    }

    public Validateable getValidateable(Class<?> classes) {
        if (!this.validateables.containsKey(classes)) {
            this.validateables.putIfAbsent(classes, read(new DefaultValidateable(), classes));
            logger.debug("初始化[" + classes.getName() + "]验证管理器成功!");
        }
        return this.validateables.get(classes);
    }
}