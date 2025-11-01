/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.entidadfinancierafront.restclient;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import mx.gob.imss.dpes.interfaces.documento.model.Documento;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

/**
 *
 * @author osiris.hernandez
 */
@RegisterRestClient
@Path("/documento")
public interface DocumentoClient {

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response create(Documento request);

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/entidadfinanciera")
    public Response updateEntidadFinanciera(Documento request);
    
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/entidadfinanciera-clean")
    public Response updateEntidadFinancieraClean(Long cveEntidadFinanciera);

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public Response obtenerDocumentoPorId(@PathParam("id") Long cveDocumento);

    
}
