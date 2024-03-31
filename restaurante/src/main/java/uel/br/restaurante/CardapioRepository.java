package uel.br.restaurante;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface CardapioRepository extends JpaRepository<Cardapio, Integer>{
    List<Cardapio> findCardapioByNome(Integer idRestaurante);       //Cardapio é a classe que representa o cardápio do restaurante ela é a entidade que será manipulada no banco de dados.
    //Cardapio é a classe que representa o cardápio do restaurante ela é a entidade que será manipulada no banco de dados.
}
