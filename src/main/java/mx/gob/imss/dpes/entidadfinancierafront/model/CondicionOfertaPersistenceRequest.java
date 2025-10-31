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

/**
 *
 * @author eduardo.loyo
 */
@Data
public class CondicionOfertaPersistenceRequest extends BaseModel{
    Collection<CondicionOfertaPersistenciaModelRequest> condicionOferta = new ArrayList();
}
