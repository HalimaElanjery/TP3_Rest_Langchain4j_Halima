
package ht.fds.mbds.elanjery.tp3_rest_langchain4j_halima;

import ht.fds.mbds.elanjery.tp3_rest_langchain4j_halima.llm.LlmClientForGuideTouristique;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/guide")
public class GuideTouristiqueResource {

    @Inject
    private LlmClientForGuideTouristique llmClient;

    @GET
    @Path("lieu/{ville_ou_pays}")
    @Produces(MediaType.APPLICATION_JSON)
    public String endroitsDeVisite(
            @PathParam("ville_ou_pays") String villeOuPays) {

        return llmClient.envoyerRequete(villeOuPays);
    }
}
