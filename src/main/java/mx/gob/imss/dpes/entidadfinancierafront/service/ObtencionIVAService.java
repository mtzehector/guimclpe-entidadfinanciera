package mx.gob.imss.dpes.entidadfinancierafront.service;

import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.model.ServiceStatusEnum;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.entidadfinancierafront.exception.ConciliacionException;
import mx.gob.imss.dpes.entidadfinancierafront.restclient.CatalogoClient;
import mx.gob.imss.dpes.interfaces.catalogo.Parametro;
import mx.gob.imss.dpes.interfaces.entidadfinanciera.model.ConciliacionRequest;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.inject.Inject;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.math.BigDecimal;
import java.util.logging.Level;

@Provider
public class ObtencionIVAService extends ServiceDefinition<ConciliacionRequest, ConciliacionRequest> {
    @Inject
    @RestClient
    private CatalogoClient catalogoClient;


    @Override
    public Message<ConciliacionRequest> execute(Message<ConciliacionRequest> request) throws BusinessException {
        Response respuesta = null;
        try {
            respuesta = catalogoClient.getParametroPorId(2L);

            if (respuesta.getStatus() == 200) {
                Parametro parametro = respuesta.readEntity(Parametro.class);
                if (parametro != null) {
                    BigDecimal iva = new BigDecimal(parametro.getValor());

                    request.getPayload().setIva(iva);

                    return request;
                }
            }

        } catch (Exception e) {
            log.log(Level.SEVERE, "ERROR ObtencionIVAService.execute() = [" + request + "]", e);
            return response(
                    null,
                    ServiceStatusEnum.EXCEPCION,
                    new ConciliacionException(
                            ConciliacionException.ERROR_AL_OBTENER_PORCENTAJE_IVA),
                    null
            );
        }

        throw new ConciliacionException(ConciliacionException.ERROR_AL_OBTENER_PORCENTAJE_IVA);
    }
}
