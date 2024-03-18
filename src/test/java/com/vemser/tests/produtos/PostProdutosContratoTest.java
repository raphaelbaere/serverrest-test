package com.vemser.tests.produtos;

import client.LoginClient;
import client.ProdutoClient;
import client.UsuarioClient;
import com.github.tomakehurst.wiremock.WireMockServer;
import data.factory.ProdutoDataFactory;
import data.factory.UsuarioDataFactory;
import io.restassured.module.jsv.JsonSchemaValidator;
import model.Login;
import model.Produto;
import model.Usuario;
import net.datafaker.Faker;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static com.github.tomakehurst.wiremock.client.WireMock.*;

import java.util.Locale;

public class PostProdutosContratoTest {

    Faker faker = new Faker(new Locale("PT-BR"));
    static String token = "";
    static String idUsuario = "";

    static String idProduto = "";

    ProdutoClient produtoClient = new ProdutoClient();

    UsuarioClient usuarioClient = new UsuarioClient();

    LoginClient loginClient = new LoginClient();



    @BeforeEach
    public void setUp() {

        Usuario usuarioACadastrar = UsuarioDataFactory.usuarioValido();
        Login usuarioALogar = new Login(usuarioACadastrar.getEmail(), usuarioACadastrar.getPassword());

        idUsuario =
                usuarioClient.cadastrarUsuario(usuarioACadastrar)
                        .then()
                        .statusCode(201)
                        .extract().path("_id")
        ;

        token =
                loginClient.logarUsuario(usuarioALogar)
                        .then()
                        .statusCode(200)
                        .extract().path("authorization");
    }

    @AfterEach
    public void cleanUp() {
        if (idProduto != "") {
            produtoClient.deletarProduto(idProduto, token)
                    .then()
            ;
        }

        usuarioClient.deletarUsuario(idUsuario)
                .then()
        ;

    }


    @Test
    public void testAdicionarProdutoContrato() {
        Produto produtoACadastrar = ProdutoDataFactory.produtoValido();

        produtoClient.cadastrarProduto(produtoACadastrar, token)
                .then()
                .statusCode(201)
                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("schemas/insere_produto.json"))
        ;
    }
}