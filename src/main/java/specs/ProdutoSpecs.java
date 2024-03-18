package specs;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.config;
import static io.restassured.config.LogConfig.logConfig;

public class ProdutoSpecs {

    private ProdutoSpecs() {}

    public static RequestSpecification produtoSpecs() {
        return new RequestSpecBuilder()
                .addRequestSpecification(InicialSpecs.setup())
                .setContentType(ContentType.JSON)
                .setConfig(config().logConfig(
                        logConfig().enableLoggingOfRequestAndResponseIfValidationFails()
                ))
                .build();
    }
}