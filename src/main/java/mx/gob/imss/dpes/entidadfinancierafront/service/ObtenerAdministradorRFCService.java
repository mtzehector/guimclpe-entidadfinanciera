package mx.gob.imss.dpes.entidadfinancierafront.service;

import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.model.ServiceStatusEnum;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.entidadfinancierafront.exception.EntidadFinancieraOTException;
import mx.gob.imss.dpes.entidadfinancierafront.model.Persona;
import mx.gob.imss.dpes.entidadfinancierafront.restclient.PersonaClient;
import mx.gob.imss.dpes.interfaces.entidadfinanciera.ot2.model.EntidadFinanciera;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.inject.Inject;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.util.logging.Level;

@Provider
public class ObtenerAdministradorRFCService extends ServiceDefinition<EntidadFinanciera, EntidadFinanciera> {

    @Inject
    @RestClient
    private PersonaClient personaClient;

    @Override
    public Message<EntidadFinanciera> execute(Message<EntidadFinanciera> request) throws BusinessException {
        try {
            Response response = personaClient.obtenerAdministradorRFC(
                    request.getPayload().getCurpAdmin()
            );
            if (response != null && response.getStatus() == 200) {
                Persona persona = response.readEntity(Persona.class);
                request.getPayload().setAdminRFC(persona.getRfc());
                return request;
            }
        }catch (Exception e){
            log.log(Level.SEVERE, "ERROR ObtenerAdministradorRFCService.execute() - curp = [" + request.getPayload().getCurpAdmin() + "]" , e);
        }
        return response(
                null,
                ServiceStatusEnum.EXCEPCION,
                new EntidadFinancieraOTException(EntidadFinancieraOTException.ERROR_AL_INVOCAR_SERVICIO_DE_PERSONA),
                null
        );
    }
}
