
package ht.fds.mbds.elanjery.tp3_rest_langchain4j_halima;

import ht.fds.mbds.elanjery.tp3_rest_langchain4j_halima.llm.InfosTouristiques;
import ht.fds.mbds.elanjery.tp3_rest_langchain4j_halima.llm.LlmClientForGuideTouristique;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/guide")
public class GuideTouristiqueResource {

    @Inject
    private LlmClientForGuideTouristique llmClient;

    @GET
    @Path("lieu/{ville_ou_pays}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response endroitsDeVisite(
            @PathParam("ville_ou_pays") String villeOuPays,
            @QueryParam("nb") @DefaultValue("2") int nb) {

        InfosTouristiques reponse =
                llmClient.envoyerRequete(villeOuPays, nb);

        Response.ResponseBuilder responseBuilder = Response.ok(reponse);

        // Autoriser la communication avec la page HTML locale
        responseBuilder.header("Access-Control-Allow-Origin", "*");

        // Éviter la mise en cache des réponses
        responseBuilder.header(
                "Cache-Control",
                "no-store, no-cache, must-revalidate, max-age=0"
        );
        responseBuilder.header("Pragma", "no-cache");
        responseBuilder.header("Expires", "0");

        return responseBuilder.build();
    }
}
