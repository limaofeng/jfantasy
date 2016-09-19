package org.jfantasy.framework.log.config;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jfantasy.framework.error.IgnoreException;
import org.springframework.beans.factory.config.TypedStringValue;
import org.springframework.beans.factory.parsing.ReaderContext;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.ManagedList;
import org.springframework.beans.factory.support.ManagedMap;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.cache.annotation.AnnotationCacheOperationSource;
import org.springframework.cache.interceptor.*;
import org.springframework.util.StringUtils;
import org.springframework.util.xml.DomUtils;
import org.w3c.dom.Element;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class LogAdviceParser extends AbstractSingleBeanDefinitionParser {

    private final static Log LOG = LogFactory.getLog(LogAdviceParser.class);

    private static class Props {

        private String key, condition, method;
        private String[] caches = null;

        Props(Element root) {
            String defaultCache = root.getAttribute("cache");
            key = root.getAttribute("key");
            condition = root.getAttribute("condition");
            method = root.getAttribute(METHOD_ATTRIBUTE);

            if (StringUtils.hasText(defaultCache)) {
                caches = StringUtils.commaDelimitedListToStringArray(defaultCache.trim());
            }
        }

        <T extends CacheOperation, B extends CacheOperation.Builder> T merge(Element element, ReaderContext readerCtx, B op, Class<T> clazz) {
            String cache = element.getAttribute("cache");
            String k = element.getAttribute("key");
            String c = element.getAttribute("condition");

            String[] localCaches = caches;
            String localKey = key, localCondition = condition;

            // sanity check
            if (StringUtils.hasText(cache)) {
                localCaches = StringUtils.commaDelimitedListToStringArray(cache.trim());
            } else {
                if (caches == null) {
                    readerCtx.error("No cache specified specified for " + element.getNodeName(), element);
                }
            }

            if (StringUtils.hasText(k)) {
                localKey = k.trim();
            }

            if (StringUtils.hasText(c)) {
                localCondition = c.trim();
            }

            op.setCacheNames(localCaches);
            op.setKey(localKey);
            op.setCondition(localCondition);

            try {
                return clazz.getConstructor(op.getClass()).newInstance(op);
            } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                LOG.error(e);
                throw new IgnoreException(e);
            }
        }

        String merge(Element element, ReaderContext readerCtx) {
            String m = element.getAttribute(METHOD_ATTRIBUTE);

            if (StringUtils.hasText(m)) {
                return m.trim();
            }
            if (StringUtils.hasText(method)) {
                return method;
            }
            readerCtx.error("No method specified for " + element.getNodeName(), element);
            return null;
        }
    }

    private static final String CACHEABLE_ELEMENT = "cacheable";
    private static final String CACHE_EVICT_ELEMENT = "cache-evict";
    private static final String CACHE_PUT_ELEMENT = "cache-put";
    private static final String METHOD_ATTRIBUTE = "method";
    private static final String DEFS_ELEMENT = "caching";

    @Override
    protected Class<CacheInterceptor> getBeanClass(Element element) {
        return CacheInterceptor.class;
    }

    @Override
    protected void doParse(Element element, ParserContext parserContext, BeanDefinitionBuilder builder) {
        builder.addPropertyReference("cacheManager", LogNamespaceHandler.extractCacheManager(element));
        LogNamespaceHandler.parseKeyGenerator(element, builder.getBeanDefinition());

        List<Element> cacheDefs = DomUtils.getChildElementsByTagName(element, DEFS_ELEMENT);
        if (!cacheDefs.isEmpty()) {
            // Using attributes source.
            List<RootBeanDefinition> attributeSourceDefinitions = parseDefinitionsSources(cacheDefs, parserContext);
            builder.addPropertyValue("cacheOperationSources", attributeSourceDefinitions);
        } else {
            // Assume annotations source.
            builder.addPropertyValue("cacheOperationSources", new RootBeanDefinition(AnnotationCacheOperationSource.class));
        }
    }

    private List<RootBeanDefinition> parseDefinitionsSources(List<Element> definitions, ParserContext parserContext) {
        ManagedList<RootBeanDefinition> defs = new ManagedList<>(definitions.size());

        // extract default param for the definition
        for (Element element : definitions) {
            defs.add(parseDefinitionSource(element, parserContext));
        }

        return defs;
    }

    private RootBeanDefinition parseDefinitionSource(Element definition, ParserContext parserContext) {
        Props prop = new Props(definition);
        // add cacheable first

        ManagedMap<TypedStringValue, Collection<CacheOperation>> cacheOpMap = new ManagedMap<>();
        cacheOpMap.setSource(parserContext.extractSource(definition));

        List<Element> cacheableCacheMethods = DomUtils.getChildElementsByTagName(definition, CACHEABLE_ELEMENT);

        for (Element opElement : cacheableCacheMethods) {
            String name = prop.merge(opElement, parserContext.getReaderContext());
            TypedStringValue nameHolder = new TypedStringValue(name);
            nameHolder.setSource(parserContext.extractSource(opElement));
            CacheableOperation.Builder builder = new CacheableOperation.Builder();
            CacheOperation op = prop.merge(opElement, parserContext.getReaderContext(), builder, CacheableOperation.class);

            Collection<CacheOperation> col = cacheOpMap.get(nameHolder);
            if (col == null) {
                col = new ArrayList<>(2);
                cacheOpMap.put(nameHolder, col);
            }
            col.add(op);
        }

        List<Element> evictCacheMethods = DomUtils.getChildElementsByTagName(definition, CACHE_EVICT_ELEMENT);

        for (Element opElement : evictCacheMethods) {
            String name = prop.merge(opElement, parserContext.getReaderContext());
            TypedStringValue nameHolder = new TypedStringValue(name);
            nameHolder.setSource(parserContext.extractSource(opElement));
            CacheEvictOperation.Builder builder = new CacheEvictOperation.Builder();
            String wide = opElement.getAttribute("all-entries");
            if (StringUtils.hasText(wide)) {
                builder.setCacheWide(Boolean.valueOf(wide.trim()));
            }
            String after = opElement.getAttribute("before-invocation");
            if (StringUtils.hasText(after)) {
                builder.setBeforeInvocation(Boolean.valueOf(after.trim()));
            }
            Collection<CacheOperation> col = cacheOpMap.get(nameHolder);
            if (col == null) {
                col = new ArrayList<>(2);
                cacheOpMap.put(nameHolder, col);
            }
            CacheEvictOperation op = prop.merge(opElement, parserContext.getReaderContext(), builder, CacheEvictOperation.class);
            col.add(op);
        }
        List<Element> putCacheMethods = DomUtils.getChildElementsByTagName(definition, CACHE_PUT_ELEMENT);

        for (Element opElement : putCacheMethods) {
            String name = prop.merge(opElement, parserContext.getReaderContext());
            TypedStringValue nameHolder = new TypedStringValue(name);
            nameHolder.setSource(parserContext.extractSource(opElement));
            CachePutOperation.Builder builder = new CachePutOperation.Builder();
            Collection<CacheOperation> col = cacheOpMap.get(nameHolder);
            if (col == null) {
                col = new ArrayList<>(2);
                cacheOpMap.put(nameHolder, col);
            }
            CacheOperation op = prop.merge(opElement, parserContext.getReaderContext(), builder, CachePutOperation.class);
            col.add(op);
        }

        RootBeanDefinition attributeSourceDefinition = new RootBeanDefinition(NameMatchCacheOperationSource.class);
        attributeSourceDefinition.setSource(parserContext.extractSource(definition));
        attributeSourceDefinition.getPropertyValues().add("nameMap", cacheOpMap);
        return attributeSourceDefinition;
    }
}
