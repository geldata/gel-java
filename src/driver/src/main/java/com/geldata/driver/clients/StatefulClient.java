package com.geldata.driver.clients;

import org.jetbrains.annotations.NotNull;

import com.geldata.driver.state.Config;
import com.geldata.driver.state.Session;

import java.util.Map;
import java.util.function.Consumer;

public interface StatefulClient {
    StatefulClient withModule(@NotNull String module);
    StatefulClient withSession(@NotNull Session session);
    StatefulClient withModuleAliases(@NotNull Map<String, String> aliases);
    StatefulClient withConfig(@NotNull Config config);
    StatefulClient withConfig(@NotNull Consumer<Config.Builder> func);
    StatefulClient withGlobals(@NotNull Map<String, Object> globals);
}
