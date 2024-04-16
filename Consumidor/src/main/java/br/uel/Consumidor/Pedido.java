package br.uel.Consumidor;

import java.io.Serial;
import java.io.Serializable;

// Nesta classe é feito o pedido do consumidor, a única diferença será que aqui terá a quantidade

public class Pedido extends CardapioConsumidor implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L; // Atributo que representa a versão da classe

    public Pedido(CardapioConsumidor cardapio, int quantidade) {
        this.setId(cardapio.getId());
        this.setNome(cardapio.getNome());
        this.setPreco(cardapio.getPreco());
        this.setQuantidade(quantidade);
        this.setDescricao(cardapio.getDescricao());
        this.setValor(cardapio.getPreco() * quantidade); // Correção aqui
    }

    public Pedido() {

    }
  //ter um cardapio com getters e setters
    private CardapioConsumidor cardapio;

    public CardapioConsumidor getCardapio() {
        return cardapio;
    }

    private double valor;

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    private int quantidade;

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
        System.out.println("QuantidadeEEEEEEEEEEEEEEEEEEEEEEEE: " + quantidade);
    }
}
