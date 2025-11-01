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
import mx.gob.imss.dpes.entidadfinancierafront.restclient.DocumentoClient;
import mx.gob.imss.dpes.interfaces.documento.model.Documento;
import org.eclipse.microprofile.rest.client.inject.RestClient;

@Provider
public class CargaDocumentoCatMaximoService extends ServiceDefinition<CatMaximoModel, CatMaximoModel> {

    @Inject
    @RestClient
    private DocumentoClient documentoClient;

    @Override
    public Message<CatMaximoModel> execute(Message<CatMaximoModel> request) throws BusinessException {
        log.log(Level.INFO, "***** Inicia CargaDocumentoCatMaximoService *****");

        Documento documentoIn = new Documento();
        documentoIn.setCveSolicitud(Long.parseLong(request.getPayload().getSolicitud()));
        documentoIn.setTipoDocumento(request.getPayload().getTipoDocumento());
        documentoIn.setRefDocBoveda(request.getPayload().getRespuestaBoveda().getIdDocumento());
        documentoIn.setDescTipoDocumento(request.getPayload().getRespuestaBoveda().getDescripcion());

        try {
            Response responseDocumentoClient = documentoClient.create(documentoIn);
            if (responseDocumentoClient != null && responseDocumentoClient.getStatus() == 200) {
                request.getPayload().setDocumentoResponse(responseDocumentoClient.readEntity(Documento.class));
                log.log(Level.INFO, "***** Finaliza CargaDocumentoCatMaximoService *****");
                return request;
            }
        } catch (Exception e) {
            log.log(Level.SEVERE, ">>>>ERROR!!! CargaDocumentoCatMaximoService = {0}", e);
        }

        return response(null, ServiceStatusEnum.EXCEPCION,
                new CatMaximoException(CatMaximoException.ERROR_AL_INVOCAR_SERVICIO_DE_DOCUMENTO), null);
    }

}
