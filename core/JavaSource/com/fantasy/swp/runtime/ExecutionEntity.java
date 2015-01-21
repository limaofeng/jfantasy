package com.fantasy.swp.runtime;

import com.fantasy.file.FileManager;
import com.fantasy.framework.freemarker.FreeMarkerTemplateUtils;
import com.fantasy.swp.OutPutUrl;
import com.fantasy.swp.PageInstance;
import com.fantasy.swp.Template;
import com.fantasy.swp.TemplateData;
import com.fantasy.swp.template.FreemarkerTemplate;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class ExecutionEntity implements PageInstance {

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
//                    com.fantasy.swp.DataAnalyzer analyzer = (com.fantasy.swp.DataAnalyzer) ClassUtil.newInstance(_data.getDataAnalyzer().getClassName());

                        return "test";//analyzer.exec(_data.getValue(), ClassUtil.forName(_data.getDataInferface().getJavaType()),_data.getDataInferface().isList(),arguments);
                    }

                });*/
            }
            try {
                FreeMarkerTemplateUtils.writer(dataMap, ((FreemarkerTemplate) template).getContent(), fileManager.writeFile(outPutUrl.getUrl(null)));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
