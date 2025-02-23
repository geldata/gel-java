module com.geldata.driver {
    exports com.geldata.driver;
    exports com.geldata.driver.datatypes;
    exports com.geldata.driver.exceptions;
    exports com.geldata.driver.namingstrategies;
    exports com.geldata.driver.annotations;
    exports com.geldata.driver.state;

    requires org.jetbrains.annotations;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;
    requires java.naming;
    requires org.slf4j;
    requires io.netty.common;
    requires io.netty.transport;
    requires io.netty.codec;
    requires io.netty.buffer;
    requires io.netty.handler;
    requires org.jooq.joou;
    requires org.reflections;
    requires java.net.http;

    opens com.geldata.driver;
}
