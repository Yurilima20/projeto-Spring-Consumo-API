package br.com.alura.screenmatch.service;

import com.theokanning.openai.completion.CompletionRequest;
import com.theokanning.openai.service.OpenAiService;

public class ConsultaChatGPT {
    public static String obterTraducao(String texto) {
        OpenAiService service = new OpenAiService(System.getenv("OPENAI_APIKEY"));

        CompletionRequest requisicao = CompletionRequest.builder()
                .model("gpt-3.5-turbo-instruct") //modelo compativrl
                .prompt("traduza para o português o texto: " + texto) // comando no gpt
                .maxTokens(1000) // limita a resposta em quantidade de palavras essa é a ideia
                .temperature(0.7) //  a modificação entre um solicitação e outra, sem isso sempre devolve a mesma resposta.
                .build();

        var resposta = service.createCompletion(requisicao);
        return resposta.getChoices().get(0).getText();
    }
}