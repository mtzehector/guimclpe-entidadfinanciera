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
import lombok.Getter;
import lombok.Setter;
import mx.gob.imss.dpes.common.model.BaseModel;
import mx.gob.imss.dpes.interfaces.entidadfinanciera.model.EntidadFinanciera;
import mx.gob.imss.dpes.support.config.CustomDateDeserializer;
import mx.gob.imss.dpes.support.config.CustomDateSerializer;

/**
 *
 * @author eduardo.loyo
 */
public class CondicionOfertaModel extends BaseModel {

    @Getter @Setter private Long id;
    @Getter @Setter private BigDecimal porTasaAnual;
    @Getter @Setter private BigDecimal porCat;
    @Getter @Setter private EntidadFinancieraRequest mclcEntidadFinanciera = new EntidadFinancieraRequest();
    @Getter @Setter private PlazoModel mclcPlazo;
    @Getter @Setter private Collection<BeneficioModel> mclcBeneficioCollection;
    @JsonDeserialize( using = CustomDateDeserializer.class )
    @JsonSerialize(using = CustomDateSerializer.class)
    @Getter @Setter private Date altaRegistro;
  @JsonDeserialize(using = CustomDateDeserializer.class)
    @JsonSerialize(using = CustomDateSerializer.class)
    @Getter @Setter private Date bajaRegistro;
    
    

   
}
