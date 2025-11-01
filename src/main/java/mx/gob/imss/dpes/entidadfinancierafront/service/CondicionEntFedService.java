/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.entidadfinancierafront.service;


import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import javax.inject.Inject;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.model.ServiceStatusEnum;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.entidadfinancierafront.assembler.CondicionesAssembler;
import mx.gob.imss.dpes.entidadfinancierafront.model.CondicionEntFedModel;
import mx.gob.imss.dpes.entidadfinancierafront.model.CondicionFederativaRequest;
import mx.gob.imss.dpes.entidadfinancierafront.model.CondicionPersistenciaModel;
import mx.gob.imss.dpes.entidadfinancierafront.restclient.CondicionEntFedClient;
import org.eclipse.microprofile.rest.client.inject.RestClient;

/**
 *
 * @author eduardo.loyo
 */
@Provider
public class CondicionEntFedService extends ServiceDefinition<CondicionPersistenciaModel, CondicionPersistenciaModel> {

    @Inject
    @RestClient
    private CondicionEntFedClient client;
    
    @Inject
    private CondicionesAssembler assembler;

    @Override
    public Message<CondicionPersistenciaModel> execute(Message<CondicionPersistenciaModel> request) throws BusinessException {
        log.log(Level.INFO, ">>>entidadFinancieraFront CondicionEntFedService Inicia Condicion Entidad Federativa ------------Step 1 ----------: {0}", request.getPayload().getMclcCondicionEntfedCollection());
    //   log.log(Level.INFO,"assembler ERPE {0}",assembler.assemble(request.getPayload()));
        Response create = client.create(assembler.assemble(request.getPayload()));
        if (create.getStatus() == 200) {
            log.log(Level.INFO,">>>entidadFinancieraFront CondicionEntFedService OK entro");
            List<CondicionEntFedModel> respuesta =  create.readEntity(new GenericType<List<CondicionEntFedModel>>(){});
            log.log(Level.INFO, ">>>entidadFinancieraFront CondicionEntFedService Repuesta CondicionEntFed: {0}", respuesta);
            request.getPayload().setMclcCondicionEntfedCollection(respuesta);
            return new Message<>(request.getPayload());
        }
        return response(null, ServiceStatusEnum.EXCEPCION, null, null);
    }

}
