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

import mx.gob.imss.dpes.entidadfinancierafront.model.EntidadFinancieraRequest;
import mx.gob.imss.dpes.entidadfinancierafront.model.UpdateAdminRequest;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

/**
 *
 * @author juanf.barragan
 */
@Path("/persona")
@RegisterRestClient
public interface PersonaClient {
    
    @POST
    @Path("/actualizaAdmin")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response actualizaAdmin(UpdateAdminRequest updateAdmin);

    @GET
    @Path("/{curp}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response obtenerAdministradorRFC(
            @PathParam("curp") String curp
    );

    @GET
    @Path("/obtenerOperadorEFFirma/{cveEntidadFinanciera}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response obtenerOperadorEFFirma(
      @PathParam("cveEntidadFinanciera") Long cveEntidadFinanciera
    );

    @POST
    @Path("/inhabilitaUsuario")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response inhabilitaAdmin(UpdateAdminRequest updateAdmin);
    
}
