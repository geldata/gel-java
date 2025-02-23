package com.geldata.examples;

import java.util.concurrent.CompletionStage;

import com.geldata.driver.GelClientPool;

public interface Example {
    CompletionStage<Void> run(GelClientPool clientPool);
}