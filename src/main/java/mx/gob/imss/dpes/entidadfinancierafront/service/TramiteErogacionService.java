package mx.gob.imss.dpes.entidadfinancierafront.service;

import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.model.PageModel;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.entidadfinancierafront.exception.ConciliacionException;
import mx.gob.imss.dpes.entidadfinancierafront.restclient.ConciliacionEFClient;
import mx.gob.imss.dpes.interfaces.entidadfinanciera.model.PageRequestModelTramiteErogacion;
import mx.gob.imss.dpes.interfaces.entidadfinanciera.model.TramiteErogacionConciliacion;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.inject.Inject;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.util.logging.Level;

@Provider
public class TramiteErogacionService extends ServiceDefinition<PageRequestModelTramiteErogacion, PageRequestModelTramiteErogacion> {

    @Inject
    @RestClient
    private ConciliacionEFClient efClient;

    @Override
    public Message<PageRequestModelTramiteErogacion> execute(Message<PageRequestModelTramiteErogacion> request) throws BusinessException {
        try {
            Response respuesta = efClient.obtenerListTramiteErogaciones(request.getPayload().getRequest());
            if (respuesta != null && respuesta.getStatus() == 200)
                return response(respuesta, request);

            return request;
        }catch (Exception e){
            log.log(Level.SEVERE, "ERROR TramiteErogacionService.execute()", e);
            throw new ConciliacionException(ConciliacionException.ERROR_AL_INVOCAR_SERVICIO_BITACORA_CARTA_RECIBO);
        }
    }

    @Override
    protected Message<PageRequestModelTramiteErogacion> onOk(Response response, Message<PageRequestModelTramiteErogacion> request){
        PageModel<TramiteErogacionConciliacion> pageModel = response.readEntity(new GenericType<PageModel<TramiteErogacionConciliacion>>(){});
        request.getPayload().setResponse(pageModel);
        return request;
    }

}
