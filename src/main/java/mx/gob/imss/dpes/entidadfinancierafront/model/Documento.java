/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.entidadfinancierafront.model;

import lombok.Data;
import mx.gob.imss.dpes.common.model.BaseModel;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

/**
 *
 * @author edgar.arenas
 */
@Data
public class Documento extends BaseModel {
    private byte[] archivo;
    private String archivoString;
    private String extencion;
    private String nombreArchivo;
    private String idDocumento;
    private String mimeType;
        
    private byte[] contrato;
    private byte[] carta;
    private byte[] amortizacion;
    private byte[] cep;
    private byte[] cepXML;
    
    
    @Override
    public String toString() {
      return ReflectionToStringBuilder.toStringExclude(this, "archivo");
    }
}
