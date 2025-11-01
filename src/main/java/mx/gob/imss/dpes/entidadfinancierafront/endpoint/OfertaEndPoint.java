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
import mx.gob.imss.dpes.common.exception.PartialContentFlowException;
import mx.gob.imss.dpes.common.exception.RecursoNoExistenteException;
import mx.gob.imss.dpes.common.exception.VariableMessageException;
import mx.gob.imss.dpes.common.model.*;
import mx.gob.imss.dpes.entidadfinancierafront.model.OfertaRequest;
import mx.gob.imss.dpes.entidadfinancierafront.restclient.OfertasClient;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.rest.client.inject.RestClient;

/**
 *
 * @author Diego Velazquez
 */
@Path("/oferta")
@RequestScoped
public class OfertaEndPoint extends BaseGUIEndPoint<BaseModel, OfertaRequest, BaseModel>{
 
    @Inject
    @RestClient
    OfertasClient client;
    
       
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Obtener oferta",
            description = "Obtener oferta")
    
    public Response load(PageRequestModel<OfertaRequest> request) {
            //throws BusinessException {

        try {
            Response load = client.load(request);

            if (load != null) {
                if (load.getStatus() == Response.Status.OK.getStatusCode())
                    return load;
                else if (load.getStatus() == Response.Status.PARTIAL_CONTENT.getStatusCode())
                    throw new VariableMessageException((load.readEntity(ErrorInfo.class)).getMessage());
            }
        } catch (VariableMessageException e) {
            return toResponse(new Message(null, ServiceStatusEnum.PARTIAL_CONTENT,
                    new PartialContentFlowException(e.getMessage()), null));
        } catch (BusinessException e) {
            log.log(Level.SEVERE, ">>>>ERROR OfertaEndPoint.load = {0}", e);
            return toResponse(new Message(null, ServiceStatusEnum.EXCEPCION, e, null));
        } catch (Exception e) {
            log.log(Level.SEVERE, ">>>>ERROR OfertaEndPoint.load = {0}", e);
        }

        return toResponse( new Message( new RecursoNoExistenteException() ));
    }
}


