package mx.gob.imss.dpes.entidadfinancierafront.restclient;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/parametro")
@RegisterRestClient
public interface CatalogoClient {

    @GET
    @Path("/{idParametro}")
    @Produces(MediaType.APPLICATION_JSON)
    Response getParametroPorId(@PathParam("idParametro") Long id);

}
