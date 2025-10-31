/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package mx.gob.imss.dpes.entidadfinancierafront.service;

import java.util.ArrayList;
import javax.ws.rs.ext.Provider;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.entidadfinancierafront.model.RegistroPatronalRequest;
import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.entidadfinancierafront.restclient.RegistrosPatronalesClient;
import mx.gob.imss.dpes.common.model.ServiceStatusEnum;
import javax.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import javax.ws.rs.core.Response;
import java.util.logging.Level;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import mx.gob.imss.dpes.entidadfinancierafront.model.RegistroPatronalModel;
import javax.ws.rs.core.GenericType;
import mx.gob.imss.dpes.interfaces.entidadfinanciera.ot2.model.RegistrosPatronales;
import mx.gob.imss.dpes.entidadfinancierafront.model.EntidadFinancieraRequest;
import mx.gob.imss.dpes.entidadfinancierafront.model.RegistroPatronalPersistenceRequest;
/**
 *
 * @author juanf.barragan
 */
@Provider
public class RegistroPatronalesService extends ServiceDefinition<EntidadFinancieraRequest, EntidadFinancieraRequest>{

    @Inject
    @RestClient
    private RegistrosPatronalesClient registrosPatronalesClient;

    @Override
    public Message<EntidadFinancieraRequest> execute(Message<EntidadFinancieraRequest> request) throws BusinessException {
        log.log(Level.INFO, "RegistroPatronalesService execute request: {0}", request.getPayload());
        if(request.getPayload().getMcltResgistrosPatronalesCollection().size() == 0){
            return request;
        }
        RegistroPatronalPersistenceRequest req = new RegistroPatronalPersistenceRequest();
        req.setRegistroPatronal(request.getPayload().getMcltResgistrosPatronalesCollection());
        for(RegistroPatronalRequest rp : req.getRegistroPatronal()){
            if(rp.getMclcEntidadFinanciera() == null){
                rp.setMclcEntidadFinanciera(request.getPayload().getEntidadFinanciera().getId());
            }
        }
        Response response = registrosPatronalesClient.create(req);
         if (response.getStatus() == 200) {
            log.log(Level.INFO, "200 OK");
            List<RegistroPatronalModel> collection =  response.readEntity(new GenericType<List<RegistroPatronalModel>>(){});
            log.log(Level.INFO, "Repuesta :{0}", collection);
            List<RegistroPatronalRequest> guardados = new ArrayList<RegistroPatronalRequest>();
            for(RegistroPatronalModel rpm : collection){
                if(rpm.getBajaRegistro()==null){
                    RegistroPatronalRequest rp = new RegistroPatronalRequest();
                    rp.setId(rpm.getId());
                    rp.setMclcEntidadFinanciera(rpm.getIdEntidadFinanciera());
                    rp.setRegistroPatronal(rpm.getRegistroPatronal());
                    guardados.add(rp);
                }
            }
            request.getPayload().setMcltResgistrosPatronalesCollection(guardados);
            return new Message<>(request.getPayload());
         }
        return response(null, ServiceStatusEnum.EXCEPCION, null, null);
    }
    public Message<RegistroPatronalRequest> obtenerRegistroPatronales(Long id ){
        log.log(Level.INFO, "RegistroPatronalesService obtenerRegistroPatronales el id es " + id);
        Response  response = registrosPatronalesClient.obtenerRegistroPatronales(id);
        if(response.getStatus() == 200){
            log.log(Level.INFO, "Respuesta correcta ");
            RegistroPatronalRequest collection =  response.readEntity(RegistroPatronalRequest.class);
            RegistroPatronalRequest respuesta = new RegistroPatronalRequest();
            //respuesta.setRegistrosPatronales(collection.getRegistrosPatronales());
            
            log.log(Level.INFO, "Repuesta :{0}", collection);
            return new Message<>(respuesta);
        }
        //return response(null, ServiceStatusEnum.EXCEPCION, null, null);
        return null;
    }

}
