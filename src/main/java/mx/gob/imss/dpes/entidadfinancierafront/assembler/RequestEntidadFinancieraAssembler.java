/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.entidadfinancierafront.assembler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import javax.ws.rs.ext.Provider;
import mx.gob.imss.dpes.common.assembler.BaseAssembler;
import mx.gob.imss.dpes.entidadfinancierafront.model.CondicionOfertaModel;
import mx.gob.imss.dpes.entidadfinancierafront.model.CondicionOfertaPersistenciaModelRequest;
import mx.gob.imss.dpes.entidadfinancierafront.model.EntidadFinancieraPersistenceRequest;
import mx.gob.imss.dpes.entidadfinancierafront.model.EntidadFinancieraRequest;
import mx.gob.imss.dpes.entidadfinancierafront.model.RegistroPatronalModel;
import mx.gob.imss.dpes.entidadfinancierafront.model.RegistroPatronalRequest;
import mx.gob.imss.dpes.entidadfinancierafront.model.RequestEntidadFinanciera;

/**
 *
 * @author eduardo.loyo
 */
@Provider
public class RequestEntidadFinancieraAssembler extends BaseAssembler<RequestEntidadFinanciera, EntidadFinancieraRequest>{

    @Override
    public EntidadFinancieraRequest assemble(RequestEntidadFinanciera source) {
        EntidadFinancieraRequest response = new EntidadFinancieraRequest();
        EntidadFinancieraPersistenceRequest out = new EntidadFinancieraPersistenceRequest();
        out.setId(source.getId());
        out.setTelefonoAtencionClientes(source.getTelefonoAtencionClientes());
        out.setCatPromedio(source.getCatPromedio());
        out.setIdDelegacion(source.getIdDelegacion());
        out.setIdSubdelegacion(source.getIdSubdelegacion());
        out.setNombreComercial(source.getNombreComercial());
        
        out.setRazonSocial(source.getRazonSocial());
        out.setNombreLegal(source.getNombreLegal());
        out.setRfc(source.getRfc());
        out.setNumeroTelefono(source.getNumeroTelefono());
        out.setNumeroProveedor(source.getNumeroProveedor() == null ? "" : source.getNumeroProveedor());
        out.setPaginaWeb(source.getPaginaWeb());
        out.setCorreoElectronico(source.getCorreoElectronico());
        out.setCatPromedio(source.getCatPromedio());
        out.setMclcEstadoEf(source.getMclcEstadoEf());
        out.setIdDelegacion(source.getIdDelegacion());
        out.setIdSubdelegacion(source.getIdSubdelegacion());
        out.setRegistroPatronal(source.getRegistroPatronal());
        out.setMclcMotivoSuspension(source.getMclcMotivoSuspension());
        out.setCurpRepresentateLegal(source.getCurpRepresentanteLegal());
        out.setNombreLegal(source.getNombreLegal());
        out.setPrimerApeLegal(source.getPrimerApeLegal());
        out.setSegundoApeLegal(source.getSegundoApeLegal());
        out.setCorreo2(source.getCorreo2());
        out.setCorreo3(source.getCorreo3());
        out.setDireccion(source.getDireccion());
        out.setFecFirmaContra(source.getFecFirmaContra());
        out.setFecIniFirmaContra(source.getFecIniFirmaContra());
        out.setInstBancaria(source.getInstBancaria());
        out.setClabe(source.getClabe());
        out.setConfClabe(source.getConfClabe());
        out.setCurpAdmin(source.getCurpAdmin());
        out.setNombreAdmin(source.getNombreAdmin());
        out.setPrimerApAdmin(source.getPrimerApAdmin());
        out.setSegundoApeAdmin(source.getSegundoApeAdmin());
        out.setCorreoAdmin(source.getCorreoAdmin());
        out.setAdminRFC(source.getAdminRFC());
        out.setIdInstFinanciera(source.getIdInstFinanciera());
        out.setSinConvenio(source.getSinConvenio());
        
        
        Iterator<CondicionOfertaModel> i = source.getMclcCondicionOfertaCollection().iterator();
        Collection<CondicionOfertaPersistenciaModelRequest> mclcCondicionOfertaCollection = new ArrayList<>();
        
        while(i.hasNext()){
           CondicionOfertaPersistenciaModelRequest collection = new CondicionOfertaPersistenciaModelRequest();
           CondicionOfertaModel co = i.next();
           collection.setId(co.getId());
           collection.setMclcPlazo(co.getMclcPlazo().getId());
           collection.setPorCat(co.getPorCat());
           collection.setPorTasaAnual(co.getPorTasaAnual());
           collection.setBajaRegistro(co.getBajaRegistro());
           mclcCondicionOfertaCollection.add(collection);
        }
        
        Iterator<RegistroPatronalModel> it = source.getMcltRegistrosPatronalesCollection().iterator();
        Collection<RegistroPatronalRequest> mcltRegistrosPatronalesCollection = new ArrayList<>();
        while(it.hasNext()){
            RegistroPatronalRequest collection = new RegistroPatronalRequest();
            RegistroPatronalModel rpm = it.next();
            collection.setId(rpm.getId());
            collection.setMclcEntidadFinanciera(rpm.getIdEntidadFinanciera());
            collection.setRegistroPatronal(rpm.getRegistroPatronal());
            collection.setBajaRegistro(rpm.getBajaRegistro());
            mcltRegistrosPatronalesCollection.add(collection);
        }
        
        response.setEntidadFinanciera(out);
        response.setEnableModificar(source.getEnableModificar());
        response.setValueCorreModAdmin(source.getValueCorreModAdmin());
        response.setValueCurpModAdmin(source.getValueCurpModAdmin());
        response.setValueRfcModAdmin(source.getValueRfcModAdmin());
        response.setMclcCondicionOfertaCollection(mclcCondicionOfertaCollection);
        response.setMcltResgistrosPatronalesCollection(mcltRegistrosPatronalesCollection);
        response.setCurp(source.getCurp());
        response.setRfc(source.getAdminRFC());
        response.setBitacoraEstatusEF(source.getBitacoraEstatusEF());
        return response;
    }

  

   

 
    
}
