package uel.br.restaurante;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface CardapioRepository extends JpaRepository<Cardapio, Integer>{   //vai extender o JpaRepository, que é uma interface de repositório de Spring Data JPA para CRUD e outras operações em entidades
    List<Cardapio> findByRestaurante(Restaurante restaurante);  //vai retornar uma lista de cardapio que tem o restaurante passado como parâmetro

    //List<Cardapio> findByRestaurante(Restaurante restaurante);
}

