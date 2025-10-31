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
public class ActualizarTramiteErogacionesService extends ServiceDefinition<ConciliacionRequest, ConciliacionRequest> {

    @Inject
    @RestClient
    private BitacoraClient bitacoraClient;

    @Override
    public Message<ConciliacionRequest> execute(Message<ConciliacionRequest> request) throws BusinessException {
        try {
            Long[] entidades =  request.getPayload().getArregloIdEntidadFinanciera();

            for (Long cveEntidadFinanciera: entidades) {

                DocumentoConciliacionRequest documentoRequest = new DocumentoConciliacionRequest();
                documentoRequest.setCveEntidadFinanciera(cveEntidadFinanciera);
                documentoRequest.setPeriodo(request.getPayload().getPeriodo());

                Response respuesta = bitacoraClient.actualizaCampoErogacion(documentoRequest);
                if (respuesta != null && respuesta.getStatus() == 200)
                   continue;
            }


            return request;
        }catch (Exception e) {
            log.log(Level.SEVERE, "ERROR ActualizarTramiteErogacionesService.execute() = {0}", e);
            throw new ConciliacionException(ConciliacionException.ERROR_AL_GENERAR_TRAMITE_EROGACIONES);
        }
    }
}
