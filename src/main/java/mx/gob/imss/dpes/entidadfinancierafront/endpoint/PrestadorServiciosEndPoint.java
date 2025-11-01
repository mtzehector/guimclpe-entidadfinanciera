package mx.gob.imss.dpes.entidadfinancierafront.endpoint;

import mx.gob.imss.dpes.common.endpoint.BaseGUIEndPoint;
import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.model.ServiceStatusEnum;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.entidadfinancierafront.assembler.PrestadorServiciosMultiPartToModelAssembler;
import mx.gob.imss.dpes.entidadfinancierafront.exception.PrestadorServiciosException;
import mx.gob.imss.dpes.entidadfinancierafront.model.CatMaximoModel;
import mx.gob.imss.dpes.entidadfinancierafront.model.PrestadorServiciosModel;
import mx.gob.imss.dpes.entidadfinancierafront.service.BitacoraPrestadorServiciosService;
import mx.gob.imss.dpes.entidadfinancierafront.service.BovedaPrestadorServiciosService;
import mx.gob.imss.dpes.entidadfinancierafront.service.CargaDocumentoPrestadorServicioService;
import mx.gob.imss.dpes.entidadfinancierafront.service.PrestadorServiciosService;
import mx.gob.imss.dpes.entidadfinancierafront.validation.ValidaPrestadorServicios;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.logging.Level;

@Path("/financiera")
@RequestScoped
public class PrestadorServiciosEndPoint extends BaseGUIEndPoint<CatMaximoModel,CatMaximoModel,CatMaximoModel>{
    @Inject
    private ValidaPrestadorServicios validaPrestadorServicios;
    @Inject
    private PrestadorServiciosMultiPartToModelAssembler prestadorServiciosAssembler;
    @Inject
    private PrestadorServiciosService prestadorServiciosService;
    @Inject
    private BovedaPrestadorServiciosService bovedaService;
    @Inject
    private CargaDocumentoPrestadorServicioService cargaDocumentoService;
    @Inject
    private BitacoraPrestadorServiciosService bitacoraService;

    @POST
    @Path("/prestadorDeServicio")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response crearActualizarPrestadorServiciosEF (MultipartFormDataInput input) {

        if(!validaPrestadorServicios.esMultipartFormDataInputValido(input))
            return toResponse(new Message(
                            null,
                            ServiceStatusEnum.EXCEPCION,
                            new PrestadorServiciosException(PrestadorServiciosException.MENSAJE_DE_SOLICITUD_INCORRECTO),
                            null
            ));

        PrestadorServiciosModel model = prestadorServiciosAssembler.assemble(input);

        if (model == null)
            return toResponse(new Message(
                    null,
                    ServiceStatusEnum.EXCEPCION,
                    new PrestadorServiciosException(PrestadorServiciosException.MENSAJE_DE_SOLICITUD_INCORRECTO),
                    null
            ));

        ServiceDefinition[] steps ={ prestadorServiciosService, bovedaService, cargaDocumentoService, bitacoraService };

        try {
            Message<PrestadorServiciosModel> response = prestadorServiciosService.executeSteps(steps, new Message<>(model));
            return toResponse(response);
        } catch (BusinessException e) {
            return toResponse(new Message(null, ServiceStatusEnum.EXCEPCION, e, null));
        } catch (Exception e) {
            log.log(Level.SEVERE, "ERROR PrestadorServiciosEndPoint.crearActualizarPrestadorServiciosEF = {0} ", e);
            return toResponse(new Message(
                    null,
                    ServiceStatusEnum.EXCEPCION,
                    new PrestadorServiciosException(PrestadorServiciosException.ERROR_DESCONOCIDO_EN_EL_SERVICIO),
                    null
            ));
        }
    }
}
