package mx.gob.imss.dpes.entidadfinancierafront.restclient;

import mx.gob.imss.dpes.common.model.PageRequestModel;
import mx.gob.imss.dpes.interfaces.bitacora.model.DocumentoConciliacionRequest;
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
@Path("/documentoConciliacion")
public interface BitacoraClient {

    @GET
    @Path("/{idEntidadFinanciera}/{idTipoDocumento}/{periodo}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response obtenerDocumentoConciliacion(
            @PathParam("idEntidadFinanciera") Long cveEntidadFinanciera,
            @PathParam("idTipoDocumento") Long cveTipoDocumento,
            @PathParam("periodo") String periodo
    );
    
    @GET
    @Path("/ultimasCartas/{idEntidadFinanciera}/{periodo}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response obtenerDocumentoFinal(
    		@PathParam("idEntidadFinanciera") Long idEntidadFinanciera,
    		@PathParam("periodo") String periodo);
    
    @GET
    @Path("/cartaReciboFirmadaAdministradorEF/{idEntidadFinanciera}/{periodo}/{cveTipoDocumento}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response cartaReciboFirmadaAdministradorEF(
    		@PathParam("idEntidadFinanciera") Long idEntidadFinanciera,
    		@PathParam("periodo") String periodo,
    		@PathParam("cveTipoDocumento") Long cveTipoDocumento);
    
    @POST
    @Path("/obtenerCartaReciboPorEntidadFinanciera")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response obtenerCartaReciboEntidadFinanciera(
            PageRequestModel<ConciliacionRequest> request
    );

    @POST
    @Path("/obtenerListCartaReciboEFTodas")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response obtenerListCartaReciboTodasEF(
            PageRequestModel<ConciliacionRequest> request
    );

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response guardarDocumentoConciliacion(DocumentoConciliacionRequest request);

    @POST
    @Path("/cartaRecibo")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response obtenerListCartaReciboConFirma(
            PageRequestModel<ConciliacionRequest> request
    );

    @POST
    @Path("/cartaRecibo/fechaDescarga")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response guardarFechaDescarga(DocumentoConciliacionRequest request);

    @POST
    @Path("/cartaRecibo/erogacion")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response actualizaCampoErogacion(DocumentoConciliacionRequest request);

    @GET
    @Path("/{tipoDocumento}/{periodo}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response obtenerResumenConciliacion(
            @PathParam("tipoDocumento") Long cveTipoDocumento,
            @PathParam("periodo") String periodo
    );

}
