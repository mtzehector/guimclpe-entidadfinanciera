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
public class ConciliacionResumenBitacoraService extends ServiceDefinition<ConciliacionRequest, ConciliacionRequest> {

    @Inject
    @RestClient
    private BitacoraClient bitacoraClient;

    @Override
    public Message<ConciliacionRequest> execute(Message<ConciliacionRequest> request) throws BusinessException {
        try {
            Response respuesta = bitacoraClient.obtenerResumenConciliacion(
                    request.getPayload().getCveTipoDocumento(),
                    request.getPayload().getPeriodo()
            );
            if (respuesta != null && respuesta.getStatus() == 200){
                DocumentoConciliacionRequest documentoConciliacion = respuesta.readEntity(DocumentoConciliacionRequest.class);
                request.getPayload().setDocumento(documentoConciliacion.getDocumento());
            }
            return request;
        }catch (Exception e){
            log.log(Level.SEVERE, "ERROR ConciliacionDocumentoService.execute()", e);
            throw new ConciliacionException(ConciliacionException.ERROR_AL_INVOCAR_SERVICIO_BITACORA_CONCILIACION);
        }
    }
}
