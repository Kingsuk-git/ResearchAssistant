package com.research.researchAssistant.Service;

import com.research.researchAssistant.Entity.ResearchRequest;
import org.springframework.beans.factory.annotation.Value;

public class ResearchService {

    @Value("${gemini.api.url}")
    private String geminiApiUrl;

    @Value("${gemini.api.key}")
    private String geminiApiKey;

    public String processContent(ResearchRequest request) {

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
