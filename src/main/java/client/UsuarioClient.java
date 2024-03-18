package client;

import io.restassured.response.Response;
import model.Usuario;
import specs.UsuarioSpecs;

import static io.restassured.RestAssured.*;

public class UsuarioClient {

    private static final String USUARIOS = "/usuarios";

    private static final String USUARIOS_ID = "/usuarios/{_id}";

    public UsuarioClient() {
    }

    public Response cadastrarUsuario(Usuario usuario) {

        return
                given()
                        .spec(UsuarioSpecs.usuarioReqSpec())
                        .body(usuario)
                        .when()
                        .post(USUARIOS)
                ;
    }

    public Response deletarUsuario(String idUsuario) {

        return
                given()
                        .spec(UsuarioSpecs.usuarioReqSpec())
                        .pathParam("_id", idUsuario)
                .when()
                        .delete(USUARIOS_ID, idUsuario);
    }
}