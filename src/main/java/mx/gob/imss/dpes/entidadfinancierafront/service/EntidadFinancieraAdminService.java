/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.entidadfinancierafront.service;

import java.util.logging.Level;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.model.ServiceStatusEnum;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.entidadfinancierafront.model.RequestEntidadFinanciera;
import mx.gob.imss.dpes.entidadfinancierafront.restclient.ObtenerRegPatronalClient;
import org.eclipse.microprofile.rest.client.inject.RestClient;

/**
 *
 * @author edgar.arenas
 */
@Provider
public class EntidadFinancieraAdminService extends ServiceDefinition<RequestEntidadFinanciera,RequestEntidadFinanciera> {
    
    @Inject
    @RestClient
    private ObtenerRegPatronalClient client;
    
    @Override
    public Message<RequestEntidadFinanciera> execute(Message<RequestEntidadFinanciera> request) throws BusinessException {
     log.log(Level.INFO, ">>> EntidadFinanciera Admin: {0}", request.getPayload());
        
        Response response = client.obtenerInfoEFAdmin(request.getPayload());
        if (response.getStatus() == 200) {
            RequestEntidadFinanciera res = response.readEntity(RequestEntidadFinanciera.class);
             return new Message<>(res);
        }
        return response(null, ServiceStatusEnum.EXCEPCION, null, null);
    }
    
}
