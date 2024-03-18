package specs;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.config;
import static io.restassured.config.LogConfig.logConfig;

public class InicialSpecs {

    private InicialSpecs() {}

    public static RequestSpecification setup() {
        return new RequestSpecBuilder()
                .setBaseUri("http://172.18.0.3:3000")
                .setConfig(config().logConfig(
                        logConfig().enableLoggingOfRequestAndResponseIfValidationFails()
                ))
                .setPort(3000)
                .build();
    }
}