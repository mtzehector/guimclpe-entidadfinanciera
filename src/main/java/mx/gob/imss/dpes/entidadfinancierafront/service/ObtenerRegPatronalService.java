/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.entidadfinancierafront.service;

import javax.inject.Inject;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.entidadfinancierafront.exception.EntidadFinancieraException;
import mx.gob.imss.dpes.entidadfinancierafront.model.EntidadFinancieraPersistenceRequest;
import mx.gob.imss.dpes.entidadfinancierafront.restclient.ObtenerRegPatronalClient;
import org.eclipse.microprofile.rest.client.inject.RestClient;

/**
 *
 * @author eduardo.loyo
 */
@Provider
public class ObtenerRegPatronalService
        extends ServiceDefinition<EntidadFinancieraPersistenceRequest, EntidadFinancieraPersistenceRequest> {

    @Inject
    @RestClient
    private ObtenerRegPatronalClient client;

    @Override
    public Message<EntidadFinancieraPersistenceRequest> execute(Message<EntidadFinancieraPersistenceRequest> request) throws BusinessException {

        Response load = client.load(request.getPayload().getRegistroPatronal());
        if (load.getStatus() == 200) {
            EntidadFinancieraPersistenceRequest response
                    = load.readEntity(EntidadFinancieraPersistenceRequest.class);
            return new Message<>(response);
        } else {
            throw new EntidadFinancieraException("err003");
        }
    }
}
