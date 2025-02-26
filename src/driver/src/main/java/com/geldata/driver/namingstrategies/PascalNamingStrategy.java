package com.geldata.driver.namingstrategies;

import org.jetbrains.annotations.NotNull;

import com.geldata.driver.util.StringsUtil;

final class PascalNamingStrategy implements NamingStrategy {
    public static final PascalNamingStrategy INSTANCE = new PascalNamingStrategy();

    @Override
    public @NotNull String convert(@NotNull String name) {
        if (StringsUtil.isNullOrEmpty(name)) {
            return name;
        }

        var result = new StringBuilder();
        char newChar;
        var toUpper = false;
        var charArray = name.toCharArray();
        for (int ctr = 0; ctr <= charArray.length - 1; ctr++) {
            if (ctr == 0) {
                newChar = Character.toUpperCase(charArray[ctr]);
                result = new StringBuilder(Character.toString(newChar));
                continue;
            }

            if (charArray[ctr] == '_') {
                toUpper = true;
                continue;
            }

            if (toUpper) {
                newChar = Character.toUpperCase(charArray[ctr]);
                result.append(newChar);
                toUpper = false;
                continue;
            }

            result.append(charArray[ctr]);
        }

        return result.toString();
    }
}
