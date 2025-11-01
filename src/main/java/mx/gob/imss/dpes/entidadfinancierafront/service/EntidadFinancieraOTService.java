package mx.gob.imss.dpes.entidadfinancierafront.service;

import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.model.ServiceStatusEnum;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.entidadfinancierafront.exception.EntidadFinancieraOTException;
import mx.gob.imss.dpes.entidadfinancierafront.restclient.EntidadFinancieraClient;
import mx.gob.imss.dpes.interfaces.entidadfinanciera.ot2.model.EntidadFinanciera;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.inject.Inject;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.util.logging.Level;

@Provider
public class EntidadFinancieraOTService extends ServiceDefinition<EntidadFinanciera, EntidadFinanciera> {

    @Inject
    @RestClient
    private EntidadFinancieraClient entidadFinancieraClient;

    @Override
    public Message<EntidadFinanciera> execute(Message<EntidadFinanciera> request) throws BusinessException {
        try {
            Response response = entidadFinancieraClient.findEntidadFinancieraById(request.getPayload().getId());
            if (response != null && 200 == response.getStatus()) {
                return new Message<>(response.readEntity(EntidadFinanciera.class));
            }
        }
        catch (Exception e) {
            log.log(Level.SEVERE, ">>>>ERROR!!! EntidadFinancieraOTService.execute = {0}", e);
        }

        return response(null, ServiceStatusEnum.EXCEPCION,
                new EntidadFinancieraOTException(
                        EntidadFinancieraOTException.ERROR_AL_INVOCAR_SERVICIO_DE_ENTIDAD_FINANCIERA), null);
    }

}
