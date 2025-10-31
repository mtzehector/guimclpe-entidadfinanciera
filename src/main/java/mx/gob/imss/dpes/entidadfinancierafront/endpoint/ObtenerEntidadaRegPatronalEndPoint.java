/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.entidadfinancierafront.endpoint;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import mx.gob.imss.dpes.common.endpoint.BaseGUIEndPoint;
import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.exception.RecursoNoExistenteException;
import mx.gob.imss.dpes.common.model.BaseModel;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.entidadfinancierafront.model.EntidadFinancieraPersistenceRequest;
import mx.gob.imss.dpes.entidadfinancierafront.service.ObtenerRegPatronalService;
//import org.eclipse.microprofile.rest.client.inject.RestClient;

/**
 *
 * @author eduardo.loyo
 */
@Path("/obtenerentidadreg")
@RequestScoped
public class ObtenerEntidadaRegPatronalEndPoint
        extends BaseGUIEndPoint<BaseModel, EntidadFinancieraPersistenceRequest, BaseModel> {
    @Inject
    //@RestClient
    private ObtenerRegPatronalService service;
    
    @GET
    @Path("/{regPatronal}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response load(@PathParam("regPatronal") String regPatronal)
            throws BusinessException {
        EntidadFinancieraPersistenceRequest request = new EntidadFinancieraPersistenceRequest();
        request.setRegistroPatronal(regPatronal);
        
        Message<EntidadFinancieraPersistenceRequest> response = service.execute(new Message<>(request));
        
        if (!Message.isException(response)) {
            return toResponse(response);
        }
        return toResponse(response);
        
    }

}
