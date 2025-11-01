package mx.gob.imss.dpes.entidadfinancierafront.restclient;

import mx.gob.imss.dpes.interfaces.entidadfinanciera.model.PrestadorServiciosEF;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/prestadorServicios")
@RegisterRestClient
public interface PrestadorServiciosClient {

    
    @GET
    @Path("/{idEntidadFinanciera}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response obtenerListaPrestadorServiciosEF(@PathParam("idEntidadFinanciera") Long idEntidadFinanciera);

	@POST
    @Path("/guardarLista")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response guardarLista(List<PrestadorServiciosEF> listaPrestadorServiciosEF);

}
