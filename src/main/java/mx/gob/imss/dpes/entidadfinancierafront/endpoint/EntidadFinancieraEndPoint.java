/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.entidadfinancierafront.endpoint;

import java.util.logging.Level;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import mx.gob.imss.dpes.common.endpoint.BaseGUIEndPoint;
import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.model.ServiceStatusEnum;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.entidadfinancierafront.assembler.RequestEntidadFinancieraAssembler;
import mx.gob.imss.dpes.entidadfinancierafront.exception.EntidadFinancieraOTException;
import mx.gob.imss.dpes.entidadfinancierafront.model.EntidadFinancieraRequest;
import mx.gob.imss.dpes.entidadfinancierafront.model.RequestEntidadFinanciera;
import mx.gob.imss.dpes.entidadfinancierafront.service.*;
import mx.gob.imss.dpes.interfaces.documento.model.DocumentoArchivo;
import mx.gob.imss.dpes.interfaces.entidadfinanciera.ot2.model.EntidadFinanciera;
import org.eclipse.microprofile.openapi.annotations.Operation;

/**
 *
 * @author eduardo.loyo
 */
@Path("/financiera")
@RequestScoped
public class EntidadFinancieraEndPoint extends BaseGUIEndPoint<EntidadFinancieraRequest, EntidadFinancieraRequest, EntidadFinancieraRequest> {

    @Inject
    private EntidadFinancieraService entidadFinanciera;
    @Inject
    private CondicionOfertaPersistanceService condicionOferta;
    @Inject
    private CorreoService enviarToken;
    @Inject
    private RequestEntidadFinancieraAssembler assembler;
    @Inject
    private EntidadFinancieraAdminService entidadFinancieraAdmin;
    @Inject
    private RegistroPatronalesService registrosPatronalesService;
    @Inject
    private EntidadFinancieraOfertasService entidadFinancieraOfertasService;
    @Inject
    private EntidadFinancieraBitacoraCatEFService entidadFinancieraBitacoraCatEFService;
    @Inject
    private EntidadFinancieraOTService entidadFinancieraOTService;
    @Inject
    private PrestadorServiciosEFOTService prestadorServiciosEFOTService;
    @Inject
    private DocumentoEFOTService documentoEFOTService;
    @Inject
    private EntidadFinancieraBitacoraEstatusEFService entidadFinancieraBitacoraEstatusEFService;
    @Inject
    private ObtenerAdministradorRFCService adminRFCService;
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Obtener condición de oferta con los plazos",
            description = "Obtener condición de oferta con los plazos")
    public Response create(RequestEntidadFinanciera inner) throws BusinessException {

        EntidadFinancieraRequest request = assembler.assemble(inner);

//        if(request.getMclcCondicionOfertaCollection() != null){
//            Iterator<CondicionOfertaModel> iterator = request.getMclcCondicionOfertaCollection().iterator();
//            while(iterator.hasNext()){
//                iterator.next().setAltaRegistro(new Date());
//            }
//        }

        log.log(Level.INFO,">>> entidadFinancieraFront EntidadFinancieraEndPoint.create inicio request:{0}", request);

        ServiceDefinition[] steps = {
                entidadFinancieraOfertasService,
                entidadFinanciera,
                condicionOferta,
                registrosPatronalesService,
                enviarToken,
                entidadFinancieraBitacoraCatEFService,
                entidadFinancieraBitacoraEstatusEFService
        };
        Message<EntidadFinancieraRequest> response = entidadFinanciera.executeSteps(
                steps,new Message<>(request)
        );

        log.log(Level.INFO,">>> entidadFinancieraFront EntidadFinancieraEndPoint.create respuesta response:{0}", response.getPayload());

        if (!Message.isException(response)) {
            return toResponse(response);
        }
        return toResponse(response);
    }
    
    @POST
    @Path("/obtenInfoEF")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response obtnerInfoEF(RequestEntidadFinanciera request)throws BusinessException {
        log.log(Level.INFO,">>>entidadFinancieraFront obtnerInfoEF request:{0}", request);
        ServiceDefinition[] steps = {entidadFinancieraAdmin};
        Message<RequestEntidadFinanciera> response = entidadFinancieraAdmin.executeSteps(steps,new Message<>(request));
        log.log(Level.INFO,">>> entidadFinancieraFront obtnerInfoEF response:{0}", response.getPayload());
        if (!Message.isException(response)) {
            return toResponse(response);
        }
        return toResponse(response);
    }
    
    @GET
    @Path("/logo/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getLogo(@PathParam("id") Long id)throws BusinessException {
        log.log(Level.INFO,">>>entidadFinancieraFront getLogo id:{0}", id);
        DocumentoArchivo response = entidadFinanciera.getLogo(id);
        log.log(Level.INFO,">>> entidadFinancieraFront getLogo response:{0}", response);
        
        return toResponse(new Message(response));
    }

    @GET
    @Path("/{id}/ot2")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findEntidadFinancieraById(@PathParam("id") Long id) {

        try {
            ServiceDefinition[] steps = {
                    entidadFinancieraOTService,
                    prestadorServiciosEFOTService,
                    documentoEFOTService,
                    adminRFCService
            };

            EntidadFinanciera entidadFinanciera = new EntidadFinanciera();
            entidadFinanciera.setId(id);

            Message<EntidadFinanciera> response = entidadFinancieraOTService.executeSteps(
                    steps, new Message<>(entidadFinanciera)
            );

            return toResponse(response);
        } catch (BusinessException e) {
            return toResponse(new Message(null, ServiceStatusEnum.EXCEPCION, e, null));
        } catch (Exception e) {
            log.log(Level.SEVERE, "ERROR EntidadFinancieraEndPoint.findEntidadFinancieraById ", e);
            return toResponse(new Message(null, ServiceStatusEnum.EXCEPCION,
                    new EntidadFinancieraOTException(EntidadFinancieraOTException.ERROR_DESCONOCIDO_EN_EL_SERVICIO),
                    null));
        }

    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findById(@PathParam("id") Long id){
        try{
            ServiceDefinition[] steps = {entidadFinancieraOTService};

            EntidadFinanciera entidadFinanciera = new EntidadFinanciera();
            entidadFinanciera.setId(id);

            Message<EntidadFinanciera> response = entidadFinancieraOTService.executeSteps(steps, new Message<>(entidadFinanciera));
            return toResponse(response);
        }catch (BusinessException e) {
            return toResponse(new Message(null, ServiceStatusEnum.EXCEPCION, e, null));
        } catch (Exception e) {
            log.log(Level.SEVERE, "ERROR EntidadFinancieraEndPoint.findById ", e);
        }
        return toResponse(new Message(
                null,
                ServiceStatusEnum.EXCEPCION,
                new EntidadFinancieraOTException(EntidadFinancieraOTException.ERROR_DESCONOCIDO_EN_EL_SERVICIO),
                null
        ));
    }


}
