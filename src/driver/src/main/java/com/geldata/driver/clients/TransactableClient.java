package com.geldata.driver.clients;

import org.jetbrains.annotations.NotNull;

import com.geldata.driver.*;
import com.geldata.driver.internal.TransactionImpl;

import java.util.concurrent.CompletionStage;
import java.util.function.Function;

public interface TransactableClient extends GelQueryable, AutoCloseable {
    TransactionState getTransactionState();

    default <T> CompletionStage<T> transaction(@NotNull Function<Transaction, CompletionStage<T>> func) {
        return transaction(TransactionSettings.DEFAULT, func);
    }

    default <T> CompletionStage<T> transaction(
            TransactionSettings settings,
            @NotNull Function<Transaction, CompletionStage<T>> func
    ) {
        var tx = new TransactionImpl(this, settings);
        return tx.run(func);
    }

    CompletionStage<Void> startTransaction(TransactionIsolation isolation, boolean readonly, boolean deferrable);

    CompletionStage<Void> commit();

    CompletionStage<Void> rollback();
}
