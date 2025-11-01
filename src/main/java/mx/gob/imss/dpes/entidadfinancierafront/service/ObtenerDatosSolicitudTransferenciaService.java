package mx.gob.imss.dpes.entidadfinancierafront.service;

import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.entidadfinancierafront.exception.ConciliacionException;
import mx.gob.imss.dpes.entidadfinancierafront.restclient.ConciliacionEFClient;
import mx.gob.imss.dpes.interfaces.entidadfinanciera.model.ConciliacionRequest;
import mx.gob.imss.dpes.interfaces.entidadfinanciera.model.SolicitudTransferencia;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.util.logging.Level;
import java.util.logging.Logger;

@RequestScoped
public class ObtenerDatosSolicitudTransferenciaService {

    @Inject
    @RestClient
    private ConciliacionEFClient conciliacionEFClient;
    private Logger log = Logger.getLogger(this.getClass().getName());

    public  SolicitudTransferencia obtenerDatosReporte(ConciliacionRequest request) throws BusinessException {
        try {
            Response respuesta = conciliacionEFClient.obtenerDatosReporte(request);
            if (respuesta != null && respuesta.getStatus() == 200)
                return respuesta.readEntity(SolicitudTransferencia.class);
        }catch (Exception e){
            log.log(Level.SEVERE, "ERROR ObtenerDatosSolicitudTransferenciaService.obtenerDatosReporte() = {0}", e);
            throw new ConciliacionException(ConciliacionException.ERROR_AL_INVOCAR_SERVICIO_ENTIDAD_FINANCIERA);
        }
        return null;
    }

}
