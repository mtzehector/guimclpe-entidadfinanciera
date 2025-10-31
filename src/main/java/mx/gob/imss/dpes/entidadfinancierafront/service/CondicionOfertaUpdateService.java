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
import mx.gob.imss.dpes.entidadfinancierafront.assembler.CondicionOfertaAssembler;
import mx.gob.imss.dpes.entidadfinancierafront.assembler.CondicionesAssembler;
import mx.gob.imss.dpes.entidadfinancierafront.model.CondicionPersistenciaModel;
import mx.gob.imss.dpes.entidadfinancierafront.restclient.CondicionOfertaUpdateClient;
import org.eclipse.microprofile.rest.client.inject.RestClient;

/**
 *
 * @author eduardo.loyo
 */
@Provider
public class CondicionOfertaUpdateService extends ServiceDefinition<CondicionPersistenciaModel, CondicionPersistenciaModel> {

    @Inject
    @RestClient
    private CondicionOfertaUpdateClient client;

    @Inject
    private CondicionOfertaAssembler assembler;

    @Override
    public Message<CondicionPersistenciaModel> execute(Message<CondicionPersistenciaModel> request) throws BusinessException {
        log.log(Level.INFO, ">>>entidadFinancieraFront CondicionOfertaUpdateService Inicia actualización condicion oferta------------Step 3 ----------: {0}", request.getPayload().getCondicionOferta());
       
        Response create = client.create(assembler.assemble(request.getPayload()));
        if (create.getStatus() == 200) {
            log.log(Level.INFO,">>>entidadFinancieraFront CondicionOfertaUpdateService OK entro");
            //request.setPayload(null);
            return  request;
        }
        return response(null, ServiceStatusEnum.EXCEPCION, null, null);
    }

}
