package br.uel.Consumidor;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "item_cardapio")
public class CardapioConsumidor implements Serializable {

    @ManyToOne
    @JoinColumn(name = "id_restaurante", nullable = false)
    private ConsumidorRestaurante restaurante; // Corrigido para apontar para ConsumidorRestaurante


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Define que o campo é uma chave primária e que é gerada automaticamente

    private int id; //id do item do cardapio
    private String nome; //nome do item do cardapio
    private String descricao; //descrição do item do cardapio
    private double preco; //preço do item do cardapio

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
        return ((CardapioConsumidor)o).id == (this.id); // Compara se o id do objeto passado como parâmetro é igual ao id do objeto atual (this
    }

    @Override           // Indica que o método está sobrescrevendo um método da superclasse
    public int hashCode() { // Método que retorna o valor do atributo id
        return id * 12345;     // retorna o valor do atributo id multiplicado por 12345
    }
}