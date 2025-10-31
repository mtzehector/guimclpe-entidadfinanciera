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
import mx.gob.imss.dpes.entidadfinancierafront.model.CartaReciboRequest;
import mx.gob.imss.dpes.entidadfinancierafront.model.EntidadFinancieraPersistenceRequest;
import mx.gob.imss.dpes.entidadfinancierafront.restclient.CartaReciboClient;
import mx.gob.imss.dpes.entidadfinancierafront.restclient.EntidadFinancieraClient;
import org.eclipse.microprofile.rest.client.inject.RestClient;

/**
 *
 * @author juanf.barragan
 */
@Provider
public class CartaReciboService extends ServiceDefinition<CartaReciboRequest,CartaReciboRequest>{
    
    @Inject
    @RestClient
    private CartaReciboClient client;
    
    @Inject
    @RestClient
    private EntidadFinancieraClient efClient;

    @Override
    public Message<CartaReciboRequest> execute(Message<CartaReciboRequest> request) throws BusinessException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public List<CartaReciboRequest> listarCartas(CartaReciboRequest request) throws BusinessException{
        log.log(Level.INFO,"CartaReciboService Inicia la busqueda de las cartas del periodo ", request.getPeriodo());
        
        Response response = client.create(request);
        
        if(response.getStatus() == 200){
            log.log(Level.INFO, "200 OK");
            List<CartaReciboRequest> resp = response.readEntity(new GenericType<List<CartaReciboRequest>>(){});
            for(CartaReciboRequest carta : resp){
                Response res = efClient.getEntidadFinanciera(carta.getCveEntFin());
                if(res.getStatus() ==200){
                    EntidadFinancieraPersistenceRequest ef = res.readEntity(EntidadFinancieraPersistenceRequest.class);
                    carta.setNomComercial(ef.getNombreComercial());
                }
            }
            return resp;
        }
        
        return null;
    }
}
