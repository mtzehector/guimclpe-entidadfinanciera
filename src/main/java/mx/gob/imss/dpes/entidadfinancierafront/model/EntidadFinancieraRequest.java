/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.entidadfinancierafront.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import lombok.Data;
import mx.gob.imss.dpes.common.model.BaseModel;
import mx.gob.imss.dpes.interfaces.bitacora.model.BitacoraEstatusEF;
import mx.gob.imss.dpes.support.config.CustomDateDeserializer;
import mx.gob.imss.dpes.support.config.CustomDateSerializer;

/**
 *
 * @author eduardo.loyo
 */@Data
public class EntidadFinancieraRequest extends BaseModel {

    
    private EntidadFinancieraPersistenceRequest entidadFinanciera  = new EntidadFinancieraPersistenceRequest();
    private Collection<CondicionEntFedModel> mclcCondicionEntfedCollection = new ArrayList<>();
    private Collection<CondicionOfertaPersistenciaModelRequest> mclcCondicionOfertaCollection = new ArrayList<>();
    private Collection<CondicionOfertaPersistenciaModelRequest> condicionOferta = new ArrayList();
    private Collection<CondicionOfertaPersistenciaModelRequest> condicionesOfertaPrevias = new ArrayList<>();
    private Boolean enableModificar;
    private String valueCorreModAdmin;
    private String valueCurpModAdmin;
    private String valueRfcModAdmin;
    private Collection<RegistroPatronalRequest> mcltResgistrosPatronalesCollection = new ArrayList();
    private BitacoraEstatusEF bitacoraEstatusEF;
    private String curp;
    private String rfc;
}
