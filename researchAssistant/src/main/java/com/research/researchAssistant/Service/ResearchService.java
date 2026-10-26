package com.research.researchAssistant.Service;

import com.research.researchAssistant.Entity.ResearchRequest;
import com.research.researchAssistant.Response.SummaryResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.reactive.function.client.WebClient;
import tools.jackson.databind.ObjectMapper;

import java.util.Map;

public class ResearchService {

    @Value("${gemini.api.url}")
    private String geminiApiUrl;

    @Value("${gemini.api.key}")
    private String geminiApiKey;

    private final WebClient webClient;

    private final ObjectMapper objectMapper;

    public ResearchService(WebClient.Builder webClientBuilder, ObjectMapper objectMapper) {
        this.webClient = WebClient.builder().build();
        this.objectMapper = objectMapper;
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
            SummaryResponse summaryResponse = objectMapper.readValue(response, SummaryResponse.class);
            if(summaryResponse.getCandidates() != null && !summaryResponse.getCandidates().isEmpty()){
                SummaryResponse.Candidate firstCandidate = (SummaryResponse.Candidate) summaryResponse.getCandidates();
                if(firstCandidate.getContent() !=null &&
                firstCandidate.getContent().getParts() !=null &&
                !firstCandidate.getContent().getParts().isEmpty()){
                    return firstCandidate.getContent().getParts().get(0).getText();
                }
            }
            return "No response";
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
