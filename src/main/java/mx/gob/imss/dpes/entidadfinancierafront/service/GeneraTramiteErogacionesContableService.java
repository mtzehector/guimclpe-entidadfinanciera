package mx.gob.imss.dpes.entidadfinancierafront.service;

import mx.gob.imss.dpes.common.enums.TipoDocumentoEnum;
import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.entidadfinancierafront.exception.ConciliacionException;
import mx.gob.imss.dpes.entidadfinancierafront.restclient.ConciliacionTramiteErogacionesClient;
import mx.gob.imss.dpes.interfaces.documento.model.Documento;
import mx.gob.imss.dpes.interfaces.entidadfinanciera.model.ConciliacionRequest;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.inject.Inject;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.util.List;
import java.util.logging.Level;

@Provider
public class GeneraTramiteErogacionesContableService extends ServiceDefinition<ConciliacionRequest, ConciliacionRequest> {

    @Inject
    @RestClient
    private ConciliacionTramiteErogacionesClient tramiteErogacionesClient;

    @Override
    public Message<ConciliacionRequest> execute(Message<ConciliacionRequest> request) throws BusinessException {
        try {
            request.getPayload().setCveTipoDocumento(
                    TipoDocumentoEnum.REPORTE_RETENCIONES_EF_DESGLOSADO_CUENTA_CONTABLE.getTipo()
            );
            Response respuesta = tramiteErogacionesClient.generarReporteEFPorCuentaContable(request.getPayload());
            if (respuesta != null && respuesta.getStatus() == 200) {
                List<Documento> listaDocumentos = respuesta.readEntity(new GenericType<List<Documento>>(){});
                request.getPayload().getListDocumentos().addAll(listaDocumentos);
            }
            return request;
        }catch (Exception e){
            log.log(Level.SEVERE, "ERROR GeneraTramiteErogacionesContableService.execute() = {0}", e);
            throw new ConciliacionException(ConciliacionException.ERROR_AL_GENERAR_TRAMITE_EROGACIONES);
        }
    }
}
