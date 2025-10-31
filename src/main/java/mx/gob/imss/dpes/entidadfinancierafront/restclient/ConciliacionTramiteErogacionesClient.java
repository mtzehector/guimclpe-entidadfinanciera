package mx.gob.imss.dpes.entidadfinancierafront.restclient;

import mx.gob.imss.dpes.interfaces.entidadfinanciera.model.ConciliacionRequest;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@RegisterRestClient
@Path("/conciliacion")
public interface ConciliacionTramiteErogacionesClient {

    @POST
    @Path("/solicitudTransferencia")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response generaReporteSolicitudTransferencia(ConciliacionRequest request);

    @POST
    @Path("/reportePorCuentaContable")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response generarReporteEFPorCuentaContable(ConciliacionRequest request);

    @POST
    @Path("/reportePorDelegacion")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response generaReporteRetencionPorDelegacion(ConciliacionRequest request);

}
