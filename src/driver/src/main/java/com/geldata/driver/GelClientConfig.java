package com.geldata.driver;

import org.jetbrains.annotations.NotNull;

import com.geldata.driver.namingstrategies.NamingStrategy;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

public class GelClientConfig {
    public static final @NotNull GelClientConfig DEFAULT = new GelClientConfig();
    public static @NotNull Builder builder() {
        return new Builder();
    }

    private int poolSize = 50;
    private ConnectionRetryMode retryMode = ConnectionRetryMode.ALWAYS_RETRY;
    private int maxConnectionRetries = 5;
    private long messageTimeout = 15;
    private TimeUnit messageTimeoutUnit = TimeUnit.SECONDS;
    private boolean explicitObjectIds = true;
    private long implicitLimit;
    private boolean implicitTypeIds;
    private NamingStrategy namingStrategy = NamingStrategy.defaultStrategy();
    private boolean useFieldSetters = false;
    private ClientType clientType = ClientType.TCP;
    private int clientAvailability = 10;
    private Duration clientMaxAge = Duration.of(10, ChronoUnit.MINUTES);

    /**
     * Gets the number of attempts to try to connect.
     * @return The current number of attempts.
     */
    public int getMaxConnectionRetries() {
        return maxConnectionRetries;
    }

    /**
     * Gets the message timeout value.
     * @return The current message timeout value.
     */
    public long getMessageTimeoutValue() {
        return messageTimeout;
    }

    /**
     * Gets the message timeouts' unit.
     * @return The current unit of the message timeout.
     */
    public TimeUnit getMessageTimeoutUnit() {
        return messageTimeoutUnit;
    }

    /**
     * Gets the message timeout as a specific unit.
     * @param unit The unit to get the timeout as.
     * @return The current timeout value, as the supplied unit.
     */
    public long getMessageTimeout(@NotNull TimeUnit unit) {
        return unit.convert(messageTimeout, messageTimeoutUnit);
    }

    /**
     * Gets whether the {@code id} property on objects need to be explicitly defined. When this is {@code false},
     * {@code id} will be included in all objects, regardless of shape.
     * @return {@code true} if {@code id} needs to be explicitly defined in the shape; otherwise {@code false}.
     */
    public boolean getExplicitObjectIds() {
        return explicitObjectIds;
    }

    /**
     * Gets the configured limit for query results.
     * @return The current maximum number of elements in the result of a query.
     */
    public long getImplicitLimit() {
        return implicitLimit;
    }

    /**
     * Gets the configured size in a client pool.
     * @return The current configured size for a client pool.
     */
    public int getPoolSize() {
        return poolSize;
    }

    /**
     * Gets the retry mode for connecting.
     * @return The current connection retry mode.
     * @see ConnectionRetryMode
     */
    public ConnectionRetryMode getConnectionRetryMode() {
        return retryMode;
    }

    /**
     * Gets the naming strategy used within the schema.
     * @return The current naming strategy.
     * @see NamingStrategy
     */
    public NamingStrategy getNamingStrategy() {
        return namingStrategy;
    }

    /**
     * Gets whether field setters will be used, if present, to populate fields in dataclasses.
     * @return {@code true} if field setters should be used.
     */
    public boolean useFieldSetters() {
        return useFieldSetters;
    }

    /**
     * Gets the client type to use in a client pool.
     * @return The client type to use in a client pool.
     * @see ClientType
     */
    public ClientType getClientType() {
        return clientType;
    }

    /**
     * Gets the total number of clients to keep around in a client pool, regardless of connection state.
     * @return The number of clients to keep in a client pool, regardless of connection state.
     */
    public int getClientAvailability() {
        return clientAvailability;
    }

    /**
     * Gets the max age of an inactive client within a client pool.
     * @return A {@linkplain Duration} that represents the max age of an inactive client within a client pool.
     */
    public Duration getClientMaxAge() {
        return clientMaxAge;
    }

    /**
     * Gets whether {@code __tid__} should be implicitly included on all objects, regardless of shape.
     * @return {@code true} if {@code __tid__} is included on all objects, regardless of shape.
     */
    public boolean getImplicitTypeIds() {
        return implicitTypeIds;
    }

    /**
     * A builder class used to construct {@linkplain GelClientConfig}s.
     */
    public static final class Builder {
        private int poolSize = DEFAULT.poolSize;
        private ConnectionRetryMode retryMode = DEFAULT.retryMode;
        private int maxConnectionRetries = DEFAULT.maxConnectionRetries;
        private long messageTimeout = DEFAULT.messageTimeout;
        private TimeUnit messageTimeoutUnit = DEFAULT.messageTimeoutUnit;
        private boolean explicitObjectIds = DEFAULT.explicitObjectIds;
        private long implicitLimit = DEFAULT.implicitLimit;
        private boolean implicitTypeIds = DEFAULT.implicitTypeIds;
        private NamingStrategy namingStrategy = DEFAULT.namingStrategy;
        private boolean useFieldSetters = DEFAULT.useFieldSetters;
        private ClientType clientType = DEFAULT.clientType;
        private int clientAvailability = DEFAULT.clientAvailability;
        private Duration clientMaxAge = DEFAULT.clientMaxAge;

        /**
         * Sets the pool size of the current builder.
         * @param poolSize The value to set.
         * @return The current builder.
         * @exception IllegalArgumentException The pool size must be greater than 0
         */
        public @NotNull Builder withPoolSize(int poolSize) {
            if(poolSize <= 0) {
                throw new IllegalArgumentException("Pool size must be at least 1");
            }

            this.poolSize = poolSize;

            return this;
        }

        /**
         * Sets the retry mode of the current builder.
         * @param retryMode The value to set.
         * @return The current builder.
         */
        public @NotNull Builder withRetryMode(@NotNull ConnectionRetryMode retryMode) {
            this.retryMode = retryMode;
            return this;
        }

        /**
         * Sets the max connection retries of the current builder.
         * @param maxConnectionRetries The value to set.
         * @return The current builder.
         */
        public @NotNull Builder withMaxConnectionRetries(int maxConnectionRetries) {
            this.maxConnectionRetries = maxConnectionRetries;
            return this;
        }

        /**
         * Sets the message timeout of the current builder.
         * @param messageTimeout The value to set.
         * @param unit The unit of the value.
         * @return The current builder.
         */
        public @NotNull Builder withMessageTimeout(long messageTimeout, TimeUnit unit) {
            this.messageTimeout = messageTimeout;
            this.messageTimeoutUnit = unit;
            return this;
        }

        /**
         * Sets whether the {@code id} property on objects need to be explicitly defined. When this is {@code false},
         * {@code id} will be included in all objects, regardless of shape.
         * @param explicitObjectIds The value to set.
         * @return The current builder.
         */
        public @NotNull Builder withExplicitObjectIds(boolean explicitObjectIds) {
            this.explicitObjectIds = explicitObjectIds;
            return this;
        }

        /**
         * Sets the implicit result limit of all queries. If the value is {@code 0}, there is not limit.
         * @param implicitLimit The value to set.
         * @return The current builder.
         */
        public @NotNull Builder withImplicitLimit(long implicitLimit) {
            this.implicitLimit = implicitLimit;
            return this;
        }

        /**
         * Sets whether {@code __tid__} should be implicitly included on all objects, regardless of shape.
         * @param implicitTypeIds The value to set.
         * @return The current builder.
         */
        public @NotNull Builder withImplicitTypeIds(boolean implicitTypeIds) {
            this.implicitTypeIds = implicitTypeIds;
            return this;
        }

        /**
         * Sets the naming strategy used within the schema.
         * @param namingStrategy The value to set.
         * @return The current builder.
         * @see NamingStrategy
         * @see NamingStrategy#snakeCase()
         * @see NamingStrategy#camelCase()
         * @see NamingStrategy#pascalCase()
         * @see NamingStrategy#defaultStrategy()
         */
        public @NotNull Builder withNamingStrategy(NamingStrategy namingStrategy) {
            this.namingStrategy = namingStrategy;
            return this;
        }

        /**
         * Sets whether the type builder will use field setters, if present, to populate data classes.
         * @param useFieldSetters The value to set.
         * @return The current builder.
         */
        public @NotNull Builder useFieldSetters(boolean useFieldSetters) {
            this.useFieldSetters = useFieldSetters;
            return this;
        }

        /**
         * Sets the client type to use.
         * @param clientType The value to set.
         * @return The current builder.
         * @see ClientType
         */
        public @NotNull Builder withClientType(@NotNull ClientType clientType) {
            this.clientType = clientType;
            return this;
        }

        /**
         * Sets the number of client instances to keep around within the client pool, regardless of their connection
         * state.
         * @param clientAvailability The value to set.
         * @return The current builder.
         * @exception IllegalArgumentException Value must be greater than zero.
         */
        public @NotNull Builder withClientAvailability(int clientAvailability) {
            if(clientAvailability < 0) {
                throw new IllegalArgumentException("Client availability must be greater than 0");
            }

            this.clientAvailability = clientAvailability;
            return this;
        }

        /**
         * Sets how long an inactive client should live in the client pool.
         * @param clientMaxAge The value to set.
         * @return The current builder.
         */
        public @NotNull Builder withClientMaxAge(@NotNull Duration clientMaxAge) {
            this.clientMaxAge = clientMaxAge;
            return this;
        }

        /**
         * Constructs a {@linkplain GelClientConfig} from the current builder.
         * @return A {@linkplain GelClientConfig} that represents the current builder.
         */
        public @NotNull GelClientConfig build() {
            GelClientConfig gelClientConfig = new GelClientConfig();
            gelClientConfig.clientType = this.clientType;
            gelClientConfig.implicitLimit = this.implicitLimit;
            gelClientConfig.explicitObjectIds = this.explicitObjectIds;
            gelClientConfig.messageTimeoutUnit = this.messageTimeoutUnit;
            gelClientConfig.maxConnectionRetries = this.maxConnectionRetries;
            gelClientConfig.clientMaxAge = this.clientMaxAge;
            gelClientConfig.poolSize = this.poolSize;
            gelClientConfig.useFieldSetters = this.useFieldSetters;
            gelClientConfig.namingStrategy = this.namingStrategy;
            gelClientConfig.clientAvailability = this.clientAvailability;
            gelClientConfig.implicitTypeIds = this.implicitTypeIds;
            gelClientConfig.retryMode = this.retryMode;
            gelClientConfig.messageTimeout = this.messageTimeout;
            return gelClientConfig;
        }
    }
}
