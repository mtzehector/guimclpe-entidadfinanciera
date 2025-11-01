package mx.gob.imss.dpes.entidadfinancierafront.service;

import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.entidadfinancierafront.exception.ConciliacionException;
import mx.gob.imss.dpes.entidadfinancierafront.restclient.BitacoraClient;
import mx.gob.imss.dpes.interfaces.bitacora.model.DocumentoConciliacionRequest;
import mx.gob.imss.dpes.interfaces.entidadfinanciera.model.ConciliacionRequest;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.inject.Inject;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.util.logging.Level;

@Provider
public class ConciliacionDocumentoConciliacionService extends ServiceDefinition<ConciliacionRequest, ConciliacionRequest> {

    @Inject
    @RestClient
    private BitacoraClient bitacoraClient;

    @Override
    public Message<ConciliacionRequest> execute(Message<ConciliacionRequest> request) throws BusinessException {

        DocumentoConciliacionRequest documentoConciliacion = new DocumentoConciliacionRequest();
        documentoConciliacion.setCveDocumento(request.getPayload().getDocumento().getId());
        documentoConciliacion.setCveEntidadFinanciera(
                request.getPayload().getCveEntidadFinanciera() == null ? 0 : request.getPayload().getCveEntidadFinanciera()
        );
        documentoConciliacion.setCurp(request.getPayload().getCurp());
        documentoConciliacion.setPeriodo(request.getPayload().getPeriodo());
        documentoConciliacion.setErogacion(request.getPayload().getErogacion() == null? false : request.getPayload().getErogacion());
        try {
            Response respuestaBitacora = bitacoraClient.guardarDocumentoConciliacion(documentoConciliacion);
            if (respuestaBitacora != null && respuestaBitacora.getStatus() == 200){
                documentoConciliacion = respuestaBitacora.readEntity(DocumentoConciliacionRequest.class);
                return request;
            }
        }catch (Exception e){
            log.log(Level.SEVERE, "ERROR ConciliacionDocumentoConciliacionService.execute()", e);
        }
        throw new ConciliacionException(ConciliacionException.ERROR_AL_INVOCAR_SERVICIO_DOCUMENTO_CONCILIACION);
    }
}
