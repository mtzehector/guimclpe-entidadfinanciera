package mx.gob.imss.dpes.entidadfinancierafront.service;

import mx.gob.imss.dpes.common.enums.BitacoraEnum;
import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.model.ServiceStatusEnum;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.entidadfinancierafront.model.CondicionOfertaPersistenciaModelRequest;
import mx.gob.imss.dpes.entidadfinancierafront.model.EntidadFinancieraRequest;
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
public class EntidadFinancieraBitacoraCatEFService
    extends ServiceDefinition<EntidadFinancieraRequest,EntidadFinancieraRequest> {
    @Inject
    @RestClient
    private BitacoraCatEFClient bitacoraCatEFClient;

    private List<BitacoraCatEF> crearListaBitacoraCatEF(Message<EntidadFinancieraRequest> request) {
        log.log(Level.INFO, "***** Inicia EntidadFinancieraBitacoraCatEFService.crearListaBitacoraCatEF *****");

        List<BitacoraCatEF> bitacoraCatEFList = new ArrayList<BitacoraCatEF>();
        BitacoraCatEF bitacoraCatEF;
        boolean existeCondicionOferta = false;

        for(CondicionOfertaPersistenciaModelRequest condicionOfertaPayload :
            request.getPayload().getMclcCondicionOfertaCollection()) {

            existeCondicionOferta = false;

            if(request.getPayload().getCondicionesOfertaPrevias() != null &&
                request.getPayload().getCondicionesOfertaPrevias().size() > 0) {

                for (CondicionOfertaPersistenciaModelRequest condicionOfertaPrevio :
                        request.getPayload().getCondicionesOfertaPrevias()) {

                    if(condicionOfertaPrevio.getId().equals(condicionOfertaPayload.getId())) {
                        existeCondicionOferta = true;

                        if(
                            !(
                                condicionOfertaPrevio.getMclcPlazo().equals(condicionOfertaPayload.getMclcPlazo()) &&
                                (condicionOfertaPrevio.getPorCat() == null
                                    ? BigDecimal.ZERO : condicionOfertaPrevio.getPorCat()).equals(
                                    (condicionOfertaPayload.getPorCat() == null
                                            ? BigDecimal.ZERO : condicionOfertaPayload.getPorCat())
                                ) &&
                                condicionOfertaPayload.getBajaRegistro() == null
                            )
                        ) {
                            bitacoraCatEF = new BitacoraCatEF();
                            bitacoraCatEF.setIdCondicionOferta(condicionOfertaPrevio.getId());
                            bitacoraCatEF.setIdTipoEvento(
                                condicionOfertaPayload.getBajaRegistro() == null ?
                                    BitacoraEnum.ACTUALIZACION_CAT_EF.getId() : BitacoraEnum.BAJA_CAT_EF.getId()
                            );
                            bitacoraCatEF.setCurp(request.getPayload().getCurp());
                            bitacoraCatEF.setIdPlazoAnterior(condicionOfertaPrevio.getMclcPlazo());
                            bitacoraCatEF.setIdPlazoNuevo(condicionOfertaPayload.getMclcPlazo());
                            bitacoraCatEF.setCatAnterior(
                                    condicionOfertaPrevio.getPorCat() == null ?
                                        BigDecimal.ZERO : condicionOfertaPrevio.getPorCat());
                            bitacoraCatEF.setCatNuevo(
                                    condicionOfertaPayload.getPorCat() == null ?
                                        BigDecimal.ZERO : condicionOfertaPayload.getPorCat());

                            log.log(Level.INFO,
                            "EntidadFinancieraBitacoraCatEFService.crearListaBitacoraCatEF DIFF - {0}",
                                bitacoraCatEF);

                            bitacoraCatEFList.add(bitacoraCatEF);
                        }

                        break;
                    }
                }
            }

            if(!existeCondicionOferta) {
                bitacoraCatEF = new BitacoraCatEF();
                bitacoraCatEF.setIdCondicionOferta(condicionOfertaPayload.getId());
                bitacoraCatEF.setIdTipoEvento(
                    condicionOfertaPayload.getBajaRegistro() == null ?
                        BitacoraEnum.NUEVO_CAT_EF.getId() : BitacoraEnum.BAJA_CAT_EF.getId()
                );
                bitacoraCatEF.setCurp(request.getPayload().getCurp());
                bitacoraCatEF.setIdPlazoAnterior(condicionOfertaPayload.getMclcPlazo());
                bitacoraCatEF.setIdPlazoNuevo(condicionOfertaPayload.getMclcPlazo());
                bitacoraCatEF.setCatAnterior(
                    condicionOfertaPayload.getPorCat() == null ? BigDecimal.ZERO : condicionOfertaPayload.getPorCat());
                bitacoraCatEF.setCatNuevo(
                    condicionOfertaPayload.getPorCat() == null ? BigDecimal.ZERO : condicionOfertaPayload.getPorCat());

                log.log(Level.INFO,
                "EntidadFinancieraBitacoraCatEFService.crearListaBitacoraCatEF NO EXIST - {0}", bitacoraCatEF);

                bitacoraCatEFList.add(bitacoraCatEF);
            }
        }

        log.log(Level.INFO, "***** Finaliza EntidadFinancieraBitacoraCatEFService.crearListaBitacoraCatEF *****");
        return bitacoraCatEFList;
    }

    @Override
    public Message<EntidadFinancieraRequest> execute(Message<EntidadFinancieraRequest> request) throws BusinessException {
        log.log(Level.INFO, "***** Inicia EntidadFinancieraBitacoraCatEFService = {0} *****", request);

        try {
            if(
                request.getPayload().getCurp() != null &&
                !request.getPayload().getCurp().trim().isEmpty() &&
                request.getPayload().getMclcCondicionOfertaCollection() != null &&
                request.getPayload().getMclcCondicionOfertaCollection().size() > 0
            ) {
                List<BitacoraCatEF> bitacoraCatEFList = this.crearListaBitacoraCatEF(request);
                if(bitacoraCatEFList != null && bitacoraCatEFList.size() > 0)
                    bitacoraCatEFClient.createBitacoras(bitacoraCatEFList);
            }

            log.log(Level.INFO, "***** Finaliza EntidadFinancieraBitacoraCatEFService *****");
            return request;
        } catch (Exception e) {
            log.log(Level.SEVERE, ">>>>ERROR!!! EntidadFinancieraBitacoraCatEFService = {0}", e);
        }

        return response(request.getPayload(),ServiceStatusEnum.EXCEPCION, null, null);
    }
    
}
