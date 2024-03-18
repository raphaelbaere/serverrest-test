package data.factory;

import model.Produto;
import net.datafaker.Faker;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.params.provider.Arguments;

import java.util.Locale;
import java.util.Random;

public class ProdutoDataFactory {
    private static Faker faker = new Faker(new Locale("PT-BR"));

    private ProdutoDataFactory() {}

    public static Produto produtoValido() {
        return novoProduto();
    }

    public static Produto produtoComDadosAusentes() {
        Produto produtoSemDados = novoProduto();
        produtoSemDados.setNome(StringUtils.EMPTY);
        produtoSemDados.setPreco(StringUtils.EMPTY);
        produtoSemDados.setDescricao(StringUtils.EMPTY);
        produtoSemDados.setQuantidade(StringUtils.EMPTY);

        return produtoSemDados;
    }
    private static Produto novoProduto() {

        Produto novoProduto = new Produto();
        novoProduto.setNome(faker.commerce().productName());
        novoProduto.setDescricao(faker.lorem().sentence());
        novoProduto.setPreco(String.valueOf(faker.number().randomDigit()));
        novoProduto.setQuantidade(String.valueOf(faker.number().randomDigit()));

        return novoProduto;
    }

    public static Produto produtoComNomeVazio() {
        Produto novoProduto = new Produto();
        novoProduto.setNome(StringUtils.EMPTY);
        novoProduto.setDescricao(faker.lorem().sentence());
        novoProduto.setPreco(String.valueOf(faker.number().randomDigit()));
        novoProduto.setQuantidade(String.valueOf(faker.number().randomDigit()));

        return novoProduto;
    }

    public static Produto produtoComPrecoInvalido() {
        Produto novoProduto = new Produto();
        novoProduto.setNome(faker.commerce().productName());
        novoProduto.setDescricao(faker.lorem().sentence());
        novoProduto.setPreco(faker.lorem().sentence());
        novoProduto.setQuantidade(String.valueOf(faker.number().randomDigit()));

        return novoProduto;
    }
    public static Produto produtoComDescricaoVazia() {
        Produto novoProduto = new Produto();
        novoProduto.setNome(faker.commerce().productName());
        novoProduto.setDescricao(StringUtils.EMPTY);
        novoProduto.setPreco(String.valueOf(faker.number().randomDigit()));
        novoProduto.setQuantidade(String.valueOf(faker.number().randomDigit()));

        return novoProduto;
    }
    public static Produto produtoComQuantidadeInvalida() {
        Produto novoProduto = new Produto();
        novoProduto.setNome(faker.commerce().productName());
        novoProduto.setDescricao(faker.lorem().sentence());
        novoProduto.setPreco(String.valueOf(faker.number().randomDigit()));
        novoProduto.setQuantidade(faker.lorem().sentence());

        return novoProduto;
    }
}