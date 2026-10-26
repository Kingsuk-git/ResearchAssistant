package com.research.researchAssistant.Service;

import com.research.researchAssistant.Entity.ResearchRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

public class ResearchService {

    @Value("${gemini.api.url}")
    private String geminiApiUrl;

    @Value("${gemini.api.key}")
    private String geminiApiKey;

    private final WebClient webClient;

    public ResearchService(WebClient.Builder webClientBuilder) {
        this.webClient = WebClient.builder().build();
    }


    public String processContent(ResearchRequest request) {
        String prompt = buildPrompt(request);

        Map<String,Object> requestBody = Map.of(
                "content",new Object[]{
                        Map.of("parts",new Object[]{
                                Map.of("text",prompt)
                        })
                }
        );

        String response = webClient.post()
                .uri(geminiApiUrl + geminiApiKey)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        return extractTextFromResponse(response);
    }

    private String extractTextFromResponse(String response) {
        try{

        } catch (Exception e) {
            return "Error Parsing" + e.getMessage();
        }
    }

    private String buildPrompt(ResearchRequest request){
        StringBuilder prompt = new StringBuilder();
        switch (request.getOperation()){
            case "summarize":
                prompt.append("Provide a brief and precise summary of the following text , highlight important points . Separate different topics using headings");
                break;
            case "suggest" :
                prompt.append("Based on the given content suggest related topics ");
                break;
                default:
                throw new IllegalArgumentException("Unknown Operation : " + request.getOperation());
        }
        prompt.append(request.getContent());
        return prompt.toString();
    }
}
