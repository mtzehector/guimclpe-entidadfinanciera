package mx.gob.imss.dpes.entidadfinancierafront.service;

import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.model.ServiceStatusEnum;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.entidadfinancierafront.exception.CondicionPersistenciaException;
import mx.gob.imss.dpes.entidadfinancierafront.model.CondicionEntFedModel;
import mx.gob.imss.dpes.entidadfinancierafront.model.CondicionOfertaPersistenciaModelRequest;
import mx.gob.imss.dpes.entidadfinancierafront.model.CondicionPersistenciaModel;
import mx.gob.imss.dpes.entidadfinancierafront.restclient.EntidadFinancieraClient;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.inject.Inject;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.util.List;
import java.util.logging.Level;

@Provider
public class CondicionesEntidadFinancieraOfertasService
    extends ServiceDefinition<CondicionPersistenciaModel, CondicionPersistenciaModel> {

    @Inject
    @RestClient
    private EntidadFinancieraClient entidadFinancieraClient;

    private Long obtenerIdEntidadFinanciera(Message<CondicionPersistenciaModel> request) {
        Long idEntidadFinanciera  =
                (request.getPayload().getLogo() != null && request.getPayload().getLogo().getCveEntidadFinanciera() != null ?
                        request.getPayload().getLogo().getCveEntidadFinanciera() : null);

        if (idEntidadFinanciera == null) {
            List<CondicionEntFedModel> condiciones =
                    (List<CondicionEntFedModel>) request.getPayload().getMclcCondicionEntfedCollection();

            idEntidadFinanciera = (condiciones != null && condiciones.size() > 0 &&
                condiciones.get(0).getMclcEntidadFinanciera() != null)
                    ? condiciones.get(0).getMclcEntidadFinanciera() : null;
        }

        return idEntidadFinanciera;
    }

    @Override
    public Message<CondicionPersistenciaModel> execute(Message<CondicionPersistenciaModel> request)
        throws BusinessException {

        try {
            Long idEntidadFinanciera = this.obtenerIdEntidadFinanciera(request);

            log.log(Level.INFO, "Inicia CondicionesEntidadFinancieraOfertasService: {0}", idEntidadFinanciera);

            if(idEntidadFinanciera != null) {
                Response responseEntidadFinanciera = entidadFinancieraClient.
                        getOfertasEntidadFinanciera(idEntidadFinanciera);

                if (responseEntidadFinanciera != null && responseEntidadFinanciera.getStatus() == 200) {

                    request.getPayload().setCondicionesOfertaPrevias(
                            responseEntidadFinanciera.readEntity(
                                    new GenericType<List<CondicionOfertaPersistenciaModelRequest>>() {
                                    }));

                    log.log(Level.INFO, "Finaliza CondicionesEntidadFinancieraOfertasService: {0},",
                        idEntidadFinanciera);

                    return request;
                }
            }
        } catch (Exception e) {
            log.log(Level.SEVERE, ">>>>ERROR!!! CondicionesEntidadFinancieraOfertasService = {0}", e);
        }

        return response(null,ServiceStatusEnum.EXCEPCION,
            new CondicionPersistenciaException(
                    CondicionPersistenciaException.CONDICION_ENTIDAD_FINANCIERA_OFERTA_SERVICE_ERROR), null);
    }
}
