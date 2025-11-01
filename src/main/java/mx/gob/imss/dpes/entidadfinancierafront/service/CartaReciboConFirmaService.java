package mx.gob.imss.dpes.entidadfinancierafront.service;

import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.model.PageModel;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.entidadfinancierafront.exception.ConciliacionException;
import mx.gob.imss.dpes.entidadfinancierafront.restclient.BitacoraClient;
import mx.gob.imss.dpes.interfaces.bitacora.model.CartaReciboFirmada;
import mx.gob.imss.dpes.interfaces.entidadfinanciera.model.PageRequestModelConciliacion;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.inject.Inject;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.util.List;
import java.util.logging.Level;

@Provider
public class CartaReciboConFirmaService extends ServiceDefinition<PageRequestModelConciliacion, PageRequestModelConciliacion> {

    @Inject
    @RestClient
    private BitacoraClient bitacoraClient;

    @Override
    public Message<PageRequestModelConciliacion> execute(Message<PageRequestModelConciliacion> request) throws BusinessException {
        try {
            Response respuesta = bitacoraClient.obtenerListCartaReciboConFirma(request.getPayload().getRequest());
            if (respuesta != null && respuesta.getStatus() == 200){
                return response(respuesta, request);
            }
            return request;
        }catch (Exception e){
            log.log(Level.SEVERE, "ERROR CartaReciboConFirmaService.execute()", e);
            throw new ConciliacionException(ConciliacionException.ERROR_AL_INVOCAR_SERVICIO_BITACORA_CARTA_RECIBO);
        }
    }

    @Override
    protected Message<PageRequestModelConciliacion> onOk(Response response, Message<PageRequestModelConciliacion> request){
        PageModel<CartaReciboFirmada> pageModel = response.readEntity(new GenericType<PageModel<CartaReciboFirmada>>() {});
        request.getPayload().setResponse(pageModel);
        return request;
    }
}
