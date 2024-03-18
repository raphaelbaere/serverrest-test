package client;
import io.restassured.response.Response;
import model.Produto;
import specs.ProdutoSpecs;
import specs.UsuarioSpecs;

import static io.restassured.RestAssured.given;

public class ProdutoClient {
    private static final String PRODUTOS = "/produtos";
    private static final String PRODUTOS_ID = "/produtos/{_id}";

    public Response cadastrarProduto(Produto produto, String token) {

        return
                given()
                        .spec(ProdutoSpecs.produtoSpecs())
                        .header("Authorization", token)
                        .body(produto)
                .when()
                        .post(PRODUTOS)
                ;
    }

    public Response deletarProduto(String idProduto, String token) {
        return
                given()
                        .spec(ProdutoSpecs.produtoSpecs())
                        .header("Authorization", token)
                .when()
                        .delete(PRODUTOS_ID, idProduto)
                ;
    }

    public Response buscarProduto(String idProduto, String token) {
        return
                given()
                        .spec(ProdutoSpecs.produtoSpecs())
                        .header("Authorization", token)
                        .pathParam("_id", idProduto)
                        .when()
                        .get(PRODUTOS_ID, idProduto)
                ;
    }

    public Response buscarProdutoComNumero(Integer idProduto, String token) {
        return
                given()
                        .spec(ProdutoSpecs.produtoSpecs())
                        .header("Authorization", token)
                        .pathParam("_id", idProduto)
                        .when()
                        .get(PRODUTOS_ID, idProduto)
                ;
    }

    public Response atualizarProduto(Produto produto, String idProduto, String token) {
        return
                given()
                        .spec(ProdutoSpecs.produtoSpecs())
                        .header("Authorization", token)
                        .pathParam("_id", idProduto)
                        .body(produto)
                .when()
                        .put(PRODUTOS_ID, idProduto)
                ;
    }
}
