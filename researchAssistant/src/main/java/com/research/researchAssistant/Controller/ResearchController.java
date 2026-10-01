package com.research.researchAssistant.Controller;

import com.research.researchAssistant.Entity.ResearchRequest;
import com.research.researchAssistant.Service.ResearchService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/research")
@CrossOrigin(origins = "*")
@AllArgsConstructor
public class ResearchController {
    private final ResearchService researchService;

    ResponseEntity<String> processContent(@RequestBody ResearchRequest request){
        String result = researchService.processContent(request);
        return ResponseEntity.ok(result);
    }
}
