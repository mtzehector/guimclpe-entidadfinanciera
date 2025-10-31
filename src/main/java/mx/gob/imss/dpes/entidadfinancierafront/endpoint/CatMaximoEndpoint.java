package mx.gob.imss.dpes.entidadfinancierafront.endpoint;

import java.util.logging.Level;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import mx.gob.imss.dpes.common.endpoint.BaseGUIEndPoint;
import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.model.ServiceStatusEnum;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.entidadfinancierafront.assembler.AssamblerCatMaximoModelFromMultipartFormDataInput;
import mx.gob.imss.dpes.entidadfinancierafront.exception.CatMaximoException;
import mx.gob.imss.dpes.entidadfinancierafront.model.CatMaximoModel;
import mx.gob.imss.dpes.entidadfinancierafront.service.*;
import mx.gob.imss.dpes.entidadfinancierafront.validation.ValidacionDeSolicitudCatMaximo;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

@Path("/catMaximo")
@RequestScoped
public class CatMaximoEndpoint extends BaseGUIEndPoint<CatMaximoModel,CatMaximoModel,CatMaximoModel> {

    @Inject
    private BovedaCatMaximoService bovedaCatMaximoService;

    @Inject
    private CargaDocumentoCatMaximoService cargaDocumentoCatMaximoService;

    @Inject
    private CatMaximoService catMaximoService;

    @Inject
    private BitacoraCatImssService bitacoraCatImssService;

    @Inject
    private CatMaximoBitacoraCatEFService catMaximoBitacoraCatEFService;

    @Inject
    private CorreoCatMaximoService correoCatMaximoService;

    @Inject
    private ValidacionDeSolicitudCatMaximo validacionDeSolicitudCatMaximo;

    @Inject
    private AssamblerCatMaximoModelFromMultipartFormDataInput assamblerCatMaximoModelFromMultipartFormDataInput;

    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response update (MultipartFormDataInput input) {
        log.log(Level.INFO, "***** Inicia CatMaximoEndpoint *****");

        CatMaximoModel request = null;

        if(
                !validacionDeSolicitudCatMaximo.esMultipartFormDataInputValidoParaActualizacionCATMaximo(input)
                || (request = assamblerCatMaximoModelFromMultipartFormDataInput.assemble(input)) == null
        )
            return toResponse(new Message(null, ServiceStatusEnum.EXCEPCION,
                    new CatMaximoException(CatMaximoException.MENSAJE_DE_SOLICITUD_INCORRECTO),
                    null));

        if(request == null)
            return toResponse(new Message(null, ServiceStatusEnum.EXCEPCION,
                    new CatMaximoException(CatMaximoException.MENSAJE_DE_SOLICITUD_INCORRECTO),
                    null));
        
        ServiceDefinition[] steps ={ bovedaCatMaximoService, cargaDocumentoCatMaximoService, catMaximoService,
                bitacoraCatImssService, catMaximoBitacoraCatEFService, correoCatMaximoService };

        try {
            Message<CatMaximoModel> response = catMaximoService.executeSteps(steps, new Message<>(request));
            log.log(Level.INFO, "***** Finaliza CatMaximoEndpoint *****");
            return toResponse(response);
        } catch (BusinessException e) {
            return toResponse(new Message(null, ServiceStatusEnum.EXCEPCION, e, null));
        } catch (Exception e) {
            log.log(Level.SEVERE, "ERROR CatMaximoEndpoint.update ", e);
            return toResponse(new Message(null, ServiceStatusEnum.EXCEPCION,
                    new CatMaximoException(CatMaximoException.ERROR_DESCONOCIDO_EN_EL_SERVICIO),
                    null));
        }


    }
    
    @GET
    @Path("/actual")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response read() throws BusinessException{
        
        CatMaximoModel response = catMaximoService.read();
        
        return toResponse(new Message<>(response));
    }
}
