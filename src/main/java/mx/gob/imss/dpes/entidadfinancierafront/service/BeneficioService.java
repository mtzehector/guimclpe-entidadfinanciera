/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.entidadfinancierafront.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import javax.inject.Inject;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.model.ServiceStatusEnum;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.entidadfinancierafront.assembler.BeneficiosAssembler;
import mx.gob.imss.dpes.entidadfinancierafront.model.BeneficioModel;
import mx.gob.imss.dpes.entidadfinancierafront.model.CondicionOfertaPersistenciaModelRequest;
import mx.gob.imss.dpes.entidadfinancierafront.model.CondicionPersistenciaModel;
import mx.gob.imss.dpes.entidadfinancierafront.restclient.BeneficioClient;
import org.eclipse.microprofile.rest.client.inject.RestClient;

/**
 *
 * @author eduardo.loyo
 */
@Provider
public class BeneficioService extends ServiceDefinition<CondicionPersistenciaModel, CondicionPersistenciaModel> {

    @Inject
    @RestClient
    private BeneficioClient client;
    @Inject 
    private BeneficiosAssembler assembler;

    private void obtenerCondicionesOferta(Message<CondicionPersistenciaModel> request) {
        List<CondicionOfertaPersistenciaModelRequest> condicionesOferta =
            new ArrayList<CondicionOfertaPersistenciaModelRequest>();

        List<BeneficioModel> beneficios = (List<BeneficioModel>) request.getPayload().getMclcBeneficioCollection();

        boolean elementoExisteEnColeccion = false;

        for(BeneficioModel beneficio : beneficios) {
            elementoExisteEnColeccion = false;

            for (CondicionOfertaPersistenciaModelRequest condicionOferta : condicionesOferta) {
                if(beneficio.getMclcCondicionOferta().getId().equals(condicionOferta.getId())) {
                    elementoExisteEnColeccion = true;
                    break;
                }
            }

            if(!elementoExisteEnColeccion) {
                condicionesOferta.add(beneficio.getMclcCondicionOferta());
            }
        }

        request.getPayload().setCondicionOferta(condicionesOferta);
    }

    @Override
    public Message<CondicionPersistenciaModel> execute(Message<CondicionPersistenciaModel> request) throws BusinessException {
        log.log(Level.INFO, ">>>entidadFinancieraFront BeneficioService Inicia Beneficio Service ------------Step 2 ----------: {0}", request.getPayload().getMclcBeneficioCollection());
        Response create = client.create(assembler.assemble(request.getPayload()));
        if (create.getStatus() == 200) {
            Collection<BeneficioModel> respuesta =  create.readEntity(new GenericType<Collection<BeneficioModel>>(){});
            log.log(Level.INFO, ">>>entidadFinancieraFront BeneficioService Repuesta Beneficio : {0}", respuesta);
            request.getPayload().setMclcBeneficioCollection(respuesta);
            //Iterator<BeneficioModel> i = request.getPayload().getMclcBeneficioCollection().iterator();
            //while(i.hasNext()){
            //    request.getPayload()
            //            .getCondicionOferta()
            //                .add(i.next().getMclcCondicionOferta());
            //}
            this.obtenerCondicionesOferta(request);

            return new Message<>(request.getPayload());
        }
        return response(null, ServiceStatusEnum.EXCEPCION, null, null);
    }

}
