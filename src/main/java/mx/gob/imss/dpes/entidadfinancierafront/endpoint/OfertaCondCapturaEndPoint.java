/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.entidadfinancierafront.endpoint;

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
import mx.gob.imss.dpes.common.exception.RecursoNoExistenteException;
import mx.gob.imss.dpes.common.model.BaseModel;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.entidadfinancierafront.model.OfertaCondCapturaRequest;
import mx.gob.imss.dpes.entidadfinancierafront.restclient.OfertasCondCapturaClient;
import mx.gob.imss.dpes.interfaces.entidadfinanciera.model.OfertaCondCaptura;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.rest.client.inject.RestClient;

/**
 *
 * @author Diego
 */

@Path("/plazosCondicion")
@RequestScoped
public class OfertaCondCapturaEndPoint extends BaseGUIEndPoint<BaseModel, OfertaCondCaptura, BaseModel>{
 
    
    @Inject
    @RestClient 
    OfertasCondCapturaClient client;
       
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Obtener condición de oferta con los plazos",
            description = "OObtener condición de oferta con los plazos")
 
    public Response load(OfertaCondCapturaRequest request)
            throws BusinessException {
        log.log(Level.INFO,"OfertaCondCapturaEndPoint FRONT {0}",request.getId());
        Response load = client.load(request);
        log.log(Level.INFO,"LISTA{0}",load);
        if(load.getStatus() == 200){
            
            return load;
        }
        return toResponse( new Message( new RecursoNoExistenteException() ));
        
   
    }
}


