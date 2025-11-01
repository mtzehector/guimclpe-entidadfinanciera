package mx.gob.imss.dpes.entidadfinancierafront.service;

import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.entidadfinancierafront.exception.ConciliacionException;
import mx.gob.imss.dpes.entidadfinancierafront.restclient.ConciliacionEFClient;
import mx.gob.imss.dpes.interfaces.entidadfinanciera.model.PageRequestModelTramiteErogacion;
import mx.gob.imss.dpes.interfaces.estatusConciliacion.model.EstatusConciliacionRequest;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.inject.Inject;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.util.logging.Level;

@Provider
public class EstatusPeriodoTramiteErogacionService extends ServiceDefinition<PageRequestModelTramiteErogacion, Boolean> {

    @Inject
    @RestClient
    private ConciliacionEFClient conciliacionEFClient;

    @Override
    public Message<Boolean> execute(Message<PageRequestModelTramiteErogacion> request) throws BusinessException {
        Response respuestaEstatus = null;
        try {
            respuestaEstatus = conciliacionEFClient.obtenerEstatusConciliacionPorPeriodo(
                    request.getPayload().getRequest().getModel().getPeriodo()
            );
        } catch (Exception e) {
            log.log(Level.SEVERE, "ERROR ConciliacionService.execute()", e);
            throw new ConciliacionException(ConciliacionException.ERROR_AL_INVOCAR_SERVICIO_ESTATUS_CONCILIACION);
        }

        if (respuestaEstatus == null)
            throw new ConciliacionException(ConciliacionException.ERROR_AL_INVOCAR_SERVICIO_ESTATUS_CONCILIACION);

        switch (respuestaEstatus.getStatus()) {
            case 204:
                return new Message<>(false);
            case 200:
                EstatusConciliacionRequest estatusConciliacion = respuestaEstatus.readEntity(EstatusConciliacionRequest.class);
                if (!estatusConciliacion.getActivo())
                    return new Message<>(false);
                break;
        }
        return new Message<>(true);
    }
}
