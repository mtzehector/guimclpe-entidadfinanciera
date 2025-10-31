package mx.gob.imss.dpes.entidadfinancierafront.service;

import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.entidadfinancierafront.exception.ConciliacionException;
import mx.gob.imss.dpes.entidadfinancierafront.model.ResumenConciliacionBean;
import mx.gob.imss.dpes.entidadfinancierafront.restclient.ConciliacionEFClient;
import mx.gob.imss.dpes.interfaces.entidadfinanciera.model.ConciliacionRequest;
import mx.gob.imss.dpes.interfaces.entidadfinanciera.model.ResumenConciliacion;
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
public class ObtenerDatosResumenConciliacionService {

    @Inject
    @RestClient
    private ConciliacionEFClient conciliacionClient;
    private Logger log = Logger.getLogger(this.getClass().getName());

    public List<ResumenConciliacion> obtenerDatosResumenConciliacion(ConciliacionRequest request) throws BusinessException {
        try {
            Response respuesta = conciliacionClient.obtenerDatosReporteResumenConciliacion(request);
            if (respuesta != null && respuesta.getStatus() == 200){
                List<ResumenConciliacion> listResumenConciliacion = respuesta.readEntity(new GenericType<List<ResumenConciliacion>>(){});
                return listResumenConciliacion;
            }
        }catch (Exception e){
            log.log(Level.SEVERE, "ERROR ObtenerDatosResumenConciliacionService.obtenerDatosResumenConciliacion() = {0}", e);
            throw new ConciliacionException(ConciliacionException.ERROR_AL_INVOCAR_SERVICIO_CONCILIACION);
        }
        return new ArrayList<>();
    }

}
