package mx.gob.imss.dpes.entidadfinancierafront.restclient;

import mx.gob.imss.dpes.interfaces.entidadfinanciera.model.ConciliacionRequest;
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
public interface ConciliacionSolicitudClient {

    @GET
    @Path("/detalleConciliacion/casosRecuperadosPorNomina/{idEntidadFinanciera}/{periodo}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response buscarCasosRecuperadosPorNomina(
            @PathParam("idEntidadFinanciera") Long cveEntidadFinanciera,
            @PathParam("periodo") String periodo
    );

    @GET
    @Path("/detalleConciliacion/casosNoRecuperadosPorNomina/{idEntidadFinanciera}/{periodo}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response buscarCasosNoRecuperadosPorNomina(
            @PathParam("idEntidadFinanciera") Long cveEntidadFinanciera,
            @PathParam("periodo") String periodo
    );

    @GET
    @Path("/detalleConciliacion/buscarCasosDefuncionPorNomina/{idEntidadFinanciera}/{periodo}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response buscarCasosDefuncionPorNomina(
            @PathParam("idEntidadFinanciera") Long cveEntidadFinanciera,
            @PathParam("periodo") String periodo
    );

    @POST
    @Path("/reportePorCuentaContable")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response obtenerDatosReporte(ConciliacionRequest request);

    @POST
    @Path("/obtenerRecuperacionFallecidos")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response obtenerRecuperacionFallecidos(ConciliacionRequest request);

}
