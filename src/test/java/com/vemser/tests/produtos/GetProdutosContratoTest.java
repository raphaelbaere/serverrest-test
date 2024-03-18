package com.vemser.tests.produtos;

import client.LoginClient;
import client.ProdutoClient;
import client.UsuarioClient;
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

import java.util.Locale;

public class GetProdutosContratoTest {

    static String token = "";
    static String idUsuario = "";
    static String idProduto = "";

    Faker faker = new Faker(new Locale("PT-BR"));
    Produto produtoACadastrar = ProdutoDataFactory.produtoValido();

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
        idProduto =
                produtoClient.cadastrarProduto(produtoACadastrar, token)
                        .then()
                        .extract().path("_id");
        ;
    }
    @AfterEach
    public void cleanUp() {
        produtoClient.deletarProduto(idProduto, token);

        usuarioClient.deletarUsuario(idUsuario)
                .then()
        ;
    }


    @Test
    public void testGetProdutoContrato() {
        produtoClient.buscarProduto(idProduto, token)
                .then()
                .statusCode(200)
                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("schemas/busca_produto_id.json"))
        ;
    }

}