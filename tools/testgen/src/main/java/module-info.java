module com.geldata.testgen {
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;
    requires com.geldata.driver;

    exports com.geldata.testgen to com.fasterxml.jackson.databind;
}