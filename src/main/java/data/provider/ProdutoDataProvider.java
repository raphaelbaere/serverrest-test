package data.provider;

import data.factory.ProdutoDataFactory;
import org.junit.jupiter.params.provider.Arguments;

import java.util.stream.Stream;

public class ProdutoDataProvider {
    public static Stream<Arguments> gerarProdutosComValoresInvalidos() {
        return Stream.of(
                Arguments.of(ProdutoDataFactory.produtoComNomeVazio(), "nome", "nome não pode ficar em branco"),
                Arguments.of(ProdutoDataFactory.produtoComPrecoInvalido(), "preco", "preco deve ser um número"),
                Arguments.of(ProdutoDataFactory.produtoComDescricaoVazia(), "descricao", "descricao não pode ficar em branco"),
                Arguments.of(ProdutoDataFactory.produtoComQuantidadeInvalida(), "quantidade", "quantidade deve ser um número")
        );
    }
}
