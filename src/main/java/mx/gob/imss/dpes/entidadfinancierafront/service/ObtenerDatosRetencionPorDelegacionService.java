package mx.gob.imss.dpes.entidadfinancierafront.service;

import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.entidadfinancierafront.exception.ConciliacionException;
import mx.gob.imss.dpes.entidadfinancierafront.restclient.ConciliacionEFClient;
import mx.gob.imss.dpes.entidadfinancierafront.restclient.EntidadFinancieraClient;
import mx.gob.imss.dpes.interfaces.entidadfinanciera.model.ConciliacionRequest;
import mx.gob.imss.dpes.interfaces.entidadfinanciera.model.PermisoItineranteYCostoAdministrativo;
import mx.gob.imss.dpes.interfaces.entidadfinanciera.model.RetencionDelegacionImporteFallecidos;
import mx.gob.imss.dpes.interfaces.entidadfinanciera.model.RetencionPorDelegacion;
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
public class ObtenerDatosRetencionPorDelegacionService {

    @Inject
    @RestClient
    private ConciliacionEFClient conciliacionEFClient;
    @Inject
    @RestClient
    private EntidadFinancieraClient entidadFinancieraClient;

    private Logger log = Logger.getLogger(this.getClass().getName());

    public PermisoItineranteYCostoAdministrativo obtenerTasasEntidadFinanciera(
            Long cveEntidadFinanciera,
            String periodo) throws BusinessException {
        try {
            Response respuesta = entidadFinancieraClient.obtenerTasasPorEntidadFinancieraPeriodo(cveEntidadFinanciera, periodo);
            if (respuesta != null && respuesta.getStatus() == 200)
                return respuesta.readEntity(PermisoItineranteYCostoAdministrativo.class);
        }catch (Exception e){
            log.log(Level.SEVERE, "ERROR ObtenerDatosRetencionPorDelegacionService.obtenerPorcentjaPermisoItinerante() = {0}", e);
            throw new ConciliacionException(ConciliacionException.ERROR_AL_INVOCAR_SERVICIO_ENTIDAD_FINANCIERA);
        }
        return null;
    }

    public List<RetencionPorDelegacion> obtenerDatosReporte (ConciliacionRequest request) throws BusinessException {
        try {
            Response respuesta = conciliacionEFClient.obtenerDatosRetencionPorDelegacion(request);
            if (respuesta != null && respuesta.getStatus() == 200)
                return respuesta.readEntity(new GenericType<List<RetencionPorDelegacion>>(){});

        }catch (Exception e){
            log.log(Level.SEVERE, "ERROR ObtenerDatosRetencionPorDelegacionService.obtenerDatosReporte() = {0}", e);
            throw new ConciliacionException(ConciliacionException.ERROR_AL_INVOCAR_SERVICIO_ENTIDAD_FINANCIERA);
        }
        return new ArrayList<>();
    }

    public RetencionDelegacionImporteFallecidos obtenerImporteFallecidos(ConciliacionRequest request) throws BusinessException {
        try {
            Response respuesta = conciliacionEFClient.obtenerImporteFallecidos(request);
            if (respuesta != null && respuesta.getStatus() == 200) {
                return respuesta.readEntity(RetencionDelegacionImporteFallecidos.class);
            }
        }catch (Exception e){
            log.log(Level.SEVERE, "ERROR ObtenerDatosRetencionPorDelegacionService.obtenerImporteFallecidos() = {0}", e);
            throw new ConciliacionException(ConciliacionException.ERROR_AL_INVOCAR_SERVICIO_ENTIDAD_FINANCIERA);
        }
        return null;
    }

}
