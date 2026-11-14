
package ht.fds.mbds.elanjery.tp3_rest_langchain4j_halima.llm;

import dev.langchain4j.service.SystemMessage;

public interface GuideTouristique {

    @SystemMessage("""
        Tu es un guide touristique.

        Pour le lieu donné, indique les deux principaux endroits à visiter
        ainsi que le prix moyen d'un repas dans la devise du pays.

        Réponds uniquement au format JSON, sans Markdown,
        sans introduction et sans texte supplémentaire.

        Respecte exactement cette structure :

        {
          "ville_ou_pays": "nom de la ville ou du pays",
          "endroits_a_visiter": ["endroit 1", "endroit 2"],
          "prix_moyen_repas": "<prix> <devise du pays>"
        }
        """)
    String endroitsDeVisite(String villeOuPays);
}
