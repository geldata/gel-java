package com.geldata.driver.abstractions;

import java.util.EnumSet;
import java.util.Map;
import java.util.concurrent.CompletionStage;

import com.geldata.driver.Capabilities;

@FunctionalInterface
public interface QueryDelegate<T, U> {
    CompletionStage<U> run(Class<T> cls, String query, Map<String, Object> args, EnumSet<Capabilities> capabilities);
}