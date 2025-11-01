/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.entidadfinancierafront.restclient;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import mx.gob.imss.dpes.entidadfinancierafront.model.RequestEntidadFinanciera;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

/**
 *
 * @author eduardo.loyo
 */
@Path("/obtenerentidadfinanciera")
@RegisterRestClient
public interface ObtenerRegPatronalClient {
    
    @GET
    @Path("/{regPatronal}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response load(@PathParam("regPatronal") String regPatronal);
    
    @POST
    @Path("/consultaAdmin")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response obtenerInfoEFAdmin(RequestEntidadFinanciera financiera);
            
}
