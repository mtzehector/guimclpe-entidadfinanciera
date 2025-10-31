package mx.gob.imss.dpes.entidadfinancierafront.model;

import lombok.Data;
import mx.gob.imss.dpes.common.model.BaseModel;

@Data
public class Usuario  extends BaseModel {
    private String idUsr;
    private Boolean owner;
    private String tipoIdUsr;
}
