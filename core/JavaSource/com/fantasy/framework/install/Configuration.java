package com.fantasy.framework.install;


import java.util.ArrayList;
import java.util.List;

public class Configuration {

    private List<String> packagesToScan = new ArrayList<String>();

    public void addPackagesToScan(String packagesToScan) {
        this.packagesToScan.add(packagesToScan);
    }

    public String[] getPackagesToScan() {
        return packagesToScan.toArray(new String[packagesToScan.size()]);
    }

}
