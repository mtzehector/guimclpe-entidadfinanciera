package mx.gob.imss.dpes.entidadfinancierafront.model;

import lombok.Getter;
import lombok.Setter;
import mx.gob.imss.dpes.common.model.BaseModel;

import java.math.BigDecimal;
import java.util.Objects;

public class ResumenConciliacionBean extends BaseModel {

    @Getter
    @Setter
    private String id;

    @Getter
    @Setter
    private String entidadFinanciera;

    @Getter
    @Setter
    private Integer numeroProveedor;

    @Getter
    @Setter
    private Long casosEmitidos301_380;

    @Getter
    @Setter
    private Double importeEmitidos301_380;

    @Getter
    @Setter
    private Long casosEmitidos301;

    @Getter
    @Setter
    private Double importeEmitidos301;

    @Getter
    @Setter
    private Long casosEmitidos380;

    @Getter
    @Setter
    private Double importeEmitidos380;

    @Getter
    @Setter
    private Long casosPagados301_380;

    @Getter
    @Setter
    private Double importePagado301_380;

    @Getter
    @Setter
    private Long casosNoPagados;

    @Getter
    @Setter
    private Double importeNoPagado;

    @Getter
    @Setter
    private Double totalRetenciones;

    @Getter
    @Setter
    private Double costoAdministrativo;

    @Getter
    @Setter
    private Double ivaCostoAdministrativo;

    @Getter
    @Setter
    private Double permisoItinerante;

    @Getter
    @Setter
    private Double ivaPermisoItinerante;

    @Getter
    @Setter
    private Double netoFallecidos;

    @Getter
    @Setter
    private Long casosFallecidos;

    @Getter
    @Setter
    private Double recuperacionFallecidos;

    @Getter
    @Setter
    private Double pagoReal;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ResumenConciliacionBean that = (ResumenConciliacionBean) o;
        return Objects.equals(id, that.id) && Objects.equals(numeroProveedor, that.numeroProveedor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, numeroProveedor);
    }
}
