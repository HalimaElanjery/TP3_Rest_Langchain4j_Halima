
package ht.fds.mbds.elanjery.tp3_rest_langchain4j_halima.llm;

import java.util.List;

import dev.langchain4j.model.output.structured.Description;
import jakarta.json.bind.annotation.JsonbProperty;
import jakarta.json.bind.annotation.JsonbPropertyOrder;

@JsonbPropertyOrder({
        "ville_ou_pays",
        "endroits_a_visiter",
        "prix_moyen_repas"
})
public record InfosTouristiques(

        @Description("Nom d'une ville ou d'un pays")
        @JsonbProperty("ville_ou_pays")
        String lieu,

        @Description("Endroits à visiter dans la ville ou le pays")
        @JsonbProperty("endroits_a_visiter")
        List<String> endroitsAVisiter,

        @Description("Prix moyen d'un repas avec la devise de la ville ou du pays")
        @JsonbProperty("prix_moyen_repas")
        String prixMoyenRepas

) {
}
