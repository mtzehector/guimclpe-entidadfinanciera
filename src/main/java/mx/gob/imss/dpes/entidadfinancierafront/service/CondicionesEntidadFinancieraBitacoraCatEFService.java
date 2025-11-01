package mx.gob.imss.dpes.entidadfinancierafront.service;

import mx.gob.imss.dpes.common.enums.BitacoraEnum;
import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.model.ServiceStatusEnum;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.entidadfinancierafront.model.CondicionOfertaPersistenciaModelRequest;
import mx.gob.imss.dpes.entidadfinancierafront.model.CondicionPersistenciaModel;
import mx.gob.imss.dpes.entidadfinancierafront.restclient.BitacoraCatEFClient;
import mx.gob.imss.dpes.interfaces.bitacora.model.BitacoraCatEF;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.inject.Inject;
import javax.ws.rs.ext.Provider;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

@Provider
public class CondicionesEntidadFinancieraBitacoraCatEFService
    extends ServiceDefinition<CondicionPersistenciaModel,CondicionPersistenciaModel> {
    @Inject
    @RestClient
    private BitacoraCatEFClient bitacoraCatEFClient;

    private List<BitacoraCatEF> crearListaBitacoraCatEF(Message<CondicionPersistenciaModel> request) {
        log.log(Level.INFO, "***** Inicia CondicionesEntidadFinancieraBitacoraCatEFService.crearListaBitacoraCatEF *****");

        List<BitacoraCatEF> bitacoraCatEFList = new ArrayList<BitacoraCatEF>();
        BitacoraCatEF bitacoraCatEF;

        for(CondicionOfertaPersistenciaModelRequest condicionOfertaPayload :
            request.getPayload().getCondicionOferta()) {

            if(request.getPayload().getCondicionesOfertaPrevias() != null &&
                request.getPayload().getCondicionesOfertaPrevias().size() > 0) {

                for (CondicionOfertaPersistenciaModelRequest condicionOfertaPrevio :
                        request.getPayload().getCondicionesOfertaPrevias()) {

                    if(condicionOfertaPrevio.getId().equals(condicionOfertaPayload.getId())) {

                        if(
                            !(
                                condicionOfertaPrevio.getPorCat() == null
                                    ? BigDecimal.ZERO : condicionOfertaPrevio.getPorCat()).equals(
                                    (condicionOfertaPayload.getPorCat() == null
                                        ? BigDecimal.ZERO : condicionOfertaPayload.getPorCat())
                            )
                        ) {
                            bitacoraCatEF = new BitacoraCatEF();
                            bitacoraCatEF.setIdCondicionOferta(condicionOfertaPrevio.getId());
                            bitacoraCatEF.setIdTipoEvento(BitacoraEnum.ACTUALIZACION_CAT_EF.getId());
                            bitacoraCatEF.setCurp(request.getPayload().getCurp());
                            bitacoraCatEF.setIdPlazoAnterior(condicionOfertaPrevio.getMclcPlazo());
                            bitacoraCatEF.setIdPlazoNuevo(condicionOfertaPrevio.getMclcPlazo());
                            bitacoraCatEF.setCatAnterior(
                                    condicionOfertaPrevio.getPorCat() == null ?
                                        BigDecimal.ZERO : condicionOfertaPrevio.getPorCat());
                            bitacoraCatEF.setCatNuevo(
                                    condicionOfertaPayload.getPorCat() == null ?
                                        BigDecimal.ZERO : condicionOfertaPayload.getPorCat());

                            log.log(Level.INFO,
                            "CondicionesEntidadFinancieraBitacoraCatEFService.crearListaBitacoraCatEF DIFF - {0}",
                                bitacoraCatEF);

                            bitacoraCatEFList.add(bitacoraCatEF);
                        }

                        break;
                    }
                }
            }
        }

        log.log(Level.INFO, "***** Finaliza CondicionesEntidadFinancieraBitacoraCatEFService.crearListaBitacoraCatEF *****");
        return bitacoraCatEFList;
    }

    @Override
    public Message<CondicionPersistenciaModel> execute(Message<CondicionPersistenciaModel> request) throws BusinessException {
        log.log(Level.INFO, "***** Inicia CondicionesEntidadFinancieraBitacoraCatEFService = {0} *****", request);

        try {
            if(
                request.getPayload().getCurp() != null &&
                !request.getPayload().getCurp().trim().isEmpty() &&
                request.getPayload().getCondicionOferta() != null &&
                request.getPayload().getCondicionOferta().size() > 0
            ) {
                List<BitacoraCatEF> bitacoraCatEFList = this.crearListaBitacoraCatEF(request);
                if (bitacoraCatEFList != null && bitacoraCatEFList.size() > 0)
                    bitacoraCatEFClient.createBitacoras(bitacoraCatEFList);
            }
            request.setPayload(null);
            log.log(Level.INFO, "***** Finaliza CondicionesEntidadFinancieraBitacoraCatEFService *****");
            return request;
        } catch (Exception e) {
            log.log(Level.SEVERE, ">>>>ERROR!!! CondicionesEntidadFinancieraBitacoraCatEFService = {0}", e);
        }

        return response(request.getPayload(),ServiceStatusEnum.EXCEPCION, null, null);
    }
    
}
