package mx.gob.imss.dpes.entidadfinancierafront.restclient;

import mx.gob.imss.dpes.interfaces.bitacora.model.BitacoraCatEF;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/bitacoraCatEF")
@RegisterRestClient
public interface BitacoraCatEFClient {
     @POST
     @Consumes(MediaType.APPLICATION_JSON)
     @Produces(MediaType.APPLICATION_JSON)
     public Response create(BitacoraCatEF model);

     @POST
     @Path("/bitacoras")
     @Consumes(MediaType.APPLICATION_JSON)
     @Produces(MediaType.APPLICATION_JSON)
     public Response createBitacoras(List<BitacoraCatEF> model);
}
