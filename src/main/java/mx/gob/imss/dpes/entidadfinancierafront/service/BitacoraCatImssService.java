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

import mx.gob.imss.dpes.common.enums.BitacoraEnum;
import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.model.ServiceStatusEnum;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.entidadfinancierafront.exception.CatMaximoException;
import mx.gob.imss.dpes.entidadfinancierafront.model.CatMaximoModel;
import mx.gob.imss.dpes.entidadfinancierafront.restclient.BitacoraCatImssClient;
import mx.gob.imss.dpes.interfaces.bitacora.model.BitacoraCatImss;
import org.eclipse.microprofile.rest.client.inject.RestClient;

/**
 *
 * @author juanf.barragan
 */
@Provider
public class BitacoraCatImssService extends ServiceDefinition<CatMaximoModel,CatMaximoModel>{
    
    @Inject
    @RestClient
    private BitacoraCatImssClient bitacoraCatImssClient;

    @Override
    public Message<CatMaximoModel> execute(Message<CatMaximoModel> request) throws BusinessException {
        log.log(Level.INFO, "***** Inicia BitacoraCatImssService *****");

        BitacoraCatImss model = new BitacoraCatImss();
        model.setIdTipoEvento(BitacoraEnum.ACTUALIZACION_CAT_MAXIMO.getId());
        model.setCveDocumento(request.getPayload().getDocumentoResponse().getId());
        model.setCurp(request.getPayload().getCurp());
        model.setCat(request.getPayload().getCatIMSS());

        try {
            Response responseBitacoraCatImssClient = bitacoraCatImssClient.create(model);
            if(responseBitacoraCatImssClient != null && responseBitacoraCatImssClient.getStatus() == 200){
                log.log(Level.INFO, "***** Finaliza BitacoraCatImssService *****");
                return request;
            }
        }
        catch (Exception e) {
            log.log(Level.SEVERE, ">>>>ERROR!!! BitacoraCatImssService = {0}", e);
        }

       return response(null, ServiceStatusEnum.EXCEPCION,
               new CatMaximoException(CatMaximoException.ERROR_AL_INVOCAR_SERVICIO_DE_BITACORA_CAT_MAXIMO), null);
    }
    
}
