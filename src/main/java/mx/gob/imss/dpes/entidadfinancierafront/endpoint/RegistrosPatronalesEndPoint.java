/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package mx.gob.imss.dpes.entidadfinancierafront.endpoint;
import mx.gob.imss.dpes.entidadfinancierafront.service.RegistroPatronalesService;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.enterprise.context.RequestScoped;
import mx.gob.imss.dpes.common.exception.BusinessException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.MediaType;
import java.util.logging.Level;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.entidadfinancierafront.model.RegistroPatronalRequest;
import mx.gob.imss.dpes.common.endpoint.BaseGUIEndPoint;

import javax.inject.Inject;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import org.eclipse.microprofile.openapi.annotations.Operation;


/**
 *
 * @author juanf.barragan
 */

@Path("/RegistrosPatronales")
@RequestScoped
public class RegistrosPatronalesEndPoint extends BaseGUIEndPoint<RegistroPatronalRequest, RegistroPatronalRequest, RegistroPatronalRequest>{

    @Inject
    private RegistroPatronalesService registrosPatronalesService;

    @GET
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response obtenerRegistroPatronales(@PathParam("id") Long id)throws BusinessException {
        log.log(Level.INFO,">>>entidadFinancieraFront RegistrosPatronalesEndPoint obtenerRegistroPatronales ID:{0}", id);

        Message<RegistroPatronalRequest> response = registrosPatronalesService.obtenerRegistroPatronales(id);

        return toResponse((response));
    }
    
//    @POST
//    @Consumes(MediaType.APPLICATION_JSON)
//    @Produces(MediaType.APPLICATION_JSON)
//    @Operation(summary = "Edita los registros patronales",
//            description = "Edita los registros patronales")
//    public Response create(RegistroPatronalRequest inner)throws BusinessException{
//        RegistroPatronalRequest request;
//        log.log(Level.INFO,">>> entidadFinancieraFront RegistrosPatronalesEndPoint create request:{0}", inner);
//        
//        ServiceDefinition[] steps = {registrosPatronalesService};
//        
//        Message<RegistroPatronalRequest> response = registrosPatronalesService.executeSteps(steps,new Message<>(inner));
//        log.log(Level.INFO,">>> entidadFinancieraFront EntidadFinancieraEndPoint.create respuesta response:{0}", response);
//        if (!Message.isException(response)) {
//            return toResponse(response);
//        }
//        return toResponse(response);
//    }
}
