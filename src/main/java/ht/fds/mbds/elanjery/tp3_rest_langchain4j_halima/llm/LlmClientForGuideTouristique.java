package ht.fds.mbds.elanjery.tp3_rest_langchain4j_halima.llm;

import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.google.genai.GoogleGenAiChatModel;
import dev.langchain4j.service.AiServices;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class LlmClientForGuideTouristique {

    private final GuideTouristique guideTouristique;

    public LlmClientForGuideTouristique() {

        // Récupération de la clé API Gemini
        String cle = System.getenv("GEMINI_KEY");

        // Configuration du modèle Gemini
        ChatModel model = GoogleGenAiChatModel.builder()
                .apiKey(cle)
                .modelName("gemini-3.5-flash")
                .build();

        // Création du service GuideTouristique
        this.guideTouristique = AiServices.builder(GuideTouristique.class)
                .chatModel(model)
                .build();
    }

    // Envoi de la requête au modèle Gemini
    public InfosTouristiques envoyerRequete(String villeOuPays, int nb) {

        return guideTouristique.chat(villeOuPays, nb);
    }
}