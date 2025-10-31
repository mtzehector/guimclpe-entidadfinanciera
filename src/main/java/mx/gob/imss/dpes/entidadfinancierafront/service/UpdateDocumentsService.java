/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.entidadfinancierafront.service;

import java.util.Collection;
import java.util.logging.Level;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.exception.UnknowException;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.model.ServiceStatusEnum;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.entidadfinancierafront.model.CondicionEntFedModel;
import mx.gob.imss.dpes.entidadfinancierafront.model.CondicionPersistenciaModel;
import mx.gob.imss.dpes.entidadfinancierafront.restclient.DocumentoClient;
import mx.gob.imss.dpes.interfaces.documento.model.Documento;
import org.eclipse.microprofile.rest.client.inject.RestClient;

/**
 *
 * @author eduardo.loyo
 */
@Provider
public class UpdateDocumentsService extends ServiceDefinition<CondicionPersistenciaModel, CondicionPersistenciaModel> {

    @Inject
    @RestClient
    private DocumentoClient documentClient;

    @Override
    public Message<CondicionPersistenciaModel> execute(Message<CondicionPersistenciaModel> request) throws BusinessException {
        log.log(Level.INFO,">>>entidadFinancieraFront UpdateDocumentsService");
        Collection<CondicionEntFedModel> condicionEntFedModelCollec = request.getPayload().getMclcCondicionEntfedCollection();
        CondicionEntFedModel firstCondicionEntFedModel = null;
        if (condicionEntFedModelCollec.iterator().hasNext()) {
            firstCondicionEntFedModel = condicionEntFedModelCollec.iterator().next();
        }


        if (firstCondicionEntFedModel != null && request.getPayload().getLogo()!=null && request.getPayload().getLogo().getId()!=null) {
            Long cveEntidadFinanciera = firstCondicionEntFedModel.getMclcEntidadFinanciera();
            log.log(Level.INFO, ">>>entidadFinancieraFront UpdateDocumentsService request.getPayload().getLogo()=", request.getPayload().getLogo());
            
            
          
            Response respuesta = null;
            
            respuesta = documentClient.updateEntidadFinancieraClean(cveEntidadFinanciera);
            log.log(Level.INFO, ">>>entidadFinancieraFront UpdateDocumentsService  cveEntidadFinanciera=" + cveEntidadFinanciera+" updateEntidadFinancieraClean. respuesta.getStatus()="+respuesta.getStatus());
            if (respuesta.getStatus() == 200) {
                Documento logo = request.getPayload().getLogo();
                logo.setCveEntidadFinanciera(cveEntidadFinanciera);
                respuesta = documentClient.updateEntidadFinanciera(logo);
                if (respuesta.getStatus() == 200) {
                    request.getPayload().setLogo(respuesta.readEntity(Documento.class));
                    log.log(Level.INFO, ">>>entidadFinancieraFront UpdateDocumentsService OK request.getPayload().getLogo()=" + request.getPayload().getLogo());
                    return new Message<>(request.getPayload());

                } else {
                    log.log(Level.SEVERE, ">>>entidadFinancieraFront UpdateDocumentsService ERROR updateDocuments [" + logo.getTipoDocumento() + "]= {0}", logo);
                    throw new UnknowException();
                }

            }
        }
        else{
            
                    return new Message<>(request.getPayload());
                     
        }
        return response(null, ServiceStatusEnum.EXCEPCION, null, null);
    }

}
