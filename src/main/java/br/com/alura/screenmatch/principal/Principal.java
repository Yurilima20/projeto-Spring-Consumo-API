package br.com.alura.screenmatch.principal;

import br.com.alura.screenmatch.model.DadosEpisodio;
import br.com.alura.screenmatch.model.DadosSerie;
import br.com.alura.screenmatch.model.Episodio;
import br.com.alura.screenmatch.service.ConsumoAPi;
import br.com.alura.screenmatch.service.ConverteDados;
import br.com.alura.screenmatch.model.DadosTemporada;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Principal {

    private ConsumoAPi consumoAPi = new ConsumoAPi();
    private ConverteDados conversor = new ConverteDados();
    private final String ENDERECO = "https://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=b4171602";
    private Scanner leitura = new Scanner(System.in);

    public void exibeMenu(){
        System.out.println("Digite o nome da série para busca");
        var nomeSerie = leitura.nextLine();
        var json = consumoAPi.obterDados( ENDERECO + nomeSerie.replace(" ", "+") + API_KEY);
        DadosSerie dados = conversor.obterDados(json, DadosSerie.class);
        System.out.println(dados);

        List<DadosTemporada> temporadaList = new ArrayList<>();
        for (int i = 1; i <= dados.totalTemporadas(); i++) {
            json = consumoAPi.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + "&season=" + i + API_KEY);
            DadosTemporada dadosTemporada = conversor.obterDados(json, DadosTemporada.class);
            temporadaList.add(dadosTemporada);
        }
        temporadaList.forEach(System.out::println);
        // equivalente a temporadaList.forEach(t -> Sysout.ou.println(t); quando se tem se um único parametro

        // aqui vem um conceito bem inteiressante, que já tem desde o java 8, que são os lambdas, veja como essa estrutra abaixo,
        //vai ficar com lambdas
//        for(int i = 0; i < dados.totalTemporadas(); i++){
//            List<DadosEpisodio> episodiosTemporada = temporadaList.get(i).episodios();
//            for (int j = 0; j < episodiosTemporada.size(); j++){
//                System.out.println(episodiosTemporada.get(j).titulo());
//            }
//        }
        temporadaList.forEach(t -> t.episodios().forEach(j -> System.out.println(j.titulo())));


        //aproveitando o assunto com lambdas, foi falado de stremas também, elas básicamente são operações intermediárias que geram
        // novos fluxos (stream novo) e tudo que faz algo com o stream são as operações finais (funções que finalizam), tipo os foreach
        //List<String> nomes = Arrays.asList("Jacque", "Iasmin", "Paulo", "Rodrigo", "Nico");
        //nomes.stream().sorted().limit(3).filter(n -> n.startsWith("N")).map(n -> n.toUpperCase()).forEach(System.out::println);

        //usando stream
        List<DadosEpisodio> dadosEpisodios = temporadaList.stream()
                .flatMap(t -> t.episodios().stream())
                .collect(Collectors.toList());


        System.out.println("\n Top 5 episodios: ");
        dadosEpisodios.stream()
                .filter(e -> !e.avaliacao().equalsIgnoreCase("N/A"))
                .sorted(Comparator.comparing(DadosEpisodio::avaliacao).reversed())
                .limit(5)
                .forEach(System.out::println);


        //obs: aqui estamos pegando um DadosEpisodio, que não tem o numero a temporada e estamos associando uma temporada a ele.
        // mas para isso criou uma nova classe Episodio, na qual fizemos o construtor personalizado para assosciar valores da classe DadosEpisodio
        // a classe Episodio, porém mesmo sendo o mesmo dado, algumas trouxe o dato em outro formato e para isso fizemos conversões e colocamos
        // try para possíveis erros, logo fizemos um monte de coisa resumida nesse stream(), o racíocinio é pesado, mas ele econimaza
        //codigo demais, o map estamos pegando o strem e jogando a lista em um map para transformar os dadosEpisodios em novos episodios,
        //com a info de temporada, por isso essa análise.
        List<Episodio> episodios = temporadaList.stream()
                .flatMap(t -> t.episodios().stream()
                        .map(d -> new Episodio(t.numero(), d)))
                .collect(Collectors.toList());

        episodios.forEach(System.out::println);

        //vamos applicar filtros por data agora
        System.out.println("A partir que ano você deseja ver os episódios ?");
        var ano = leitura.nextInt();
        leitura.nextLine();

        LocalDate dataBusca = LocalDate.of(ano, 1, 1);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        episodios.stream()
                .filter( e   -> e.getDataLancamento() != null && e.getDataLancamento().isAfter(dataBusca))
                .forEach(e -> System.out.println(
                        "Temporada: " + e.getTemporada() +
                                "Episódio: " + e.getTitulo() +
                                "Data Lançamento: " + e.getDataLancamento().format(formatter)
                ));


    }
}
