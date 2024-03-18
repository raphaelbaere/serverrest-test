package com.vemser.tests.produtos;

import client.LoginClient;
import client.ProdutoClient;
import client.UsuarioClient;
import data.factory.ProdutoDataFactory;
import data.factory.UsuarioDataFactory;
import model.*;
import net.datafaker.Faker;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Locale;

public class GetProdutosFuncionalTest {

    static String token = "";
    static String idUsuario = "";
    static String idProduto = "";

    Produto produtoACadastrar = ProdutoDataFactory.produtoValido();

    ProdutoClient produtoClient = new ProdutoClient();

    UsuarioClient usuarioClient = new UsuarioClient();

    LoginClient loginClient = new LoginClient();

    Faker faker = new Faker(new Locale("PT-BR"));

    static Integer idProdutoInvalido = ((int) (Math.random() * 9999));

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
    public void testBuscarProdutoPorIDComSucesso() {
        ProdutoResponse produtoBuscado =
        produtoClient.buscarProduto(idProduto, token)
        .then()
                .statusCode(200)
                .extract().as(ProdutoResponse.class);
        ;

        Assertions.assertEquals(produtoBuscado.getNome(), produtoACadastrar.getNome());
        Assertions.assertEquals(produtoBuscado.getPreco(), produtoACadastrar.getPreco());
        Assertions.assertEquals(produtoBuscado.getDescricao(), produtoACadastrar.getDescricao());
        Assertions.assertEquals(produtoBuscado.getQuantidade(), produtoACadastrar.getQuantidade());
        Assertions.assertEquals(produtoBuscado.get_id(), idProduto);
    }

    @Test
    public void testBuscarProdutoPorIDSemSucessoComIdInvalido() {

        ProdutoGenericRes productResponse =
            produtoClient.buscarProduto(String.valueOf(idProdutoInvalido), token)
            .then()
                    .statusCode(400)
                    .extract().as(ProdutoGenericRes.class);

        Assertions.assertEquals(productResponse.getMessage(), "Produto não encontrado");
        Assertions.assertNull(productResponse.get_id());
        ;
    }

    @Test
    public void testBuscarProdutoPorIDSemSucessoComIdInvalidoComoNumero() {

        ProdutoGenericRes productResponse =
                produtoClient.buscarProdutoComNumero(123456, token)
                        .then()
                            .statusCode(400)
                            .extract().as(ProdutoGenericRes.class);

        Assertions.assertEquals(productResponse.getMessage(), "Produto não encontrado");
        Assertions.assertNull(productResponse.get_id());
        ;
    }

}
