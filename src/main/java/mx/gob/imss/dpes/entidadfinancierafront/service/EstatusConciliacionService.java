package mx.gob.imss.dpes.entidadfinancierafront.service;

import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.model.ServiceStatusEnum;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.entidadfinancierafront.exception.ConciliacionException;
import mx.gob.imss.dpes.entidadfinancierafront.restclient.ConciliacionEFClient;
import mx.gob.imss.dpes.interfaces.estatusConciliacion.model.EstatusConciliacionRequest;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.inject.Inject;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.util.logging.Level;

@Provider
public class EstatusConciliacionService extends ServiceDefinition<EstatusConciliacionRequest, EstatusConciliacionRequest> {

    @Inject
    @RestClient
    private ConciliacionEFClient estatusClient;

    @Override
    public Message<EstatusConciliacionRequest> execute(Message<EstatusConciliacionRequest> request) throws BusinessException {
        try {
            Response respuesta = estatusClient.obtenerEstatusConciliacionPorPeriodo(request.getPayload().getPeriodo());
            if (respuesta != null && respuesta.getStatus() == 200){
                EstatusConciliacionRequest estatusConciliacion = respuesta.readEntity(EstatusConciliacionRequest.class);
                request.getPayload().setId(estatusConciliacion.getId());
                request.getPayload().setActivo(estatusConciliacion.getActivo());
                return request;
            }
        }catch (Exception e){
            log.log(Level.SEVERE, "ERROR EstatusConciliacionService.execute() = {0}", e);
            return response(
                    null,
                    ServiceStatusEnum.EXCEPCION,
                    new ConciliacionException(ConciliacionException.ERROR_AL_INVOCAR_SERVICIO_ESTATUS_CONCILIACION),
                    null
            );
        }
        throw new ConciliacionException(ConciliacionException.NO_SE_ENCONTRO_INFORMACION);
    }

}
