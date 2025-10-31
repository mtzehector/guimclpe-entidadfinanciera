/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.entidadfinancierafront.service;

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
import mx.gob.imss.dpes.entidadfinancierafront.assembler.EntidadCondicionAssembler;
import mx.gob.imss.dpes.entidadfinancierafront.model.CondicionOfertaPersistenceRequest;
import mx.gob.imss.dpes.entidadfinancierafront.model.CondicionOfertaPersistenciaModelRequest;
import mx.gob.imss.dpes.entidadfinancierafront.model.EntidadFinancieraRequest;
import mx.gob.imss.dpes.entidadfinancierafront.restclient.CondicionOfertaUpdateClient;
import org.eclipse.microprofile.rest.client.inject.RestClient;

/**
 *
 * @author eduardo.loyo
 */
@Provider
public class CondicionOfertaPersistanceService extends ServiceDefinition<EntidadFinancieraRequest,EntidadFinancieraRequest>{

    @Inject
    @RestClient
    private CondicionOfertaUpdateClient client;
    
    @Inject
    private EntidadCondicionAssembler assembler;
            
    @Override
    public Message<EntidadFinancieraRequest> execute(Message<EntidadFinancieraRequest> request) throws BusinessException {
        CondicionOfertaPersistenceRequest requestClient = assembler.assemble(request.getPayload());
        Response create = client.create(requestClient);
        log.log(Level.INFO,"----------------------Inicia Step 2 ------------------------");
        log.log(Level.INFO,"request: {0}", request.getPayload().getMclcCondicionOfertaCollection());
        if (create.getStatus() == 200) {
            
            List<CondicionOfertaPersistenciaModelRequest> respuesta =  create.readEntity(new GenericType<List<CondicionOfertaPersistenciaModelRequest>>(){});
            request.getPayload().setMclcCondicionOfertaCollection(respuesta);
            return new Message<>(request.getPayload());
        }
        return response(request.getPayload(),ServiceStatusEnum.EXCEPCION, null, null);
        
    }
    
}
