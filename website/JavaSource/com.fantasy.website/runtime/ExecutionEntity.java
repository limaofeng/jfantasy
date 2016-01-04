package org.jfantasy.website.runtime;

import org.jfantasy.file.FileManager;
import org.jfantasy.framework.freemarker.FreeMarkerTemplateUtils;
import org.jfantasy.website.OutPutUrl;
import org.jfantasy.website.PageInstance;
import org.jfantasy.website.Template;
import org.jfantasy.website.TemplateData;
import org.jfantasy.website.template.FreemarkerTemplate;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class ExecutionEntity implements PageInstance {

    private final static Log LOG = LogFactory.getLog(ExecutionEntity.class);

    private Template<?> template;
    private OutPutUrl outPutUrl;
    private TemplateData[] datas;
    private FileManager fileManager;

    public ExecutionEntity(FileManager fileManager,OutPutUrl outPutUrl, Template template, TemplateData[] datas) {
        this.template = template;
        this.outPutUrl = outPutUrl;
        this.datas = datas;
        this.fileManager = fileManager;
    }

    @Override
    public void execute() {
        Map<String, Object> dataMap = new HashMap<String, Object>();
        if (template instanceof FreemarkerTemplate) {
            for (Map.Entry<String, TemplateData> entry : template.getData().entrySet()) {
                dataMap.put(entry.getKey(), entry.getValue().getValue());
                /*
                dataMap.put(entry.getKey(), new TemplateMethodModel() {
                    @Override
                    public Object exec(List arguments) throws TemplateModelException {
//                    org.jfantasy.website.DataAnalyzer analyzer = (org.jfantasy.website.DataAnalyzer) ClassUtil.newInstance(_data.getDataAnalyzer().getClassName());

                        return "test";//analyzer.exec(_data.getValue(), ClassUtil.forName(_data.getDataInferface().getJavaType()),_data.getDataInferface().isList(),arguments);
                    }

                });*/
            }
            try {
                FreeMarkerTemplateUtils.writer(dataMap, ((FreemarkerTemplate) template).getContent(), fileManager.writeFile(outPutUrl.getUrl(null)));
            } catch (IOException e) {
                LOG.error(e.getMessage(),e);
            }
        }
    }

}
