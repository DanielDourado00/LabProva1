package br.uel.Consumidor;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ConsumidorRestauranteRepository extends JpaRepository<ConsumidorRestaurante, Integer> {
    ConsumidorRestaurante findByNome(String nome);
}
