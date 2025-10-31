/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.entidadfinancierafront.service;

import java.util.logging.Level;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.model.ServiceStatusEnum;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.entidadfinancierafront.exception.CatMaximoException;
import mx.gob.imss.dpes.entidadfinancierafront.model.CatMaximoModel;
import mx.gob.imss.dpes.entidadfinancierafront.restclient.CatMaximoClient;
import org.eclipse.microprofile.rest.client.inject.RestClient;

/**
 *
 * @author juanf.barragan
 */
@Provider
public class CatMaximoService extends ServiceDefinition<CatMaximoModel,CatMaximoModel>{
    
    @Inject
    @RestClient
    private CatMaximoClient catMaximoClient;

    @Override
    public Message<CatMaximoModel> execute(Message<CatMaximoModel> request) throws BusinessException {
        log.log(Level.INFO, "***** Inicia CatMaximoService *****");

        try {
            Response responseCatMaximoClient = catMaximoClient.create(request.getPayload());
            if (responseCatMaximoClient != null && responseCatMaximoClient.getStatus() == 200) {
                CatMaximoModel catMaximoModel = responseCatMaximoClient.readEntity(CatMaximoModel.class);
                request.getPayload().setCondicionesOferta(catMaximoModel.getCondicionesOferta());
                log.log(Level.INFO, "***** Finaliza CatMaximoService *****");
                return request;
            }
        } catch (Exception e) {
            log.log(Level.SEVERE, ">>>>ERROR!!! CatMaximoService = {0}", e);
        }

        return response(null, ServiceStatusEnum.EXCEPCION,
                new CatMaximoException(CatMaximoException.ERROR_AL_INVOCAR_SERVICIO_DE_CAT_MAXIMO), null);
    }
    
    public CatMaximoModel read () throws BusinessException {
        CatMaximoModel model = null;

        Response resp = catMaximoClient.read();
        if (resp.getStatus() == 200) {
            model = resp.readEntity(CatMaximoModel.class);
            log.log(Level.INFO,"Consulta del cat Actual ", model.getCatAnterior());
            return model;
        }

        return null;
    }

}
