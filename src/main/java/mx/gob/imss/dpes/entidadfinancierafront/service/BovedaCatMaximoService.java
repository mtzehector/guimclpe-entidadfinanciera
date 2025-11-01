package mx.gob.imss.dpes.entidadfinancierafront.service;

import java.util.logging.Level;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.model.ServiceStatusEnum;
import mx.gob.imss.dpes.common.service.ServiceDefinition;

import mx.gob.imss.dpes.entidadfinancierafront.exception.CatMaximoException;
import mx.gob.imss.dpes.entidadfinancierafront.model.CatMaximoModel;
import mx.gob.imss.dpes.entidadfinancierafront.model.CreateDocumentReq;
import mx.gob.imss.dpes.entidadfinancierafront.model.RespuestaBoveda;
import mx.gob.imss.dpes.entidadfinancierafront.restclient.BovedaClient;
import org.eclipse.microprofile.rest.client.inject.RestClient;

/**
 *
 * @author eduardo.loyo
 */
@Provider
public class BovedaCatMaximoService extends ServiceDefinition<CatMaximoModel, CatMaximoModel> {

    @Inject
    @RestClient
    private BovedaClient bovedaClient;

    @Override
    public Message<CatMaximoModel> execute(Message<CatMaximoModel> request) throws BusinessException {
        log.log(Level.INFO, "***** Inicia BovedaCatMaximoService *****");

        CreateDocumentReq bovedaIn = new CreateDocumentReq();
        bovedaIn.getDocumento().setArchivo(request.getPayload().getArchivo());
        bovedaIn.getDocumento().setNombreArchivo(request.getPayload().getNombreArchivo());
        bovedaIn.getTramite().setFolioTramite(String.valueOf(request.getPayload().getSolicitud()));
        bovedaIn.getUsuario().setIdUsr("1");
        bovedaIn.getDocumento().setExtencion(".pdf");
        bovedaIn.setSesion(request.getPayload().getSesion());

        try {
            Response responseBovedaClient = bovedaClient.create(bovedaIn);
            if (responseBovedaClient != null && responseBovedaClient.getStatus() == 200) {
                request.getPayload().setRespuestaBoveda(responseBovedaClient.readEntity(CreateDocumentReq.class).
                        getRespuestaBoveda());
                log.log(Level.INFO, "***** Finaliza BovedaCatMaximoService *****");
                return new Message<>(request.getPayload());
            }
        } catch (Exception e) {
            log.log(Level.SEVERE, ">>>>ERROR!!! BovedaCatMaximoService = {0}", e);
        }
        return response(null, ServiceStatusEnum.EXCEPCION,
                new CatMaximoException(CatMaximoException.ERROR_AL_INVOCAR_SERVICIO_DE_BOVEDA), null);
    }

}
