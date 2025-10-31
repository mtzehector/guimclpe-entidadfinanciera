/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.entidadfinancierafront.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;
import mx.gob.imss.dpes.common.model.BaseModel;
import mx.gob.imss.dpes.support.config.CustomDateDeserializer;
import mx.gob.imss.dpes.support.config.CustomDateSerializer;

/**
 *
 * @author eduardo.loyo
 */
@Data
public class EntidadFinancieraPersistenceRequest extends BaseModel {
    private Long id;
    private String nombreComercial;
    private String razonSocial;
    private String rfc;
    private String numeroTelefono;
    private String telefonoAtencionClientes;
    private String correoElectronico;
    private BigDecimal catPromedio;
    private String idDelegacion;
    private String idSubdelegacion;
    private String registroPatronal;
    private Long mclcEstadoEf;
    private Long mclcMotivoSuspension;
    private String curpRepresentateLegal;
    private String nombreLegal;
    private String primerApeLegal;
    private String segundoApeLegal;
    private String numProveedor;
    private String correo2;
    private String correo3;
    private String direccion;
    private String numeroProveedor;
    private String instBancaria;
    private String clabe;
    private String confClabe;
    private String curpAdmin;
    private String nombreAdmin;
    private String primerApAdmin;
    private String segundoApeAdmin;
    private String correoAdmin;
    private String idInstFinanciera;
    @JsonDeserialize(using = CustomDateDeserializer.class)
    @JsonSerialize(using = CustomDateSerializer.class)
    private Date fecFirmaContra;
    @JsonDeserialize(using = CustomDateDeserializer.class)
    @JsonSerialize(using = CustomDateSerializer.class)
    private Date fecIniFirmaContra;
    private String paginaWeb;
    private Long sinConvenio;
    private String adminRFC;
}
