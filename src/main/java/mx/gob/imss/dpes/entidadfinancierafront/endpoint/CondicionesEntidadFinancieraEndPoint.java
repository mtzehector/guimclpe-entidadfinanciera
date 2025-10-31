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
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.entidadfinancierafront.model.CondicionPersistenciaModel;
import mx.gob.imss.dpes.entidadfinancierafront.service.*;
import org.eclipse.microprofile.openapi.annotations.Operation;

/**
 *
 * @author eduardo.loyo
 */
@RequestScoped
@Path("/condicionespersistencia")
public class CondicionesEntidadFinancieraEndPoint extends BaseGUIEndPoint<CondicionPersistenciaModel, CondicionPersistenciaModel, CondicionPersistenciaModel> {

    @Inject
    private BeneficioService beneficio;

    @Inject
    private CondicionEntFedService entidadFederativa;

    @Inject
    private CondicionOfertaUpdateService condicionOferta;

    @Inject
    private UpdateDocumentsService updateDocuments;

    @Inject
    CondicionesEntidadFinancieraOfertasService condicionesEntidadFinancieraOfertasService;

    @Inject
    CondicionesEntidadFinancieraBitacoraCatEFService condicionesEntidadFinancieraBitacoraCatEFService;
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Orquestador condiciones",
            description = "Orquestador condiciones")
    @Override
    public Response create(CondicionPersistenciaModel request) throws BusinessException {
        log.log(Level.INFO,">>>entidadFinancieraFront CondicionesEntidadFinancieraEndPoint Request: {0}", request);
        ServiceDefinition[] steps = {condicionesEntidadFinancieraOfertasService, entidadFederativa, updateDocuments,
                beneficio, condicionOferta, condicionesEntidadFinancieraBitacoraCatEFService};
        Message<CondicionPersistenciaModel> response
                = beneficio.executeSteps(steps, new Message<>(request));

        return toResponse(response);
    }
}
