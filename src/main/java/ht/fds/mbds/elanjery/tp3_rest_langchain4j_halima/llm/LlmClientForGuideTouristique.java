
package ht.fds.mbds.elanjery.tp3_rest_langchain4j_halima.llm;

import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.google.genai.GoogleGenAiChatModel;
import dev.langchain4j.service.AiServices;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class LlmClientForGuideTouristique {

    private final GuideTouristique guideTouristique;

    public LlmClientForGuideTouristique() {

        String cle = System.getenv("GEMINI_KEY");

        ChatModel model = GoogleGenAiChatModel.builder()
                .apiKey(cle)
                .modelName("gemini-3.5-flash")
                .build();

        this.guideTouristique = AiServices.builder(GuideTouristique.class)
                .chatModel(model)
                .build();
    }

    public String envoyerRequete(String villeOuPays) {
        return guideTouristique.endroitsDeVisite(villeOuPays);
    }
}
