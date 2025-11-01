/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.entidadfinancierafront.model;

import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;
import mx.gob.imss.dpes.common.model.BaseModel;

/**
 *
 * @author eduardo.loyo
 */
public class CondicionEntFedModel extends BaseModel{
    @Getter @Setter private Long id;
    @Getter @Setter private Short cveDelegacion;
    @Getter @Setter private Short numEdadLimite;
    @Getter @Setter private BigDecimal monMinimo;
    @Getter @Setter private BigDecimal monMaximo;
    @Getter @Setter private Long mclcEntidadFinanciera;
    @Getter @Setter private SexoModel mclcSexo;
}
