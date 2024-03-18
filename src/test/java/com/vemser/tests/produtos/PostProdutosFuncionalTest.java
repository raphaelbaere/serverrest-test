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
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import java.util.Locale;
import static org.hamcrest.Matchers.equalTo;

public class PostProdutosFuncionalTest {

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
    public void testAdicionarProdutoComSucesso() {
        Produto produtoACadastrar = ProdutoDataFactory.produtoValido();

        ProdutoGenericRes productResponse =
                produtoClient.cadastrarProduto(produtoACadastrar, token)
                .then()
                        .statusCode(201)
                        .extract().as(ProdutoGenericRes.class);

                idProduto = productResponse.get_id();
                Assertions.assertEquals(productResponse.getMessage(), "Cadastro realizado com sucesso");
                Assertions.assertNotNull(productResponse.get_id());
        ;
    }

    @Test
    public void testAdicionarProdutoSemSucessoSemToken() {
        Produto produtoACadastrar = ProdutoDataFactory.produtoValido();

        ProdutoGenericRes productResponse =
                produtoClient.cadastrarProduto(produtoACadastrar, "")
                .then()
                        .statusCode(401)
                        .extract().as(ProdutoGenericRes.class);

        Assertions.assertEquals(productResponse.getMessage(), "Token de acesso ausente, inválido, expirado ou usuário do token não existe mais");
        Assertions.assertNull(productResponse.get_id());
        ;
    }

    @Test
    public void testAdicionarProdutoSemSucessoComTokenValidoEProdutoInvalido() {
        Produto produtoACadastrar = ProdutoDataFactory.produtoComDadosAusentes();

        ProdutoResponse productResponse =
                produtoClient.cadastrarProduto(produtoACadastrar, token)
                        .then()
                        .statusCode(400)
                        .extract().as(ProdutoResponse.class);

        Assertions.assertEquals(productResponse.getNome(), "nome não pode ficar em branco");
        Assertions.assertEquals(productResponse.getPreco(), "preco deve ser um número");
        Assertions.assertEquals(productResponse.getDescricao(), "descricao não pode ficar em branco");
        Assertions.assertEquals(productResponse.getQuantidade(), "quantidade deve ser um número");

        Assertions.assertNull(productResponse.get_id());
        ;
    }


    @ParameterizedTest
    @MethodSource("data.provider.ProdutoDataProvider#gerarProdutosComValoresInvalidos")
    public void testAdicionarProdutoSemSucessoComTokenValidoEProdutoComDiferentesCamposInvalidos(Produto produto, String key, String value) {
        ProdutoResponse productResponse =
                produtoClient.cadastrarProduto(produto, token)
                        .then()
                        .statusCode(400)
                        .body(key, equalTo(value))
                        .extract().as(ProdutoResponse.class);
    }
}
