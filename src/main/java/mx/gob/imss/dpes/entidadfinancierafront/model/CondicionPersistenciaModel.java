/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.entidadfinancierafront.model;

import java.util.ArrayList;
import java.util.Collection;
import lombok.Data;
import mx.gob.imss.dpes.common.model.BaseModel;
import mx.gob.imss.dpes.interfaces.documento.model.Documento;

/**
 *
 * @author eduardo.loyo
 */
@Data
public class CondicionPersistenciaModel extends BaseModel{
    private Collection<CondicionEntFedModel> mclcCondicionEntfedCollection = new ArrayList();
    private Collection<BeneficioModel> mclcBeneficioCollection = new ArrayList();
    private Collection<CondicionOfertaPersistenciaModelRequest> condicionOferta = new ArrayList();
    private Documento logo = new Documento();
    private Collection<CondicionOfertaPersistenciaModelRequest> condicionesOfertaPrevias = new ArrayList<>();
    private String curp;
}
