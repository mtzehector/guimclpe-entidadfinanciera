/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.entidadfinancierafront.model;

import java.math.BigDecimal;
import java.util.List;

import lombok.Data;
import mx.gob.imss.dpes.common.enums.TipoDocumentoEnum;
import mx.gob.imss.dpes.common.model.BaseModel;
import mx.gob.imss.dpes.interfaces.documento.model.Documento;

/**
 *
 * @author juanf.barragan
 */
@Data
public class CatMaximoModel  extends BaseModel{
    private BigDecimal catIMSS;
    private String catText;
    private String curp;
    private String catAnterior;
    private byte[] archivo;
    private String nombreArchivo;
    private String solicitud;
    private TipoDocumentoEnum tipoDocumento;
    private RespuestaBoveda respuestaBoveda;
    private Documento documentoResponse = new Documento();
    private List<CondicionOfertaPersistenciaModelRequest> condicionesOferta;
    private Long sesion;
}
