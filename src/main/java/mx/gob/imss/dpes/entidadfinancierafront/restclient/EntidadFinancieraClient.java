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
import mx.gob.imss.dpes.entidadfinancierafront.model.EntidadFinancieraPersistenceRequest;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

/**
 *
 * @author eduardo.loyo
 */
@Path("/entidadfinanciera")
@RegisterRestClient
public interface EntidadFinancieraClient {

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response create(EntidadFinancieraPersistenceRequest financiera);
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/logo/{id}")
    public Response getLogo(@PathParam("id") Long id);
    
    @GET
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getEntidadFinanciera(@PathParam("id") Long id);

    @GET
    @Path("/{id}/ofertas")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getOfertasEntidadFinanciera(@PathParam("id") Long idEntidadFinanciera);

    @GET
    @Path("/{idEntidadFinanciera}/ot2")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findEntidadFinancieraById(@PathParam("idEntidadFinanciera") Long idEntidadFinanciera);

    @GET
    @Path("/tasas/{cveEntidadFinanciera}/{periodo}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response obtenerTasasPorEntidadFinancieraPeriodo(
        @PathParam("cveEntidadFinanciera") Long cveEntidadFinanciera,
        @PathParam("periodo") String periodo
    );

}
