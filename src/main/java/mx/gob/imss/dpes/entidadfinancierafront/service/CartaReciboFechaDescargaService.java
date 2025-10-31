package mx.gob.imss.dpes.entidadfinancierafront.service;

import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.entidadfinancierafront.exception.ConciliacionException;
import mx.gob.imss.dpes.entidadfinancierafront.restclient.BitacoraClient;
import mx.gob.imss.dpes.interfaces.bitacora.model.DocumentoConciliacionRequest;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.inject.Inject;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.util.logging.Level;

@Provider
public class CartaReciboFechaDescargaService extends ServiceDefinition<DocumentoConciliacionRequest, DocumentoConciliacionRequest> {

    @Inject
    @RestClient
    private BitacoraClient bitacoraClient;

    @Override
    public Message<DocumentoConciliacionRequest> execute(Message<DocumentoConciliacionRequest> request) throws BusinessException {
        try {
            DocumentoConciliacionRequest cartaReciboConFechaDescarga = null;
            Response respuesta = bitacoraClient.guardarFechaDescarga(request.getPayload());
            if (respuesta != null && respuesta.getStatus() == 200)
                cartaReciboConFechaDescarga = respuesta.readEntity(DocumentoConciliacionRequest.class);

            return new Message<>(cartaReciboConFechaDescarga);
        }catch (Exception e){
            log.log(Level.SEVERE, "ERROR CartaReciboFechaDescargaService.execute()", e);
            throw new ConciliacionException(ConciliacionException.ERROR_AL_INVOCAR_SERVICIO_BITACORA_CARTA_RECIBO);
        }
    }
}
