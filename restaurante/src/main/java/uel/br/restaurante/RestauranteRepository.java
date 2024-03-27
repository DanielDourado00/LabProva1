package uel.br.restaurante;

import org.springframework.data.repository.CrudRepository;

public interface RestauranteRepository extends CrudRepository<Restaurante, Integer> {
    //crud é um acrônimo para create, read, update e delete, que são as quatro operações básicas usadas em bancos de dados.
}
