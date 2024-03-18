package com.vemser.tests.produtos;

import client.LoginClient;
import client.ProdutoClient;
import client.UsuarioClient;
import data.factory.ProdutoDataFactory;
import data.factory.UsuarioDataFactory;
import model.*;
import net.datafaker.Faker;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Locale;
import static org.hamcrest.Matchers.*;

public class PutProdutosFuncionalTest {

    static String token = "";
    static String idUsuario = "";
    static String idProduto = "";

    ProdutoClient produtoClient = new ProdutoClient();

    UsuarioClient usuarioClient = new UsuarioClient();

    LoginClient loginClient = new LoginClient();

    Faker faker = new Faker(new Locale("PT-BR"));

    static Integer idProdutoInvalido = ((int) (Math.random() * 9999));

    @BeforeEach
    public void setUp() {
        Usuario usuarioACadastrar = UsuarioDataFactory.usuarioValido();
        Login usuarioALogar = new Login(usuarioACadastrar.getEmail(), usuarioACadastrar.getPassword());
        Produto produtoACadastrar = ProdutoDataFactory.produtoValido();

        idUsuario =
                usuarioClient.cadastrarUsuario(usuarioACadastrar)
                        .then()
                        .log().all()
                        .statusCode(201)
                        .extract().path("_id")
        ;

        token =
                loginClient.logarUsuario(usuarioALogar)
                        .then()
                        .log().all()
                        .statusCode(200)
                        .extract().path("authorization");
        idProduto =
                produtoClient.cadastrarProduto(produtoACadastrar, token)
                        .then()
                        .log().all()
                        .extract().path("_id");
        ;
    }

    @AfterEach
    public void cleanUp() {
        produtoClient.deletarProduto(idProduto, token);

        usuarioClient.deletarUsuario(idUsuario)
                .then()
                .log().all()
        ;
    }


    @Test
    public void testEditarProdutoComSucesso() {
        Produto produtoACadastrar = ProdutoDataFactory.produtoValido();
        ProdutoGenericRes produtoCadastrado = produtoClient.cadastrarProduto(produtoACadastrar, token).as(ProdutoGenericRes.class);

        Produto produtoAtualizado = ProdutoDataFactory.produtoValido();

        produtoClient.atualizarProduto(produtoAtualizado, produtoCadastrado.get_id(), token)
        .then()
                .statusCode(200)
                .body("message", equalTo("Registro alterado com sucesso"))
                .body("_id", nullValue())
                .log().all()
        ;
    }

    @Test
    public void testEditarProdutoSemSucessoComIdInválido() {
        Produto produtoAtualizado = ProdutoDataFactory.produtoValido();

        produtoClient.atualizarProduto(produtoAtualizado, String.valueOf(idProdutoInvalido), token)
                .then()
                .statusCode(201)
                .body("message", equalTo("Cadastro realizado com sucesso"))
                .body("_id", notNullValue())
                .log().all()
        ;
    }
    @Test
    public void testEditarProdutoSemSucessoComIdValidoETokenInvalido() {
        Produto produtoAtualizado = ProdutoDataFactory.produtoValido();

        produtoClient.atualizarProduto(produtoAtualizado, idProduto, "123")
                .then()
                .statusCode(401)
                .body("message", equalTo("Token de acesso ausente, inválido, expirado ou usuário do token não existe mais"))
                .body("_id", nullValue())
                .log().all()
        ;
    }
}
