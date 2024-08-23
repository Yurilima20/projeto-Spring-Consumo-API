package br.com.alura.screenmatch.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
@JsonIgnoreProperties(ignoreUnknown = true) // ignora o que você não encontrar
public record DadosSerie(@JsonAlias("Title") String titulo,
                         @JsonAlias("totalSeasons") Integer totalTemporadas,
                         @JsonAlias("imdbRating") String avaliacao,
                         @JsonAlias("Genre") String genero,
                         @JsonAlias("Actors") String atores,
                         @JsonAlias("Poster") String poster,
                         @JsonAlias("Plot") String sinopse) {
    //@JsonProperty("imdbVotes") String votos esse aqui foi para exemplo,m irei deixar ele aqui para lembrar do property,
    // ele estava depois do avaliacao
    // diferencça entre Alias e property, o property ele serializa e desrealiza, enquanto o alias so desarializa, em outras
    // palavras ele lé title, mas na hora de escrever serializar ele escreve o nome do atributo titulo, como eu indiquei,
    // o property, le title e na hora de serializar escreve title, no original, o legal é que o JsonAlias('title', 'titulo'),
    //podemos passar um array como no exemplo de nomes para o alias, para ele procurar, caso formos trabalhar com mais de uma API
}
