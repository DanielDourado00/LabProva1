package uel.br.restaurante;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.io.Serializable;
import java.util.Set;

/**
 - ter um projeto com uma aplicação para administração, que vai permitir que o
 administrador: (i) cadastre, liste, edite e apague restaurantes; (ii) cadastre, liste,
 edite e apague itens de cardápio de um restaurante. Todos estes dados devem ser armazenados
 no banco de dados.  */

/*Ideia, colocar estrelas de recomendacao*/



@Entity
@Table(name = "restaurante")
public class Restaurante implements Serializable {

    @OneToMany(mappedBy = "restaurante")    //Anotação que indica que a classe tem um relacionamento de um para muitos com a classe Cardapio
    //Anotação que indica que a classe tem um relacionamento de um para muitos com a classe Cardapio
    private Set<Cardapio> cardapio;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id; //id do restaurante
    @NotBlank(message = "O nome é obrigatório")
    private String nome; //nome do restaurante


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

    @Override
    public boolean equals(Object o) { // Método que compara se dois objetos são iguais
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        Restaurante restaurante = (Restaurante) o;
        return this.id == restaurante.id;
    }

    @Override                       //Método que gera um código hash para o objeto (numero que representa objeto)
    public int hashCode() {
        return this.id * 31;    //31 é um número primo, que é usado para gerar o código hash
    }


    public Set<Cardapio> getCardapioSet() {
        return cardapio;
    }
}
