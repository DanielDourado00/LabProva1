package uel.br.restaurante;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CardapioController {

    @Autowired                          //A anotação @Autowired é usada para permitir que o Spring resolva e injete colaboradores em nosso bean.
     CardapioRepositoy cardapioRepositoy;       //CardapioRepositoy é a interface que estende JpaRepository e que será usada para manipular os dados do banco de dados.
}
