package org.jfantasy.file;


import org.jfantasy.file.bean.enums.FileManagerType;

import java.util.Map;

public interface FileManagerBuilder<T extends FileManager> {

    FileManagerType getType();

    T register(Map<String,String> params);

}
