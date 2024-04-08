package uel.br.restaurante;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface RestauranteRepository extends JpaRepository<Restaurante, Integer> {
}

