/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.entidadfinancierafront.endpoint;

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
import mx.gob.imss.dpes.common.model.BaseModel;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.model.ServiceStatusEnum;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.entidadfinancierafront.exception.CatMaximoException;
import mx.gob.imss.dpes.entidadfinancierafront.model.PlazosRequest;
import mx.gob.imss.dpes.entidadfinancierafront.restclient.PlazosClient;
import mx.gob.imss.dpes.entidadfinancierafront.service.ValidarCapacidadCreditoService;
import mx.gob.imss.dpes.interfaces.entidadfinanciera.model.Plazo;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.util.Date;
import java.util.logging.Level;

/**
 *
 * @author Diego Velazquez
 */
@Path("/plazos")
@RequestScoped
public class PlazosEndPoint extends BaseGUIEndPoint<BaseModel, Plazo, BaseModel> {
 
    @Inject
    @RestClient
    PlazosClient client;
    
    @Inject
    ValidarCapacidadCreditoService service;
        
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Obtener los plazos por monto del prestamo",
            description = "Obtener los plazos por monto del prestamo")
    public Response load(PlazosRequest request) {

        try {
            ServiceDefinition[] steps = {service};
            Message<PlazosRequest> peticion = service.executeSteps(steps, new Message<>(request));

            Response data = client.load(peticion.getPayload());

            if (
                data.getStatus() == Response.Status.OK.getStatusCode() ||
                data.getStatus() == Response.Status.NO_CONTENT.getStatusCode()
            )
                return data;
        } catch (BusinessException be) {
            return toResponse(new Message(null, ServiceStatusEnum.EXCEPCION, be, null));
        } catch (Exception e) {
            log.log(Level.SEVERE, "ERROR PlazosEndPoint.load request = [" + request + "]", e);
        }

        return toResponse(new Message(null, ServiceStatusEnum.EXCEPCION,
                new CatMaximoException(CatMaximoException.ERROR_DESCONOCIDO_EN_EL_SERVICIO),
                null));
    }
}



