package mx.gob.imss.dpes.entidadfinancierafront.service;

import mx.gob.imss.dpes.common.enums.TipoPrestadorServiciosEnum;
import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.model.ServiceStatusEnum;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.entidadfinancierafront.exception.PrestadorServiciosException;
import mx.gob.imss.dpes.entidadfinancierafront.model.PrestadorServiciosModel;
import mx.gob.imss.dpes.entidadfinancierafront.restclient.PrestadorServiciosClient;
import mx.gob.imss.dpes.interfaces.entidadfinanciera.model.PrestadorServiciosEF;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.inject.Inject;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;

@Provider
public class PrestadorServiciosService extends ServiceDefinition<PrestadorServiciosModel, PrestadorServiciosModel>{

    @Inject
    @RestClient
    private PrestadorServiciosClient prestadorServiciosClient;

    private List<PrestadorServiciosEF> obtenerListaPrestadoresServicios(Message<PrestadorServiciosModel> request) {
        List<PrestadorServiciosEF> listaPrestadorServiciosEF = new ArrayList<PrestadorServiciosEF>();
        listaPrestadorServiciosEF.add(request.getPayload().getPsCertificacionAlta());
        listaPrestadorServiciosEF.add(request.getPayload().getPsValidacionBiometricaAlta());
        if(request.getPayload().getPsCertificacionBaja() != null) {
            listaPrestadorServiciosEF.add(request.getPayload().getPsCertificacionBaja());
        }
        if(request.getPayload().getPsValidacionBiometricaBaja() != null) {
            listaPrestadorServiciosEF.add(request.getPayload().getPsValidacionBiometricaBaja());
        }

        return listaPrestadorServiciosEF;
    }

    private void actualizarPrestadoresDeServicios(Message<PrestadorServiciosModel> request,
        Collection<PrestadorServiciosEF> listaPrestadorServicios) {

        if(listaPrestadorServicios != null && !listaPrestadorServicios.isEmpty()) {
            for(PrestadorServiciosEF prestadorServiciosEF : listaPrestadorServicios) {
                if(TipoPrestadorServiciosEnum.CERTIFICADO.getTipo().equals(
                    prestadorServiciosEF.getCveTipoPrestadorServicios())) {

                    if(prestadorServiciosEF.getBajaRegistro() == null)
                        request.getPayload().setPsCertificacionAlta(prestadorServiciosEF);
                    else
                        request.getPayload().setPsCertificacionBaja(prestadorServiciosEF);
                } else if(TipoPrestadorServiciosEnum.VALIDACION_BIOMETRICA.getTipo().equals(
                    prestadorServiciosEF.getCveTipoPrestadorServicios())) {

                    if(prestadorServiciosEF.getBajaRegistro() == null)
                        request.getPayload().setPsValidacionBiometricaAlta(prestadorServiciosEF);
                    else
                        request.getPayload().setPsValidacionBiometricaBaja(prestadorServiciosEF);
                }
            }
        }
    }

    @Override
    public Message<PrestadorServiciosModel> execute(Message<PrestadorServiciosModel> request) throws BusinessException {
        try{
            Response responseClient = prestadorServiciosClient.guardarLista(
                    this.obtenerListaPrestadoresServicios(request));

            if (responseClient != null && responseClient.getStatus() == 200) {
                this.actualizarPrestadoresDeServicios(request,
                    responseClient.readEntity(new GenericType<Collection<PrestadorServiciosEF>>(){}));

                return request;
            }
        }catch (Exception e){
            log.log(Level.SEVERE, "ERROR PrestadorServiciosService.execute = {0}", e);
        }
        return response(
            null,
            ServiceStatusEnum.EXCEPCION,
            new PrestadorServiciosException(PrestadorServiciosException.ERROR_AL_INVOCAR_SERVICIO_PRESTADOR_DE_SERVCIOS),
            null
        );
    }
}
