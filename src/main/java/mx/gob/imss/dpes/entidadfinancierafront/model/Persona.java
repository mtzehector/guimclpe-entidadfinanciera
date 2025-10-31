package mx.gob.imss.dpes.entidadfinancierafront.model;

import lombok.Getter;
import lombok.Setter;
import mx.gob.imss.dpes.common.model.BaseModel;

import java.util.Objects;

public class Persona extends BaseModel {

    @Getter
    @Setter
    private Long id;

    @Getter
    @Setter
    private String nombre;

    @Getter
    @Setter
    private String primerApellido;

    @Getter
    @Setter
    private String segundoApellido;

    @Getter
    @Setter
    private String correoElectronico;

    @Getter
    @Setter
    private String cveCurp;

    @Getter
    @Setter
    private String numNss;

    @Getter
    @Setter
    private String rfc;

    @Getter
    @Setter
    private Long cveUsuario;

    @Getter
    @Setter
    private Long cveEntidadFinanciera;

    @Getter
    @Setter
    private Long cvePersonalEf;

    @Getter
    @Setter
    private Long cveTipoEmpleado;

    @Getter
    @Setter
    private String numEmpleado;

    @Getter
    @Setter
    private Integer indRegistrado;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Persona persona = (Persona) o;
        return Objects.equals(id, persona.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
