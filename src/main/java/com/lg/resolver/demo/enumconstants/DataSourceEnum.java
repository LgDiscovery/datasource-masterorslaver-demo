package com.lg.resolver.demo.enumconstants;

import lombok.Getter;


@Getter
public enum DataSourceEnum {

    MASTER("master"),
    SLAVER("slaver");

     private String dataSourceName;

    DataSourceEnum(String dataSourceName) {
        this.dataSourceName = dataSourceName;
    }

}
