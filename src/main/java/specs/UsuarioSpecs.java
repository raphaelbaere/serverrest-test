package specs;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.config;
import static io.restassured.config.LogConfig.logConfig;

public class UsuarioSpecs {

    private UsuarioSpecs() {}

    public static RequestSpecification usuarioReqSpec() {
        return new RequestSpecBuilder()
                .addRequestSpecification(InicialSpecs.setup())
                .setContentType(ContentType.JSON)
                .setConfig(config().logConfig(
                        logConfig().enableLoggingOfRequestAndResponseIfValidationFails()
                ))
                .build();
    }
}