package mx.gob.imss.dpes.entidadfinancierafront.restclient;

import mx.gob.imss.dpes.interfaces.bitacora.model.DocumentoEFPS;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/documentoEFPS")
@RegisterRestClient
public interface DocumentoEFPSClient {

    @GET
    @Path("/{idEntidadFinanciera}/{idTipoPrestadorDeServicios}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response obtenerListaDeDocumentos(@PathParam("idEntidadFinanciera") Long idEntidadFinanciera,
        @PathParam("idTipoPrestadorDeServicios") Integer idTipoPrestadorDeServicios);

	@POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/documentos")
    public Response crearDocumentos(List<DocumentoEFPS> listaDocumentos);
}