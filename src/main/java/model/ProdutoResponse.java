package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProdutoResponse {
    private String nome;
    private String preco;
    private String descricao;
    private String quantidade;
    private String _id;
}
