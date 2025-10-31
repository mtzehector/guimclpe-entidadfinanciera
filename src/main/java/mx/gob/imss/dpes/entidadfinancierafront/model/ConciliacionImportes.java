package mx.gob.imss.dpes.entidadfinancierafront.model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

public class ConciliacionImportes {

    @Getter
    @Setter
    private BigDecimal importeCostoAdministrativo;

    @Getter
    @Setter
    private BigDecimal importeIVACostoAdministrativo;

    @Getter
    @Setter
    private BigDecimal importePermisoItinerante;

    @Getter
    @Setter
    private BigDecimal importeIVAPermisoItinerante;

    @Getter
    @Setter
    private BigDecimal importeNetoRecuperado;

    @Getter
    @Setter
    private BigDecimal importeNetoDefuncion;

    @Getter
    @Setter
    private String importeLetra;

    @Getter
    @Setter
    private BigDecimal[] sumas;

}
