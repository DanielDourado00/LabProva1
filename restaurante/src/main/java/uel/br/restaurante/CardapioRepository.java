package uel.br.restaurante;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface CardapioRepository extends JpaRepository<Cardapio, Integer>{   //vai extender o JpaRepository, que é uma interface de repositório de Spring Data JPA para CRUD e outras operações em entidades
    List<Cardapio> findCardapioByRestauranteId(Integer restauranteId);//vai retornar uma lista de cardapio, vai buscar o cardapio pelo id do restaurante

    //List<Cardapio> findByRestaurante(Restaurante restaurante);
}

