package com.edgedb.driver.namingstrategies;

import com.edgedb.driver.util.StringsUtil;

final class CamelCaseNamingStrategy implements NamingStrategy {
    public static final CamelCaseNamingStrategy instance = new CamelCaseNamingStrategy();

    @Override
    public String convert(String name) {
        if (StringsUtil.isNullOrEmpty(name)) {
            return name;
        }

        return (Character.toLowerCase(name.charAt(0)) + name.substring(1)).replaceAll("_", "");
    }
}
