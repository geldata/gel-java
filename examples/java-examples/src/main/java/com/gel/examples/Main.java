package com.geldata.examples;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.geldata.driver.*;
import com.geldata.driver.exceptions.GelException;
import com.geldata.driver.namingstrategies.NamingStrategy;

import java.io.IOException;
import java.util.ArrayList;
import java.util.function.Supplier;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws IOException, GelException {
        var clientPool = new GelClientPool(GelClientConfig.builder()
                .withNamingStrategy(NamingStrategy.snakeCase())
                .useFieldSetters(true)
                .build()
        ).withModule("examples");

        runJavaExamples(clientPool);

        logger.info("Examples complete");

        System.exit(0);
    }

    private static void runJavaExamples(GelClientPool clientPool) {
        var examples = new ArrayList<Supplier<Example>>() {
            {
                add(AbstractTypes::new);
                add(BasicQueryFunctions::new);
                add(CustomDeserializer::new);
                add(GlobalsAndConfig::new);
                add(JsonResults::new);
                add(LinkProperties::new);
                add(QueryResults::new);
                add(Transactions::new);
            }
        };

        for (var example : examples) {
            var inst = example.get();
            logger.info("Running Java example {}...", inst);
            try {
                inst.run(clientPool).toCompletableFuture().get();
                logger.info("Java example {} complete!", inst);
            }
            catch (Exception x) {
                logger.error("Failed to run Java example {}", inst, x);
            }
        }
    }
}