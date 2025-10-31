package mx.gob.imss.dpes.entidadfinancierafront.service;

import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.entidadfinancierafront.exception.ConciliacionException;
import mx.gob.imss.dpes.entidadfinancierafront.restclient.ConciliacionCartaReciboClient;
import mx.gob.imss.dpes.interfaces.entidadfinanciera.model.CartaRecibo;
import mx.gob.imss.dpes.interfaces.entidadfinanciera.model.ConciliacionRequest;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.util.logging.Level;
import java.util.logging.Logger;

@RequestScoped
public class ObtenerDatosCartaRecibo {

    private Logger log = Logger.getLogger(this.getClass().getName());
    @Inject
    @RestClient
    private ConciliacionCartaReciboClient cartaReciboClient;


    public CartaRecibo obtenerDatosCartaRecibo(ConciliacionRequest request) throws BusinessException {
        try{
            Response response = cartaReciboClient.obtenerDatosCartaRecibo(request);
            if (response != null && response.getStatus() == 200) {
                return response.readEntity(CartaRecibo.class);
            }
        }catch (Exception e){
            log.log(Level.SEVERE, "ERROR ObtenerDatosCartaRecibo.obtenerDatosCartaRecibo()", e);
            throw new ConciliacionException(ConciliacionException.ERROR_AL_GENERAR_REPORTE_CARTA_RECIBO);
        }
        return null;
    }


}
