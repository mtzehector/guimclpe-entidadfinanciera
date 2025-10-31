/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.entidadfinancierafront.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;
import mx.gob.imss.dpes.common.model.BaseModel;
import mx.gob.imss.dpes.support.config.CustomDateDeserializer;
import mx.gob.imss.dpes.support.config.CustomDateSerializer;

/**
 *
 * @author juanf.barragan
 */
public class CartaReciboRequest extends BaseModel{
    @Getter
    @Setter
    private Long id;
    @Getter
    @Setter
    private Long cveEntFin;
    @Getter
    @Setter
    private String nomComercial;
    @Getter
    @Setter
    private String periodo;
    @Getter
    @Setter
    private String proveedor;
    @Getter
    @Setter
    private String rfc;
    @Getter
    @Setter
    private String monto;
    @Getter
    @Setter
    private String montoLetra;
    @Getter
    @Setter
    private String mes;
    @Getter
    @Setter
    private String anio;
    @Getter
    @Setter
    private String primas;
    @Getter
    @Setter
    private String tasa;
    @Getter
    @Setter
    private String iva;
    @Getter
    @Setter
    private String demasia;
    @Getter
    @Setter
    private String neto;
    @Getter
    @Setter
    private String firma;
    @Getter
    @Setter
    private String refDocBoveda;
    @JsonDeserialize(using = CustomDateDeserializer.class)
    @JsonSerialize(using = CustomDateSerializer.class)
    @Getter
    @Setter
    private Date altaRegistro;
    
}
