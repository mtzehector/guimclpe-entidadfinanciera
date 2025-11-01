package mx.gob.imss.dpes.entidadfinancierafront.restclient;

import mx.gob.imss.dpes.common.model.PageRequestModel;
import mx.gob.imss.dpes.interfaces.entidadfinanciera.model.ConciliacionRequest;
import mx.gob.imss.dpes.interfaces.estatusConciliacion.model.EstatusConciliacionRequest;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@RegisterRestClient
@Path("/conciliacion")
public interface ConciliacionEFClient {

    @GET
    @Path("/estatusConciliacion/{periodo}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response obtenerEstatusConciliacionPorPeriodo(@PathParam("periodo") String periodo);

    @POST
    @Path("/estatusConciliacion")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response guardarEstatusConciliacion(EstatusConciliacionRequest request);

    @POST
    @Path("/resumenConciliacion")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response obtenerDatosReporteResumenConciliacion(ConciliacionRequest request);

    @POST
    @Path("/solicitudTransferencia")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response obtenerDatosReporte(ConciliacionRequest request);

    @POST
    @Path("/reportePorDelegacion")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response obtenerDatosRetencionPorDelegacion(ConciliacionRequest request);

    @POST
    @Path("/obtenerImporteFallecidos")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response obtenerImporteFallecidos(ConciliacionRequest request);

    @POST
    @Path("/tramiteErogacion")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response obtenerListTramiteErogaciones(
            PageRequestModel<ConciliacionRequest> request
    );

}
