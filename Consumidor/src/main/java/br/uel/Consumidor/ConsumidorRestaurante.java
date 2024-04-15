package br.uel.Consumidor;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Set;
//entidade de consumidor, nao fara alteracao no banco de dados, apenas obter os itens do banco de dados
//esta entidade vai ser o consumidor, que vai consumir os itens do banco de dados

//lida com tabela restaurante
@Entity
@Table(name = "restaurante")
public class ConsumidorRestaurante implements Serializable {

    @OneToMany(mappedBy = "restaurante")
    private Set<CardapioConsumidor> cardapio;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // Indica que o valor do atributo é gerado automaticamente pelo banco de dados
    private int id;                                     // Atributo que representa a chave primária da tabela, se ele estiver vazio, o banco de dados irá preencher automaticamente

    private String nome;


    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }


    public void setId(int id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Set<CardapioConsumidor> getPedidos() {      // Método que retorna o cardápio
        return cardapio;
    }

    @Override
    public boolean equals(Object o) { // Método que compara se dois objetos são iguais
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        return ((ConsumidorRestaurante) o).id == (this.id); // Compara se o id do objeto passado como parâmetro é igual ao id do objeto atual (this
    }

    @Override           // Indica que o método está sobrescrevendo um método da superclasse
    public int hashCode() { // Método que retorna o valor do atributo id
        return id * 12345;     // retorna o valor
    }
}
