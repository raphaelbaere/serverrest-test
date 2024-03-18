package com.vemser.tests.produtos;

import client.LoginClient;
import client.ProdutoClient;
import client.UsuarioClient;
import data.factory.ProdutoDataFactory;
import data.factory.UsuarioDataFactory;
import model.Login;
import model.Produto;
import model.ProdutoGenericRes;
import model.Usuario;
import net.datafaker.Faker;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Locale;

public class DeleteProdutosFuncionalTest {

    Faker faker = new Faker(new Locale("PT-BR"));
    static String token = "";
    static String idUsuario = "";
    static String idProduto = "";

    ProdutoClient produtoClient = new ProdutoClient();

    UsuarioClient usuarioClient = new UsuarioClient();

    LoginClient loginClient = new LoginClient();

    static Integer idProdutoInvalido = ((int) (Math.random() * 9999));

    @BeforeEach
    public void setUp() {
        Usuario usuarioACadastrar = UsuarioDataFactory.usuarioValido();
        Login usuarioALogar = new Login(usuarioACadastrar.getEmail(), usuarioACadastrar.getPassword());
        Produto produtoACadastrar = ProdutoDataFactory.produtoValido();

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
    public void testDeletarProdutoComSucesso() {
        ProdutoGenericRes productResponse =
            produtoClient.deletarProduto(idProduto, token)
            .then()
                    .statusCode(200)
                    .extract().as(ProdutoGenericRes.class);

        Assertions.assertEquals(productResponse.getMessage(), "Registro excluído com sucesso");
        Assertions.assertNull(productResponse.get_id());
        ;
    }

    @Test
    public void testDeletarProdutoComIdInválidoComToken() {

        ProdutoGenericRes productResponse =
            produtoClient.deletarProduto(String.valueOf(idProdutoInvalido), token)
            .then()
                    .statusCode(200)
                    .extract().as(ProdutoGenericRes.class);

        Assertions.assertEquals(productResponse.getMessage(), "Nenhum registro excluído");
        Assertions.assertNull(productResponse.get_id());
            ;
    }

    @Test
    public void testDeletarProdutoComIdValidoSemToken() {

        ProdutoGenericRes productResponse =
                produtoClient.deletarProduto(String.valueOf(idProdutoInvalido), "123")
                        .then()
                        .statusCode(401)
                        .extract().as(ProdutoGenericRes.class);

        Assertions.assertEquals(productResponse.getMessage(), "Token de acesso ausente, inválido, expirado ou usuário do token não existe mais");
        Assertions.assertNull(productResponse.get_id());
        ;
    }

}
