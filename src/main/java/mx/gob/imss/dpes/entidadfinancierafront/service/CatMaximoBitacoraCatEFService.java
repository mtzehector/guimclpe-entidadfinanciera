package mx.gob.imss.dpes.entidadfinancierafront.service;

import mx.gob.imss.dpes.common.enums.BitacoraEnum;
import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.model.ServiceStatusEnum;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.entidadfinancierafront.exception.CatMaximoException;
import mx.gob.imss.dpes.entidadfinancierafront.model.CatMaximoModel;
import mx.gob.imss.dpes.entidadfinancierafront.model.CondicionOfertaPersistenciaModelRequest;
import mx.gob.imss.dpes.entidadfinancierafront.restclient.BitacoraCatEFClient;
import mx.gob.imss.dpes.interfaces.bitacora.model.BitacoraCatEF;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.inject.Inject;
import javax.ws.rs.ext.Provider;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

@Provider
public class CatMaximoBitacoraCatEFService extends ServiceDefinition<CatMaximoModel,CatMaximoModel>{
    
    @Inject
    @RestClient
    private BitacoraCatEFClient bitacoraCatEFClient;

    private List<BitacoraCatEF> crearListaBitacoraCatEF(Message<CatMaximoModel> request) {
        List<BitacoraCatEF> bitacoraCatEFList = new ArrayList<BitacoraCatEF>();
        log.log(Level.INFO, "***** Inicia CatMaximoBitacoraCatEFService.crearListaBitacoraCatEF *****");

        BitacoraCatEF bitacoraCatEF;

        for(CondicionOfertaPersistenciaModelRequest condicionOferta : request.getPayload().getCondicionesOferta()) {
            bitacoraCatEF = new BitacoraCatEF();
            bitacoraCatEF.setIdCondicionOferta(condicionOferta.getId());
            bitacoraCatEF.setIdTipoEvento(BitacoraEnum.ACTUALIZACION_CAT_MAXIMO.getId());
            bitacoraCatEF.setCurp(request.getPayload().getCurp());
            bitacoraCatEF.setIdPlazoAnterior(condicionOferta.getMclcPlazo());
            bitacoraCatEF.setIdPlazoNuevo(condicionOferta.getMclcPlazo());
            bitacoraCatEF.setCatAnterior(condicionOferta.getPorCat());
            bitacoraCatEF.setCatNuevo(request.getPayload().getCatIMSS());

            bitacoraCatEFList.add(bitacoraCatEF);
        }

        log.log(Level.INFO, "***** Finaliza CatMaximoBitacoraCatEFService.crearListaBitacoraCatEF *****");
        return bitacoraCatEFList;
    }

    @Override
    public Message<CatMaximoModel> execute(Message<CatMaximoModel> request) throws BusinessException {
        log.log(Level.INFO, "***** Inicia CatMaximoBitacoraCatEFService *****");

        try {
            if(
                request.getPayload().getCondicionesOferta() != null &&
                request.getPayload().getCondicionesOferta().size() > 0
            ) {
                List<BitacoraCatEF> bitacoraCatEFList = this.crearListaBitacoraCatEF(request);
                if(bitacoraCatEFList != null && bitacoraCatEFList.size() > 0)
                    bitacoraCatEFClient.createBitacoras(bitacoraCatEFList);
            }

            log.log(Level.INFO, "***** Finaliza CatMaximoBitacoraCatEFService *****");
            return request;
        }
        catch (Exception e) {
            log.log(Level.SEVERE, ">>>>ERROR!!! CatMaximoBitacoraCatEFService = {0}", e);
        }

       return response(null, ServiceStatusEnum.EXCEPCION,
               new CatMaximoException(CatMaximoException.ERROR_AL_INVOCAR_SERVICIO_DE_BITACORA_CAT_EF), null);
    }
    
}
