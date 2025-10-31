/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.entidadfinancierafront.endpoint;

import java.util.List;
import java.util.logging.Level;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import mx.gob.imss.dpes.common.endpoint.BaseGUIEndPoint;
import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.entidadfinancierafront.model.CartaReciboRequest;
import mx.gob.imss.dpes.entidadfinancierafront.model.CartaReciboResponse;
import mx.gob.imss.dpes.entidadfinancierafront.service.CartaReciboService;

/**
 *
 * @author juanf.barragan
 */
@Path("/cartaRecibo")
@RequestScoped
public class CartaReciboEndPoint extends BaseGUIEndPoint<CartaReciboRequest,CartaReciboRequest,CartaReciboRequest> {
    
    @Inject
    private CartaReciboService service;
    
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response ConsultaPeriodo(CartaReciboRequest request)throws BusinessException{
        log.log(Level.INFO,"Inicia la consulta de las cartas por periodo ", request.getPeriodo());
        
        List<CartaReciboRequest> lista = service.listarCartas(request);
        CartaReciboResponse response = new CartaReciboResponse();
        response.setCartas(lista);
        log.log(Level.INFO,"regreso de informacion ", lista);
        
        return toResponse(new Message(response));
    }
    
}
