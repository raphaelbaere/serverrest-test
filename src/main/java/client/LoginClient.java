package client;
import io.restassured.response.Response;
import model.Login;
import model.Usuario;
import specs.UsuarioSpecs;

import static io.restassured.RestAssured.given;

public class LoginClient {
    private static final String LOGIN = "/login";

    public Response logarUsuario(Login usuarioALogar) {

        return
                given()
                        .log().all()
                        .spec(UsuarioSpecs.usuarioReqSpec())
                        .body(usuarioALogar)
                        .when()
                        .post("/login")
                ;
    }
}
