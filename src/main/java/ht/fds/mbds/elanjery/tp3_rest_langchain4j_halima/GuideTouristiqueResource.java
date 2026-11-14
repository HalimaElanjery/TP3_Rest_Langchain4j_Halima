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

        InfosTouristiques reponse = llmClient.envoyerRequete(villeOuPays, nb);

        return Response.ok(reponse).build();
    }
}