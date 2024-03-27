package uel.br.restaurante;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;
import java.io.Serializable;
import java.util.Date;

//itens do cardapio do restaurante
@Entity //Anotação que indica que a classe é uma entidade
@Table(name = "restaurante") //Anotação que indica o nome da tabela no banco de dados
public class Cardapio implements Serializable {

    @ManyToOne //Anotação que indica que a classe tem um relacionamento de muitos para um com a classe Restaurante
    @JoinColumn(name = "id_restaurante", nullable = false) //Anotação que indica a chave estrangeira que faz o relacionamento //nullable = false indica que o campo não pode ser nulo
    private Restaurante restaurante; //Restaurante é a classe que representa o restaurante
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id; //id do item do cardapio
    private String nome; //nome do item do cardapio
    private String descricao; //descrição do item do cardapio
    private double preco; //preço do item do cardapio


    public Restaurante getRestaurante() {
        return restaurante;
    }

    public void setRestaurante(Restaurante restaurante) {
        this.restaurante = restaurante;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }


    @Override
    public boolean equals(Object o) { // Método que compara se dois objetos são iguais
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        Cardapio cardapio = (Cardapio) o;
        return this.id == cardapio.id;
    }

    @Override //Método que gera um código hash para o objeto (numero que representa objeto)
    public int hashCode() {
        return this.id * 31; //31 é um número primo, que é usado para gerar o código hash
    }
}
