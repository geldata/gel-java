package com.geldata.driver.abstractions;

import java.util.EnumSet;
import java.util.Map;
import java.util.concurrent.CompletionStage;

import com.geldata.driver.Capabilities;
import com.geldata.driver.GelQueryable;

@FunctionalInterface
public interface ClientQueryDelegate<T, U> {
    CompletionStage<U> run(GelQueryable client, Class<T> cls, String query, Map<String, Object> args, EnumSet<Capabilities> capabilities);
}
