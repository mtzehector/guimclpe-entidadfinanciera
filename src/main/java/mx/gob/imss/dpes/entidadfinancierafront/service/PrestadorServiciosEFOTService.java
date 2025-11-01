package mx.gob.imss.dpes.entidadfinancierafront.service;

import mx.gob.imss.dpes.common.enums.TipoPrestadorServiciosEnum;
import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.model.ServiceStatusEnum;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.entidadfinancierafront.exception.EntidadFinancieraOTException;
import mx.gob.imss.dpes.entidadfinancierafront.restclient.PrestadorServiciosClient;
import mx.gob.imss.dpes.interfaces.entidadfinanciera.model.PrestadorServiciosEF;
import mx.gob.imss.dpes.interfaces.entidadfinanciera.ot2.model.EntidadFinanciera;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.inject.Inject;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.util.List;
import java.util.logging.Level;

@Provider
public class PrestadorServiciosEFOTService extends ServiceDefinition<EntidadFinanciera, EntidadFinanciera> {

    @Inject
    @RestClient
    private PrestadorServiciosClient prestadorServiciosClient;

    private void obtenerPrestadoresDeServicios(Message<EntidadFinanciera> request,
        List<PrestadorServiciosEF> listaPrestadoresServiciosEF) {

        if (listaPrestadoresServiciosEF != null && !listaPrestadoresServiciosEF.isEmpty()) {
            boolean existePSCertificado = false;
            boolean existePSValidacionBiometrica = false;

            for (PrestadorServiciosEF prestadorServiciosEF : listaPrestadoresServiciosEF) {
                if(!existePSCertificado && TipoPrestadorServiciosEnum.CERTIFICADO.getTipo() ==
                        prestadorServiciosEF.getCveTipoPrestadorServicios()) {
                    existePSCertificado = true;
                    request.getPayload().setPrestadorServicioCertificado(prestadorServiciosEF);
                } else if(!existePSValidacionBiometrica && TipoPrestadorServiciosEnum.VALIDACION_BIOMETRICA.
                        getTipo() == prestadorServiciosEF.getCveTipoPrestadorServicios()) {
                    existePSValidacionBiometrica = true;
                    request.getPayload().setPrestadorServicioValidacionBiometrica(prestadorServiciosEF);
                }

                if (existePSCertificado && existePSValidacionBiometrica)
                    break;
            }
        }
    }

    @Override
    public Message<EntidadFinanciera> execute(Message<EntidadFinanciera> request) throws BusinessException {
        try {
            Response response = prestadorServiciosClient.obtenerListaPrestadorServiciosEF(request.getPayload().getId());

            if(response != null) {
                if (200 == response.getStatus()) {
                    this.obtenerPrestadoresDeServicios(request,
                            response.readEntity(new GenericType<List<PrestadorServiciosEF>>() {
                            }));
                    return request;
                } else if (204 == response.getStatus())
                    return request;
            }
        }
        catch (Exception e) {
            log.log(Level.SEVERE, ">>>>ERROR!!! PrestadorServiciosEFOTService.execute = {0}", e);
        }

        return response(null, ServiceStatusEnum.EXCEPCION,
                new EntidadFinancieraOTException(
                        EntidadFinancieraOTException.ERROR_AL_INVOCAR_SERVICIO_DE_PRESTADOR_SERVICIO), null);
    }

}
