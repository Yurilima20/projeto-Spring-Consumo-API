package br.com.alura.screenmatch;

import br.com.alura.screenmatch.principal.Principal;
import br.com.alura.screenmatch.repository.SerieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ScreenmatchApplicationComWeb implements CommandLineRunner {

   @Autowired
   private SerieRepository repositorioSerie;
   public static void main(String[] args) {
       SpringApplication.run(ScreenmatchApplicationComWeb.class, args);
   }
   @Override
   public void run(String... args) throws Exception {
       Principal principal = new Principal(repositorioSerie);
       principal.exibeMenu();
   }
}
