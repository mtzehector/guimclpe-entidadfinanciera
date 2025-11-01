package mx.gob.imss.dpes.entidadfinancierafront.service;

import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.entidadfinancierafront.exception.ConciliacionException;
import mx.gob.imss.dpes.entidadfinancierafront.restclient.ConciliacionSolicitudClient;
import mx.gob.imss.dpes.interfaces.entidadfinanciera.model.ConciliacionRequest;
import mx.gob.imss.dpes.interfaces.entidadfinanciera.model.ReporteEFPorCuentaContable;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@RequestScoped
public class ObtenerDatosEFPorCuentaContableService {

    @Inject
    @RestClient
    private ConciliacionSolicitudClient conciliacionSolicitudClient;

    private Logger log = Logger.getLogger(this.getClass().getName());


    public List<ReporteEFPorCuentaContable> obtenerDatosReporte(ConciliacionRequest request) throws BusinessException {
        try {
            Response respuesta = conciliacionSolicitudClient.obtenerDatosReporte(request);
            if (respuesta != null && respuesta.getStatus() == 200){
                return respuesta.readEntity(new GenericType<List<ReporteEFPorCuentaContable>>(){});
            }
        }catch (Exception e){
            log.log(Level.SEVERE, "ERROR ObtenerDatosEFPorCuentaContableService.obtenerDatosReporte() = {0}", e);
           throw new ConciliacionException(ConciliacionException.ERROR_AL_INVOCAR_SERVICIO_SOLICITUD);
        }
        return new ArrayList<>();
    }

}
