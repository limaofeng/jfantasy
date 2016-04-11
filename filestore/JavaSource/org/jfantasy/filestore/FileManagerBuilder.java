package org.jfantasy.filestore;


import org.jfantasy.filestore.bean.enums.FileManagerType;

import java.util.Map;

public interface FileManagerBuilder<T extends FileManager> {

    FileManagerType getType();

    T register(Map<String,String> params);

}
